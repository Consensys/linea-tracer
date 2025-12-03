package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_NB;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class NumberInstruction extends BlockDataInstruction {

  private final boolean firstBlockInConflation;

  public NumberInstruction(
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber) {
    super(OpCode.NUMBER, chain, hub, wcp, euc, blockHeader, prevBlockHeader, firstBlockNumber);
    this.firstBlockInConflation = (blockHeader.getNumber() == firstBlockNumber);
  }

  public void handle() {
    data = EWord.of(blockHeader.getNumber());

    exoCalls[0] = BlockDataExoCall.callToIsZero(this.wcp, EWord.of(firstBlockNumber));

    // row i
    if (firstBlockInConflation) {
      exoCalls[1] = BlockDataExoCall.callToLT(this.wcp, data, POWER_256_8);
    }
  }

  public int nbRows() {
    return nROWS_NB;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    trace.isNumber(true);
  }
}
