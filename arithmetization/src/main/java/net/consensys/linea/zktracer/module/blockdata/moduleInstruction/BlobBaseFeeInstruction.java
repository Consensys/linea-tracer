package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.LINEA_BLOB_BASE_FEE;
import static net.consensys.linea.zktracer.TraceCancun.Blockdata.nROWS_BL;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class BlobBaseFeeInstruction extends BlockDataInstruction {

  public BlobBaseFeeInstruction(
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
    data = EWord.of(LINEA_BLOB_BASE_FEE);

    // row i
    exoCalls[0] = BlockDataExoCall.callToGEQ(this.wcp, data, EWord.ZERO);
  }

  public int nbRows() {
    return nROWS_BL;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    trace.isBlobbasefee(true);
  }
}
