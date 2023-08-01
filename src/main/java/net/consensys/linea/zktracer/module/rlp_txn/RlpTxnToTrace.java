package net.consensys.linea.zktracer.module.rlp_txn;

import java.math.BigInteger;

import org.apache.tuweni.bytes.Bytes;

public class RlpTxnToTrace {
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
  public boolean COMP;
  public int COUNTER;
  public int DATA_HI;
  public int DATA_LO;
  public int DATAGASCOST;
  public boolean DEPTH_1;
  public boolean DEPTH_2;
  public boolean end_phase;
  public int INDEX_DATA;
  public int INDEX_LT;
  public int INDEX_LX;
  public Bytes INPUT_1;
  public Bytes INPUT_2;
  public boolean is_bytesize;
  public boolean is_list;
  public boolean is_padding;
  public boolean is_prefix;
  public Bytes LIMB;
  public boolean LIMB_CONSTRUCTED;
  public boolean LT;
  public boolean LX;
  public int nBYTES;
  public int nb_Addr;
  public int nb_Sto;
  public int nb_Sto_per_Addr;
  public int number_step;

  public int phase;
  public int PHASE_BYTESIZE;
  public BigInteger POWER;
  public boolean REQUIRES_EVM_EXECUTION;
  public int RLP_LT_BYTESIZE;
  public int RLP_LX_BYTESIZE;
  public int TYPE;

  public RlpTxnToTrace PartialReset(
      RlpTxnToTrace data, int phase, int number_step, boolean LT, boolean LX) {
    data.phase = phase;
    data.number_step = number_step;
    data.LT = LT;
    data.LX = LX;

    /** Set to default local values */
    data.LIMB_CONSTRUCTED = false;
    data.ACC_1 = 0;
    data.ACC_2 = 0;
    data.ACC_BYTESIZE = 0;
    data.ADDR_HI = Bytes.ofUnsignedShort(0);
    data.ADDR_LO = Bytes.ofUnsignedShort(0);
    data.BIT = false;
    data.BIT_ACC = 0;
    data.BYTE_1 = 0;
    data.BYTE_2 = 0;
    data.COMP = false;
    data.COUNTER = 0;
    data.DEPTH_1 = false;
    data.DEPTH_2 = false;
    data.end_phase = false;
    data.INPUT_1 = 0;
    data.INPUT_2 = 0;
    data.is_bytesize = false;
    data.is_list = false;
    data.is_padding = false;
    data.is_prefix = false;
    data.LIMB = 0;
    data.nBYTES = 0;
    data.POWER = BigInteger.ZERO;

    return data;
  }
}
