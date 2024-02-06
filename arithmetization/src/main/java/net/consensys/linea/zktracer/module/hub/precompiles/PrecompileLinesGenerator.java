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
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Blake2FPrecompile1;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Blake2FPrecompile2;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.EcRecover;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Identity;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.RipeMd160;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.precompiles.Sha2;

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
      case MODEXP -> {}
      case EC_ADD -> {}
      case EC_MUL -> {}
      case EC_PAIRING -> {}
      case BLAKE2F -> {
        if (p.hubFailure()) {
          // hub failure - one line only
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile1(p)));
        } else if (p.ramFailure()) {
          // happy path or RAM failure - two lines
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile1(p)).callMmu());
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile2(p)));
        } else {
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile1(p)).callMmu());
          r.add(ImcFragment.empty().callOob(new Blake2FPrecompile2(p)).callMmu());
          r.add(ImcFragment.empty().callMmu());
          r.add(ImcFragment.empty().callMmu());
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
