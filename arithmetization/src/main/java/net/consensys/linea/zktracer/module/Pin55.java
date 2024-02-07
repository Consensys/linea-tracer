/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.module.hub.Hub;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.DelegatingBytes;
import org.hyperledger.besu.datatypes.Quantity;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.ProcessableBlockHeader;

@Slf4j
@RequiredArgsConstructor
public class Pin55 {
  public static class PinLevel {
    private static final int CONFLATION = 1;
    private static final int BLOCK = 1 << 2;
    private static final int TX = 1 << 3;
    private static final int INST = 1 << 4;
    private static final int CONTEXT = 1 << 5;
    private static final int STACK = 1 << 6;

    int level;

    private PinLevel(int x) {
      this.level = x;
    }

    public PinLevel() {
      this.level = 0;
    }

    public PinLevel all() {
      this.level = 0xffff;
      return this;
    }

    public boolean none() {
      return this.level == 0;
    }

    public PinLevel conflation() {
      this.level |= CONFLATION;
      return this;
    }

    public PinLevel block() {
      this.level |= BLOCK;
      return this;
    }

    public PinLevel tx() {
      this.level |= TX;
      return this;
    }

    public PinLevel context() {
      this.level |= CONTEXT;
      return this;
    }

    public PinLevel opCode() {
      this.level |= INST;
      return this;
    }

    public PinLevel stack() {
      this.level |= STACK;
      return this;
    }

    public boolean doConflation() {
      return (this.level & CONFLATION) > 0;
    }

    public boolean doBlock() {
      return (this.level & BLOCK) > 0;
    }

    public boolean doTx() {
      return (this.level & TX) > 0;
    }

    public boolean doContext() {
      return (this.level & CONTEXT) > 0;
    }

    public boolean doOpcode() {
      return (this.level & INST) > 0;
    }

    public boolean doStack() {
      return (this.level & STACK) > 0;
    }
  }

  private static final int MAX_STACK_ELT_DISPLAY = 5;
  private final PinLevel l;
  private final Hub hub;

  public void traceStartConflation(long blockCount) {
    if (!this.l.doConflation()) {
      return;
    }
    log.info("=== Start conflation ===");
  }

  public void traceEndConflation() {
    if (!this.l.doConflation()) {
      return;
    }
    log.info("=== Stop conflation ===");
  }

  public void traceStartBlock(ProcessableBlockHeader processableBlockHeader, final BlockBody body) {
    if (!this.l.doBlock()) {
      return;
    }
    log.info(
        "== Enter block {} ({} txs.)",
        processableBlockHeader.getNumber(),
        body.getTransactions().size());
    log.info("    miner:     {}", processableBlockHeader.getCoinbase());
    log.info("    gas limit: {}", processableBlockHeader.getGasLimit());
    log.info(
        "    base fee:  {}",
        processableBlockHeader.getBaseFee().map(Quantity::toHexString).orElse("N/A"));
  }

  public void traceEndBlock() {
    if (!this.l.doBlock()) {
      return;
    }
    log.info("== End of block");
  }

  public void traceStartTx(WorldView worldView, Transaction tx) {
    if (!this.l.doTx()) {
      return;
    }
    log.info("= Starting transaction {}", tx.getHash());
    log.info(" -- General --");
    log.info("  nonce: {}", tx.getNonce());
    log.info("  from:  {}", tx.getSender());
    log.info("  to:    {}", tx.getTo().map(DelegatingBytes::toString).orElse("NONE"));
    log.info("  type:  {}", tx.getType());
    log.info("  value: {}", tx.getValue().toHexString());
    log.info("  data:  {}B", tx.getPayload().size());
    log.info(" -- Gas info ---");
    log.info("  gas limit:              {}", tx.getGasLimit());
    log.info(
        "  gas price:              {}", tx.getGasPrice().map(Quantity::toHexString).orElse("N/A"));
    log.info(
        "  max. fee per gas:       {}",
        tx.getMaxFeePerGas().map(Quantity::toHexString).orElse("N/A"));
    log.info(
        "  max. prio. fee per gas: {}",
        tx.getMaxPriorityFeePerGas().map(Quantity::toHexString).orElse("N/A"));
  }

  public void traceEndTx(
      WorldView worldView,
      Transaction tx,
      boolean isSuccessful,
      Bytes output,
      List<Log> logs,
      long gasUsed) {
    if (!this.l.doTx()) {
      return;
    }
    log.info("= Ending transaction: {}", isSuccessful ? "SUCCESS" : "FAILURE");
  }

  public void traceContextEnter(final MessageFrame frame) {
    if (!this.l.doContext()) {
      return;
    }
    log.info(
        "--> ID: {} CN: {} {} @ {}",
        hub.currentFrame().id(),
        hub.currentFrame().contextNumber(),
        hub.currentFrame().type(),
        hub.currentFrame().codeAddress().toUnprefixedHexString());
  }

  public void traceContextReEnter(final MessageFrame frame) {
    if (!this.l.doContext()) {
      return;
    }
    log.info(
        "<-> ID: {} CN: {} {} @ {}",
        hub.currentFrame().id(),
        hub.currentFrame().contextNumber(),
        hub.currentFrame().type(),
        hub.currentFrame().codeAddress().toUnprefixedHexString());
  }

  public void traceContextExit(final MessageFrame frame) {
    if (!this.l.doContext()) {
      return;
    }
    log.info(
        "<-- ID: {} CN: {} {} @ {}",
        hub.currentFrame().id(),
        hub.currentFrame().contextNumber(),
        hub.currentFrame().type(),
        hub.currentFrame().codeAddress().toUnprefixedHexString());
  }

  public void tracePreOpcode(final MessageFrame frame) {
    if (!this.l.doOpcode()) {
      return;
    }

    log.info(
        "{}@{} {}",
        Integer.toHexString(hub.currentFrame().pc()),
        hub.currentFrame().id(),
        hub.opCode());
  }

  public void tracePostOpcode(final MessageFrame frame) {
    if (!this.l.doStack()) {
      return;
    }

    final int stackSize = Math.min(MAX_STACK_ELT_DISPLAY, frame.stackSize());
    final StringBuilder s = new StringBuilder(MAX_STACK_ELT_DISPLAY * 33);
    s.append(">>| ");
    for (int i = 0; i < stackSize; i++) {
      s.append(frame.getStackItem(i).toQuantityHexString());
      if (i == stackSize - 1) {
        s.append(" ]");
      } else {
        s.append(" | ");
      }
    }
    log.info("{}", s);
  }
}
