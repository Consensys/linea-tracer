package net.consensys.linea.zktracer.module.rlp_txn;

import java.math.BigInteger;

import org.apache.tuweni.bytes.Bytes;

public class RlpTxnColumnsValue {
  public int ABS_TX_NUM;
  public int ABS_TX_NUM_INFINY;
  public Bytes ACC_1;
  public Bytes ACC_2;
  public int ACC_BYTESIZE;
  public int ACCESS_TUPLE_BYTESIZE;
  public Bytes ADDR_HI;
  public Bytes ADDR_LO;
  public boolean BIT;
  public int BIT_ACC;
  public byte BYTE_1;
  public byte BYTE_2;
  public int CODE_FRAGMENT_INDEX;
  public int COUNTER;
  public BigInteger DATA_HI;
  public BigInteger DATA_LO;
  public int DATAGASCOST;
  public boolean DEPTH_1;
  public boolean DEPTH_2;
  public boolean PHASE_END;
  public int INDEX_DATA;
  public int INDEX_LT;
  public int INDEX_LX;
  public Bytes INPUT_1;
  public Bytes INPUT_2;
  public boolean LC_CORRECTION;
  public boolean IS_PREFIX;
  public BigInteger LIMB;
  public boolean LIMB_CONSTRUCTED;
  public boolean LT;
  public boolean LX;
  public int nBYTES;
  public int nb_Addr;
  public int nb_Sto;
  public int nb_Sto_per_Addr;
  public int nSTEP;
  public int phase;
  public int PHASE_BYTESIZE;
  public BigInteger POWER;
  public boolean REQUIRES_EVM_EXECUTION;
  public int RLP_LT_BYTESIZE;
  public int RLP_LX_BYTESIZE;
  public int TYPE;

  public void partialReset(int phase, int number_step, boolean LT, boolean LX) {
    this.phase = phase;
    this.nSTEP = number_step;
    this.LT = LT;
    this.LX = LX;

    /** Set to default local values */
    this.LIMB_CONSTRUCTED = false;
    this.ACC_1 = Bytes.EMPTY;
    this.ACC_2 = Bytes.EMPTY;
    this.ACC_BYTESIZE = 0;
    this.ADDR_HI = Bytes.ofUnsignedShort(0);
    this.ADDR_LO = Bytes.ofUnsignedShort(0);
    this.BIT = false;
    this.BIT_ACC = 0;
    this.BYTE_1 = 0;
    this.BYTE_2 = 0;
    this.COUNTER = 0;
    this.DEPTH_1 = false;
    this.DEPTH_2 = false;
    this.PHASE_END = false;
    this.INPUT_1 = Bytes.EMPTY;
    this.INPUT_2 = Bytes.EMPTY;
    this.LC_CORRECTION = false;
    this.IS_PREFIX = false;
    this.LIMB = BigInteger.ZERO;
    this.nBYTES = 0;
    this.POWER = BigInteger.ZERO;
  }

  public void DataHiLoReset() {
    this.DATA_HI = BigInteger.ZERO;
    this.DATA_LO = BigInteger.ZERO;
  }
}
