package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.TraceCancun.Blockdata.nROWS_BL;
import static net.consensys.linea.zktracer.types.PublicInputs.LINEA_BLOB_BASE_FEE_BYTES;

import java.util.Map;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class BlobBaseFeeInstruction extends BlockDataInstruction {

  final EWord blobBaseFee;

  public BlobBaseFeeInstruction(
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber,
      Map<Long, Bytes> blobBaseFees) {
    super(OpCode.BLOBBASEFEE, chain, hub, wcp, euc, blockHeader, prevBlockHeader, firstBlockNumber);
    this.blobBaseFee =
        EWord.of(blobBaseFees.getOrDefault(blockHeader.getNumber(), LINEA_BLOB_BASE_FEE_BYTES));
  }

  public void handle() {
    data = this.blobBaseFee;

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
