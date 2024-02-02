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
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.MiscFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.MmuSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Blake2fPrecompileFirstSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Blake2fPrecompileSecondSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.EcRecover;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Identity;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.RipeMd160;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Sha2;

@RequiredArgsConstructor
public class PrecompileLinesGenerator {
  public static List<TraceFragment> generateFor(final Hub hub, final PrecompileInvocation p) {
    final List<TraceFragment> r = new ArrayList<>(10);
    switch (p.precompile()) {
      case EC_RECOVER -> {
        if (p.hubFailure()) {
          r.add(MiscFragment.empty().withOob(new EcRecover(p)));
        } else {
          final boolean recoverySuccessful =
              false; // TODO: execute the ECRECOVER ourselves and check it

          r.add(
              MiscFragment.empty()
                  .withOob(new EcRecover(p))
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forEcRecover(hub, p, recoverySuccessful, 0)));
          r.add(
              MiscFragment.empty()
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forEcRecover(hub, p, recoverySuccessful, 1)));
          r.add(
              MiscFragment.empty()
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forEcRecover(hub, p, recoverySuccessful, 2)));
        }
      }
      case SHA2_256 -> {
        if (p.hubFailure()) {
          r.add(MiscFragment.empty().withOob(new Sha2(p)));
        } else {
          r.add(
              MiscFragment.empty()
                  .withOob(new Sha2(p))
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forSha2(hub, p, 0)));
          r.add(
              MiscFragment.empty()
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forSha2(hub, p, 1)));
          r.add(
              MiscFragment.empty()
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forSha2(hub, p, 2)));
        }
      }
      case RIPEMD_160 -> {
        if (p.hubFailure()) {
          r.add(MiscFragment.empty().withOob(new RipeMd160(p)));
        } else {
          r.add(
              MiscFragment.empty()
                  .withOob(new Sha2(p))
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forRipeMd160(hub, p, 0)));
          r.add(
              MiscFragment.empty()
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forRipeMd160(hub, p, 1)));
          r.add(
              MiscFragment.empty()
                  .withMmu(
                      p.callDataSource().isEmpty()
                          ? MmuSubFragment.nop()
                          : MmuSubFragment.forRipeMd160(hub, p, 2)));
        }
      }
      case IDENTITY -> {
        if (p.hubFailure()) {
          r.add(MiscFragment.empty().withOob(new Identity(p)));
        } else {
          r.add(
              MiscFragment.empty()
                  .withOob(new Identity(p))
                  .withMmu(MmuSubFragment.forIdentity(hub, p, 0)));
          r.add(MiscFragment.empty().withMmu(MmuSubFragment.forIdentity(hub, p, 1)));
        }
      }
      case MODEXP -> {}
      case EC_ADD -> {}
      case EC_MUL -> {}
      case EC_PAIRING -> {}
      case BLAKE2F -> {
        if (p.hubFailure()) {
          // hub failure - one line only
          r.add(MiscFragment.empty().withOob(new Blake2fPrecompileFirstSubFragment(p)));
        } else if (p.ramFailure()) {
          // happy path or RAM failure - two lines
          r.add(MiscFragment.empty().withOob(new Blake2fPrecompileFirstSubFragment(p)).withMmu());
          r.add(MiscFragment.empty().withOob(new Blake2fPrecompileSecondSubFragment(p)));
        } else {
          r.add(MiscFragment.empty().withOob(new Blake2fPrecompileFirstSubFragment(p)).withMmu());
          r.add(MiscFragment.empty().withOob(new Blake2fPrecompileSecondSubFragment(p)).withMmu());
          r.add(MiscFragment.empty().withMmu());
          r.add(MiscFragment.empty().withMmu());
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
