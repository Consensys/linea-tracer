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

package net.consensys.linea.zktracer.module.oob;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_BLAKE_CDS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_BLAKE_PARAMS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECADD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECMUL;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECPAIRING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_ECRECOVER;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_IDENTITY;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_MODEXP_CDS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_MODEXP_EXTRACT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_MODEXP_LEAD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_MODEXP_PRICING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_MODEXP_XBS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_RIPEMD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_SHA2;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.hyperledger.besu.evm.frame.MessageFrame;

@RequiredArgsConstructor
/** Implementation of a {@link Module} for out of bounds. */
public class Oob implements Module {

  /** A list of the operations to trace */
  @Getter private final StackedList<OobOperation> chunks = new StackedList<>();

  private final Hub hub;
  private final Add add;
  private final Mod mod;
  private final Wcp wcp;

  private OobOperation oobOperation;

  @Override
  public String moduleKey() {
    return "OOB";
  }

  static final List<Integer> PRECOMPILES_HANDLED_BY_OOB =
      List.of(
          OOB_INST_ECRECOVER,
          OOB_INST_SHA2,
          OOB_INST_RIPEMD,
          OOB_INST_IDENTITY,
          OOB_INST_ECADD,
          OOB_INST_ECMUL,
          OOB_INST_ECPAIRING,
          OOB_INST_BLAKE_CDS,
          OOB_INST_BLAKE_PARAMS,
          OOB_INST_MODEXP_CDS,
          OOB_INST_MODEXP_XBS,
          OOB_INST_MODEXP_LEAD,
          OOB_INST_MODEXP_PRICING,
          OOB_INST_MODEXP_EXTRACT);

  public void call(OobCall oobCall) {
    // TODO: this logic can be refined. It is quick-and-dirty for now
    boolean isPrecompile = PRECOMPILES_HANDLED_BY_OOB.contains(oobCall.oobInstruction());
    int blake2FCallNumber =
        switch (oobCall.oobInstruction()) {
          case OOB_INST_BLAKE_CDS -> 1;
          case OOB_INST_BLAKE_PARAMS -> 2;
          default -> 0;
        };
    int modexpCallNumber =
        switch (oobCall.oobInstruction()) {
          case OOB_INST_MODEXP_CDS -> 1;
          case OOB_INST_MODEXP_XBS -> 2;
          case OOB_INST_MODEXP_LEAD -> 3;
          case OOB_INST_MODEXP_PRICING -> 4;
          case OOB_INST_MODEXP_EXTRACT -> 5;
          default -> 0;
        };
    OobOperation oobOperation =
        new OobOperation(
            oobCall,
            hub.messageFrame(),
            add,
            mod,
            wcp,
            hub,
            isPrecompile,
            blake2FCallNumber,
            modexpCallNumber);
    this.chunks.add(oobOperation);
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) { // TODO: maybe move in the hub
    /*
    oobOperation = new OobOperation(frame, add, mod, wcp, hub, false, 0, 0);
    this.chunks.add(oobOperation);
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());

    if (opCode.isCall()) {
      Address target = Words.toAddress(frame.getStackItem(1));

      if (PRECOMPILES_HANDLED_BY_OOB.contains(target)) {
        if (target.equals(Address.BLAKE2B_F_COMPRESSION)) {
          oobOperation = new OobOperation(frame, add, mod, wcp, hub, true, 1, 0);
          this.chunks.add(oobOperation);
          boolean validCds = oobOperation.getOutgoingResLo()[0].equals(BigInteger.ONE);
          if (validCds) {
            oobOperation = new OobOperation(frame, add, mod, wcp, hub, true, 2, 0);
            this.chunks.add(oobOperation);
          }
        } else if (target.equals(Address.MODEXP)) {
          for (int i = 1; i <= 7; i++) {
            oobOperation = new OobOperation(frame, add, mod, wcp, hub, true, 0, i);
            this.chunks.add(oobOperation);
          }
        } else {
          // Other precompiles case
          oobOperation = new OobOperation(frame, add, mod, wcp, hub, true, 0, 0);
          this.chunks.add(oobOperation);
        }
      }
    }
    */
  }

  final void traceChunk(final OobOperation chunk, int stamp, Trace trace) {
    int nRows = chunk.nRows();

    for (int ct = 0; ct < nRows; ct++) {
      trace = chunk.getOobCall().trace(trace);

      // Note: if a value is bigger than 128, do not use Bytes.of and use Bytes.ofUnsignedType
      // instead (according to size)
      trace
          .stamp(stamp)
          .ct((short) ct)
          .ctMax((short) chunk.maxCt())
          .oobInst(bigIntegerToBytes(chunk.getOobInst()))
          .isJump(chunk.isJump())
          .isJumpi(chunk.isJumpi())
          .isRdc(chunk.isRdc())
          .isCdl(chunk.isCdl())
          .isXcall(chunk.isXCall())
          .isCall(chunk.isCall())
          .isCreate(chunk.isCreate())
          .isSstore(chunk.isSstore())
          .isDeployment(chunk.isDeployment())
          .isEcrecover(chunk.isEcRecover())
          .isSha2(chunk.isSha2())
          .isRipemd(chunk.isRipemd())
          .isIdentity(chunk.isIdentity())
          .isEcadd(chunk.isEcadd())
          .isEcmul(chunk.isEcmul())
          .isEcpairing(chunk.isEcpairing())
          .isBlake2FCds(chunk.isBlake2FCds())
          .isBlake2FParams(chunk.isBlake2FParams())
          .isModexpCds(chunk.isModexpCds())
          .isModexpXbs(chunk.isModexpXbs())
          .isModexpLead(chunk.isModexpLead())
          .isModexpPricing(chunk.isPrcModexpPricing())
          .isModexpExtract(chunk.isPrcModexpExtract())
          .addFlag(chunk.getAddFlag()[ct])
          .modFlag(chunk.getModFlag()[ct])
          .wcpFlag(chunk.getWcpFlag()[ct])
          .outgoingInst(chunk.getOutgoingInst()[ct])
          .outgoingData1(bigIntegerToBytes(chunk.getOutgoingData1()[ct]))
          .outgoingData2(bigIntegerToBytes(chunk.getOutgoingData2()[ct]))
          .outgoingData3(bigIntegerToBytes(chunk.getOutgoingData3()[ct]))
          .outgoingData4(bigIntegerToBytes(chunk.getOutgoingData4()[ct]))
          .outgoingResLo(bigIntegerToBytes(chunk.getOutgoingResLo()[ct]))
          .validateRow();
    }
  }

  @Override
  public void enterTransaction() {
    this.chunks.enter();
  }

  @Override
  public void popTransaction() {
    this.chunks.pop();
  }

  @Override
  public int lineCount() {
    return this.chunks.stream().mapToInt(OobOperation::nRows).sum();
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    Trace trace = new Trace(buffers);
    for (int i = 0; i < this.chunks.size(); i++) {
      this.traceChunk(this.chunks.get(i), i + 1, trace);
    }
  }

  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }
}
