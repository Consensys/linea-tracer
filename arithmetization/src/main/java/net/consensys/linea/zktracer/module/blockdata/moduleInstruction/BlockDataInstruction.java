package net.consensys.linea.zktracer.module.blockdata.moduleInstruction;

import static net.consensys.linea.zktracer.Trace.TWOFIFTYSIX_TO_THE_TWENTY;

import java.math.BigInteger;

import net.consensys.linea.zktracer.ChainConfig;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.blockdata.BlockDataExoCall;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.plugin.data.BlockHeader;

public abstract class BlockDataInstruction {
  public final Hub hub;
  public final Wcp wcp;
  public final Euc euc;
  public final BlockHeader blockHeader;
  public final BlockHeader prevBlockHeader;
  public final ChainConfig chainConfig;
  public final long firstBlockNumber;

  public static final EWord POWER_256_20 = EWord.of(TWOFIFTYSIX_TO_THE_TWENTY);
  public static final EWord POWER_256_8 = EWord.of(BigInteger.ONE.shiftLeft(8 * 8));

  /** Store all wcp and euc computations with params and results */
  // TODO ctMax vs nbRows
  public final BlockDataExoCall[] exoCalls = new BlockDataExoCall[nbRows()];

  public BlockDataInstruction(
      ChainConfig chain,
      Hub hub,
      Wcp wcp,
      Euc euc,
      BlockHeader blockHeader,
      BlockHeader prevBlockHeader,
      long firstBlockNumber) {
    this.hub = hub;
    this.wcp = wcp;
    this.euc = euc;
    this.blockHeader = blockHeader;
    this.chainConfig = chain;
    this.firstBlockNumber = firstBlockNumber;
    this.prevBlockHeader = prevBlockHeader;
  }

  public abstract void handle();

  public abstract int nbRows();

  public abstract void traceInstruction(Trace.Blockdata trace);
}
