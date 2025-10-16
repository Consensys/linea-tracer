package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_CB;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class CoinbaseInstruction extends BlockDataInstruction {

  private final int relBlock;

  public CoinbaseInstruction(
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber) {
    super(chain, hub, wcp, euc, blockHeader, prevBlockHeader, firstBlockNumber);
    this.relBlock = (int) (blockHeader.getNumber() - firstBlockNumber + 1);
  }

  public void handle() {
    EWord data = EWord.ofHexString(this.hub.coinbaseAddressOfRelativeBlock(relBlock).toHexString());
    // row i
    exoCalls[0] = BlockDataExoCall.callToLT(this.wcp, data, POWER_256_20);
  }

  public int nbRows() {
    return nROWS_CB;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    trace.isCoinbase(true);
  }
}
