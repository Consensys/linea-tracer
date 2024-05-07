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

package net.consensys.linea.zktracer.module.oob;

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
  public static final int ADD = 0x1;
  public static final int CT_MAX_BLAKE2F_cds = 0x1;
  public static final int CT_MAX_BLAKE2F_params = 0x1;
  public static final int CT_MAX_CALL = 0x2;
  public static final int CT_MAX_CDL = 0x0;
  public static final int CT_MAX_CREATE = 0x2;
  public static final int CT_MAX_DEPLOYMENT = 0x0;
  public static final int CT_MAX_ECADD = 0x2;
  public static final int CT_MAX_ECMUL = 0x2;
  public static final int CT_MAX_ECPAIRING = 0x4;
  public static final int CT_MAX_ECRECOVER = 0x2;
  public static final int CT_MAX_IDENTITY = 0x3;
  public static final int CT_MAX_JUMP = 0x0;
  public static final int CT_MAX_JUMPI = 0x1;
  public static final int CT_MAX_MODEXP_cds = 0x2;
  public static final int CT_MAX_MODEXP_extract = 0x3;
  public static final int CT_MAX_MODEXP_lead = 0x3;
  public static final int CT_MAX_MODEXP_pricing = 0x5;
  public static final int CT_MAX_MODEXP_xbs = 0x2;
  public static final int CT_MAX_RDC = 0x2;
  public static final int CT_MAX_RIPEMD = 0x3;
  public static final int CT_MAX_SHA2 = 0x3;
  public static final int CT_MAX_SSTORE = 0x0;
  public static final int CT_MAX_XCALL = 0x0;
  public static final int DIV = 0x4;
  public static final int EC_DATA_PHASE_ECADD_DATA = 0x3;
  public static final int EC_DATA_PHASE_ECADD_RESULT = 0x4;
  public static final int EC_DATA_PHASE_ECMUL_DATA = 0x5;
  public static final int EC_DATA_PHASE_ECMUL_RESULT = 0x6;
  public static final int EC_DATA_PHASE_ECRECOVER_DATA = 0x1;
  public static final int EC_DATA_PHASE_ECRECOVER_RESULT = 0x2;
  public static final int EC_DATA_PHASE_PAIRING_DATA = 0x7;
  public static final int EC_DATA_PHASE_PAIRING_RESULT = 0x8;
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
  public static final int EQ = 0x14;
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
  public static final int GT = 0x11;
  public static final int G_CALLSTIPEND = 0x8fc;
  public static final int G_QUADDIVISOR = 0x3;
  public static final int ISZERO = 0x15;
  public static final int LINEA_BLOCK_GAS_LIMIT = 0x1c9c380;
  public static final int LLARGE = 0x10;
  public static final int LLARGEMO = 0xf;
  public static final int LLARGEPO = 0x11;
  public static final int LT = 0x10;
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
  public static final int MOD = 0x6;
  public static final int OOB_INST_blake_cds = 0xfa09;
  public static final int OOB_INST_blake_cds = 0xfa09;
  public static final int OOB_INST_blake_params = 0xfb09;
  public static final int OOB_INST_blake_params = 0xfb09;
  public static final int OOB_INST_call = 0xca;
  public static final int OOB_INST_call = 0xca;
  public static final int OOB_INST_cdl = 0x35;
  public static final int OOB_INST_cdl = 0x35;
  public static final int OOB_INST_create = 0xce;
  public static final int OOB_INST_create = 0xce;
  public static final int OOB_INST_deployment = 0xf3;
  public static final int OOB_INST_deployment = 0xf3;
  public static final int OOB_INST_ecadd = 0xff06;
  public static final int OOB_INST_ecadd = 0xff06;
  public static final int OOB_INST_ecmul = 0xff07;
  public static final int OOB_INST_ecmul = 0xff07;
  public static final int OOB_INST_ecpairing = 0xff08;
  public static final int OOB_INST_ecpairing = 0xff08;
  public static final int OOB_INST_ecrecover = 0xff01;
  public static final int OOB_INST_ecrecover = 0xff01;
  public static final int OOB_INST_identity = 0xff04;
  public static final int OOB_INST_identity = 0xff04;
  public static final int OOB_INST_jump = 0x56;
  public static final int OOB_INST_jump = 0x56;
  public static final int OOB_INST_jumpi = 0x57;
  public static final int OOB_INST_jumpi = 0x57;
  public static final int OOB_INST_modexp_cds = 0xfa05;
  public static final int OOB_INST_modexp_cds = 0xfa05;
  public static final int OOB_INST_modexp_extract = 0xfe05;
  public static final int OOB_INST_modexp_extract = 0xfe05;
  public static final int OOB_INST_modexp_lead = 0xfc05;
  public static final int OOB_INST_modexp_lead = 0xfc05;
  public static final int OOB_INST_modexp_pricing = 0xfd05;
  public static final int OOB_INST_modexp_pricing = 0xfd05;
  public static final int OOB_INST_modexp_xbs = 0xfb05;
  public static final int OOB_INST_modexp_xbs = 0xfb05;
  public static final int OOB_INST_rdc = 0x3e;
  public static final int OOB_INST_rdc = 0x3e;
  public static final int OOB_INST_ripemd = 0xff03;
  public static final int OOB_INST_ripemd = 0xff03;
  public static final int OOB_INST_sha2 = 0xff02;
  public static final int OOB_INST_sha2 = 0xff02;
  public static final int OOB_INST_sstore = 0x55;
  public static final int OOB_INST_sstore = 0x55;
  public static final int OOB_INST_xcall = 0xcc;
  public static final int OOB_INST_xcall = 0xcc;
  public static final int PHASE_BLAKE_DATA = 0x5;
  public static final int PHASE_BLAKE_PARAMS = 0x6;
  public static final int PHASE_BLAKE_RESULT = 0x7;
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
  public static final int WCP_INST_GEQ = 0xe;
  public static final int WCP_INST_LEQ = 0xf;
  public static final int WORD_SIZE = 0x20;
  public static final int WORD_SIZE_MO = 0x1f;

  private final BitSet filled = new BitSet();
  private int currentLine = 0;

  private final MappedByteBuffer addFlag;
  private final MappedByteBuffer ct;
  private final MappedByteBuffer ctMax;
  private final MappedByteBuffer data1;
  private final MappedByteBuffer data2;
  private final MappedByteBuffer data3;
  private final MappedByteBuffer data4;
  private final MappedByteBuffer data5;
  private final MappedByteBuffer data6;
  private final MappedByteBuffer data7;
  private final MappedByteBuffer data8;
  private final MappedByteBuffer isBlake2FCds;
  private final MappedByteBuffer isBlake2FParams;
  private final MappedByteBuffer isCall;
  private final MappedByteBuffer isCdl;
  private final MappedByteBuffer isCreate;
  private final MappedByteBuffer isDeployment;
  private final MappedByteBuffer isEcadd;
  private final MappedByteBuffer isEcmul;
  private final MappedByteBuffer isEcpairing;
  private final MappedByteBuffer isEcrecover;
  private final MappedByteBuffer isIdentity;
  private final MappedByteBuffer isJump;
  private final MappedByteBuffer isJumpi;
  private final MappedByteBuffer isModexpCds;
  private final MappedByteBuffer isModexpExtract;
  private final MappedByteBuffer isModexpLead;
  private final MappedByteBuffer isModexpPricing;
  private final MappedByteBuffer isModexpXbs;
  private final MappedByteBuffer isRdc;
  private final MappedByteBuffer isRipemd;
  private final MappedByteBuffer isSha2;
  private final MappedByteBuffer isSstore;
  private final MappedByteBuffer isXcall;
  private final MappedByteBuffer modFlag;
  private final MappedByteBuffer oobInst;
  private final MappedByteBuffer outgoingData1;
  private final MappedByteBuffer outgoingData2;
  private final MappedByteBuffer outgoingData3;
  private final MappedByteBuffer outgoingData4;
  private final MappedByteBuffer outgoingInst;
  private final MappedByteBuffer outgoingResLo;
  private final MappedByteBuffer stamp;
  private final MappedByteBuffer wcpFlag;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("oob.ADD_FLAG", 1, length),
        new ColumnHeader("oob.CT", 2, length),
        new ColumnHeader("oob.CT_MAX", 2, length),
        new ColumnHeader("oob.DATA_1", 32, length),
        new ColumnHeader("oob.DATA_2", 32, length),
        new ColumnHeader("oob.DATA_3", 32, length),
        new ColumnHeader("oob.DATA_4", 32, length),
        new ColumnHeader("oob.DATA_5", 32, length),
        new ColumnHeader("oob.DATA_6", 32, length),
        new ColumnHeader("oob.DATA_7", 32, length),
        new ColumnHeader("oob.DATA_8", 32, length),
        new ColumnHeader("oob.IS_BLAKE2F_cds", 1, length),
        new ColumnHeader("oob.IS_BLAKE2F_params", 1, length),
        new ColumnHeader("oob.IS_CALL", 1, length),
        new ColumnHeader("oob.IS_CDL", 1, length),
        new ColumnHeader("oob.IS_CREATE", 1, length),
        new ColumnHeader("oob.IS_DEPLOYMENT", 1, length),
        new ColumnHeader("oob.IS_ECADD", 1, length),
        new ColumnHeader("oob.IS_ECMUL", 1, length),
        new ColumnHeader("oob.IS_ECPAIRING", 1, length),
        new ColumnHeader("oob.IS_ECRECOVER", 1, length),
        new ColumnHeader("oob.IS_IDENTITY", 1, length),
        new ColumnHeader("oob.IS_JUMP", 1, length),
        new ColumnHeader("oob.IS_JUMPI", 1, length),
        new ColumnHeader("oob.IS_MODEXP_cds", 1, length),
        new ColumnHeader("oob.IS_MODEXP_extract", 1, length),
        new ColumnHeader("oob.IS_MODEXP_lead", 1, length),
        new ColumnHeader("oob.IS_MODEXP_pricing", 1, length),
        new ColumnHeader("oob.IS_MODEXP_xbs", 1, length),
        new ColumnHeader("oob.IS_RDC", 1, length),
        new ColumnHeader("oob.IS_RIPEMD", 1, length),
        new ColumnHeader("oob.IS_SHA2", 1, length),
        new ColumnHeader("oob.IS_SSTORE", 1, length),
        new ColumnHeader("oob.IS_XCALL", 1, length),
        new ColumnHeader("oob.MOD_FLAG", 1, length),
        new ColumnHeader("oob.OOB_INST", 32, length),
        new ColumnHeader("oob.OUTGOING_DATA_1", 32, length),
        new ColumnHeader("oob.OUTGOING_DATA_2", 32, length),
        new ColumnHeader("oob.OUTGOING_DATA_3", 32, length),
        new ColumnHeader("oob.OUTGOING_DATA_4", 32, length),
        new ColumnHeader("oob.OUTGOING_INST", 1, length),
        new ColumnHeader("oob.OUTGOING_RES_LO", 32, length),
        new ColumnHeader("oob.STAMP", 8, length),
        new ColumnHeader("oob.WCP_FLAG", 1, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.addFlag = buffers.get(0);
    this.ct = buffers.get(1);
    this.ctMax = buffers.get(2);
    this.data1 = buffers.get(3);
    this.data2 = buffers.get(4);
    this.data3 = buffers.get(5);
    this.data4 = buffers.get(6);
    this.data5 = buffers.get(7);
    this.data6 = buffers.get(8);
    this.data7 = buffers.get(9);
    this.data8 = buffers.get(10);
    this.isBlake2FCds = buffers.get(11);
    this.isBlake2FParams = buffers.get(12);
    this.isCall = buffers.get(13);
    this.isCdl = buffers.get(14);
    this.isCreate = buffers.get(15);
    this.isDeployment = buffers.get(16);
    this.isEcadd = buffers.get(17);
    this.isEcmul = buffers.get(18);
    this.isEcpairing = buffers.get(19);
    this.isEcrecover = buffers.get(20);
    this.isIdentity = buffers.get(21);
    this.isJump = buffers.get(22);
    this.isJumpi = buffers.get(23);
    this.isModexpCds = buffers.get(24);
    this.isModexpExtract = buffers.get(25);
    this.isModexpLead = buffers.get(26);
    this.isModexpPricing = buffers.get(27);
    this.isModexpXbs = buffers.get(28);
    this.isRdc = buffers.get(29);
    this.isRipemd = buffers.get(30);
    this.isSha2 = buffers.get(31);
    this.isSstore = buffers.get(32);
    this.isXcall = buffers.get(33);
    this.modFlag = buffers.get(34);
    this.oobInst = buffers.get(35);
    this.outgoingData1 = buffers.get(36);
    this.outgoingData2 = buffers.get(37);
    this.outgoingData3 = buffers.get(38);
    this.outgoingData4 = buffers.get(39);
    this.outgoingInst = buffers.get(40);
    this.outgoingResLo = buffers.get(41);
    this.stamp = buffers.get(42);
    this.wcpFlag = buffers.get(43);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace addFlag(final Boolean b) {
    if (filled.get(0)) {
      throw new IllegalStateException("oob.ADD_FLAG already set");
    } else {
      filled.set(0);
    }

    addFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace ct(final short b) {
    if (filled.get(1)) {
      throw new IllegalStateException("oob.CT already set");
    } else {
      filled.set(1);
    }

    ct.putShort(b);

    return this;
  }

  public Trace ctMax(final short b) {
    if (filled.get(2)) {
      throw new IllegalStateException("oob.CT_MAX already set");
    } else {
      filled.set(2);
    }

    ctMax.putShort(b);

    return this;
  }

  public Trace data1(final Bytes b) {
    if (filled.get(3)) {
      throw new IllegalStateException("oob.DATA_1 already set");
    } else {
      filled.set(3);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data1.put((byte) 0);
    }
    data1.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data2(final Bytes b) {
    if (filled.get(4)) {
      throw new IllegalStateException("oob.DATA_2 already set");
    } else {
      filled.set(4);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data2.put((byte) 0);
    }
    data2.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data3(final Bytes b) {
    if (filled.get(5)) {
      throw new IllegalStateException("oob.DATA_3 already set");
    } else {
      filled.set(5);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data3.put((byte) 0);
    }
    data3.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data4(final Bytes b) {
    if (filled.get(6)) {
      throw new IllegalStateException("oob.DATA_4 already set");
    } else {
      filled.set(6);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data4.put((byte) 0);
    }
    data4.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data5(final Bytes b) {
    if (filled.get(7)) {
      throw new IllegalStateException("oob.DATA_5 already set");
    } else {
      filled.set(7);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data5.put((byte) 0);
    }
    data5.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data6(final Bytes b) {
    if (filled.get(8)) {
      throw new IllegalStateException("oob.DATA_6 already set");
    } else {
      filled.set(8);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data6.put((byte) 0);
    }
    data6.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data7(final Bytes b) {
    if (filled.get(9)) {
      throw new IllegalStateException("oob.DATA_7 already set");
    } else {
      filled.set(9);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data7.put((byte) 0);
    }
    data7.put(b.toArrayUnsafe());

    return this;
  }

  public Trace data8(final Bytes b) {
    if (filled.get(10)) {
      throw new IllegalStateException("oob.DATA_8 already set");
    } else {
      filled.set(10);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      data8.put((byte) 0);
    }
    data8.put(b.toArrayUnsafe());

    return this;
  }

  public Trace isBlake2FCds(final Boolean b) {
    if (filled.get(11)) {
      throw new IllegalStateException("oob.IS_BLAKE2F_cds already set");
    } else {
      filled.set(11);
    }

    isBlake2FCds.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isBlake2FParams(final Boolean b) {
    if (filled.get(12)) {
      throw new IllegalStateException("oob.IS_BLAKE2F_params already set");
    } else {
      filled.set(12);
    }

    isBlake2FParams.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isCall(final Boolean b) {
    if (filled.get(13)) {
      throw new IllegalStateException("oob.IS_CALL already set");
    } else {
      filled.set(13);
    }

    isCall.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isCdl(final Boolean b) {
    if (filled.get(14)) {
      throw new IllegalStateException("oob.IS_CDL already set");
    } else {
      filled.set(14);
    }

    isCdl.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isCreate(final Boolean b) {
    if (filled.get(15)) {
      throw new IllegalStateException("oob.IS_CREATE already set");
    } else {
      filled.set(15);
    }

    isCreate.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isDeployment(final Boolean b) {
    if (filled.get(16)) {
      throw new IllegalStateException("oob.IS_DEPLOYMENT already set");
    } else {
      filled.set(16);
    }

    isDeployment.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcadd(final Boolean b) {
    if (filled.get(17)) {
      throw new IllegalStateException("oob.IS_ECADD already set");
    } else {
      filled.set(17);
    }

    isEcadd.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcmul(final Boolean b) {
    if (filled.get(18)) {
      throw new IllegalStateException("oob.IS_ECMUL already set");
    } else {
      filled.set(18);
    }

    isEcmul.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcpairing(final Boolean b) {
    if (filled.get(19)) {
      throw new IllegalStateException("oob.IS_ECPAIRING already set");
    } else {
      filled.set(19);
    }

    isEcpairing.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isEcrecover(final Boolean b) {
    if (filled.get(20)) {
      throw new IllegalStateException("oob.IS_ECRECOVER already set");
    } else {
      filled.set(20);
    }

    isEcrecover.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isIdentity(final Boolean b) {
    if (filled.get(21)) {
      throw new IllegalStateException("oob.IS_IDENTITY already set");
    } else {
      filled.set(21);
    }

    isIdentity.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isJump(final Boolean b) {
    if (filled.get(22)) {
      throw new IllegalStateException("oob.IS_JUMP already set");
    } else {
      filled.set(22);
    }

    isJump.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isJumpi(final Boolean b) {
    if (filled.get(23)) {
      throw new IllegalStateException("oob.IS_JUMPI already set");
    } else {
      filled.set(23);
    }

    isJumpi.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isModexpCds(final Boolean b) {
    if (filled.get(24)) {
      throw new IllegalStateException("oob.IS_MODEXP_cds already set");
    } else {
      filled.set(24);
    }

    isModexpCds.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isModexpExtract(final Boolean b) {
    if (filled.get(25)) {
      throw new IllegalStateException("oob.IS_MODEXP_extract already set");
    } else {
      filled.set(25);
    }

    isModexpExtract.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isModexpLead(final Boolean b) {
    if (filled.get(26)) {
      throw new IllegalStateException("oob.IS_MODEXP_lead already set");
    } else {
      filled.set(26);
    }

    isModexpLead.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isModexpPricing(final Boolean b) {
    if (filled.get(27)) {
      throw new IllegalStateException("oob.IS_MODEXP_pricing already set");
    } else {
      filled.set(27);
    }

    isModexpPricing.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isModexpXbs(final Boolean b) {
    if (filled.get(28)) {
      throw new IllegalStateException("oob.IS_MODEXP_xbs already set");
    } else {
      filled.set(28);
    }

    isModexpXbs.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isRdc(final Boolean b) {
    if (filled.get(29)) {
      throw new IllegalStateException("oob.IS_RDC already set");
    } else {
      filled.set(29);
    }

    isRdc.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isRipemd(final Boolean b) {
    if (filled.get(30)) {
      throw new IllegalStateException("oob.IS_RIPEMD already set");
    } else {
      filled.set(30);
    }

    isRipemd.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isSha2(final Boolean b) {
    if (filled.get(31)) {
      throw new IllegalStateException("oob.IS_SHA2 already set");
    } else {
      filled.set(31);
    }

    isSha2.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isSstore(final Boolean b) {
    if (filled.get(32)) {
      throw new IllegalStateException("oob.IS_SSTORE already set");
    } else {
      filled.set(32);
    }

    isSstore.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace isXcall(final Boolean b) {
    if (filled.get(33)) {
      throw new IllegalStateException("oob.IS_XCALL already set");
    } else {
      filled.set(33);
    }

    isXcall.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace modFlag(final Boolean b) {
    if (filled.get(34)) {
      throw new IllegalStateException("oob.MOD_FLAG already set");
    } else {
      filled.set(34);
    }

    modFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace oobInst(final Bytes b) {
    if (filled.get(35)) {
      throw new IllegalStateException("oob.OOB_INST already set");
    } else {
      filled.set(35);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      oobInst.put((byte) 0);
    }
    oobInst.put(b.toArrayUnsafe());

    return this;
  }

  public Trace outgoingData1(final Bytes b) {
    if (filled.get(36)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_1 already set");
    } else {
      filled.set(36);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      outgoingData1.put((byte) 0);
    }
    outgoingData1.put(b.toArrayUnsafe());

    return this;
  }

  public Trace outgoingData2(final Bytes b) {
    if (filled.get(37)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_2 already set");
    } else {
      filled.set(37);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      outgoingData2.put((byte) 0);
    }
    outgoingData2.put(b.toArrayUnsafe());

    return this;
  }

  public Trace outgoingData3(final Bytes b) {
    if (filled.get(38)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_3 already set");
    } else {
      filled.set(38);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      outgoingData3.put((byte) 0);
    }
    outgoingData3.put(b.toArrayUnsafe());

    return this;
  }

  public Trace outgoingData4(final Bytes b) {
    if (filled.get(39)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_4 already set");
    } else {
      filled.set(39);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      outgoingData4.put((byte) 0);
    }
    outgoingData4.put(b.toArrayUnsafe());

    return this;
  }

  public Trace outgoingInst(final UnsignedByte b) {
    if (filled.get(40)) {
      throw new IllegalStateException("oob.OUTGOING_INST already set");
    } else {
      filled.set(40);
    }

    outgoingInst.put(b.toByte());

    return this;
  }

  public Trace outgoingResLo(final Bytes b) {
    if (filled.get(41)) {
      throw new IllegalStateException("oob.OUTGOING_RES_LO already set");
    } else {
      filled.set(41);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      outgoingResLo.put((byte) 0);
    }
    outgoingResLo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace stamp(final long b) {
    if (filled.get(42)) {
      throw new IllegalStateException("oob.STAMP already set");
    } else {
      filled.set(42);
    }

    stamp.putLong(b);

    return this;
  }

  public Trace wcpFlag(final Boolean b) {
    if (filled.get(43)) {
      throw new IllegalStateException("oob.WCP_FLAG already set");
    } else {
      filled.set(43);
    }

    wcpFlag.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(0)) {
      throw new IllegalStateException("oob.ADD_FLAG has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("oob.CT has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("oob.CT_MAX has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("oob.DATA_1 has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("oob.DATA_2 has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("oob.DATA_3 has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("oob.DATA_4 has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("oob.DATA_5 has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("oob.DATA_6 has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("oob.DATA_7 has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("oob.DATA_8 has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("oob.IS_BLAKE2F_cds has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("oob.IS_BLAKE2F_params has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("oob.IS_CALL has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("oob.IS_CDL has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("oob.IS_CREATE has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("oob.IS_DEPLOYMENT has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("oob.IS_ECADD has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("oob.IS_ECMUL has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("oob.IS_ECPAIRING has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("oob.IS_ECRECOVER has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("oob.IS_IDENTITY has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("oob.IS_JUMP has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("oob.IS_JUMPI has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("oob.IS_MODEXP_cds has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("oob.IS_MODEXP_extract has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("oob.IS_MODEXP_lead has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("oob.IS_MODEXP_pricing has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("oob.IS_MODEXP_xbs has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("oob.IS_RDC has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("oob.IS_RIPEMD has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("oob.IS_SHA2 has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("oob.IS_SSTORE has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("oob.IS_XCALL has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("oob.MOD_FLAG has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("oob.OOB_INST has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_1 has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_2 has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_3 has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("oob.OUTGOING_DATA_4 has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("oob.OUTGOING_INST has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("oob.OUTGOING_RES_LO has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("oob.STAMP has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("oob.WCP_FLAG has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(0)) {
      addFlag.position(addFlag.position() + 1);
    }

    if (!filled.get(1)) {
      ct.position(ct.position() + 2);
    }

    if (!filled.get(2)) {
      ctMax.position(ctMax.position() + 2);
    }

    if (!filled.get(3)) {
      data1.position(data1.position() + 32);
    }

    if (!filled.get(4)) {
      data2.position(data2.position() + 32);
    }

    if (!filled.get(5)) {
      data3.position(data3.position() + 32);
    }

    if (!filled.get(6)) {
      data4.position(data4.position() + 32);
    }

    if (!filled.get(7)) {
      data5.position(data5.position() + 32);
    }

    if (!filled.get(8)) {
      data6.position(data6.position() + 32);
    }

    if (!filled.get(9)) {
      data7.position(data7.position() + 32);
    }

    if (!filled.get(10)) {
      data8.position(data8.position() + 32);
    }

    if (!filled.get(11)) {
      isBlake2FCds.position(isBlake2FCds.position() + 1);
    }

    if (!filled.get(12)) {
      isBlake2FParams.position(isBlake2FParams.position() + 1);
    }

    if (!filled.get(13)) {
      isCall.position(isCall.position() + 1);
    }

    if (!filled.get(14)) {
      isCdl.position(isCdl.position() + 1);
    }

    if (!filled.get(15)) {
      isCreate.position(isCreate.position() + 1);
    }

    if (!filled.get(16)) {
      isDeployment.position(isDeployment.position() + 1);
    }

    if (!filled.get(17)) {
      isEcadd.position(isEcadd.position() + 1);
    }

    if (!filled.get(18)) {
      isEcmul.position(isEcmul.position() + 1);
    }

    if (!filled.get(19)) {
      isEcpairing.position(isEcpairing.position() + 1);
    }

    if (!filled.get(20)) {
      isEcrecover.position(isEcrecover.position() + 1);
    }

    if (!filled.get(21)) {
      isIdentity.position(isIdentity.position() + 1);
    }

    if (!filled.get(22)) {
      isJump.position(isJump.position() + 1);
    }

    if (!filled.get(23)) {
      isJumpi.position(isJumpi.position() + 1);
    }

    if (!filled.get(24)) {
      isModexpCds.position(isModexpCds.position() + 1);
    }

    if (!filled.get(25)) {
      isModexpExtract.position(isModexpExtract.position() + 1);
    }

    if (!filled.get(26)) {
      isModexpLead.position(isModexpLead.position() + 1);
    }

    if (!filled.get(27)) {
      isModexpPricing.position(isModexpPricing.position() + 1);
    }

    if (!filled.get(28)) {
      isModexpXbs.position(isModexpXbs.position() + 1);
    }

    if (!filled.get(29)) {
      isRdc.position(isRdc.position() + 1);
    }

    if (!filled.get(30)) {
      isRipemd.position(isRipemd.position() + 1);
    }

    if (!filled.get(31)) {
      isSha2.position(isSha2.position() + 1);
    }

    if (!filled.get(32)) {
      isSstore.position(isSstore.position() + 1);
    }

    if (!filled.get(33)) {
      isXcall.position(isXcall.position() + 1);
    }

    if (!filled.get(34)) {
      modFlag.position(modFlag.position() + 1);
    }

    if (!filled.get(35)) {
      oobInst.position(oobInst.position() + 32);
    }

    if (!filled.get(36)) {
      outgoingData1.position(outgoingData1.position() + 32);
    }

    if (!filled.get(37)) {
      outgoingData2.position(outgoingData2.position() + 32);
    }

    if (!filled.get(38)) {
      outgoingData3.position(outgoingData3.position() + 32);
    }

    if (!filled.get(39)) {
      outgoingData4.position(outgoingData4.position() + 32);
    }

    if (!filled.get(40)) {
      outgoingInst.position(outgoingInst.position() + 1);
    }

    if (!filled.get(41)) {
      outgoingResLo.position(outgoingResLo.position() + 32);
    }

    if (!filled.get(42)) {
      stamp.position(stamp.position() + 8);
    }

    if (!filled.get(43)) {
      wcpFlag.position(wcpFlag.position() + 1);
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
