/*
 * Copyright Consensys Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.hub.precompiles;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.EcRecoverComputer;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.ModExpLogCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Blake2FPrecompile1;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Blake2FPrecompile2;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.EcAdd;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.EcMul;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.EcPairing;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.EcRecover;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Identity;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.ModexpCds;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.ModexpExtract;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.ModexpLead;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.ModexpPricing;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.ModexpXbs;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.RipeMd160;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Sha2;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
public class PrecompileLinesGenerator {
  public static List<TraceFragment> generateFor(final Hub hub, final PrecompileInvocation p) {
    final List<TraceFragment> r = new ArrayList<>(10);
    switch (p.precompile()) {
      case EC_RECOVER -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new EcRecover(p)));
        } else {
          final boolean recoverySuccessful =
              EcRecoverComputer.ecRecoverSuccessful(hub.transients().op().callData());

          r.add(
              ImcFragment.empty()
                  .callOob(new EcRecover(p))
                  .callMmu(
                      p.callDataSource().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forEcRecover(hub, p, recoverySuccessful, 0)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forEcRecover(hub, p, recoverySuccessful, 1)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forEcRecover(hub, p, recoverySuccessful, 2)));
        }
      }
      case SHA2_256 -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new Sha2(p)));
        } else {
          r.add(
              ImcFragment.empty()
                  .callOob(new Sha2(p))
                  .callMmu(
                      p.callDataSource().isEmpty() ? MmuCall.nop() : MmuCall.forSha2(hub, p, 0)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty() ? MmuCall.nop() : MmuCall.forSha2(hub, p, 1)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty() ? MmuCall.nop() : MmuCall.forSha2(hub, p, 2)));
        }
      }
      case RIPEMD_160 -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new RipeMd160(p)));
        } else {
          r.add(
              ImcFragment.empty()
                  .callOob(new Sha2(p))
                  .callMmu(
                      p.callDataSource().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forRipeMd160(hub, p, 0)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forRipeMd160(hub, p, 1)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forRipeMd160(hub, p, 2)));
        }
      }
      case IDENTITY -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new Identity(p)));
        } else {
          r.add(
              ImcFragment.empty().callOob(new Identity(p)).callMmu(MmuCall.forIdentity(hub, p, 0)));
          r.add(ImcFragment.empty().callMmu(MmuCall.forIdentity(hub, p, 1)));
        }
      }
      case MODEXP -> {
        /** Put all that in ModExp metadata */
        final boolean extractBbs = !p.callDataSource().isEmpty();
        final boolean extractEbs = p.callDataSource().length() > 32;
        final boolean extractMbs = p.callDataSource().length() > 64;

        final int bbsShift = 32 - (int) Math.min(32, p.callDataSource().length());
        final Bytes rawBbs =
            extractBbs
                ? hub.messageFrame().shadowReadMemory(p.callDataSource().offset(), 32)
                : Bytes.EMPTY;
        final EWord bbs = EWord.of(rawBbs.shiftRight(bbsShift).shiftLeft(bbsShift));

        final int ebsShift =
            extractEbs ? 32 - (int) Math.min(32, p.callDataSource().length() - 32) : 0;
        final Bytes rawEbs =
            extractEbs
                ? hub.messageFrame().shadowReadMemory(p.callDataSource().offset() + 32, 32)
                : Bytes.EMPTY;
        final EWord ebs = EWord.of(rawEbs.shiftRight(ebsShift).shiftLeft(ebsShift));

        final int mbsShift =
            extractMbs ? 32 - (int) Math.min(32, p.callDataSource().length() - 64) : 0;
        final Bytes rawMbs =
            extractMbs
                ? hub.messageFrame().shadowReadMemory(p.callDataSource().offset() + 64, 32)
                : Bytes.EMPTY;
        final EWord mbs = EWord.of(rawMbs.shiftRight(mbsShift).shiftLeft(mbsShift));

        final boolean loadRawLeadingWord =
            p.callDataSource().length() > 96 + bbs.lo().toInt() && !ebs.isZero();

        final EWord rawLeadingWord =
            loadRawLeadingWord
                ? EWord.of(
                    hub.messageFrame()
                        .shadowReadMemory(p.callDataSource().offset() + 96 + bbs.toInt(), 32))
                : EWord.ZERO;

        r.add(ImcFragment.empty().callOob(new ModexpCds(p.requestedReturnDataTarget().length())));
        r.add(
            ImcFragment.empty()
                .callOob(new ModexpXbs(bbs, EWord.ZERO, false))
                .callMmu(extractBbs ? MmuCall.forModExp(hub, p, 2) : MmuCall.nop()));
        r.add(
            ImcFragment.empty()
                .callOob(new ModexpXbs(ebs, EWord.ZERO, false))
                .callMmu(extractEbs ? MmuCall.forModExp(hub, p, 3) : MmuCall.nop()));
        r.add(
            ImcFragment.empty()
                .callOob(new ModexpXbs(mbs, bbs, true))
                .callMmu(extractEbs ? MmuCall.forModExp(hub, p, 4) : MmuCall.nop()));
        final ImcFragment line5 =
            ImcFragment.empty()
                .callOob(
                    new ModexpLead(bbs.lo().toInt(), p.callDataSource().length(), ebs.lo().toInt()))
                .callMmu(loadRawLeadingWord ? MmuCall.forModExp(hub, p, 5) : MmuCall.nop());
        if (loadRawLeadingWord) {
          line5.callModExp(
              new ModExpLogCall(
                  rawLeadingWord,
                  Math.min((int) (p.callDataSource().length() - 96 - bbs.toInt()), 32),
                  Math.min(ebs.toInt(), 32)));
        }
        r.add(line5);
        r.add(
            ImcFragment.empty()
                .callOob(
                    new ModexpPricing(
                        p,
                        ModExpLogCall.exponentLeadingWordLog(
                            rawLeadingWord,
                            Math.min((int) (p.callDataSource().length() - 96 - bbs.toInt()), 32),
                            Math.min(ebs.toInt(), 32)),
                        Math.max(mbs.toInt(), bbs.toInt()))));

        if (p.ramFailure()) {
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else {
          final boolean extractModulus =
              (p.callDataSource().length() > 96 + bbs.toInt() + ebs.toInt()) && !mbs.isZero();
          final boolean extractBase = extractModulus && !bbs.isZero();
          final boolean extractExponent = extractModulus && !ebs.isZero();

          r.add(
              ImcFragment.empty()
                  .callOob(
                      new ModexpExtract(
                          p.callDataSource().length(),
                          bbs.lo().toInt(),
                          ebs.lo().toInt(),
                          mbs.lo().toInt()))
                  .callMmu(extractModulus ? MmuCall.forModExp(hub, p, 7) : MmuCall.nop()));

          if (extractModulus) {
            r.add(ImcFragment.empty().callMmu(MmuCall.forModExp(hub, p, 8)));
            r.add(ImcFragment.empty().callMmu(MmuCall.forModExp(hub, p, 9)));
            r.add(ImcFragment.empty().callMmu(MmuCall.forModExp(hub, p, 10)));
          } else {
            for (int i = 0; i < 4; i++) {
              r.add(ImcFragment.empty());
            }
          }

          if (!mbs.isZero() && !p.requestedReturnDataTarget().isEmpty()) {
            r.add(ImcFragment.empty().callMmu(MmuCall.forModExp(hub, p, 11)));
          }

          r.add(ContextFragment.providesReturnData(hub.callStack()));
        }
      }
      case EC_ADD -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new EcAdd(p)));
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else if (p.ramFailure()) {
          r.add(ImcFragment.empty().callOob(new EcAdd(p)).callMmu(MmuCall.forEcAdd(hub, p, 0)));
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else {
          r.add(ImcFragment.empty().callOob(new EcAdd(p)).callMmu(MmuCall.forEcAdd(hub, p, 0)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty() ? MmuCall.nop() : MmuCall.forEcAdd(hub, p, 1)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.requestedReturnDataTarget().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forEcAdd(hub, p, 2)));
          r.add(ContextFragment.providesReturnData(hub.callStack()));
        }
      }
      case EC_MUL -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new EcMul(p)));
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else if (p.ramFailure()) {
          r.add(ImcFragment.empty().callOob(new EcMul(p)).callMmu(MmuCall.forEcMul(hub, p, 0)));
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else {
          r.add(ImcFragment.empty().callOob(new EcMul(p)).callMmu(MmuCall.forEcMul(hub, p, 0)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.callDataSource().isEmpty() ? MmuCall.nop() : MmuCall.forEcMul(hub, p, 1)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.requestedReturnDataTarget().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forEcMul(hub, p, 2)));
          r.add(ContextFragment.providesReturnData(hub.callStack()));
        }
      }
      case EC_PAIRING -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new EcPairing(p)));
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else if (p.ramFailure()) {
          r.add(
              ImcFragment.empty()
                  .callOob(new EcPairing(p))
                  .callMmu(MmuCall.forEcPairing(hub, p, 0)));
          r.add(ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
        } else {
          r.add(
              ImcFragment.empty()
                  .callOob(new EcPairing(p))
                  .callMmu(MmuCall.forEcPairing(hub, p, 0)));
          r.add(ImcFragment.empty().callMmu(MmuCall.forEcPairing(hub, p, 1)));
          r.add(
              ImcFragment.empty()
                  .callMmu(
                      p.requestedReturnDataTarget().isEmpty()
                          ? MmuCall.nop()
                          : MmuCall.forEcPairing(hub, p, 2)));
          r.add(ContextFragment.providesReturnData(hub.callStack()));
        }
      }
      case BLAKE2F -> {
        if (p.hubFailure()) {
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile1(p)));
        } else if (p.ramFailure()) {
          r.add(
              ImcFragment.empty()
                  .callOob(new Blake2FPrecompile1(p))
                  .callMmu(MmuCall.forBlake2f(hub, p, 0)));
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile2(p)));
        } else {
          r.add(
              ImcFragment.empty()
                  .callOob(new Blake2FPrecompile1(p))
                  .callMmu(MmuCall.forBlake2f(hub, p, 0)));
          r.add(
              ImcFragment.empty()
                  .callOob(new Blake2FPrecompile2(p))
                  .callMmu(MmuCall.forBlake2f(hub, p, 1)));
          r.add(ImcFragment.empty().callMmu(MmuCall.forBlake2f(hub, p, 2)));
          r.add(ImcFragment.empty().callMmu(MmuCall.forBlake2f(hub, p, 3)));
        }
      }
    }

    r.add(
        p.success()
            ? ContextFragment.providesReturnData(hub.callStack())
            : ContextFragment.nonExecutionEmptyReturnData(hub.callStack()));
    return r;
  }
}
