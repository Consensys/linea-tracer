package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_ID;
import static net.consensys.linea.zktracer.Trace.LLARGE;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class ChainIdInstruction extends BlockDataInstruction {

  private final OpCode opCode = OpCode.CHAINID;
  private final Wcp wcp;
  private EWord data;
  private final int chainId;
  private final Hub hub;
  private final int relBlock;
  private final BlockHeader blockHeader;
  private final long firstBlockNumber;
  private final int relTxMax;

  public ChainIdInstruction(
      Hub hub,
      Wcp wcp,
      int chainId,
      int relBlock,
      BlockHeader blockHeader,
      long firstBlockNumber,
      int relTxMax,
      EWord data) {
    this.chainId = chainId;
    this.wcp = wcp;
    this.blockHeader = blockHeader;
    this.data = data;
    this.hub = hub;
    this.relBlock = relBlock;
    this.firstBlockNumber = firstBlockNumber;
    this.relTxMax = relTxMax;
  }

  public void handle() {
    this.data = EWord.of(chainId);
    // row i
    exoCalls[0] = BlockDataExoCall.callToGEQ(wcp, this.data, EWord.ZERO);
  }

  public int nbRows() {
    return nROWS_ID;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    for (short ct = 0; ct < nbRows(); ct++) {
      trace
          .iomf(true)
          .ctMax(nbRows() - 1)
          .ct(ct)
          .isChainid(true)
          .inst(opCode.unsignedByteValue()) // not fork dependant
          .coinbaseHi(hub.coinbaseAddressOfRelativeBlock(relBlock).slice(0, 4).toLong())
          .coinbaseLo(hub.coinbaseAddressOfRelativeBlock(relBlock).slice(4, LLARGE))
          .blockGasLimit(Bytes.ofUnsignedLong(blockHeader.getGasLimit()))
          .basefee(bigIntegerToBytes(blockHeader.getBaseFee().get().getAsBigInteger()))
          .firstBlockNumber(firstBlockNumber)
          .relBlock((short) relBlock);
      // traceRelTxNumMax(trace, (short) relTxMax);
      trace
          .dataHi(data.hi())
          .dataLo(data.lo())
          .arg1Hi(exoCalls[ct].arg1Hi())
          .arg1Lo(exoCalls[ct].arg1Lo())
          .arg2Hi(exoCalls[ct].arg2Hi())
          .arg2Lo(exoCalls[ct].arg2Lo())
          .res(exoCalls[ct].res())
          .exoInst(exoCalls[ct].instruction())
          .wcpFlag(exoCalls[ct].wcpFlag())
          .eucFlag(exoCalls[ct].eucFlag())
          .fillAndValidateRow();
    }
    ;
  }
}
