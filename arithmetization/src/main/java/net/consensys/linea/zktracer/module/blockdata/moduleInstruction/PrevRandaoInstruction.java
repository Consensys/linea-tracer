package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.TraceShanghai.Blockdata.nROWS_PV;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class PrevRandaoInstruction extends BlockDataInstruction {

  public PrevRandaoInstruction(
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber) {
    super(chain, hub, wcp, euc, blockHeader, prevBlockHeader, firstBlockNumber);
  }

  public void handle() {
    EWord data = EWord.of(blockHeader.getDifficulty().getAsBigInteger());

    // row i
    exoCalls[0] = BlockDataExoCall.callToLT(this.wcp, data, EWord.ZERO);
  }

  public int nbRows() {
    return nROWS_PV;
  }

  // TODO reverse for Paris
  public void traceInstruction(Trace.Blockdata trace) {
    trace.isPrevrandao(true);
  }
}
