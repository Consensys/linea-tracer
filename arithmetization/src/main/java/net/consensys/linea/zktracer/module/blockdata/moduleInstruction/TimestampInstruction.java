package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_TS;

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

public class TimestampInstruction extends BlockDataInstruction {

  public TimestampInstruction(
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
    data = EWord.of(Bytes.ofUnsignedLong(blockHeader.getTimestamp()));
    final EWord prevData =
        prevBlockHeader == null ? EWord.ZERO : EWord.of(prevBlockHeader.getTimestamp());

    // row i
    exoCalls[0] = BlockDataExoCall.callToLT(this.wcp, data, POWER_256_8);

    // row i + 1
    exoCalls[1] = BlockDataExoCall.callToLT(this.wcp, data, prevData);
  }

  public int nbRows() {
    return nROWS_TS;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    trace.isTimestamp(true);
  }
}
