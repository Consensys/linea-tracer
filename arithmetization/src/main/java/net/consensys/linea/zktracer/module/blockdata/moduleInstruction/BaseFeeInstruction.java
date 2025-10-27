package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_BF;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class BaseFeeInstruction extends BlockDataInstruction {

  public BaseFeeInstruction(
      OpCode opCode,
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber) {
    super(opCode, chain, hub, wcp, euc, blockHeader, prevBlockHeader, firstBlockNumber);
  }

  public void handle() {
    data = EWord.of(blockHeader.getBaseFee().get().getAsBigInteger());

    // row i
    exoCalls[0] = BlockDataExoCall.callToLT(this.wcp, data, EWord.ZERO);
  }

  public int nbRows() {
    return nROWS_BF;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    trace.isBasefee(true);
  }
}
