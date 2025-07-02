/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.rlptxn.cancun.phaseSection;

import static net.consensys.linea.zktracer.Trace.LLARGE;
import static net.consensys.linea.zktracer.module.rlpUtils.RlpUtils.BYTES_PREFIX_SHORT_INT;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.rlptxn.cancun.TracedValues;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

public class ToPhaseSection extends PhaseSection {
  public ToPhaseSection(TransactionProcessingMetadata tx) {
    super(tx);
  }

  @Override
  protected void traceComputationsRows(
      Trace.Rlptxn trace, TransactionProcessingMetadata tx, TracedValues tracedValues) {
    if (tx.isDeployment()) {
      tracePreValues(trace, tracedValues);
      trace
          .cmp(true)
          .limbConstructed(true)
          .lt(true)
          .lx(true)
          .limb(Bytes16.rightPad(BYTES_PREFIX_SHORT_INT))
          .nBytes(1)
          .done(true)
          .phaseEnd(true);
      tracedValues.decrementLtAndLxSizeBy(1);
      tracePostValues(trace, tracedValues);
    } else {
      final Address to = tx.getBesuTransaction().getTo().get();

      // first row for deployment
      tracePreValues(trace, tracedValues);
      trace
          .cmp(true)
          .ctMax(1)
          .pCmpTrmFlag(true)
          .pCmpExoData1(to.slice(0, 4))
          .pCmpExoData2(to.slice(4, LLARGE))
          .limbConstructed(true)
          .lt(true)
          .lx(true)
          .limb(Bytes16.rightPad(Bytes.concatenate(BYTES_PREFIX_SHORT_INT, to.slice(0, 4))))
          .nBytes(5);
      tracedValues.decrementLtAndLxSizeBy(5);
      tracePostValues(trace, tracedValues);

      // second row for deployment
      tracePreValues(trace, tracedValues);
      trace
          .cmp(true)
          .ct(1)
          .ctMax(1)
          .done(true)
          .limbConstructed(true)
          .lt(true)
          .lx(true)
          .limb(to.slice(4, LLARGE))
          .nBytes(LLARGE)
          .phaseEnd(true);
      tracedValues.decrementLtAndLxSizeBy(LLARGE);
      tracePostValues(trace, tracedValues);
    }
  }

  @Override
  protected void traceIsPhaseX(Trace.Rlptxn trace) {
    trace.isTo(true);
  }

  @Override
  public int lineCount() {
    return 1 + (tx.isDeployment() ? 1 : 2);
  }
}
