package net.consensys.linea.zktracer.module.rlp_txrcpt;

import java.math.BigInteger;

import org.apache.tuweni.bytes.Bytes;

public class RlpTxrcptColumnsValue {
  public Bytes ACC_1;
  public Bytes ACC_2;
  public Bytes ACC_3;
  public Bytes ACC_4;
  public int ACC_SIZE;
  public boolean BIT;
  public int BIT_ACC;
  public byte BYTE_1;
  public byte BYTE_2;
  public byte BYTE_3;
  public byte BYTE_4;
  public int COUNTER;
  public boolean DEPTH_1;
  public int INDEX;
  public int INDEX_LOCAL;
  public Bytes INPUT_1;
  public Bytes INPUT_2;
  public Bytes INPUT_3;
  public Bytes INPUT_4;
  public boolean IS_DATA;
  public boolean IS_PREFIX;
  public boolean IS_TOPIC;
  public boolean LC_CORRECTION;
  public BigInteger LIMB;
  public boolean LIMB_CONSTRUCTED;
  public int LOCAL_SIZE;
  public int LOG_ENTRY_SIZE;
  public int nBYTES;
  public int nSTEP;
  public int phase;
  public boolean PHASE_END;
  public int PHASE_SIZE;
  public BigInteger POWER;
  public int TXRCPT_SIZE;

  public void PartialReset(int phase, int nStep) {
    this.phase = phase;
    this.nSTEP = nStep;

    /** Set to default local values */
    this.ACC_1 = Bytes.ofUnsignedShort(0);
    this.ACC_2 = Bytes.ofUnsignedShort(0);
    this.ACC_3 = Bytes.ofUnsignedShort(0);
    this.ACC_4 = Bytes.ofUnsignedShort(0);
    this.ACC_SIZE = 0;
    this.BIT = false;
    this.BIT_ACC = 0;
    this.BYTE_1 = 0;
    this.BYTE_2 = 0;
    this.BYTE_3 = 0;
    this.BYTE_4 = 0;
    this.COUNTER = 0;
    this.DEPTH_1 = false;
    this.INPUT_1 = Bytes.ofUnsignedShort(0);
    this.INPUT_2 = Bytes.ofUnsignedShort(0);
    this.INPUT_3 = Bytes.ofUnsignedShort(0);
    this.INPUT_4 = Bytes.ofUnsignedShort(0);
    this.IS_DATA = false;
    this.IS_PREFIX = false;
    this.IS_TOPIC = false;
    this.LC_CORRECTION = false;
    this.LIMB = BigInteger.ZERO;
    this.LIMB_CONSTRUCTED = false;
    this.nBYTES = 0;
    this.PHASE_END = false;
    this.POWER = BigInteger.valueOf(0);
  }
}
