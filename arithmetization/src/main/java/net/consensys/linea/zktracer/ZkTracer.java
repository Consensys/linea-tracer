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

package net.consensys.linea.zktracer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.add.ORCWriter;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCodes;
import org.apache.orc.Writer;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;
import org.hyperledger.besu.evm.gascalculator.LondonGasCalculator;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;

@RequiredArgsConstructor
@Slf4j
public class ZkTracer implements ZkBlockAwareOperationTracer {
    /**
     * The {@link GasCalculator} used in this version of the arithmetization
     */
    public static final GasCalculator gasCalculator = new LondonGasCalculator();

    private final ZkTraceBuilder zkTraceBuilder = new ZkTraceBuilder();
    private final Hub hub;

    public ZkTracer() {
        // Load opcodes configured in src/main/resources/opcodes.yml.
        OpCodes.load();

        this.hub = new Hub();
    }

    public ZkTrace getTrace() {
//    for (Module<?> module : this.hub.getModulesToTrace()) {
//      zkTraceBuilder.addTrace(module);
//    }
//    return zkTraceBuilder.build();
        try {
            this.writeToFile("franklin.lt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return zkTraceBuilder.build();
    }

    public void hugeWriteToFile(String filename) throws IOException {
//    final List<ColumnHeader> traceMap =
//        this.hub.getModulesToTrace().stream().flatMap(m -> m.columnsHeaders().stream()).toList();
//    final long headerSize = traceMap.stream().mapToInt(ColumnHeader::headerSize).sum();
//    final long traceSize = traceMap.stream().mapToLong(ColumnHeader::cumulatedSize).sum();
//
//    assert traceSize < Integer.MAX_VALUE;
        //    final Path path = Paths.get("/Users/franklin/pipo.hex");

        //    try (ResourceScope scope = ResourceScope.newConfinedScope()) {
        //      MemorySegment mmap = MemorySegment.mapFile(path, 0, traceSize,
        // FileChannel.MapMode.READ_WRITE, scope);

        // ByteBuffer header = mmap.asSlice(0, headerSize).asByteBuffer();
        //      for (ColumnHeader h : traceMap) {
        //        final String name = h.name();
        //        assert h.name().length() < 128;
        //        header.put((byte) name.length());
        //        for (int i = 0; i < name.length(); i++) {
        //          header.putChar(name.charAt(i));
        //        }
        //        header.put(h.bitPerElement());
        //        header.putInt(h.length());
        //      }

        //      mmap.force();
        //    }
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    public void writeToFile(String filename) throws IOException {
        log.warn("[TRACING] starting tracing");

//      int i = (int)headerSize;
        for (Module<?> m : this.hub.getModulesToTrace()) {

            Stopwatch sw = Stopwatch.createStarted();
            switch (m.jsonKey().toUpperCase()) {
                case "ADD" -> {
                    try (Writer writer = ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "ROM" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.rom.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "MUL" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.mul.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "ROMLEX" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.romLex.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "EXT" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.ext.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "MOD" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.mod.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "MXP" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.mxp.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "RLPTXN" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.rlp_txn.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                case "RLPADDR" -> {
                    try (Writer writer =  net.consensys.linea.zktracer.module.rlpAddr.ORCWriter.getWriter(filename)) {
                        m.commitToBuffer(writer);
                    }
                }
                default -> {
                }
            }
            log.warn("[TRACING] done for module {}, it took {}ms.", m.jsonKey(), sw.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @Override
    public void traceStartConflation(final long numBlocksInConflation) {
        hub.traceStartConflation(numBlocksInConflation);
    }

    @Override
    public void traceEndConflation() {
        this.hub.traceEndConflation();
    }

    @Override
    public String getJsonTrace() {
        return getTrace().toJson();
    }

    @Override
    public void traceStartBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
        this.hub.traceStartBlock(blockHeader, blockBody);
    }

    @Override
    public void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
        this.hub.traceEndBlock(blockHeader, blockBody);
    }

    @Override
    public void traceStartTransaction(WorldView worldView, Transaction transaction) {
        this.hub.traceStartTx(worldView, transaction);
    }

    @Override
    public void traceEndTransaction(
            WorldView worldView,
            Transaction tx,
            boolean status,
            Bytes output,
            List<Log> logs,
            long gasUsed,
            long timeNs) {
        this.hub.traceEndTx(worldView, tx, status, output, logs, gasUsed);
    }

    @Override
    public void tracePreExecution(final MessageFrame frame) {
        this.hub.tracePreOpcode(frame);
    }

    @Override
    public void tracePostExecution(MessageFrame frame, Operation.OperationResult operationResult) {
        this.hub.tracePostExecution(frame, operationResult);
    }

    @Override
    public void traceContextEnter(MessageFrame frame) {
        // We only want to trigger on creation of new contexts, not on re-entry in existing contexts
        if (frame.getState() == MessageFrame.State.NOT_STARTED) {
            this.hub.traceContextEnter(frame);
        }
    }

    @Override
    public void traceContextReEnter(MessageFrame frame) {
        this.hub.traceContextReEnter(frame);
    }

    @Override
    public void traceContextExit(MessageFrame frame) {
        this.hub.traceContextExit(frame);
    }

    /**
     * When called, erase all tracing related to the last included transaction.
     */
    public void popTransaction() {
        hub.popTransaction();
    }
}
