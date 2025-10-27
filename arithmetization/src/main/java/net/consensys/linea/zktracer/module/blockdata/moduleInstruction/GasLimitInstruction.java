package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.Blockdata.nROWS_GL;
import static net.consensys.linea.zktracer.Trace.GAS_LIMIT_ADJUSTMENT_FACTOR;

import java.math.BigInteger;

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

public class GasLimitInstruction extends BlockDataInstruction {

  private final boolean firstBlockInConflation;
  private final EWord gasLimitMinimum;
  private final EWord gasLimitMaximum;

  public GasLimitInstruction(
      OpCode opCode,
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber) {
    super(opCode, chain, hub, wcp, euc, blockHeader, prevBlockHeader, firstBlockNumber);
    this.firstBlockInConflation = (blockHeader.getNumber() == firstBlockNumber);
    this.gasLimitMinimum = EWord.of(chain.gasLimitMinimum);
    this.gasLimitMaximum = EWord.of(chain.gasLimitMaximum);
  }

  public void handle() {
    data = EWord.of(blockHeader.getGasLimit());

    // row i
    // comparison to minimum
    exoCalls[0] = BlockDataExoCall.callToGEQ(this.wcp, data, gasLimitMinimum);

    // row i + 1
    // comparison to maximum
    exoCalls[1] = BlockDataExoCall.callToGEQ(this.wcp, data, gasLimitMaximum);

    if (!firstBlockInConflation) {
      final BigInteger prevGasLimit = BigInteger.valueOf(prevBlockHeader.getGasLimit());
      // row i + 2
      exoCalls[2] =
          BlockDataExoCall.callToEUC(
              this.euc, EWord.of(prevGasLimit), EWord.of(GAS_LIMIT_ADJUSTMENT_FACTOR));
      Bytes maxDeviation = exoCalls[2].res();

      final BigInteger gasLimitDeviationUpperBound =
          prevGasLimit.add(maxDeviation.toUnsignedBigInteger());
      final BigInteger gasLimitDeviationLowerBound =
          prevGasLimit.subtract(maxDeviation.toUnsignedBigInteger());
      // row i + 3
      exoCalls[3] =
          BlockDataExoCall.callToLT(this.wcp, data, EWord.of(gasLimitDeviationUpperBound));
      // row i + 4
      exoCalls[4] =
          BlockDataExoCall.callToGT(this.wcp, data, EWord.of(gasLimitDeviationLowerBound));
    }
  }

  public int nbRows() {
    return nROWS_GL;
  }

  public void traceInstruction(Trace.Blockdata trace) {
    trace.isGaslimit(true);
  }
}
