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

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

/** Implementation of a {@link Module} for out of bounds. */
public class Oob implements Module {

  /** A list of the operations to trace */
  @Getter private final StackedList<OobChunk> chunks = new StackedList<>();

  private final Hub hub;
  private final Add add;
  private final Mod mod;
  private final Wcp wcp;

  @Override
  public String moduleKey() {
    return "OOB";
  }

  public Oob(Hub hub, Add add, Mod mod, Wcp wcp) {
    this.hub = hub;
    this.add = add;
    this.mod = mod;
    this.wcp = wcp;
  }

  Address[] precompilesHandledByOob =
      new Address[] {
        Address.ECREC,
        Address.SHA256,
        Address.RIPEMD160,
        Address.ID,
        Address.ALTBN128_ADD,
        Address.ALTBN128_MUL,
        Address.ALTBN128_PAIRING,
        Address.BLAKE2B_F_COMPRESSION
      };

  @Override
  public void tracePreOpcode(MessageFrame frame) { // This will be renamed to tracePreOp
    this.chunks.add(new OobChunk(frame, add, mod, wcp, hub, false, 0, 0));
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());

    if (hub.pch().exceptions().none() && hub.pch().aborts().none() && opCode.isCall()) {
      Address target = Words.toAddress(frame.getStackItem(1));

      if (Arrays.asList(precompilesHandledByOob).contains(target)) {
        if (target.equals(Address.BLAKE2B_F_COMPRESSION)) {
          OobChunk oobChunk = new OobChunk(frame, add, mod, wcp, hub, true, 1, 0);
          this.chunks.add(oobChunk);
          boolean validCds = oobChunk.getOutgoingResLo()[0].equals(BigInteger.ONE);
          if (validCds) {
            this.chunks.add(new OobChunk(frame, add, mod, wcp, hub, true, 2, 0));
          }
        } else if (target.equals(Address.MODEXP)) {
          for (int i = 1; i <= 7; i++) {
            this.chunks.add(new OobChunk(frame, add, mod, wcp, hub, true, 0, i));
          }
        } else {
          // Other precompiles case
          this.chunks.add(new OobChunk(frame, add, mod, wcp, hub, true, 0, 0));
        }
      }
    }
  }

  final void traceChunk(final OobChunk chunk, int stamp, Trace trace) {
    int nRows = chunk.nRows();

    for (int ct = 0; ct < nRows; ct++) {
      trace = chunk.getOobParameters().trace(trace);

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
    return this.chunks.stream().mapToInt(OobChunk::nRows).sum();
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
