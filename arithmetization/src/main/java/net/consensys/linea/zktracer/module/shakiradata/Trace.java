/*
 * Copyright ConsenSys Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.shakiradata;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.BitSet;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

/**
 * WARNING: This code is generated automatically.
 *
 * <p>Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class Trace {
  public static final BigInteger EMPTY_KECCAK_HI =
      new BigInteger("16434357337474432580558001204043214908");
  public static final BigInteger EMPTY_KECCAK_LO =
      new BigInteger("19024806816994025362060938983270537799");
  public static final int EMPTY_RIPEMD_HI = 0x9c1185a;
  public static final BigInteger EMPTY_RIPEMD_LO =
      new BigInteger("16442052386882578548602430796343695571");
  public static final BigInteger EMPTY_SHA2_HI =
      new BigInteger("18915786244935348617899154533661473682");
  public static final BigInteger EMPTY_SHA2_LO =
      new BigInteger("3296542996298665609207448061432114053");
  public static final int EVM_INST_ADD = 0x1;
  public static final int EVM_INST_ADDMOD = 0x8;
  public static final int EVM_INST_ADDRESS = 0x30;
  public static final int EVM_INST_AND = 0x16;
  public static final int EVM_INST_BALANCE = 0x31;
  public static final int EVM_INST_BASEFEE = 0x48;
  public static final int EVM_INST_BLOCKHASH = 0x40;
  public static final int EVM_INST_BYTE = 0x1a;
  public static final int EVM_INST_CALL = 0xf1;
  public static final int EVM_INST_CALLCODE = 0xf2;
  public static final int EVM_INST_CALLDATACOPY = 0x37;
  public static final int EVM_INST_CALLDATALOAD = 0x35;
  public static final int EVM_INST_CALLDATASIZE = 0x36;
  public static final int EVM_INST_CALLER = 0x33;
  public static final int EVM_INST_CALLVALUE = 0x34;
  public static final int EVM_INST_CHAINID = 0x46;
  public static final int EVM_INST_CODECOPY = 0x39;
  public static final int EVM_INST_CODESIZE = 0x38;
  public static final int EVM_INST_COINBASE = 0x41;
  public static final int EVM_INST_CREATE = 0xf0;
  public static final int EVM_INST_CREATE2 = 0xf5;
  public static final int EVM_INST_DELEGATECALL = 0xf4;
  public static final int EVM_INST_DIFFICULTY = 0x44;
  public static final int EVM_INST_DIV = 0x4;
  public static final int EVM_INST_DUP1 = 0x80;
  public static final int EVM_INST_DUP10 = 0x89;
  public static final int EVM_INST_DUP11 = 0x8a;
  public static final int EVM_INST_DUP12 = 0x8b;
  public static final int EVM_INST_DUP13 = 0x8c;
  public static final int EVM_INST_DUP14 = 0x8d;
  public static final int EVM_INST_DUP15 = 0x8e;
  public static final int EVM_INST_DUP16 = 0x8f;
  public static final int EVM_INST_DUP2 = 0x81;
  public static final int EVM_INST_DUP3 = 0x82;
  public static final int EVM_INST_DUP4 = 0x83;
  public static final int EVM_INST_DUP5 = 0x84;
  public static final int EVM_INST_DUP6 = 0x85;
  public static final int EVM_INST_DUP7 = 0x86;
  public static final int EVM_INST_DUP8 = 0x87;
  public static final int EVM_INST_DUP9 = 0x88;
  public static final int EVM_INST_EQ = 0x14;
  public static final int EVM_INST_EXP = 0xa;
  public static final int EVM_INST_EXTCODECOPY = 0x3c;
  public static final int EVM_INST_EXTCODEHASH = 0x3f;
  public static final int EVM_INST_EXTCODESIZE = 0x3b;
  public static final int EVM_INST_GAS = 0x5a;
  public static final int EVM_INST_GASLIMIT = 0x45;
  public static final int EVM_INST_GASPRICE = 0x3a;
  public static final int EVM_INST_GT = 0x11;
  public static final int EVM_INST_INVALID = 0xfe;
  public static final int EVM_INST_ISZERO = 0x15;
  public static final int EVM_INST_JUMP = 0x56;
  public static final int EVM_INST_JUMPDEST = 0x5b;
  public static final int EVM_INST_JUMPI = 0x57;
  public static final int EVM_INST_LOG0 = 0xa0;
  public static final int EVM_INST_LOG1 = 0xa1;
  public static final int EVM_INST_LOG2 = 0xa2;
  public static final int EVM_INST_LOG3 = 0xa3;
  public static final int EVM_INST_LOG4 = 0xa4;
  public static final int EVM_INST_LT = 0x10;
  public static final int EVM_INST_MLOAD = 0x51;
  public static final int EVM_INST_MOD = 0x6;
  public static final int EVM_INST_MSIZE = 0x59;
  public static final int EVM_INST_MSTORE = 0x52;
  public static final int EVM_INST_MSTORE8 = 0x53;
  public static final int EVM_INST_MUL = 0x2;
  public static final int EVM_INST_MULMOD = 0x9;
  public static final int EVM_INST_NOT = 0x19;
  public static final int EVM_INST_NUMBER = 0x43;
  public static final int EVM_INST_OR = 0x17;
  public static final int EVM_INST_ORIGIN = 0x32;
  public static final int EVM_INST_PC = 0x58;
  public static final int EVM_INST_POP = 0x50;
  public static final int EVM_INST_PUSH1 = 0x60;
  public static final int EVM_INST_PUSH10 = 0x69;
  public static final int EVM_INST_PUSH11 = 0x6a;
  public static final int EVM_INST_PUSH12 = 0x6b;
  public static final int EVM_INST_PUSH13 = 0x6c;
  public static final int EVM_INST_PUSH14 = 0x6d;
  public static final int EVM_INST_PUSH15 = 0x6e;
  public static final int EVM_INST_PUSH16 = 0x6f;
  public static final int EVM_INST_PUSH17 = 0x70;
  public static final int EVM_INST_PUSH18 = 0x71;
  public static final int EVM_INST_PUSH19 = 0x72;
  public static final int EVM_INST_PUSH2 = 0x61;
  public static final int EVM_INST_PUSH20 = 0x73;
  public static final int EVM_INST_PUSH21 = 0x74;
  public static final int EVM_INST_PUSH22 = 0x75;
  public static final int EVM_INST_PUSH23 = 0x76;
  public static final int EVM_INST_PUSH24 = 0x77;
  public static final int EVM_INST_PUSH25 = 0x78;
  public static final int EVM_INST_PUSH26 = 0x79;
  public static final int EVM_INST_PUSH27 = 0x7a;
  public static final int EVM_INST_PUSH28 = 0x7b;
  public static final int EVM_INST_PUSH29 = 0x7c;
  public static final int EVM_INST_PUSH3 = 0x62;
  public static final int EVM_INST_PUSH30 = 0x7d;
  public static final int EVM_INST_PUSH31 = 0x7e;
  public static final int EVM_INST_PUSH32 = 0x7f;
  public static final int EVM_INST_PUSH4 = 0x63;
  public static final int EVM_INST_PUSH5 = 0x64;
  public static final int EVM_INST_PUSH6 = 0x65;
  public static final int EVM_INST_PUSH7 = 0x66;
  public static final int EVM_INST_PUSH8 = 0x67;
  public static final int EVM_INST_PUSH9 = 0x68;
  public static final int EVM_INST_RETURN = 0xf3;
  public static final int EVM_INST_RETURNDATACOPY = 0x3e;
  public static final int EVM_INST_RETURNDATASIZE = 0x3d;
  public static final int EVM_INST_REVERT = 0xfd;
  public static final int EVM_INST_SAR = 0x1d;
  public static final int EVM_INST_SDIV = 0x5;
  public static final int EVM_INST_SELFBALANCE = 0x47;
  public static final int EVM_INST_SELFDESTRUCT = 0xff;
  public static final int EVM_INST_SGT = 0x13;
  public static final int EVM_INST_SHA3 = 0x20;
  public static final int EVM_INST_SHL = 0x1b;
  public static final int EVM_INST_SHR = 0x1c;
  public static final int EVM_INST_SIGNEXTEND = 0xb;
  public static final int EVM_INST_SLOAD = 0x54;
  public static final int EVM_INST_SLT = 0x12;
  public static final int EVM_INST_SMOD = 0x7;
  public static final int EVM_INST_SSTORE = 0x55;
  public static final int EVM_INST_STATICCALL = 0xfa;
  public static final int EVM_INST_STOP = 0x0;
  public static final int EVM_INST_SUB = 0x3;
  public static final int EVM_INST_SWAP1 = 0x90;
  public static final int EVM_INST_SWAP10 = 0x99;
  public static final int EVM_INST_SWAP11 = 0x9a;
  public static final int EVM_INST_SWAP12 = 0x9b;
  public static final int EVM_INST_SWAP13 = 0x9c;
  public static final int EVM_INST_SWAP14 = 0x9d;
  public static final int EVM_INST_SWAP15 = 0x9e;
  public static final int EVM_INST_SWAP16 = 0x9f;
  public static final int EVM_INST_SWAP2 = 0x91;
  public static final int EVM_INST_SWAP3 = 0x92;
  public static final int EVM_INST_SWAP4 = 0x93;
  public static final int EVM_INST_SWAP5 = 0x94;
  public static final int EVM_INST_SWAP6 = 0x95;
  public static final int EVM_INST_SWAP7 = 0x96;
  public static final int EVM_INST_SWAP8 = 0x97;
  public static final int EVM_INST_SWAP9 = 0x98;
  public static final int EVM_INST_TIMESTAMP = 0x42;
  public static final int EVM_INST_XOR = 0x18;
  public static final int EXO_SUM_INDEX_BLAKEMODEXP = 0x6;
  public static final int EXO_SUM_INDEX_ECDATA = 0x4;
  public static final int EXO_SUM_INDEX_KEC = 0x1;
  public static final int EXO_SUM_INDEX_LOG = 0x2;
  public static final int EXO_SUM_INDEX_RIPSHA = 0x5;
  public static final int EXO_SUM_INDEX_ROM = 0x0;
  public static final int EXO_SUM_INDEX_TXCD = 0x3;
  public static final int EXO_SUM_WEIGHT_BLAKEMODEXP = 0x40;
  public static final int EXO_SUM_WEIGHT_ECDATA = 0x10;
  public static final int EXO_SUM_WEIGHT_KEC = 0x2;
  public static final int EXO_SUM_WEIGHT_LOG = 0x4;
  public static final int EXO_SUM_WEIGHT_RIPSHA = 0x20;
  public static final int EXO_SUM_WEIGHT_ROM = 0x1;
  public static final int EXO_SUM_WEIGHT_TXCD = 0x8;
  public static final int EXP_INST_EXPLOG = 0xee0a;
  public static final int EXP_INST_MODEXPLOG = 0xee05;
  public static final int GAS_CONST_G_ACCESS_LIST_ADRESS = 0x960;
  public static final int GAS_CONST_G_ACCESS_LIST_STORAGE = 0x76c;
  public static final int GAS_CONST_G_BASE = 0x2;
  public static final int GAS_CONST_G_BLOCKHASH = 0x14;
  public static final int GAS_CONST_G_CALL_STIPEND = 0x8fc;
  public static final int GAS_CONST_G_CALL_VALUE = 0x2328;
  public static final int GAS_CONST_G_CODE_DEPOSIT = 0xc8;
  public static final int GAS_CONST_G_COLD_ACCOUNT_ACCESS = 0xa28;
  public static final int GAS_CONST_G_COLD_SLOAD = 0x834;
  public static final int GAS_CONST_G_COPY = 0x3;
  public static final int GAS_CONST_G_CREATE = 0x7d00;
  public static final int GAS_CONST_G_EXP = 0xa;
  public static final int GAS_CONST_G_EXP_BYTE = 0x32;
  public static final int GAS_CONST_G_HIGH = 0xa;
  public static final int GAS_CONST_G_JUMPDEST = 0x1;
  public static final int GAS_CONST_G_KECCAK_256 = 0x1e;
  public static final int GAS_CONST_G_KECCAK_256_WORD = 0x6;
  public static final int GAS_CONST_G_LOG = 0x177;
  public static final int GAS_CONST_G_LOG_DATA = 0x8;
  public static final int GAS_CONST_G_LOG_TOPIC = 0x177;
  public static final int GAS_CONST_G_LOW = 0x5;
  public static final int GAS_CONST_G_MEMORY = 0x3;
  public static final int GAS_CONST_G_MID = 0x8;
  public static final int GAS_CONST_G_NEW_ACCOUNT = 0x61a8;
  public static final int GAS_CONST_G_SELFDESTRUCT = 0x1388;
  public static final int GAS_CONST_G_SRESET = 0xb54;
  public static final int GAS_CONST_G_SSET = 0x4e20;
  public static final int GAS_CONST_G_TRANSACTION = 0x5208;
  public static final int GAS_CONST_G_TX_CREATE = 0x7d00;
  public static final int GAS_CONST_G_TX_DATA_NONZERO = 0x10;
  public static final int GAS_CONST_G_TX_DATA_ZERO = 0x4;
  public static final int GAS_CONST_G_VERY_LOW = 0x3;
  public static final int GAS_CONST_G_WARM_ACCESS = 0x64;
  public static final int GAS_CONST_G_ZERO = 0x0;
  public static final int INDEX_MAX_KECCAK_DATA = 0x0;
  public static final int INDEX_MAX_KECCAK_RESULT = 0x1;
  public static final int INDEX_MAX_RIPEMD_DATA = 0x3;
  public static final int INDEX_MAX_RIPEMD_RESULT = 0x1;
  public static final int INDEX_MAX_SHA2_DATA = 0xb;
  public static final int INDEX_MAX_SHA2_RESULT = 0x1;
  public static final int INVALID_CODE_PREFIX_VALUE = 0xef;
  public static final int LLARGE = 0x10;
  public static final int LLARGEMO = 0xf;
  public static final int LLARGEPO = 0x11;
  public static final int MISC_EXP_WEIGHT = 0x1;
  public static final int MISC_MMU_WEIGHT = 0x2;
  public static final int MISC_MXP_WEIGHT = 0x4;
  public static final int MISC_OOB_WEIGHT = 0x8;
  public static final int MISC_STP_WEIGHT = 0x10;
  public static final int MMEDIUM = 0x8;
  public static final int MMEDIUMMO = 0x7;
  public static final int MMIO_INST_LIMB_TO_RAM_ONE_TARGET = 0xfe12;
  public static final int MMIO_INST_LIMB_TO_RAM_TRANSPLANT = 0xfe11;
  public static final int MMIO_INST_LIMB_TO_RAM_TWO_TARGET = 0xfe13;
  public static final int MMIO_INST_LIMB_VANISHES = 0xfe01;
  public static final int MMIO_INST_RAM_EXCISION = 0xfe41;
  public static final int MMIO_INST_RAM_TO_LIMB_ONE_SOURCE = 0xfe22;
  public static final int MMIO_INST_RAM_TO_LIMB_TRANSPLANT = 0xfe21;
  public static final int MMIO_INST_RAM_TO_LIMB_TWO_SOURCE = 0xfe23;
  public static final int MMIO_INST_RAM_TO_RAM_PARTIAL = 0xfe32;
  public static final int MMIO_INST_RAM_TO_RAM_TRANSPLANT = 0xfe31;
  public static final int MMIO_INST_RAM_TO_RAM_TWO_SOURCE = 0xfe34;
  public static final int MMIO_INST_RAM_TO_RAM_TWO_TARGET = 0xfe33;
  public static final int MMIO_INST_RAM_VANISHES = 0xfe42;
  public static final int MMU_INST_ANY_TO_RAM_WITH_PADDING = 0xfe50;
  public static final int MMU_INST_ANY_TO_RAM_WITH_PADDING_PURE_PADDING = 0xfe52;
  public static final int MMU_INST_ANY_TO_RAM_WITH_PADDING_SOME_DATA = 0xfe51;
  public static final int MMU_INST_BLAKE = 0xfe80;
  public static final int MMU_INST_EXO_TO_RAM_TRANSPLANTS = 0xfe30;
  public static final int MMU_INST_INVALID_CODE_PREFIX = 0xfe00;
  public static final int MMU_INST_MLOAD = 0xfe01;
  public static final int MMU_INST_MODEXP_DATA = 0xfe70;
  public static final int MMU_INST_MODEXP_ZERO = 0xfe60;
  public static final int MMU_INST_MSTORE = 0xfe02;
  public static final int MMU_INST_MSTORE8 = 0x53;
  public static final int MMU_INST_RAM_TO_EXO_WITH_PADDING = 0xfe20;
  public static final int MMU_INST_RAM_TO_RAM_SANS_PADDING = 0xfe40;
  public static final int MMU_INST_RIGHT_PADDED_WORD_EXTRACTION = 0xfe10;
  public static final int OOB_INST_blake_cds = 0xfa09;
  public static final int OOB_INST_blake_params = 0xfb09;
  public static final int OOB_INST_call = 0xca;
  public static final int OOB_INST_cdl = 0x35;
  public static final int OOB_INST_create = 0xce;
  public static final int OOB_INST_deployment = 0xf3;
  public static final int OOB_INST_ecadd = 0xff06;
  public static final int OOB_INST_ecmul = 0xff07;
  public static final int OOB_INST_ecpairing = 0xff08;
  public static final int OOB_INST_ecrecover = 0xff01;
  public static final int OOB_INST_identity = 0xff04;
  public static final int OOB_INST_jump = 0x56;
  public static final int OOB_INST_jumpi = 0x57;
  public static final int OOB_INST_modexp_cds = 0xfa05;
  public static final int OOB_INST_modexp_extract = 0xfe05;
  public static final int OOB_INST_modexp_lead = 0xfc05;
  public static final int OOB_INST_modexp_pricing = 0xfd05;
  public static final int OOB_INST_modexp_xbs = 0xfb05;
  public static final int OOB_INST_rdc = 0x3e;
  public static final int OOB_INST_ripemd = 0xff03;
  public static final int OOB_INST_sha2 = 0xff02;
  public static final int OOB_INST_sstore = 0x55;
  public static final int OOB_INST_xcall = 0xcc;
  public static final int PHASE_BLAKE_DATA = 0x5;
  public static final int PHASE_BLAKE_PARAMS = 0x6;
  public static final int PHASE_BLAKE_RESULT = 0x7;
  public static final int PHASE_KECCAK_DATA = 0x8;
  public static final int PHASE_KECCAK_RESULT = 0x9;
  public static final int PHASE_MODEXP_BASE = 0x1;
  public static final int PHASE_MODEXP_EXPONENT = 0x2;
  public static final int PHASE_MODEXP_MODULUS = 0x3;
  public static final int PHASE_MODEXP_RESULT = 0x4;
  public static final int PHASE_RIPEMD_DATA = 0xc;
  public static final int PHASE_RIPEMD_RESULT = 0xd;
  public static final int PHASE_SHA2_DATA = 0xa;
  public static final int PHASE_SHA2_RESULT = 0xb;
  public static final int REFUND_CONST_R_SCLEAR = 0x3a98;
  public static final int REFUND_CONST_R_SELFDESTRUCT = 0x5dc0;
  public static final int RLP_ADDR_RECIPE_1 = 0x1;
  public static final int RLP_ADDR_RECIPE_2 = 0x2;
  public static final int RLP_PREFIX_INT_LONG = 0xb7;
  public static final int RLP_PREFIX_INT_SHORT = 0x80;
  public static final int RLP_PREFIX_LIST_LONG = 0xf7;
  public static final int RLP_PREFIX_LIST_SHORT = 0xc0;
  public static final int RLP_RCPT_SUBPHASE_ID_ADDR = 0x35;
  public static final int RLP_RCPT_SUBPHASE_ID_CUMUL_GAS = 0x3;
  public static final int RLP_RCPT_SUBPHASE_ID_DATA_LIMB = 0x4d;
  public static final int RLP_RCPT_SUBPHASE_ID_DATA_SIZE = 0x53;
  public static final int RLP_RCPT_SUBPHASE_ID_NO_LOG_ENTRY = 0xb;
  public static final int RLP_RCPT_SUBPHASE_ID_STATUS_CODE = 0x2;
  public static final int RLP_RCPT_SUBPHASE_ID_TOPIC_BASE = 0x41;
  public static final int RLP_RCPT_SUBPHASE_ID_TOPIC_DELTA = 0x60;
  public static final int RLP_RCPT_SUBPHASE_ID_TYPE = 0x7;
  public static final int RLP_TXN_PHASE_ACCESS_LIST_VALUE = 0xb;
  public static final int RLP_TXN_PHASE_BETA_VALUE = 0xc;
  public static final int RLP_TXN_PHASE_CHAIN_ID_VALUE = 0x2;
  public static final int RLP_TXN_PHASE_DATA_VALUE = 0xa;
  public static final int RLP_TXN_PHASE_GAS_LIMIT_VALUE = 0x7;
  public static final int RLP_TXN_PHASE_GAS_PRICE_VALUE = 0x4;
  public static final int RLP_TXN_PHASE_MAX_FEE_PER_GAS_VALUE = 0x6;
  public static final int RLP_TXN_PHASE_MAX_PRIORITY_FEE_PER_GAS_VALUE = 0x5;
  public static final int RLP_TXN_PHASE_NONCE_VALUE = 0x3;
  public static final int RLP_TXN_PHASE_RLP_PREFIX_VALUE = 0x1;
  public static final int RLP_TXN_PHASE_R_VALUE = 0xe;
  public static final int RLP_TXN_PHASE_S_VALUE = 0xf;
  public static final int RLP_TXN_PHASE_TO_VALUE = 0x8;
  public static final int RLP_TXN_PHASE_VALUE_VALUE = 0x9;
  public static final int RLP_TXN_PHASE_Y_VALUE = 0xd;
  public static final int WCP_INST_GEQ = 0xe;
  public static final int WCP_INST_LEQ = 0xf;
  public static final int WORD_SIZE = 0x20;
  public static final int WORD_SIZE_MO = 0x1f;

  private final BitSet filled = new BitSet();
  private int currentLine = 0;

  private final MappedByteBuffer deltaByte;
  private final MappedByteBuffer id;
  private final MappedByteBuffer index;
  private final MappedByteBuffer indexMax;
  private final MappedByteBuffer isExtra;
  private final MappedByteBuffer isKeccakData;
  private final MappedByteBuffer isKeccakResult;
  private final MappedByteBuffer isRipemdData;
  private final MappedByteBuffer isRipemdResult;
  private final MappedByteBuffer isSha2Data;
  private final MappedByteBuffer isSha2Result;
  private final MappedByteBuffer limb;
  private final MappedByteBuffer nBytes;
  private final MappedByteBuffer nBytesAcc;
  private final MappedByteBuffer phase;
  private final MappedByteBuffer ripshaStamp;
  private final MappedByteBuffer totalSize;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("shakira.DELTA_BYTE", 1, length),
        new ColumnHeader("shakira.ID", 8, length),
        new ColumnHeader("shakira.INDEX", 8, length),
        new ColumnHeader("shakira.INDEX_MAX", 8, length),
        new ColumnHeader("shakira.IS_EXTRA", 1, length),
        new ColumnHeader("shakira.IS_KECCAK_DATA", 1, length),
        new ColumnHeader("shakira.IS_KECCAK_RESULT", 1, length),
        new ColumnHeader("shakira.IS_RIPEMD_DATA", 1, length),
        new ColumnHeader("shakira.IS_RIPEMD_RESULT", 1, length),
        new ColumnHeader("shakira.IS_SHA2_DATA", 1, length),
        new ColumnHeader("shakira.IS_SHA2_RESULT", 1, length),
        new ColumnHeader("shakira.LIMB", 32, length),
        new ColumnHeader("shakira.nBYTES", 1, length),
        new ColumnHeader("shakira.nBYTES_ACC", 8, length),
        new ColumnHeader("shakira.PHASE", 1, length),
        new ColumnHeader("shakira.RIPSHA_STAMP", 8, length),
        new ColumnHeader("shakira.TOTAL_SIZE", 8, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.deltaByte = buffers.get(0);
    this.id = buffers.get(1);
    this.index = buffers.get(2);
    this.indexMax = buffers.get(3);
    this.isExtra = buffers.get(4);
    this.isKeccakData = buffers.get(5);
    this.isKeccakResult = buffers.get(6);
    this.isRipemdData = buffers.get(7);
    this.isRipemdResult = buffers.get(8);
    this.isSha2Data = buffers.get(9);
    this.isSha2Result = buffers.get(10);
    this.limb = buffers.get(11);
    this.nBytes = buffers.get(12);
    this.nBytesAcc = buffers.get(13);
    this.phase = buffers.get(14);
    this.ripshaStamp = buffers.get(15);
    this.totalSize = buffers.get(16);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace deltaByte(final UnsignedByte b) {
    if (filled.get(0)) {
      throw new IllegalStateException("shakira.DELTA_BYTE already set");
    } else {
      filled.set(0);
    }

    deltaByte.put(b.toByte());

    return this;
  }

  public Trace id(final long b) {
    if (filled.get(1)) {
      throw new IllegalStateException("shakira.ID already set");
    } else {
      filled.set(1);
    }

    id.putLong(b);

    return this;
  }

  public Trace index(final long b) {
    if (filled.get(2)) {
      throw new IllegalStateException("shakira.INDEX already set");
    } else {
      filled.set(2);
    }

    index.putLong(b);

    return this;
  }

  public Trace indexMax(final long b) {
    if (filled.get(3)) {
      throw new IllegalStateException("shakira.INDEX_MAX already set");
    } else {
      filled.set(3);
    }

    indexMax.putLong(b);

    return this;
  }

  public Trace isExtra(final Boolean b) {
    if (filled.get(4)) {
      throw new IllegalStateException("shakira.IS_EXTRA already set");
    } else {
      filled.set(4);
    }

    isExtra.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isKeccakData(final Boolean b) {
    if (filled.get(5)) {
      throw new IllegalStateException("shakira.IS_KECCAK_DATA already set");
    } else {
      filled.set(5);
    }

    isKeccakData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isKeccakResult(final Boolean b) {
    if (filled.get(6)) {
      throw new IllegalStateException("shakira.IS_KECCAK_RESULT already set");
    } else {
      filled.set(6);
    }

    isKeccakResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isRipemdData(final Boolean b) {
    if (filled.get(7)) {
      throw new IllegalStateException("shakira.IS_RIPEMD_DATA already set");
    } else {
      filled.set(7);
    }

    isRipemdData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isRipemdResult(final Boolean b) {
    if (filled.get(8)) {
      throw new IllegalStateException("shakira.IS_RIPEMD_RESULT already set");
    } else {
      filled.set(8);
    }

    isRipemdResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isSha2Data(final Boolean b) {
    if (filled.get(9)) {
      throw new IllegalStateException("shakira.IS_SHA2_DATA already set");
    } else {
      filled.set(9);
    }

    isSha2Data.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isSha2Result(final Boolean b) {
    if (filled.get(10)) {
      throw new IllegalStateException("shakira.IS_SHA2_RESULT already set");
    } else {
      filled.set(10);
    }

    isSha2Result.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace limb(final Bytes b) {
    if (filled.get(11)) {
      throw new IllegalStateException("shakira.LIMB already set");
    } else {
      filled.set(11);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      limb.put((byte) 0);
    }
    limb.put(b.toArrayUnsafe());

    return this;
  }

  public Trace nBytes(final UnsignedByte b) {
    if (filled.get(15)) {
      throw new IllegalStateException("shakira.nBYTES already set");
    } else {
      filled.set(15);
    }

    nBytes.put(b.toByte());

    return this;
  }

  public Trace nBytesAcc(final long b) {
    if (filled.get(16)) {
      throw new IllegalStateException("shakira.nBYTES_ACC already set");
    } else {
      filled.set(16);
    }

    nBytesAcc.putLong(b);

    return this;
  }

  public Trace phase(final UnsignedByte b) {
    if (filled.get(12)) {
      throw new IllegalStateException("shakira.PHASE already set");
    } else {
      filled.set(12);
    }

    phase.put(b.toByte());

    return this;
  }

  public Trace ripshaStamp(final long b) {
    if (filled.get(13)) {
      throw new IllegalStateException("shakira.RIPSHA_STAMP already set");
    } else {
      filled.set(13);
    }

    ripshaStamp.putLong(b);

    return this;
  }

  public Trace totalSize(final long b) {
    if (filled.get(14)) {
      throw new IllegalStateException("shakira.TOTAL_SIZE already set");
    } else {
      filled.set(14);
    }

    totalSize.putLong(b);

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(0)) {
      throw new IllegalStateException("shakira.DELTA_BYTE has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("shakira.ID has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("shakira.INDEX has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("shakira.INDEX_MAX has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("shakira.IS_EXTRA has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("shakira.IS_KECCAK_DATA has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("shakira.IS_KECCAK_RESULT has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("shakira.IS_RIPEMD_DATA has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("shakira.IS_RIPEMD_RESULT has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("shakira.IS_SHA2_DATA has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("shakira.IS_SHA2_RESULT has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("shakira.LIMB has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("shakira.nBYTES has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("shakira.nBYTES_ACC has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("shakira.PHASE has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("shakira.RIPSHA_STAMP has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("shakira.TOTAL_SIZE has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(0)) {
      deltaByte.position(deltaByte.position() + 1);
    }

    if (!filled.get(1)) {
      id.position(id.position() + 8);
    }

    if (!filled.get(2)) {
      index.position(index.position() + 8);
    }

    if (!filled.get(3)) {
      indexMax.position(indexMax.position() + 8);
    }

    if (!filled.get(4)) {
      isExtra.position(isExtra.position() + 1);
    }

    if (!filled.get(5)) {
      isKeccakData.position(isKeccakData.position() + 1);
    }

    if (!filled.get(6)) {
      isKeccakResult.position(isKeccakResult.position() + 1);
    }

    if (!filled.get(7)) {
      isRipemdData.position(isRipemdData.position() + 1);
    }

    if (!filled.get(8)) {
      isRipemdResult.position(isRipemdResult.position() + 1);
    }

    if (!filled.get(9)) {
      isSha2Data.position(isSha2Data.position() + 1);
    }

    if (!filled.get(10)) {
      isSha2Result.position(isSha2Result.position() + 1);
    }

    if (!filled.get(11)) {
      limb.position(limb.position() + 32);
    }

    if (!filled.get(15)) {
      nBytes.position(nBytes.position() + 1);
    }

    if (!filled.get(16)) {
      nBytesAcc.position(nBytesAcc.position() + 8);
    }

    if (!filled.get(12)) {
      phase.position(phase.position() + 1);
    }

    if (!filled.get(13)) {
      ripshaStamp.position(ripshaStamp.position() + 8);
    }

    if (!filled.get(14)) {
      totalSize.position(totalSize.position() + 8);
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public void build() {
    if (!filled.isEmpty()) {
      throw new IllegalStateException("Cannot build trace with a non-validated row.");
    }
  }
}
