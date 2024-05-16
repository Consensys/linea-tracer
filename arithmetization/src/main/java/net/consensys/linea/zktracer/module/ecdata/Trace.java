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

package net.consensys.linea.zktracer.module.ecdata;

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
  public static final int ADDMOD = 0x8;
  public static final int CT_MAX_LARGE_POINT = 0x7;
  public static final int CT_MAX_SMALL_POINT = 0x3;
  public static final int ECADD = 0x6;
  public static final int ECMUL = 0x7;
  public static final int ECPAIRING = 0x8;
  public static final int ECRECOVER = 0x1;
  public static final int EIP_3541_MARKER = 0xef;
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
  public static final int EVM_INST_BLOCKHASH_MAX_HISTORY = 0x100;
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
  public static final int INDEX_MAX_ECADD_DATA = 0x7;
  public static final int INDEX_MAX_ECADD_RESULT = 0x3;
  public static final int INDEX_MAX_ECMUL_DATA = 0x5;
  public static final int INDEX_MAX_ECMUL_RESULT = 0x3;
  public static final int INDEX_MAX_ECPAIRING_DATA_MIN = 0xb;
  public static final int INDEX_MAX_ECPAIRING_RESULT = 0x1;
  public static final int INDEX_MAX_ECRECOVER_DATA = 0x7;
  public static final int INDEX_MAX_ECRECOVER_RESULT = 0x1;
  public static final int LINEA_BASE_FEE = 0x7;
  public static final int LINEA_BLOCK_GAS_LIMIT = 0x3a2c940;
  public static final int LINEA_CHAIN_ID = 0xe708;
  public static final int LINEA_DIFFICULTY = 0x2;
  public static final int LINEA_GOERLI_CHAIN_ID = 0xe704;
  public static final int LINEA_SEPOLIA_CHAIN_ID = 0xe705;
  public static final int LLARGE = 0x10;
  public static final int LLARGEMO = 0xf;
  public static final int LLARGEPO = 0x11;
  public static final int MAX_REFUND_QUOTIENT = 0x5;
  public static final int MISC_WEIGHT_EXP = 0x1;
  public static final int MISC_WEIGHT_MMU = 0x2;
  public static final int MISC_WEIGHT_MXP = 0x4;
  public static final int MISC_WEIGHT_OOB = 0x8;
  public static final int MISC_WEIGHT_STP = 0x10;
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
  public static final int MMU_INST_BLAKE = 0xfe80;
  public static final int MMU_INST_EXO_TO_RAM_TRANSPLANTS = 0xfe30;
  public static final int MMU_INST_INVALID_CODE_PREFIX = 0xfe00;
  public static final int MMU_INST_MLOAD = 0xfe01;
  public static final int MMU_INST_MODEXP_DATA = 0xfe70;
  public static final int MMU_INST_MODEXP_ZERO = 0xfe60;
  public static final int MMU_INST_MSTORE = 0xfe02;
  public static final int MMU_INST_MSTORE8 = 0xfe03;
  public static final int MMU_INST_RAM_TO_EXO_WITH_PADDING = 0xfe20;
  public static final int MMU_INST_RAM_TO_RAM_SANS_PADDING = 0xfe40;
  public static final int MMU_INST_RIGHT_PADDED_WORD_EXTRACTION = 0xfe10;
  public static final int MULMOD = 0x9;
  public static final int OOB_INST_BLAKE_CDS = 0xfa09;
  public static final int OOB_INST_BLAKE_PARAMS = 0xfb09;
  public static final int OOB_INST_CALL = 0xca;
  public static final int OOB_INST_CDL = 0x35;
  public static final int OOB_INST_CREATE = 0xce;
  public static final int OOB_INST_DEPLOYMENT = 0xf3;
  public static final int OOB_INST_ECADD = 0xff06;
  public static final int OOB_INST_ECMUL = 0xff07;
  public static final int OOB_INST_ECPAIRING = 0xff08;
  public static final int OOB_INST_ECRECOVER = 0xff01;
  public static final int OOB_INST_IDENTITY = 0xff04;
  public static final int OOB_INST_JUMP = 0x56;
  public static final int OOB_INST_JUMPI = 0x57;
  public static final int OOB_INST_MODEXP_CDS = 0xfa05;
  public static final int OOB_INST_MODEXP_EXTRACT = 0xfe05;
  public static final int OOB_INST_MODEXP_LEAD = 0xfc05;
  public static final int OOB_INST_MODEXP_PRICING = 0xfd05;
  public static final int OOB_INST_MODEXP_XBS = 0xfb05;
  public static final int OOB_INST_RDC = 0x3e;
  public static final int OOB_INST_RIPEMD = 0xff03;
  public static final int OOB_INST_SHA2 = 0xff02;
  public static final int OOB_INST_SSTORE = 0x55;
  public static final int OOB_INST_XCALL = 0xcc;
  public static final int PHASE_BLAKE_DATA = 0x5;
  public static final int PHASE_BLAKE_PARAMS = 0x6;
  public static final int PHASE_BLAKE_RESULT = 0x7;
  public static final int PHASE_ECADD_DATA = 0x60a;
  public static final int PHASE_ECADD_RESULT = 0x60b;
  public static final int PHASE_ECMUL_DATA = 0x70a;
  public static final int PHASE_ECMUL_RESULT = 0x70b;
  public static final int PHASE_ECPAIRING_DATA = 0x80a;
  public static final int PHASE_ECPAIRING_RESULT = 0x80b;
  public static final int PHASE_ECRECOVER_DATA = 0x10a;
  public static final int PHASE_ECRECOVER_RESULT = 0x10b;
  public static final int PHASE_KECCAK_DATA = 0x5;
  public static final int PHASE_KECCAK_RESULT = 0x6;
  public static final int PHASE_MODEXP_BASE = 0x1;
  public static final int PHASE_MODEXP_EXPONENT = 0x2;
  public static final int PHASE_MODEXP_MODULUS = 0x3;
  public static final int PHASE_MODEXP_RESULT = 0x4;
  public static final int PHASE_RIPEMD_DATA = 0x3;
  public static final int PHASE_RIPEMD_RESULT = 0x4;
  public static final int PHASE_SHA2_DATA = 0x1;
  public static final int PHASE_SHA2_RESULT = 0x2;
  public static final BigInteger P_BN_HI = new BigInteger("64323764613183177041862057485226039389");
  public static final BigInteger P_BN_LO =
      new BigInteger("201385395114098847380338600778089168199");
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
  public static final int RLP_TXN_PHASE_ACCESS_LIST = 0xb;
  public static final int RLP_TXN_PHASE_BETA = 0xc;
  public static final int RLP_TXN_PHASE_CHAIN_ID = 0x2;
  public static final int RLP_TXN_PHASE_DATA = 0xa;
  public static final int RLP_TXN_PHASE_GAS_LIMIT = 0x7;
  public static final int RLP_TXN_PHASE_GAS_PRICE = 0x4;
  public static final int RLP_TXN_PHASE_MAX_FEE_PER_GAS = 0x6;
  public static final int RLP_TXN_PHASE_MAX_PRIORITY_FEE_PER_GAS = 0x5;
  public static final int RLP_TXN_PHASE_NONCE = 0x3;
  public static final int RLP_TXN_PHASE_R = 0xe;
  public static final int RLP_TXN_PHASE_RLP_PREFIX = 0x1;
  public static final int RLP_TXN_PHASE_S = 0xf;
  public static final int RLP_TXN_PHASE_TO = 0x8;
  public static final int RLP_TXN_PHASE_VALUE = 0x9;
  public static final int RLP_TXN_PHASE_Y = 0xd;
  public static final BigInteger SECP256K1N_HI =
      new BigInteger("340282366920938463463374607431768211455");
  public static final BigInteger SECP256K1N_LO =
      new BigInteger("340282366920938463463374607427473243183");
  public static final int TOTAL_SIZE_ECADD_DATA = 0x80;
  public static final int TOTAL_SIZE_ECADD_RESULT = 0x40;
  public static final int TOTAL_SIZE_ECMUL_DATA = 0x60;
  public static final int TOTAL_SIZE_ECMUL_RESULT = 0x40;
  public static final int TOTAL_SIZE_ECPAIRING_DATA_MIN = 0xc4;
  public static final int TOTAL_SIZE_ECPAIRING_RESULT = 0x20;
  public static final int TOTAL_SIZE_ECRECOVER_DATA = 0x80;
  public static final int TOTAL_SIZE_ECRECOVER_RESULT = 0x20;
  public static final int WCP_INST_GEQ = 0xe;
  public static final int WCP_INST_LEQ = 0xf;
  public static final int WORD_SIZE = 0x20;
  public static final int WORD_SIZE_MO = 0x1f;

  private final BitSet filled = new BitSet();
  private int currentLine = 0;

  private final MappedByteBuffer accPairings;
  private final MappedByteBuffer acceptablePairOfPointForPairingCircuit;
  private final MappedByteBuffer byteDelta;
  private final MappedByteBuffer circuitSelectorEcadd;
  private final MappedByteBuffer circuitSelectorEcmul;
  private final MappedByteBuffer circuitSelectorEcpairing;
  private final MappedByteBuffer circuitSelectorEcrecover;
  private final MappedByteBuffer circuitSelectorG2Membership;
  private final MappedByteBuffer ct;
  private final MappedByteBuffer ctMax;
  private final MappedByteBuffer extArg1Hi;
  private final MappedByteBuffer extArg1Lo;
  private final MappedByteBuffer extArg2Hi;
  private final MappedByteBuffer extArg2Lo;
  private final MappedByteBuffer extArg3Hi;
  private final MappedByteBuffer extArg3Lo;
  private final MappedByteBuffer extFlag;
  private final MappedByteBuffer extInst;
  private final MappedByteBuffer extResHi;
  private final MappedByteBuffer extResLo;
  private final MappedByteBuffer g2MembershipTestRequired;
  private final MappedByteBuffer hurdle;
  private final MappedByteBuffer id;
  private final MappedByteBuffer index;
  private final MappedByteBuffer indexMax;
  private final MappedByteBuffer internalChecksPassed;
  private final MappedByteBuffer isEcaddData;
  private final MappedByteBuffer isEcaddResult;
  private final MappedByteBuffer isEcmulData;
  private final MappedByteBuffer isEcmulResult;
  private final MappedByteBuffer isEcpairingData;
  private final MappedByteBuffer isEcpairingResult;
  private final MappedByteBuffer isEcrecoverData;
  private final MappedByteBuffer isEcrecoverResult;
  private final MappedByteBuffer isInfinity;
  private final MappedByteBuffer isLargePoint;
  private final MappedByteBuffer isSmallPoint;
  private final MappedByteBuffer limb;
  private final MappedByteBuffer notOnG2;
  private final MappedByteBuffer notOnG2Acc;
  private final MappedByteBuffer notOnG2AccMax;
  private final MappedByteBuffer overallTrivialPairing;
  private final MappedByteBuffer phase;
  private final MappedByteBuffer stamp;
  private final MappedByteBuffer successBit;
  private final MappedByteBuffer totalPairings;
  private final MappedByteBuffer totalSize;
  private final MappedByteBuffer wcpArg1Hi;
  private final MappedByteBuffer wcpArg1Lo;
  private final MappedByteBuffer wcpArg2Hi;
  private final MappedByteBuffer wcpArg2Lo;
  private final MappedByteBuffer wcpFlag;
  private final MappedByteBuffer wcpInst;
  private final MappedByteBuffer wcpRes;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("ecdata.ACC_PAIRINGS", 32, length),
        new ColumnHeader("ecdata.ACCEPTABLE_PAIR_OF_POINT_FOR_PAIRING_CIRCUIT", 1, length),
        new ColumnHeader("ecdata.BYTE_DELTA", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECADD", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECMUL", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECPAIRING", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_ECRECOVER", 1, length),
        new ColumnHeader("ecdata.CIRCUIT_SELECTOR_G2_MEMBERSHIP", 1, length),
        new ColumnHeader("ecdata.CT", 2, length),
        new ColumnHeader("ecdata.CT_MAX", 2, length),
        new ColumnHeader("ecdata.EXT_ARG1_HI", 32, length),
        new ColumnHeader("ecdata.EXT_ARG1_LO", 32, length),
        new ColumnHeader("ecdata.EXT_ARG2_HI", 32, length),
        new ColumnHeader("ecdata.EXT_ARG2_LO", 32, length),
        new ColumnHeader("ecdata.EXT_ARG3_HI", 32, length),
        new ColumnHeader("ecdata.EXT_ARG3_LO", 32, length),
        new ColumnHeader("ecdata.EXT_FLAG", 1, length),
        new ColumnHeader("ecdata.EXT_INST", 1, length),
        new ColumnHeader("ecdata.EXT_RES_HI", 32, length),
        new ColumnHeader("ecdata.EXT_RES_LO", 32, length),
        new ColumnHeader("ecdata.G2_MEMBERSHIP_TEST_REQUIRED", 1, length),
        new ColumnHeader("ecdata.HURDLE", 1, length),
        new ColumnHeader("ecdata.ID", 8, length),
        new ColumnHeader("ecdata.INDEX", 1, length),
        new ColumnHeader("ecdata.INDEX_MAX", 32, length),
        new ColumnHeader("ecdata.INTERNAL_CHECKS_PASSED", 1, length),
        new ColumnHeader("ecdata.IS_ECADD_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECADD_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_ECMUL_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECMUL_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_ECPAIRING_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECPAIRING_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_ECRECOVER_DATA", 1, length),
        new ColumnHeader("ecdata.IS_ECRECOVER_RESULT", 1, length),
        new ColumnHeader("ecdata.IS_INFINITY", 1, length),
        new ColumnHeader("ecdata.IS_LARGE_POINT", 1, length),
        new ColumnHeader("ecdata.IS_SMALL_POINT", 1, length),
        new ColumnHeader("ecdata.LIMB", 32, length),
        new ColumnHeader("ecdata.NOT_ON_G2", 1, length),
        new ColumnHeader("ecdata.NOT_ON_G2_ACC", 1, length),
        new ColumnHeader("ecdata.NOT_ON_G2_ACC_MAX", 1, length),
        new ColumnHeader("ecdata.OVERALL_TRIVIAL_PAIRING", 1, length),
        new ColumnHeader("ecdata.PHASE", 4, length),
        new ColumnHeader("ecdata.STAMP", 8, length),
        new ColumnHeader("ecdata.SUCCESS_BIT", 1, length),
        new ColumnHeader("ecdata.TOTAL_PAIRINGS", 32, length),
        new ColumnHeader("ecdata.TOTAL_SIZE", 32, length),
        new ColumnHeader("ecdata.WCP_ARG1_HI", 32, length),
        new ColumnHeader("ecdata.WCP_ARG1_LO", 32, length),
        new ColumnHeader("ecdata.WCP_ARG2_HI", 32, length),
        new ColumnHeader("ecdata.WCP_ARG2_LO", 32, length),
        new ColumnHeader("ecdata.WCP_FLAG", 1, length),
        new ColumnHeader("ecdata.WCP_INST", 1, length),
        new ColumnHeader("ecdata.WCP_RES", 1, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.accPairings = buffers.get(0);
    this.acceptablePairOfPointForPairingCircuit = buffers.get(1);
    this.byteDelta = buffers.get(2);
    this.circuitSelectorEcadd = buffers.get(3);
    this.circuitSelectorEcmul = buffers.get(4);
    this.circuitSelectorEcpairing = buffers.get(5);
    this.circuitSelectorEcrecover = buffers.get(6);
    this.circuitSelectorG2Membership = buffers.get(7);
    this.ct = buffers.get(8);
    this.ctMax = buffers.get(9);
    this.extArg1Hi = buffers.get(10);
    this.extArg1Lo = buffers.get(11);
    this.extArg2Hi = buffers.get(12);
    this.extArg2Lo = buffers.get(13);
    this.extArg3Hi = buffers.get(14);
    this.extArg3Lo = buffers.get(15);
    this.extFlag = buffers.get(16);
    this.extInst = buffers.get(17);
    this.extResHi = buffers.get(18);
    this.extResLo = buffers.get(19);
    this.g2MembershipTestRequired = buffers.get(20);
    this.hurdle = buffers.get(21);
    this.id = buffers.get(22);
    this.index = buffers.get(23);
    this.indexMax = buffers.get(24);
    this.internalChecksPassed = buffers.get(25);
    this.isEcaddData = buffers.get(26);
    this.isEcaddResult = buffers.get(27);
    this.isEcmulData = buffers.get(28);
    this.isEcmulResult = buffers.get(29);
    this.isEcpairingData = buffers.get(30);
    this.isEcpairingResult = buffers.get(31);
    this.isEcrecoverData = buffers.get(32);
    this.isEcrecoverResult = buffers.get(33);
    this.isInfinity = buffers.get(34);
    this.isLargePoint = buffers.get(35);
    this.isSmallPoint = buffers.get(36);
    this.limb = buffers.get(37);
    this.notOnG2 = buffers.get(38);
    this.notOnG2Acc = buffers.get(39);
    this.notOnG2AccMax = buffers.get(40);
    this.overallTrivialPairing = buffers.get(41);
    this.phase = buffers.get(42);
    this.stamp = buffers.get(43);
    this.successBit = buffers.get(44);
    this.totalPairings = buffers.get(45);
    this.totalSize = buffers.get(46);
    this.wcpArg1Hi = buffers.get(47);
    this.wcpArg1Lo = buffers.get(48);
    this.wcpArg2Hi = buffers.get(49);
    this.wcpArg2Lo = buffers.get(50);
    this.wcpFlag = buffers.get(51);
    this.wcpInst = buffers.get(52);
    this.wcpRes = buffers.get(53);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace accPairings(final Bytes b) {
    if (filled.get(1)) {
      throw new IllegalStateException("ecdata.ACC_PAIRINGS already set");
    } else {
      filled.set(1);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accPairings.put((byte) 0);
    }
    accPairings.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acceptablePairOfPointForPairingCircuit(final Boolean b) {
    if (filled.get(0)) {
      throw new IllegalStateException(
          "ecdata.ACCEPTABLE_PAIR_OF_POINT_FOR_PAIRING_CIRCUIT already set");
    } else {
      filled.set(0);
    }

    acceptablePairOfPointForPairingCircuit.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace byteDelta(final UnsignedByte b) {
    if (filled.get(2)) {
      throw new IllegalStateException("ecdata.BYTE_DELTA already set");
    } else {
      filled.set(2);
    }

    byteDelta.put(b.toByte());

    return this;
  }

  public Trace circuitSelectorEcadd(final Boolean b) {
    if (filled.get(3)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECADD already set");
    } else {
      filled.set(3);
    }

    circuitSelectorEcadd.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorEcmul(final Boolean b) {
    if (filled.get(4)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECMUL already set");
    } else {
      filled.set(4);
    }

    circuitSelectorEcmul.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorEcpairing(final Boolean b) {
    if (filled.get(5)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECPAIRING already set");
    } else {
      filled.set(5);
    }

    circuitSelectorEcpairing.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorEcrecover(final Boolean b) {
    if (filled.get(6)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECRECOVER already set");
    } else {
      filled.set(6);
    }

    circuitSelectorEcrecover.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace circuitSelectorG2Membership(final Boolean b) {
    if (filled.get(7)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_G2_MEMBERSHIP already set");
    } else {
      filled.set(7);
    }

    circuitSelectorG2Membership.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace ct(final short b) {
    if (filled.get(8)) {
      throw new IllegalStateException("ecdata.CT already set");
    } else {
      filled.set(8);
    }

    ct.putShort(b);

    return this;
  }

  public Trace ctMax(final short b) {
    if (filled.get(9)) {
      throw new IllegalStateException("ecdata.CT_MAX already set");
    } else {
      filled.set(9);
    }

    ctMax.putShort(b);

    return this;
  }

  public Trace extArg1Hi(final Bytes b) {
    if (filled.get(10)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_HI already set");
    } else {
      filled.set(10);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg1Hi.put((byte) 0);
    }
    extArg1Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg1Lo(final Bytes b) {
    if (filled.get(11)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_LO already set");
    } else {
      filled.set(11);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg1Lo.put((byte) 0);
    }
    extArg1Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg2Hi(final Bytes b) {
    if (filled.get(12)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_HI already set");
    } else {
      filled.set(12);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg2Hi.put((byte) 0);
    }
    extArg2Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg2Lo(final Bytes b) {
    if (filled.get(13)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_LO already set");
    } else {
      filled.set(13);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg2Lo.put((byte) 0);
    }
    extArg2Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg3Hi(final Bytes b) {
    if (filled.get(14)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_HI already set");
    } else {
      filled.set(14);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg3Hi.put((byte) 0);
    }
    extArg3Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extArg3Lo(final Bytes b) {
    if (filled.get(15)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_LO already set");
    } else {
      filled.set(15);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extArg3Lo.put((byte) 0);
    }
    extArg3Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extFlag(final Boolean b) {
    if (filled.get(16)) {
      throw new IllegalStateException("ecdata.EXT_FLAG already set");
    } else {
      filled.set(16);
    }

    extFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace extInst(final UnsignedByte b) {
    if (filled.get(17)) {
      throw new IllegalStateException("ecdata.EXT_INST already set");
    } else {
      filled.set(17);
    }

    extInst.put(b.toByte());

    return this;
  }

  public Trace extResHi(final Bytes b) {
    if (filled.get(18)) {
      throw new IllegalStateException("ecdata.EXT_RES_HI already set");
    } else {
      filled.set(18);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extResHi.put((byte) 0);
    }
    extResHi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace extResLo(final Bytes b) {
    if (filled.get(19)) {
      throw new IllegalStateException("ecdata.EXT_RES_LO already set");
    } else {
      filled.set(19);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      extResLo.put((byte) 0);
    }
    extResLo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace g2MembershipTestRequired(final Boolean b) {
    if (filled.get(20)) {
      throw new IllegalStateException("ecdata.G2_MEMBERSHIP_TEST_REQUIRED already set");
    } else {
      filled.set(20);
    }

    g2MembershipTestRequired.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace hurdle(final Boolean b) {
    if (filled.get(21)) {
      throw new IllegalStateException("ecdata.HURDLE already set");
    } else {
      filled.set(21);
    }

    hurdle.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace id(final long b) {
    if (filled.get(22)) {
      throw new IllegalStateException("ecdata.ID already set");
    } else {
      filled.set(22);
    }

    id.putLong(b);

    return this;
  }

  public Trace index(final UnsignedByte b) {
    if (filled.get(23)) {
      throw new IllegalStateException("ecdata.INDEX already set");
    } else {
      filled.set(23);
    }

    index.put(b.toByte());

    return this;
  }

  public Trace indexMax(final Bytes b) {
    if (filled.get(24)) {
      throw new IllegalStateException("ecdata.INDEX_MAX already set");
    } else {
      filled.set(24);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      indexMax.put((byte) 0);
    }
    indexMax.put(b.toArrayUnsafe());

    return this;
  }

  public Trace internalChecksPassed(final Boolean b) {
    if (filled.get(25)) {
      throw new IllegalStateException("ecdata.INTERNAL_CHECKS_PASSED already set");
    } else {
      filled.set(25);
    }

    internalChecksPassed.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcaddData(final Boolean b) {
    if (filled.get(26)) {
      throw new IllegalStateException("ecdata.IS_ECADD_DATA already set");
    } else {
      filled.set(26);
    }

    isEcaddData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcaddResult(final Boolean b) {
    if (filled.get(27)) {
      throw new IllegalStateException("ecdata.IS_ECADD_RESULT already set");
    } else {
      filled.set(27);
    }

    isEcaddResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcmulData(final Boolean b) {
    if (filled.get(28)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_DATA already set");
    } else {
      filled.set(28);
    }

    isEcmulData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcmulResult(final Boolean b) {
    if (filled.get(29)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_RESULT already set");
    } else {
      filled.set(29);
    }

    isEcmulResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcpairingData(final Boolean b) {
    if (filled.get(30)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_DATA already set");
    } else {
      filled.set(30);
    }

    isEcpairingData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcpairingResult(final Boolean b) {
    if (filled.get(31)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_RESULT already set");
    } else {
      filled.set(31);
    }

    isEcpairingResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcrecoverData(final Boolean b) {
    if (filled.get(32)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_DATA already set");
    } else {
      filled.set(32);
    }

    isEcrecoverData.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcrecoverResult(final Boolean b) {
    if (filled.get(33)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_RESULT already set");
    } else {
      filled.set(33);
    }

    isEcrecoverResult.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isInfinity(final Boolean b) {
    if (filled.get(34)) {
      throw new IllegalStateException("ecdata.IS_INFINITY already set");
    } else {
      filled.set(34);
    }

    isInfinity.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isLargePoint(final Boolean b) {
    if (filled.get(35)) {
      throw new IllegalStateException("ecdata.IS_LARGE_POINT already set");
    } else {
      filled.set(35);
    }

    isLargePoint.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isSmallPoint(final Boolean b) {
    if (filled.get(36)) {
      throw new IllegalStateException("ecdata.IS_SMALL_POINT already set");
    } else {
      filled.set(36);
    }

    isSmallPoint.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace limb(final Bytes b) {
    if (filled.get(37)) {
      throw new IllegalStateException("ecdata.LIMB already set");
    } else {
      filled.set(37);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      limb.put((byte) 0);
    }
    limb.put(b.toArrayUnsafe());

    return this;
  }

  public Trace notOnG2(final Boolean b) {
    if (filled.get(38)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2 already set");
    } else {
      filled.set(38);
    }

    notOnG2.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace notOnG2Acc(final Boolean b) {
    if (filled.get(39)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC already set");
    } else {
      filled.set(39);
    }

    notOnG2Acc.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace notOnG2AccMax(final Boolean b) {
    if (filled.get(40)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC_MAX already set");
    } else {
      filled.set(40);
    }

    notOnG2AccMax.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace overallTrivialPairing(final Boolean b) {
    if (filled.get(41)) {
      throw new IllegalStateException("ecdata.OVERALL_TRIVIAL_PAIRING already set");
    } else {
      filled.set(41);
    }

    overallTrivialPairing.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace phase(final int b) {
    if (filled.get(42)) {
      throw new IllegalStateException("ecdata.PHASE already set");
    } else {
      filled.set(42);
    }

    phase.putInt(b);

    return this;
  }

  public Trace stamp(final long b) {
    if (filled.get(43)) {
      throw new IllegalStateException("ecdata.STAMP already set");
    } else {
      filled.set(43);
    }

    stamp.putLong(b);

    return this;
  }

  public Trace successBit(final Boolean b) {
    if (filled.get(44)) {
      throw new IllegalStateException("ecdata.SUCCESS_BIT already set");
    } else {
      filled.set(44);
    }

    successBit.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace totalPairings(final Bytes b) {
    if (filled.get(45)) {
      throw new IllegalStateException("ecdata.TOTAL_PAIRINGS already set");
    } else {
      filled.set(45);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      totalPairings.put((byte) 0);
    }
    totalPairings.put(b.toArrayUnsafe());

    return this;
  }

  public Trace totalSize(final Bytes b) {
    if (filled.get(46)) {
      throw new IllegalStateException("ecdata.TOTAL_SIZE already set");
    } else {
      filled.set(46);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      totalSize.put((byte) 0);
    }
    totalSize.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg1Hi(final Bytes b) {
    if (filled.get(47)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_HI already set");
    } else {
      filled.set(47);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg1Hi.put((byte) 0);
    }
    wcpArg1Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg1Lo(final Bytes b) {
    if (filled.get(48)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_LO already set");
    } else {
      filled.set(48);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg1Lo.put((byte) 0);
    }
    wcpArg1Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg2Hi(final Bytes b) {
    if (filled.get(49)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_HI already set");
    } else {
      filled.set(49);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg2Hi.put((byte) 0);
    }
    wcpArg2Hi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpArg2Lo(final Bytes b) {
    if (filled.get(50)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_LO already set");
    } else {
      filled.set(50);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      wcpArg2Lo.put((byte) 0);
    }
    wcpArg2Lo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace wcpFlag(final Boolean b) {
    if (filled.get(51)) {
      throw new IllegalStateException("ecdata.WCP_FLAG already set");
    } else {
      filled.set(51);
    }

    wcpFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace wcpInst(final UnsignedByte b) {
    if (filled.get(52)) {
      throw new IllegalStateException("ecdata.WCP_INST already set");
    } else {
      filled.set(52);
    }

    wcpInst.put(b.toByte());

    return this;
  }

  public Trace wcpRes(final Boolean b) {
    if (filled.get(53)) {
      throw new IllegalStateException("ecdata.WCP_RES already set");
    } else {
      filled.set(53);
    }

    wcpRes.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(1)) {
      throw new IllegalStateException("ecdata.ACC_PAIRINGS has not been filled");
    }

    if (!filled.get(0)) {
      throw new IllegalStateException(
          "ecdata.ACCEPTABLE_PAIR_OF_POINT_FOR_PAIRING_CIRCUIT has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("ecdata.BYTE_DELTA has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECADD has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECMUL has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECPAIRING has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_ECRECOVER has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("ecdata.CIRCUIT_SELECTOR_G2_MEMBERSHIP has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("ecdata.CT has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("ecdata.CT_MAX has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_HI has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("ecdata.EXT_ARG1_LO has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_HI has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("ecdata.EXT_ARG2_LO has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_HI has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("ecdata.EXT_ARG3_LO has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("ecdata.EXT_FLAG has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("ecdata.EXT_INST has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("ecdata.EXT_RES_HI has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("ecdata.EXT_RES_LO has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("ecdata.G2_MEMBERSHIP_TEST_REQUIRED has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("ecdata.HURDLE has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("ecdata.ID has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("ecdata.INDEX has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("ecdata.INDEX_MAX has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("ecdata.INTERNAL_CHECKS_PASSED has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("ecdata.IS_ECADD_DATA has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("ecdata.IS_ECADD_RESULT has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_DATA has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("ecdata.IS_ECMUL_RESULT has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_DATA has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("ecdata.IS_ECPAIRING_RESULT has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_DATA has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("ecdata.IS_ECRECOVER_RESULT has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("ecdata.IS_INFINITY has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("ecdata.IS_LARGE_POINT has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("ecdata.IS_SMALL_POINT has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("ecdata.LIMB has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2 has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("ecdata.NOT_ON_G2_ACC_MAX has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("ecdata.OVERALL_TRIVIAL_PAIRING has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("ecdata.PHASE has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("ecdata.STAMP has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("ecdata.SUCCESS_BIT has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("ecdata.TOTAL_PAIRINGS has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("ecdata.TOTAL_SIZE has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_HI has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("ecdata.WCP_ARG1_LO has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_HI has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("ecdata.WCP_ARG2_LO has not been filled");
    }

    if (!filled.get(51)) {
      throw new IllegalStateException("ecdata.WCP_FLAG has not been filled");
    }

    if (!filled.get(52)) {
      throw new IllegalStateException("ecdata.WCP_INST has not been filled");
    }

    if (!filled.get(53)) {
      throw new IllegalStateException("ecdata.WCP_RES has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(1)) {
      accPairings.position(accPairings.position() + 32);
    }

    if (!filled.get(0)) {
      acceptablePairOfPointForPairingCircuit.position(
          acceptablePairOfPointForPairingCircuit.position() + 1);
    }

    if (!filled.get(2)) {
      byteDelta.position(byteDelta.position() + 1);
    }

    if (!filled.get(3)) {
      circuitSelectorEcadd.position(circuitSelectorEcadd.position() + 1);
    }

    if (!filled.get(4)) {
      circuitSelectorEcmul.position(circuitSelectorEcmul.position() + 1);
    }

    if (!filled.get(5)) {
      circuitSelectorEcpairing.position(circuitSelectorEcpairing.position() + 1);
    }

    if (!filled.get(6)) {
      circuitSelectorEcrecover.position(circuitSelectorEcrecover.position() + 1);
    }

    if (!filled.get(7)) {
      circuitSelectorG2Membership.position(circuitSelectorG2Membership.position() + 1);
    }

    if (!filled.get(8)) {
      ct.position(ct.position() + 2);
    }

    if (!filled.get(9)) {
      ctMax.position(ctMax.position() + 2);
    }

    if (!filled.get(10)) {
      extArg1Hi.position(extArg1Hi.position() + 32);
    }

    if (!filled.get(11)) {
      extArg1Lo.position(extArg1Lo.position() + 32);
    }

    if (!filled.get(12)) {
      extArg2Hi.position(extArg2Hi.position() + 32);
    }

    if (!filled.get(13)) {
      extArg2Lo.position(extArg2Lo.position() + 32);
    }

    if (!filled.get(14)) {
      extArg3Hi.position(extArg3Hi.position() + 32);
    }

    if (!filled.get(15)) {
      extArg3Lo.position(extArg3Lo.position() + 32);
    }

    if (!filled.get(16)) {
      extFlag.position(extFlag.position() + 1);
    }

    if (!filled.get(17)) {
      extInst.position(extInst.position() + 1);
    }

    if (!filled.get(18)) {
      extResHi.position(extResHi.position() + 32);
    }

    if (!filled.get(19)) {
      extResLo.position(extResLo.position() + 32);
    }

    if (!filled.get(20)) {
      g2MembershipTestRequired.position(g2MembershipTestRequired.position() + 1);
    }

    if (!filled.get(21)) {
      hurdle.position(hurdle.position() + 1);
    }

    if (!filled.get(22)) {
      id.position(id.position() + 8);
    }

    if (!filled.get(23)) {
      index.position(index.position() + 1);
    }

    if (!filled.get(24)) {
      indexMax.position(indexMax.position() + 32);
    }

    if (!filled.get(25)) {
      internalChecksPassed.position(internalChecksPassed.position() + 1);
    }

    if (!filled.get(26)) {
      isEcaddData.position(isEcaddData.position() + 1);
    }

    if (!filled.get(27)) {
      isEcaddResult.position(isEcaddResult.position() + 1);
    }

    if (!filled.get(28)) {
      isEcmulData.position(isEcmulData.position() + 1);
    }

    if (!filled.get(29)) {
      isEcmulResult.position(isEcmulResult.position() + 1);
    }

    if (!filled.get(30)) {
      isEcpairingData.position(isEcpairingData.position() + 1);
    }

    if (!filled.get(31)) {
      isEcpairingResult.position(isEcpairingResult.position() + 1);
    }

    if (!filled.get(32)) {
      isEcrecoverData.position(isEcrecoverData.position() + 1);
    }

    if (!filled.get(33)) {
      isEcrecoverResult.position(isEcrecoverResult.position() + 1);
    }

    if (!filled.get(34)) {
      isInfinity.position(isInfinity.position() + 1);
    }

    if (!filled.get(35)) {
      isLargePoint.position(isLargePoint.position() + 1);
    }

    if (!filled.get(36)) {
      isSmallPoint.position(isSmallPoint.position() + 1);
    }

    if (!filled.get(37)) {
      limb.position(limb.position() + 32);
    }

    if (!filled.get(38)) {
      notOnG2.position(notOnG2.position() + 1);
    }

    if (!filled.get(39)) {
      notOnG2Acc.position(notOnG2Acc.position() + 1);
    }

    if (!filled.get(40)) {
      notOnG2AccMax.position(notOnG2AccMax.position() + 1);
    }

    if (!filled.get(41)) {
      overallTrivialPairing.position(overallTrivialPairing.position() + 1);
    }

    if (!filled.get(42)) {
      phase.position(phase.position() + 4);
    }

    if (!filled.get(43)) {
      stamp.position(stamp.position() + 8);
    }

    if (!filled.get(44)) {
      successBit.position(successBit.position() + 1);
    }

    if (!filled.get(45)) {
      totalPairings.position(totalPairings.position() + 32);
    }

    if (!filled.get(46)) {
      totalSize.position(totalSize.position() + 32);
    }

    if (!filled.get(47)) {
      wcpArg1Hi.position(wcpArg1Hi.position() + 32);
    }

    if (!filled.get(48)) {
      wcpArg1Lo.position(wcpArg1Lo.position() + 32);
    }

    if (!filled.get(49)) {
      wcpArg2Hi.position(wcpArg2Hi.position() + 32);
    }

    if (!filled.get(50)) {
      wcpArg2Lo.position(wcpArg2Lo.position() + 32);
    }

    if (!filled.get(51)) {
      wcpFlag.position(wcpFlag.position() + 1);
    }

    if (!filled.get(52)) {
      wcpInst.position(wcpInst.position() + 1);
    }

    if (!filled.get(53)) {
      wcpRes.position(wcpRes.position() + 1);
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
