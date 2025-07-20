/*
 * Copyright Consensys Software Inc.
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
 package net.consensys.linea.zktracer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

/**
 * WARNING: This code is generated automatically.
 *
 * <p>Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly</p>.
 *
 */
public interface Trace {
   final int BLOCKHASH_MAX_HISTORY = 0x100;
   final int CREATE2_SHIFT = 0xff;
   final BigInteger EIP2681_MAX_NONCE = new BigInteger("18446744073709551615");
   final int EIP_3541_MARKER = 0xef;
   final BigInteger EMPTY_KECCAK_HI = new BigInteger("262949717399590921288928019264691438528");
   final BigInteger EMPTY_KECCAK_LO = new BigInteger("304396909071904405792975023732328604784");
   final long EMPTY_RIPEMD_HI = 0x9c1185a5L;
   final BigInteger EMPTY_RIPEMD_LO = new BigInteger("263072838190121256777638892741499129137");
   final BigInteger EMPTY_SHA2_HI = new BigInteger("302652579918965577886386472538583578916");
   final BigInteger EMPTY_SHA2_LO = new BigInteger("52744687940778649747319168982913824853");
   final BigInteger ETHEREUM_GAS_LIMIT_MAXIMUM = new BigInteger("18446744073709551615");
   final int ETHEREUM_GAS_LIMIT_MINIMUM = 0x1388;
   final int EVM_CANCUN = 0x11;
   final int EVM_FORK = 0xe;
   final int EVM_INST_ADD = 0x1;
   final int EVM_INST_ADDMOD = 0x8;
   final int EVM_INST_ADDRESS = 0x30;
   final int EVM_INST_AND = 0x16;
   final int EVM_INST_BALANCE = 0x31;
   final int EVM_INST_BASEFEE = 0x48;
   final int EVM_INST_BLOBBASEFEE = 0x4a;
   final int EVM_INST_BLOBHASH = 0x49;
   final int EVM_INST_BLOCKHASH = 0x40;
   final int EVM_INST_BYTE = 0x1a;
   final int EVM_INST_CALL = 0xf1;
   final int EVM_INST_CALLCODE = 0xf2;
   final int EVM_INST_CALLDATACOPY = 0x37;
   final int EVM_INST_CALLDATALOAD = 0x35;
   final int EVM_INST_CALLDATASIZE = 0x36;
   final int EVM_INST_CALLER = 0x33;
   final int EVM_INST_CALLVALUE = 0x34;
   final int EVM_INST_CHAINID = 0x46;
   final int EVM_INST_CODECOPY = 0x39;
   final int EVM_INST_CODESIZE = 0x38;
   final int EVM_INST_COINBASE = 0x41;
   final int EVM_INST_CREATE = 0xf0;
   final int EVM_INST_CREATE2 = 0xf5;
   final int EVM_INST_DELEGATECALL = 0xf4;
   final int EVM_INST_DIFFICULTY = 0x44;
   final int EVM_INST_DIV = 0x4;
   final int EVM_INST_DUP1 = 0x80;
   final int EVM_INST_DUP10 = 0x89;
   final int EVM_INST_DUP11 = 0x8a;
   final int EVM_INST_DUP12 = 0x8b;
   final int EVM_INST_DUP13 = 0x8c;
   final int EVM_INST_DUP14 = 0x8d;
   final int EVM_INST_DUP15 = 0x8e;
   final int EVM_INST_DUP16 = 0x8f;
   final int EVM_INST_DUP2 = 0x81;
   final int EVM_INST_DUP3 = 0x82;
   final int EVM_INST_DUP4 = 0x83;
   final int EVM_INST_DUP5 = 0x84;
   final int EVM_INST_DUP6 = 0x85;
   final int EVM_INST_DUP7 = 0x86;
   final int EVM_INST_DUP8 = 0x87;
   final int EVM_INST_DUP9 = 0x88;
   final int EVM_INST_EQ = 0x14;
   final int EVM_INST_EXP = 0xa;
   final int EVM_INST_EXTCODECOPY = 0x3c;
   final int EVM_INST_EXTCODEHASH = 0x3f;
   final int EVM_INST_EXTCODESIZE = 0x3b;
   final int EVM_INST_GAS = 0x5a;
   final int EVM_INST_GASLIMIT = 0x45;
   final int EVM_INST_GASPRICE = 0x3a;
   final int EVM_INST_GT = 0x11;
   final int EVM_INST_INVALID = 0xfe;
   final int EVM_INST_ISZERO = 0x15;
   final int EVM_INST_JUMP = 0x56;
   final int EVM_INST_JUMPDEST = 0x5b;
   final int EVM_INST_JUMPI = 0x57;
   final int EVM_INST_LOG0 = 0xa0;
   final int EVM_INST_LOG1 = 0xa1;
   final int EVM_INST_LOG2 = 0xa2;
   final int EVM_INST_LOG3 = 0xa3;
   final int EVM_INST_LOG4 = 0xa4;
   final int EVM_INST_LT = 0x10;
   final int EVM_INST_MCOPY = 0x5e;
   final int EVM_INST_MLOAD = 0x51;
   final int EVM_INST_MOD = 0x6;
   final int EVM_INST_MSIZE = 0x59;
   final int EVM_INST_MSTORE = 0x52;
   final int EVM_INST_MSTORE8 = 0x53;
   final int EVM_INST_MUL = 0x2;
   final int EVM_INST_MULMOD = 0x9;
   final int EVM_INST_NOT = 0x19;
   final int EVM_INST_NUMBER = 0x43;
   final int EVM_INST_OR = 0x17;
   final int EVM_INST_ORIGIN = 0x32;
   final int EVM_INST_PC = 0x58;
   final int EVM_INST_POP = 0x50;
   final int EVM_INST_PREVRANDAO = 0x44;
   final int EVM_INST_PUSH0 = 0x5f;
   final int EVM_INST_PUSH1 = 0x60;
   final int EVM_INST_PUSH10 = 0x69;
   final int EVM_INST_PUSH11 = 0x6a;
   final int EVM_INST_PUSH12 = 0x6b;
   final int EVM_INST_PUSH13 = 0x6c;
   final int EVM_INST_PUSH14 = 0x6d;
   final int EVM_INST_PUSH15 = 0x6e;
   final int EVM_INST_PUSH16 = 0x6f;
   final int EVM_INST_PUSH17 = 0x70;
   final int EVM_INST_PUSH18 = 0x71;
   final int EVM_INST_PUSH19 = 0x72;
   final int EVM_INST_PUSH2 = 0x61;
   final int EVM_INST_PUSH20 = 0x73;
   final int EVM_INST_PUSH21 = 0x74;
   final int EVM_INST_PUSH22 = 0x75;
   final int EVM_INST_PUSH23 = 0x76;
   final int EVM_INST_PUSH24 = 0x77;
   final int EVM_INST_PUSH25 = 0x78;
   final int EVM_INST_PUSH26 = 0x79;
   final int EVM_INST_PUSH27 = 0x7a;
   final int EVM_INST_PUSH28 = 0x7b;
   final int EVM_INST_PUSH29 = 0x7c;
   final int EVM_INST_PUSH3 = 0x62;
   final int EVM_INST_PUSH30 = 0x7d;
   final int EVM_INST_PUSH31 = 0x7e;
   final int EVM_INST_PUSH32 = 0x7f;
   final int EVM_INST_PUSH4 = 0x63;
   final int EVM_INST_PUSH5 = 0x64;
   final int EVM_INST_PUSH6 = 0x65;
   final int EVM_INST_PUSH7 = 0x66;
   final int EVM_INST_PUSH8 = 0x67;
   final int EVM_INST_PUSH9 = 0x68;
   final int EVM_INST_RETURN = 0xf3;
   final int EVM_INST_RETURNDATACOPY = 0x3e;
   final int EVM_INST_RETURNDATASIZE = 0x3d;
   final int EVM_INST_REVERT = 0xfd;
   final int EVM_INST_SAR = 0x1d;
   final int EVM_INST_SDIV = 0x5;
   final int EVM_INST_SELFBALANCE = 0x47;
   final int EVM_INST_SELFDESTRUCT = 0xff;
   final int EVM_INST_SGT = 0x13;
   final int EVM_INST_SHA3 = 0x20;
   final int EVM_INST_SHL = 0x1b;
   final int EVM_INST_SHR = 0x1c;
   final int EVM_INST_SIGNEXTEND = 0xb;
   final int EVM_INST_SLOAD = 0x54;
   final int EVM_INST_SLT = 0x12;
   final int EVM_INST_SMOD = 0x7;
   final int EVM_INST_SSTORE = 0x55;
   final int EVM_INST_STATICCALL = 0xfa;
   final int EVM_INST_STOP = 0x0;
   final int EVM_INST_SUB = 0x3;
   final int EVM_INST_SWAP1 = 0x90;
   final int EVM_INST_SWAP10 = 0x99;
   final int EVM_INST_SWAP11 = 0x9a;
   final int EVM_INST_SWAP12 = 0x9b;
   final int EVM_INST_SWAP13 = 0x9c;
   final int EVM_INST_SWAP14 = 0x9d;
   final int EVM_INST_SWAP15 = 0x9e;
   final int EVM_INST_SWAP16 = 0x9f;
   final int EVM_INST_SWAP2 = 0x91;
   final int EVM_INST_SWAP3 = 0x92;
   final int EVM_INST_SWAP4 = 0x93;
   final int EVM_INST_SWAP5 = 0x94;
   final int EVM_INST_SWAP6 = 0x95;
   final int EVM_INST_SWAP7 = 0x96;
   final int EVM_INST_SWAP8 = 0x97;
   final int EVM_INST_SWAP9 = 0x98;
   final int EVM_INST_TIMESTAMP = 0x42;
   final int EVM_INST_TLOAD = 0x5c;
   final int EVM_INST_TSTORE = 0x5d;
   final int EVM_INST_XOR = 0x18;
   final int EVM_LONDON = 0xe;
   final int EVM_PARIS = 0xf;
   final int EVM_PRAGUE = 0x12;
   final int EVM_SHANGHAI = 0x10;
   final int EXO_SUM_INDEX_BLAKEMODEXP = 0x6;
   final int EXO_SUM_INDEX_ECDATA = 0x4;
   final int EXO_SUM_INDEX_KEC = 0x1;
   final int EXO_SUM_INDEX_LOG = 0x2;
   final int EXO_SUM_INDEX_RIPSHA = 0x5;
   final int EXO_SUM_INDEX_ROM = 0x0;
   final int EXO_SUM_INDEX_TXCD = 0x3;
   final int EXO_SUM_WEIGHT_BLAKEMODEXP = 0x40;
   final int EXO_SUM_WEIGHT_ECDATA = 0x10;
   final int EXO_SUM_WEIGHT_KEC = 0x2;
   final int EXO_SUM_WEIGHT_LOG = 0x4;
   final int EXO_SUM_WEIGHT_RIPSHA = 0x20;
   final int EXO_SUM_WEIGHT_ROM = 0x1;
   final int EXO_SUM_WEIGHT_TXCD = 0x8;
   final int EXP_INST_EXPLOG = 0xee0a;
   final int EXP_INST_MODEXPLOG = 0xee05;
   final int GAS_CONST_BLAKE2_PER_ROUND = 0x1;
   final int GAS_CONST_ECADD = 0x96;
   final int GAS_CONST_ECMUL = 0x1770;
   final int GAS_CONST_ECPAIRING = 0xafc8;
   final int GAS_CONST_ECPAIRING_PAIR = 0x84d0;
   final int GAS_CONST_ECRECOVER = 0xbb8;
   final int GAS_CONST_G_ACCESS_LIST_ADRESS = 0x960;
   final int GAS_CONST_G_ACCESS_LIST_STORAGE = 0x76c;
   final int GAS_CONST_G_BASE = 0x2;
   final int GAS_CONST_G_BLOCKHASH = 0x14;
   final int GAS_CONST_G_CALL_STIPEND = 0x8fc;
   final int GAS_CONST_G_CALL_VALUE = 0x2328;
   final int GAS_CONST_G_CODE_DEPOSIT = 0xc8;
   final int GAS_CONST_G_COLD_ACCOUNT_ACCESS = 0xa28;
   final int GAS_CONST_G_COLD_SLOAD = 0x834;
   final int GAS_CONST_G_COPY = 0x3;
   final int GAS_CONST_G_CREATE = 0x7d00;
   final int GAS_CONST_G_EXP = 0xa;
   final int GAS_CONST_G_EXP_BYTE = 0x32;
   final int GAS_CONST_G_HIGH = 0xa;
   final int GAS_CONST_G_JUMPDEST = 0x1;
   final int GAS_CONST_G_KECCAK_256 = 0x1e;
   final int GAS_CONST_G_KECCAK_256_WORD = 0x6;
   final int GAS_CONST_G_LOG = 0x177;
   final int GAS_CONST_G_LOG_DATA = 0x8;
   final int GAS_CONST_G_LOG_TOPIC = 0x177;
   final int GAS_CONST_G_LOW = 0x5;
   final int GAS_CONST_G_MEMORY = 0x3;
   final int GAS_CONST_G_MID = 0x8;
   final int GAS_CONST_G_NEW_ACCOUNT = 0x61a8;
   final int GAS_CONST_G_SELFDESTRUCT = 0x1388;
   final int GAS_CONST_G_SRESET = 0xb54;
   final int GAS_CONST_G_SSET = 0x4e20;
   final int GAS_CONST_G_TRANSACTION = 0x5208;
   final int GAS_CONST_G_TX_CREATE = 0x7d00;
   final int GAS_CONST_G_TX_DATA_NONZERO = 0x10;
   final int GAS_CONST_G_TX_DATA_ZERO = 0x4;
   final int GAS_CONST_G_VERY_LOW = 0x3;
   final int GAS_CONST_G_WARM_ACCESS = 0x64;
   final int GAS_CONST_G_ZERO = 0x0;
   final int GAS_CONST_HASH_OPCODE_GAS = 0x3;
   final int GAS_CONST_IDENTITY = 0xf;
   final int GAS_CONST_IDENTITY_WORD = 0x3;
   final int GAS_CONST_INIT_CODE_WORD = 0x2;
   final int GAS_CONST_MODEXP = 0xc8;
   final int GAS_CONST_RIPEMD = 0x258;
   final int GAS_CONST_RIPEMD_WORD = 0x78;
   final int GAS_CONST_SHA2 = 0x3c;
   final int GAS_CONST_SHA2_WORD = 0xc;
   final int GAS_LIMIT_ADJUSTMENT_FACTOR = 0x400;
   final int LINEA_BASE_FEE = 0x7;
   final int LINEA_BLOB_BASE_FEE = 0x1;
   final int LINEA_BLOB_PER_TRANSACTION_MAXIMUM = 0x0;
   final int LINEA_BLOCK_GAS_LIMIT = 0x77359400;
   final int LINEA_CHAIN_ID = 0xe708;
   final int LINEA_DIFFICULTY = 0x2;
   final int LINEA_GAS_LIMIT_MAXIMUM = 0x77359400;
   final int LINEA_GAS_LIMIT_MINIMUM = 0x3a2c940;
   final int LINEA_GOERLI_CHAIN_ID = 0xe704;
   final int LINEA_MAX_NUMBER_OF_TRANSACTIONS_IN_BATCH = 0xc8;
   final int LINEA_SEPOLIA_CHAIN_ID = 0xe705;
   final int LLARGE = 0x10;
   final int LLARGEMO = 0xf;
   final int LLARGEPO = 0x11;
   final int MAX_CODE_SIZE = 0x6000;
   final int MAX_INIT_CODE_SIZE = 0xc000;
   final int MAX_PRC_ADDRESS = 0x9;
   final int MAX_REFUND_QUOTIENT = 0x5;
   final int MIN_BASE_FEE_PER_BLOB_GAS = 0x1;
   final int MISC_WEIGHT_EXP = 0x1;
   final int MISC_WEIGHT_MMU = 0x2;
   final int MISC_WEIGHT_MXP = 0x4;
   final int MISC_WEIGHT_OOB = 0x8;
   final int MISC_WEIGHT_STP = 0x10;
   final int MMEDIUM = 0x8;
   final int MMEDIUMMO = 0x7;
   final int MMIO_INST_LIMB_TO_RAM_ONE_TARGET = 0xfe12;
   final int MMIO_INST_LIMB_TO_RAM_TRANSPLANT = 0xfe11;
   final int MMIO_INST_LIMB_TO_RAM_TWO_TARGET = 0xfe13;
   final int MMIO_INST_LIMB_VANISHES = 0xfe01;
   final int MMIO_INST_RAM_EXCISION = 0xfe41;
   final int MMIO_INST_RAM_TO_LIMB_ONE_SOURCE = 0xfe22;
   final int MMIO_INST_RAM_TO_LIMB_TRANSPLANT = 0xfe21;
   final int MMIO_INST_RAM_TO_LIMB_TWO_SOURCE = 0xfe23;
   final int MMIO_INST_RAM_TO_RAM_PARTIAL = 0xfe32;
   final int MMIO_INST_RAM_TO_RAM_TRANSPLANT = 0xfe31;
   final int MMIO_INST_RAM_TO_RAM_TWO_SOURCE = 0xfe34;
   final int MMIO_INST_RAM_TO_RAM_TWO_TARGET = 0xfe33;
   final int MMIO_INST_RAM_VANISHES = 0xfe42;
   final int MMU_INST_ANY_TO_RAM_WITH_PADDING = 0xfe50;
   final int MMU_INST_BLAKE = 0xfe80;
   final int MMU_INST_EXO_TO_RAM_TRANSPLANTS = 0xfe30;
   final int MMU_INST_INVALID_CODE_PREFIX = 0xfe00;
   final int MMU_INST_MLOAD = 0xfe01;
   final int MMU_INST_MODEXP_DATA = 0xfe70;
   final int MMU_INST_MODEXP_ZERO = 0xfe60;
   final int MMU_INST_MSTORE = 0xfe02;
   final int MMU_INST_MSTORE8 = 0xfe03;
   final int MMU_INST_RAM_TO_EXO_WITH_PADDING = 0xfe20;
   final int MMU_INST_RAM_TO_RAM_SANS_PADDING = 0xfe40;
   final int MMU_INST_RIGHT_PADDED_WORD_EXTRACTION = 0xfe10;
   final int OOB_INST_BLAKE_CDS = 0xfa09;
   final int OOB_INST_BLAKE_PARAMS = 0xfb09;
   final int OOB_INST_CALL = 0xca;
   final int OOB_INST_CDL = 0x35;
   final int OOB_INST_CREATE = 0xce;
   final int OOB_INST_DEPLOYMENT = 0xf3;
   final int OOB_INST_ECADD = 0xff06;
   final int OOB_INST_ECMUL = 0xff07;
   final int OOB_INST_ECPAIRING = 0xff08;
   final int OOB_INST_ECRECOVER = 0xff01;
   final int OOB_INST_IDENTITY = 0xff04;
   final int OOB_INST_JUMP = 0x56;
   final int OOB_INST_JUMPI = 0x57;
   final int OOB_INST_MODEXP_CDS = 0xfa05;
   final int OOB_INST_MODEXP_EXTRACT = 0xfe05;
   final int OOB_INST_MODEXP_LEAD = 0xfc05;
   final int OOB_INST_MODEXP_PRICING = 0xfd05;
   final int OOB_INST_MODEXP_XBS = 0xfb05;
   final int OOB_INST_RDC = 0x3e;
   final int OOB_INST_RIPEMD = 0xff03;
   final int OOB_INST_SHA2 = 0xff02;
   final int OOB_INST_SSTORE = 0x55;
   final int OOB_INST_XCALL = 0xcc;
   final int OOB_INST_XCREATE = 0xcd;
   final int PHASE_BLAKE_DATA = 0x5;
   final int PHASE_BLAKE_PARAMS = 0x6;
   final int PHASE_BLAKE_RESULT = 0x7;
   final int PHASE_ECADD_DATA = 0x60a;
   final int PHASE_ECADD_RESULT = 0x60b;
   final int PHASE_ECMUL_DATA = 0x70a;
   final int PHASE_ECMUL_RESULT = 0x70b;
   final int PHASE_ECPAIRING_DATA = 0x80a;
   final int PHASE_ECPAIRING_RESULT = 0x80b;
   final int PHASE_ECRECOVER_DATA = 0x10a;
   final int PHASE_ECRECOVER_RESULT = 0x10b;
   final int PHASE_KECCAK_DATA = 0x5;
   final int PHASE_KECCAK_RESULT = 0x6;
   final int PHASE_MODEXP_BASE = 0x1;
   final int PHASE_MODEXP_EXPONENT = 0x2;
   final int PHASE_MODEXP_MODULUS = 0x3;
   final int PHASE_MODEXP_RESULT = 0x4;
   final int PHASE_RIPEMD_DATA = 0x3;
   final int PHASE_RIPEMD_RESULT = 0x4;
   final int PHASE_SHA2_DATA = 0x1;
   final int PHASE_SHA2_RESULT = 0x2;
   final int PRC_BLAKE2F_SIZE = 0xd5;
   final int PRC_ECPAIRING_SIZE = 0xc0;
   final int PROTECTED_BASE_V = 0x23;
   final int PROTECTED_BASE_V_PO = 0x24;
   final int REFUND_CONST_R_SCLEAR = 0x12c0;
   final int RLP_ADDR_RECIPE_1 = 0x1;
   final int RLP_ADDR_RECIPE_2 = 0x2;
   final int RLP_PREFIX_INT_LONG = 0xb7;
   final int RLP_PREFIX_INT_SHORT = 0x80;
   final int RLP_PREFIX_LIST_LONG = 0xf7;
   final int RLP_PREFIX_LIST_SHORT = 0xc0;
   final int RLP_RCPT_SUBPHASE_ID_ADDR = 0x35;
   final int RLP_RCPT_SUBPHASE_ID_CUMUL_GAS = 0x3;
   final int RLP_RCPT_SUBPHASE_ID_DATA_LIMB = 0x4d;
   final int RLP_RCPT_SUBPHASE_ID_DATA_SIZE = 0x53;
   final int RLP_RCPT_SUBPHASE_ID_NO_LOG_ENTRY = 0xb;
   final int RLP_RCPT_SUBPHASE_ID_STATUS_CODE = 0x2;
   final int RLP_RCPT_SUBPHASE_ID_TOPIC_BASE = 0x41;
   final int RLP_RCPT_SUBPHASE_ID_TOPIC_DELTA = 0x60;
   final int RLP_RCPT_SUBPHASE_ID_TYPE = 0x7;
   final int RLP_TXN_PHASE_ACCESS_LIST = 0xb;
   final int RLP_TXN_PHASE_BETA = 0xc;
   final int RLP_TXN_PHASE_CHAIN_ID = 0x2;
   final int RLP_TXN_PHASE_DATA = 0xa;
   final int RLP_TXN_PHASE_GAS_LIMIT = 0x7;
   final int RLP_TXN_PHASE_GAS_PRICE = 0x4;
   final int RLP_TXN_PHASE_MAX_FEE_PER_GAS = 0x6;
   final int RLP_TXN_PHASE_MAX_PRIORITY_FEE_PER_GAS = 0x5;
   final int RLP_TXN_PHASE_NONCE = 0x3;
   final int RLP_TXN_PHASE_R = 0xe;
   final int RLP_TXN_PHASE_RLP_PREFIX = 0x1;
   final int RLP_TXN_PHASE_S = 0xf;
   final int RLP_TXN_PHASE_TO = 0x8;
   final int RLP_TXN_PHASE_VALUE = 0x9;
   final int RLP_TXN_PHASE_Y = 0xd;
   final int RLP_UTILS_INST_BYTES32 = 0x4;
   final int RLP_UTILS_INST_BYTE_STRING_PREFIX = 0x2;
   final int RLP_UTILS_INST_DATA_PRICING = 0x8;
   final int RLP_UTILS_INST_INTEGER = 0x1;
   final long TWOFIFTYSIX_TO_THE_FIVE = 0x10000000000L;
   final long TWOFIFTYSIX_TO_THE_FOUR = 0x100000000L;
   final long TWOFIFTYSIX_TO_THE_FOUR_MO = 0xffffffffL;
   final BigInteger TWOFIFTYSIX_TO_THE_TWELVE = new BigInteger("79228162514264337593543950336");
   final BigInteger TWOFIFTYSIX_TO_THE_TWELVE_MO = new BigInteger("79228162514264337593543950335");
   final BigInteger TWOFIFTYSIX_TO_THE_TWENTY = new BigInteger("1461501637330902918203684832716283019655932542976");
   final BigInteger TWOFIFTYSIX_TO_THE_TWENTY_MO = new BigInteger("1461501637330902918203684832716283019655932542975");
   final int UNPROTECTED_V = 0x1b;
   final int UNPROTECTED_V_PO = 0x1c;
   final int WCP_INST_GEQ = 0xe;
   final int WCP_INST_LEQ = 0xf;
   final int WORD_SIZE = 0x20;
   final int WORD_SIZE_MO = 0x1f;
   int spillage();
   // Submodules
   default Add add() { throw new IllegalArgumentException(); }
   default Bin bin() { throw new IllegalArgumentException(); }
   default Binreftable binreftable() { throw new IllegalArgumentException(); }
   default Blake2fmodexpdata blake2fmodexpdata() { throw new IllegalArgumentException(); }
   default Blockdata blockdata() { throw new IllegalArgumentException(); }
   default Blockhash blockhash() { throw new IllegalArgumentException(); }
   default Ecdata ecdata() { throw new IllegalArgumentException(); }
   default Euc euc() { throw new IllegalArgumentException(); }
   default Exp exp() { throw new IllegalArgumentException(); }
   default Ext ext() { throw new IllegalArgumentException(); }
   default Gas gas() { throw new IllegalArgumentException(); }
   default Hub hub() { throw new IllegalArgumentException(); }
   default Instdecoder instdecoder() { throw new IllegalArgumentException(); }
   default Logdata logdata() { throw new IllegalArgumentException(); }
   default Loginfo loginfo() { throw new IllegalArgumentException(); }
   default Mmio mmio() { throw new IllegalArgumentException(); }
   default Mmu mmu() { throw new IllegalArgumentException(); }
   default Mod mod() { throw new IllegalArgumentException(); }
   default Mul mul() { throw new IllegalArgumentException(); }
   default Mxp mxp() { throw new IllegalArgumentException(); }
   default Oob oob() { throw new IllegalArgumentException(); }
   default Power power() { throw new IllegalArgumentException(); }
   default Rlpaddr rlpaddr() { throw new IllegalArgumentException(); }
   default Rlptxn rlptxn() { throw new IllegalArgumentException(); }
   default Rlptxrcpt rlptxrcpt() { throw new IllegalArgumentException(); }
   default Rlputils rlputils() { throw new IllegalArgumentException(); }
   default Rom rom() { throw new IllegalArgumentException(); }
   default Romlex romlex() { throw new IllegalArgumentException(); }
   default Shakiradata shakiradata() { throw new IllegalArgumentException(); }
   default Shf shf() { throw new IllegalArgumentException(); }
   default Shfreftable shfreftable() { throw new IllegalArgumentException(); }
   default Stp stp() { throw new IllegalArgumentException(); }
   default Trm trm() { throw new IllegalArgumentException(); }
   default Txndata txndata() { throw new IllegalArgumentException(); }
   default Wcp wcp() { throw new IllegalArgumentException(); }

   List<ColumnHeader> headers(int length);
   Trace validateRow();
   Trace fillAndValidateRow();
   public interface Add {
      final BigInteger THETA = new BigInteger("340282366920938463463374607431768211456");
      int spillage();
      List<ColumnHeader> headers(int length);
      default Add acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Add acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Add arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Add arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Add arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Add arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Add byte1(final long val) { throw new IllegalArgumentException(); }
      default Add byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Add byte2(final long val) { throw new IllegalArgumentException(); }
      default Add byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Add ct(final long val) { throw new IllegalArgumentException(); }
      default Add ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Add ctMax(final long val) { throw new IllegalArgumentException(); }
      default Add ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Add inst(final long val) { throw new IllegalArgumentException(); }
      default Add inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Add overflow(final boolean val) { throw new IllegalArgumentException(); }
      default Add resHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Add resLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Add stamp(final long val) { throw new IllegalArgumentException(); }
      Add validateRow();
      Add fillAndValidateRow();
   }
   public interface Bin {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Bin acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin acc3(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin acc4(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin acc5(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin acc6(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin argument1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin argument1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin argument2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin argument2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin bits(final boolean val) { throw new IllegalArgumentException(); }
      default Bin bit1(final boolean val) { throw new IllegalArgumentException(); }
      default Bin bitB4(final boolean val) { throw new IllegalArgumentException(); }
      default Bin byte1(final long val) { throw new IllegalArgumentException(); }
      default Bin byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin byte2(final long val) { throw new IllegalArgumentException(); }
      default Bin byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin byte3(final long val) { throw new IllegalArgumentException(); }
      default Bin byte3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin byte4(final long val) { throw new IllegalArgumentException(); }
      default Bin byte4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin byte5(final long val) { throw new IllegalArgumentException(); }
      default Bin byte5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin byte6(final long val) { throw new IllegalArgumentException(); }
      default Bin byte6(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin counter(final long val) { throw new IllegalArgumentException(); }
      default Bin counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin ctMax(final long val) { throw new IllegalArgumentException(); }
      default Bin ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin inst(final long val) { throw new IllegalArgumentException(); }
      default Bin inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin isAnd(final boolean val) { throw new IllegalArgumentException(); }
      default Bin isByte(final boolean val) { throw new IllegalArgumentException(); }
      default Bin isNot(final boolean val) { throw new IllegalArgumentException(); }
      default Bin isOr(final boolean val) { throw new IllegalArgumentException(); }
      default Bin isSignextend(final boolean val) { throw new IllegalArgumentException(); }
      default Bin isXor(final boolean val) { throw new IllegalArgumentException(); }
      default Bin low4(final long val) { throw new IllegalArgumentException(); }
      default Bin low4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin neg(final boolean val) { throw new IllegalArgumentException(); }
      default Bin pivot(final long val) { throw new IllegalArgumentException(); }
      default Bin pivot(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin resultHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin resultLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Bin small(final boolean val) { throw new IllegalArgumentException(); }
      default Bin stamp(final long val) { throw new IllegalArgumentException(); }
      default Bin xxxByteHi(final long val) { throw new IllegalArgumentException(); }
      default Bin xxxByteHi(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Bin xxxByteLo(final long val) { throw new IllegalArgumentException(); }
      default Bin xxxByteLo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      Bin validateRow();
      Bin fillAndValidateRow();
   }
   public interface Binreftable {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Binreftable inputByte1(final long val) { throw new IllegalArgumentException(); }
      default Binreftable inputByte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Binreftable inputByte2(final long val) { throw new IllegalArgumentException(); }
      default Binreftable inputByte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Binreftable inst(final long val) { throw new IllegalArgumentException(); }
      default Binreftable inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Binreftable resultByte(final long val) { throw new IllegalArgumentException(); }
      default Binreftable resultByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      Binreftable validateRow();
      Binreftable fillAndValidateRow();
   }
   public interface Blake2fmodexpdata {
      final int INDEX_MAX_BLAKE_DATA = 0xc;
      final int INDEX_MAX_BLAKE_PARAMS = 0x1;
      final int INDEX_MAX_BLAKE_RESULT = 0x3;
      final int INDEX_MAX_MODEXP = 0x1f;
      final int INDEX_MAX_MODEXP_BASE = 0x1f;
      final int INDEX_MAX_MODEXP_EXPONENT = 0x1f;
      final int INDEX_MAX_MODEXP_MODULUS = 0x1f;
      final int INDEX_MAX_MODEXP_RESULT = 0x1f;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Blake2fmodexpdata id(final long val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata index(final long val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata index(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata indexMax(final long val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata indexMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isBlakeData(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isBlakeParams(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isBlakeResult(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isModexpBase(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isModexpExponent(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isModexpModulus(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata isModexpResult(final boolean val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata phase(final long val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata phase(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blake2fmodexpdata stamp(final long val) { throw new IllegalArgumentException(); }
      Blake2fmodexpdata validateRow();
      Blake2fmodexpdata fillAndValidateRow();
   }
   public interface Blockdata {
      final int GAS_LIMIT_ENABLE = 0x1;
      final BigInteger GAS_LIMIT_MAXIMUM = new BigInteger("2000000000");
      final BigInteger GAS_LIMIT_MINIMUM = new BigInteger("61000000");
      final int nROWS_BF = 0x1;
      final int nROWS_CB = 0x1;
      final int nROWS_GL = 0x5;
      final int nROWS_ID = 0x1;
      final int nROWS_NB = 0x2;
      final int nROWS_TS = 0x2;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Blockdata arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata basefee(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata blockGasLimit(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata coinbaseHi(final long val) { throw new IllegalArgumentException(); }
      default Blockdata coinbaseLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata ct(final long val) { throw new IllegalArgumentException(); }
      default Blockdata ctMax(final long val) { throw new IllegalArgumentException(); }
      default Blockdata dataHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata dataLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata eucFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata exoInst(final long val) { throw new IllegalArgumentException(); }
      default Blockdata exoInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blockdata firstBlockNumber(final long val) { throw new IllegalArgumentException(); }
      default Blockdata inst(final long val) { throw new IllegalArgumentException(); }
      default Blockdata inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blockdata iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isBasefee(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isBlobbasefee(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isChainid(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isCoinbase(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isDifficulty(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isGaslimit(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isNumber(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isPrevrandao(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata isTimestamp(final boolean val) { throw new IllegalArgumentException(); }
      default Blockdata relBlock(final long val) { throw new IllegalArgumentException(); }
      default Blockdata relTxNumMax(final long val) { throw new IllegalArgumentException(); }
      default Blockdata res(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockdata wcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      Blockdata validateRow();
      Blockdata fillAndValidateRow();
   }
   public interface Blockhash {
      final int BLOCKHASH_DEPTH = 0x6;
      final int ROFF___ABS___comparison_to_256 = 0x3;
      final int ROFF___BLOCKHASH_arguments___equality_test = 0x2;
      final int ROFF___BLOCKHASH_arguments___monotony = 0x1;
      final int ROFF___curr_BLOCKHASH_argument___comparison_to_max = 0x4;
      final int ROFF___curr_BLOCKHASH_argument___comparison_to_min = 0x5;
      final int nROWS_MACRO = 0x1;
      final int nROWS_PRPRC = 0x5;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Blockhash ct(final long val) { throw new IllegalArgumentException(); }
      default Blockhash ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blockhash ctMax(final long val) { throw new IllegalArgumentException(); }
      default Blockhash ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blockhash iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Blockhash macro(final boolean val) { throw new IllegalArgumentException(); }
      default Blockhash prprc(final boolean val) { throw new IllegalArgumentException(); }
      Blockhash validateRow();
      Blockhash fillAndValidateRow();
      default Blockhash pMacroAbsBlock(final long val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroBlockhashArgHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroBlockhashArgLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroBlockhashResHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroBlockhashResLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroBlockhashValHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroBlockhashValLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pMacroRelBlock(final long val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoArg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoInst(final long val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Blockhash pPreprocessingExoRes(final boolean val) { throw new IllegalArgumentException(); }
   }
   public interface Ecdata {
      final int ADDMOD = 0x8;
      final int CT_MAX_LARGE_POINT = 0x7;
      final int CT_MAX_SMALL_POINT = 0x3;
      final int ECADD = 0x6;
      final int ECMUL = 0x7;
      final int ECPAIRING = 0x8;
      final int ECRECOVER = 0x1;
      final int INDEX_MAX_ECADD_DATA = 0x7;
      final int INDEX_MAX_ECADD_RESULT = 0x3;
      final int INDEX_MAX_ECMUL_DATA = 0x5;
      final int INDEX_MAX_ECMUL_RESULT = 0x3;
      final int INDEX_MAX_ECPAIRING_DATA_MIN = 0xb;
      final int INDEX_MAX_ECPAIRING_RESULT = 0x1;
      final int INDEX_MAX_ECRECOVER_DATA = 0x7;
      final int INDEX_MAX_ECRECOVER_RESULT = 0x1;
      final int MULMOD = 0x9;
      final BigInteger P_BN_HI = new BigInteger("64323764613183177041862057485226039389");
      final BigInteger P_BN_LO = new BigInteger("201385395114098847380338600778089168199");
      final BigInteger SECP256K1N_HI = new BigInteger("340282366920938463463374607431768211455");
      final BigInteger SECP256K1N_LO = new BigInteger("340282366920938463463374607427473243183");
      final int TOTAL_SIZE_ECADD_DATA = 0x80;
      final int TOTAL_SIZE_ECADD_RESULT = 0x40;
      final int TOTAL_SIZE_ECMUL_DATA = 0x60;
      final int TOTAL_SIZE_ECMUL_RESULT = 0x40;
      final int TOTAL_SIZE_ECPAIRING_DATA_MIN = 0xc0;
      final int TOTAL_SIZE_ECPAIRING_RESULT = 0x20;
      final int TOTAL_SIZE_ECRECOVER_DATA = 0x80;
      final int TOTAL_SIZE_ECRECOVER_RESULT = 0x20;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Ecdata acceptablePairOfPointsForPairingCircuit(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata accPairings(final long val) { throw new IllegalArgumentException(); }
      default Ecdata byteDelta(final long val) { throw new IllegalArgumentException(); }
      default Ecdata byteDelta(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ecdata circuitSelectorEcadd(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata circuitSelectorEcmul(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata circuitSelectorEcpairing(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata circuitSelectorEcrecover(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata circuitSelectorG2Membership(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata ct(final long val) { throw new IllegalArgumentException(); }
      default Ecdata ctMax(final long val) { throw new IllegalArgumentException(); }
      default Ecdata extArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extArg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extArg3Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extArg3Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata extInst(final long val) { throw new IllegalArgumentException(); }
      default Ecdata extInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ecdata extResHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata extResLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata g2MembershipTestRequired(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata hurdle(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata id(final long val) { throw new IllegalArgumentException(); }
      default Ecdata index(final long val) { throw new IllegalArgumentException(); }
      default Ecdata indexMax(final long val) { throw new IllegalArgumentException(); }
      default Ecdata internalChecksPassed(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcaddData(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcaddResult(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcmulData(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcmulResult(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcpairingData(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcpairingResult(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcrecoverData(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isEcrecoverResult(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isInfinity(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isLargePoint(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata isSmallPoint(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata notOnG2(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata notOnG2Acc(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata notOnG2AccMax(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata overallTrivialPairing(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata phase(final long val) { throw new IllegalArgumentException(); }
      default Ecdata stamp(final long val) { throw new IllegalArgumentException(); }
      default Ecdata successBit(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata totalPairings(final long val) { throw new IllegalArgumentException(); }
      default Ecdata totalSize(final long val) { throw new IllegalArgumentException(); }
      default Ecdata wcpArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata wcpArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata wcpArg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata wcpArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ecdata wcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Ecdata wcpInst(final long val) { throw new IllegalArgumentException(); }
      default Ecdata wcpInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ecdata wcpRes(final boolean val) { throw new IllegalArgumentException(); }
      Ecdata validateRow();
      Ecdata fillAndValidateRow();
   }
   public interface Euc {
      final int MAX_INPUT_LENGTH = 0x8;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Euc ceil(final Bytes val) { throw new IllegalArgumentException(); }
      default Euc ct(final long val) { throw new IllegalArgumentException(); }
      default Euc ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Euc ctMax(final long val) { throw new IllegalArgumentException(); }
      default Euc ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Euc dividend(final Bytes val) { throw new IllegalArgumentException(); }
      default Euc divisor(final Bytes val) { throw new IllegalArgumentException(); }
      default Euc divisorByte(final long val) { throw new IllegalArgumentException(); }
      default Euc divisorByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Euc done(final boolean val) { throw new IllegalArgumentException(); }
      default Euc iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Euc quotient(final Bytes val) { throw new IllegalArgumentException(); }
      default Euc quotientByte(final long val) { throw new IllegalArgumentException(); }
      default Euc quotientByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Euc remainder(final Bytes val) { throw new IllegalArgumentException(); }
      default Euc remainderByte(final long val) { throw new IllegalArgumentException(); }
      default Euc remainderByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      Euc validateRow();
      Euc fillAndValidateRow();
   }
   public interface Exp {
      final int CT_MAX_CMPTN_EXP_LOG = 0xf;
      final int CT_MAX_CMPTN_MODEXP_LOG = 0xf;
      final int CT_MAX_MACRO_EXP_LOG = 0x0;
      final int CT_MAX_MACRO_MODEXP_LOG = 0x0;
      final int CT_MAX_PRPRC_EXP_LOG = 0x0;
      final int CT_MAX_PRPRC_MODEXP_LOG = 0x3;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Exp cmptn(final boolean val) { throw new IllegalArgumentException(); }
      default Exp ct(final long val) { throw new IllegalArgumentException(); }
      default Exp ctMax(final long val) { throw new IllegalArgumentException(); }
      default Exp isExpLog(final boolean val) { throw new IllegalArgumentException(); }
      default Exp isModexpLog(final boolean val) { throw new IllegalArgumentException(); }
      default Exp macro(final boolean val) { throw new IllegalArgumentException(); }
      default Exp prprc(final boolean val) { throw new IllegalArgumentException(); }
      default Exp stamp(final long val) { throw new IllegalArgumentException(); }
      Exp validateRow();
      Exp fillAndValidateRow();
      default Exp pComputationManzb(final boolean val) { throw new IllegalArgumentException(); }
      default Exp pComputationManzbAcc(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationMsb(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationMsb(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Exp pComputationMsbAcc(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationMsbAcc(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Exp pComputationMsbBit(final boolean val) { throw new IllegalArgumentException(); }
      default Exp pComputationPltBit(final boolean val) { throw new IllegalArgumentException(); }
      default Exp pComputationPltJmp(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationRawAcc(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pComputationRawByte(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationRawByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Exp pComputationTanzb(final boolean val) { throw new IllegalArgumentException(); }
      default Exp pComputationTanzbAcc(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationTrimAcc(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pComputationTrimByte(final long val) { throw new IllegalArgumentException(); }
      default Exp pComputationTrimByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Exp pMacroData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pMacroData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pMacroData3(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pMacroData4(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pMacroData5(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pMacroExpInst(final long val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpArg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpInst(final long val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Exp pPreprocessingWcpRes(final boolean val) { throw new IllegalArgumentException(); }
   }
   public interface Ext {
      final BigInteger THETA = new BigInteger("18446744073709551616");
      final BigInteger THETA2 = new BigInteger("340282366920938463463374607431768211456");
      int spillage();
      List<ColumnHeader> headers(int length);
      default Ext accA0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accA1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accA2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accA3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accB0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accB1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accB2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accB3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accC0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accC1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accC2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accC3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accDelta0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accDelta1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accDelta2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accDelta3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accH0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accH1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accH2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accH3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accH4(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accH5(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI4(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI5(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accI6(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ4(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ5(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ6(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accJ7(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ4(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ5(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ6(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accQ7(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accR0(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accR1(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accR2(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext accR3(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext arg3Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext arg3Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext bit1(final boolean val) { throw new IllegalArgumentException(); }
      default Ext bit2(final boolean val) { throw new IllegalArgumentException(); }
      default Ext bit3(final boolean val) { throw new IllegalArgumentException(); }
      default Ext byteA0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteA0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteA1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteA1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteA2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteA2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteA3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteA3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteB0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteB0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteB1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteB1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteB2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteB2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteB3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteB3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteC0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteC0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteC1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteC1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteC2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteC2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteC3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteC3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteDelta0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteDelta0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteDelta1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteDelta1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteDelta2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteDelta2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteDelta3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteDelta3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteH0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteH0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteH1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteH1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteH2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteH2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteH3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteH3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteH4(final long val) { throw new IllegalArgumentException(); }
      default Ext byteH4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteH5(final long val) { throw new IllegalArgumentException(); }
      default Ext byteH5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI4(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI5(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteI6(final long val) { throw new IllegalArgumentException(); }
      default Ext byteI6(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ4(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ5(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ6(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ6(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteJ7(final long val) { throw new IllegalArgumentException(); }
      default Ext byteJ7(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ4(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ5(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ6(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ6(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteQ7(final long val) { throw new IllegalArgumentException(); }
      default Ext byteQ7(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteR0(final long val) { throw new IllegalArgumentException(); }
      default Ext byteR0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteR1(final long val) { throw new IllegalArgumentException(); }
      default Ext byteR1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteR2(final long val) { throw new IllegalArgumentException(); }
      default Ext byteR2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext byteR3(final long val) { throw new IllegalArgumentException(); }
      default Ext byteR3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext cmp(final boolean val) { throw new IllegalArgumentException(); }
      default Ext ct(final long val) { throw new IllegalArgumentException(); }
      default Ext inst(final long val) { throw new IllegalArgumentException(); }
      default Ext inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Ext ofH(final boolean val) { throw new IllegalArgumentException(); }
      default Ext ofI(final boolean val) { throw new IllegalArgumentException(); }
      default Ext ofJ(final boolean val) { throw new IllegalArgumentException(); }
      default Ext ofRes(final boolean val) { throw new IllegalArgumentException(); }
      default Ext oli(final boolean val) { throw new IllegalArgumentException(); }
      default Ext resHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext resLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Ext stamp(final long val) { throw new IllegalArgumentException(); }
      Ext validateRow();
      Ext fillAndValidateRow();
   }
   public interface Gas {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Gas ct(final long val) { throw new IllegalArgumentException(); }
      default Gas ctMax(final long val) { throw new IllegalArgumentException(); }
      default Gas exceptionsAhoy(final boolean val) { throw new IllegalArgumentException(); }
      default Gas first(final boolean val) { throw new IllegalArgumentException(); }
      default Gas gasActual(final Bytes val) { throw new IllegalArgumentException(); }
      default Gas gasCost(final Bytes val) { throw new IllegalArgumentException(); }
      default Gas inputsAndOutputsAreMeaningful(final boolean val) { throw new IllegalArgumentException(); }
      default Gas outOfGasException(final boolean val) { throw new IllegalArgumentException(); }
      default Gas wcpArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Gas wcpArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Gas wcpInst(final long val) { throw new IllegalArgumentException(); }
      default Gas wcpInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Gas wcpRes(final boolean val) { throw new IllegalArgumentException(); }
      Gas validateRow();
      Gas fillAndValidateRow();
   }
   public interface Hub {
      final int CALL_1st_callee_account_row___row_offset = 0x4;
      final int CALL_1st_caller_account_row___row_offset = 0x3;
      final int CALL_1st_context_row___row_offset = 0x1;
      final int CALL_1st_scenario_row___row_offset = 0x0;
      final int CALL_2nd_callee_account_row___abort_will_revert___row_offset = 0x5;
      final int CALL_2nd_callee_account_row___row_offset = 0x6;
      final int CALL_2nd_caller_account_row___row_offset = 0x5;
      final int CALL_2nd_scenario_row_PRC_failure___row_offset = 0x5;
      final int CALL_2nd_scenario_row_PRC_success_will_revert_2nd_scenario___row_offset = 0x7;
      final int CALL_2nd_scenario_row_PRC_success_wont_revert_2nd_scenario___row_offset = 0x5;
      final int CALL_3rd_callee_account_row___row_offset = 0x7;
      final int CALL_ABORT_WILL_REVERT___current_context_update___row_offset = 0x6;
      final int CALL_ABORT_WONT_REVERT___current_context_update___row_offset = 0x5;
      final int CALL_EOA_will_revert_caller_context_row___row_offset = 0x7;
      final int CALL_EOA_wont_revert_caller_context_row___row_offset = 0x5;
      final int CALL_SMC_failure_will_revert_initialize_callee_context_row___row_offset = 0x8;
      final int CALL_SMC_failure_wont_revert_initialize_callee_context_row___row_offset = 0x7;
      final int CALL_SMC_success_will_revert_initialize_callee_context_row___row_offset = 0x7;
      final int CALL_SMC_success_wont_revert_initialize_callee_context_row___row_offset = 0x5;
      final int CALL___first_half_nsr___prc_failure = 0x5;
      final int CALL___first_half_nsr___prc_success_will_revert = 0x7;
      final int CALL___first_half_nsr___prc_success_wont_revert = 0x5;
      final int CALL_misc_row___row_offset = 0x2;
      final int CALL_mxpx_update_parent_context_row___row_offset = 0x3;
      final int CALL_nsr___abort_will_revert = 0x6;
      final int CALL_nsr___abort_wont_revert = 0x5;
      final int CALL_nsr___eoa_success_will_revert = 0x7;
      final int CALL_nsr___eoa_success_wont_revert = 0x5;
      final int CALL_nsr___mxpx = 0x3;
      final int CALL_nsr___oogx = 0x5;
      final int CALL_nsr___smc_failure_will_revert = 0x8;
      final int CALL_nsr___smc_failure_wont_revert = 0x7;
      final int CALL_nsr___smc_success_will_revert = 0x7;
      final int CALL_nsr___smc_success_wont_revert = 0x5;
      final int CALL_nsr___staticx = 0x3;
      final int CALL_oogx_update_parent_context_row___row_offset = 0x5;
      final int CALL_staticx_update_parent_context_row___row_offset = 0x3;
      final int CREATE_abort_current_account_row___row_offset = 0x3;
      final int CREATE_abort_current_context_row___row_offset = 0x4;
      final int CREATE_current_context_row___row_offset = 0x1;
      final int CREATE_empty_init_code_will_revert_current_context_row___row_offset = 0x7;
      final int CREATE_empty_init_code_wont_revert_current_context_row___row_offset = 0x5;
      final int CREATE_exception_caller_context_row___row_offset = 0x3;
      final int CREATE_fcond_will_revert_current_context_row___row_offset = 0x7;
      final int CREATE_fcond_wont_revert_current_context_row___row_offset = 0x5;
      final int CREATE_first_createe_account_row___row_offset = 0x4;
      final int CREATE_first_creator_account_row___row_offset = 0x3;
      final int CREATE_miscellaneous_row___row_offset = 0x2;
      final int CREATE_nonempty_init_code_failure_will_revert_new_context_row___row_offset = 0x9;
      final int CREATE_nonempty_init_code_failure_wont_revert_new_context_row___row_offset = 0x7;
      final int CREATE_nonempty_init_code_success_will_revert_new_context_row___row_offset = 0x7;
      final int CREATE_nonempty_init_code_success_wont_revert_new_context_row___row_offset = 0x5;
      final int CREATE_second_createe_account_row___row_offset = 0x6;
      final int CREATE_second_creator_account_row___row_offset = 0x5;
      final int CREATE_third_createe_account_row___row_offset = 0x8;
      final int CREATE_third_creator_account_row___row_offset = 0x7;
      final int DOM_SUB_STAMP_OFFSET___FINALIZATION = 0x9;
      final int DOM_SUB_STAMP_OFFSET___REVERT = 0x8;
      final int DOM_SUB_STAMP_OFFSET___SELFDESTRUCT = 0xa;
      final int ECADD_RETURN_DATA_SIZE = 0x40;
      final int ECMUL_RETURN_DATA_SIZE = 0x40;
      final int ECPAIRING_RETURN_DATA_SIZE = 0x20;
      final int MULTIPLIER___DOM_SUB_STAMPS = 0x10;
      final int MULTIPLIER___STACK_STAMP = 0x8;
      final int ROFF_ACC___ACCOUNT_DOING_ROW = 0x1;
      final int ROFF_ACC___ACCOUNT_READING_ROW = 0x2;
      final int ROFF_ACC___ACCOUNT_UNDOING_ROW = 0x2;
      final int ROFF_ACC___CONTEXT_ROW = 0x1;
      final int ROFF_CALLDATACOPY_CONTEXT_ROW = 0x2;
      final int ROFF_CODECOPY_NO_XAHOY_ACCOUNT_ROW = 0x3;
      final int ROFF_CODECOPY_NO_XAHOY_CONTEXT_ROW = 0x2;
      final int ROFF_CODECOPY_XAHOY_CONTEXT_ROW = 0x2;
      final int ROFF_CONTEXT_INSTRUCTION___CONTEXT_ROW = 0x1;
      final int ROFF_COPY_INST_MISCELLANEOUS_ROW = 0x1;
      final int ROFF_EXTCODECOPY_MXPX_CONTEXT_ROW = 0x2;
      final int ROFF_EXTCODECOPY_NO_XAHOY_ACCOUNT_ROW = 0x2;
      final int ROFF_EXTCODECOPY_NO_XAHOY_NO_REVERT_ACCOUNT_ROW = 0x2;
      final int ROFF_EXTCODECOPY_NO_XAHOY_REVERT_ACCOUNT_DOING_ROW = 0x2;
      final int ROFF_EXTCODECOPY_NO_XAHOY_REVERT_ACCOUNT_UNDOING_ROW = 0x3;
      final int ROFF_EXTCODECOPY_OOGX_ACCOUNT_ROW = 0x2;
      final int ROFF_EXTCODECOPY_OOGX_CONTEXT_ROW = 0x3;
      final int ROFF_LOG___CURRENT_CONTEXT_ROW = 0x2;
      final int ROFF_LOG___MISCELLANEOUS_ROW = 0x3;
      final int ROFF_LOG___OTHERX_XCONTEXT_ROW = 0x4;
      final int ROFF_LOG___STATICX_XCONTEXT_ROW = 0x3;
      final int ROFF_MACHINESTATE___MSIZE___MISC_ROW = 0x1;
      final int ROFF_MACHINESTATE___MSIZE___XCONTEXT_ROW = 0x2;
      final int ROFF_MACHINESTATE___NOT_MSIZE___XCONTEXT_ROW = 0x1;
      final int ROFF_RETURNDATACOPY_CALLER_CONTEXT_ROW = 0x3;
      final int ROFF_RETURNDATACOPY_CURRENT_CONTEXT_ROW = 0x2;
      final int ROFF_RETURN___1ST_MISC_ROW = 0x2;
      final int ROFF_RETURN___2ND_MISC_ROW___DEPLOY_AND_HASH = 0x3;
      final int ROFF_RETURN___CALLER_CONTEXT___EMPTY_DEPLOYMENT_WILL_REVERT = 0x5;
      final int ROFF_RETURN___CALLER_CONTEXT___EMPTY_DEPLOYMENT_WONT_REVERT = 0x4;
      final int ROFF_RETURN___CALLER_CONTEXT___EXCEPTION = 0x3;
      final int ROFF_RETURN___CALLER_CONTEXT___MESSAGE_CALL = 0x3;
      final int ROFF_RETURN___CALLER_CONTEXT___NONEMPTY_DEPLOYMENT_WILL_REVERT = 0x6;
      final int ROFF_RETURN___CALLER_CONTEXT___NONEMPTY_DEPLOYMENT_WONT_REVERT = 0x5;
      final int ROFF_RETURN___CURRENT_CONTEXT_ROW = 0x1;
      final int ROFF_RETURN___EMPTY_DEPLOYMENT___1ST_ACCOUNT_ROW = 0x3;
      final int ROFF_RETURN___EMPTY_DEPLOYMENT___2ND_ACCOUNT_ROW = 0x4;
      final int ROFF_RETURN___NONEMPTY_DEPLOYMENT___1ST_ACCOUNT_ROW = 0x4;
      final int ROFF_RETURN___NONEMPTY_DEPLOYMENT___2ND_ACCOUNT_ROW = 0x5;
      final int ROFF_RETURN___SCENARIO_ROW = 0x0;
      final int ROFF_REVERT___MISC_ROW = 0x1;
      final int ROFF_REVERT___NO_XAHOY_CALLER_CONTEXT_ROW = 0x3;
      final int ROFF_REVERT___NO_XAHOY_CURRENT_CONTEXT_ROW = 0x2;
      final int ROFF_REVERT___XAHOY_CALLER_CONTEXT_ROW = 0x2;
      final int ROFF_SELFDESTRUCT___1ST_CONTEXT_ROW = 0x1;
      final int ROFF_SELFDESTRUCT___ACCOUNT_DELETION_ROW = 0x4;
      final int ROFF_SELFDESTRUCT___ACCOUNT___1ST_DOING_ROW = 0x2;
      final int ROFF_SELFDESTRUCT___ACCOUNT___1ST_UNDOING_ROW = 0x4;
      final int ROFF_SELFDESTRUCT___ACCOUNT___2ND_DOING_ROW = 0x3;
      final int ROFF_SELFDESTRUCT___ACCOUNT___2ND_UNDOING_ROW = 0x5;
      final int ROFF_SELFDESTRUCT___SCENARIO_ROW = 0x0;
      final int ROFF_STACK_RAM___CONTEXT_ROW = 0x2;
      final int ROFF_STACK_RAM___MISC_ROW = 0x1;
      final int ROFF___KEC___MISCELLANEOUS_ROW = 0x1;
      final int ROW_OFFSET_FOR_JUMP_NO_OOGX_ADDRESS_ROW = 0x2;
      final int ROW_OFFSET_FOR_JUMP_NO_OOGX_CURRENT_CONTEXT_ROW = 0x1;
      final int ROW_OFFSET_FOR_JUMP_NO_OOGX_JUMPX_CALLER_CONTEXT_ROW = 0x4;
      final int ROW_OFFSET_FOR_JUMP_NO_OOGX_MISC_ROW = 0x3;
      final int ROW_OFFSET_FOR_JUMP_OOGX_CONTEXT_ROW = 0x1;
      final int precompile_processing___BLAKE2f___context_row_offset___squashing_caller_return_data_for_FKTH = 0x2;
      final int precompile_processing___BLAKE2f___context_row_offset___squashing_caller_return_data_for_FKTR = 0x3;
      final int precompile_processing___BLAKE2f___context_row_offset___updating_caller_return_data = 0x5;
      final int precompile_processing___BLAKE2f___misc_row_offset___BLAKE_call_data_extraction = 0x2;
      final int precompile_processing___BLAKE2f___misc_row_offset___BLAKE_parameter_extraction = 0x1;
      final int precompile_processing___BLAKE2f___misc_row_offset___BLAKE_partial_return_data_copy = 0x4;
      final int precompile_processing___BLAKE2f___misc_row_offset___BLAKE_return_data_full_transfer = 0x3;
      final int precompile_processing___ECADD_MUL_PAIRING___context_row_offset___updating_caller_context = 0x4;
      final int precompile_processing___ECADD_MUL_PAIRING___misc_row_offset___full_return_data_transfer = 0x2;
      final int precompile_processing___ECADD_MUL_PAIRING___misc_row_offset___partial_return_data_copy = 0x3;
      final int precompile_processing___IDENTITY___2nd_misc_row___row_offset = 0x2;
      final int precompile_processing___IDENTITY___context_row_success___row_offset = 0x3;
      final int precompile_processing___MODEXP___context_row_offset___FKTR = 0x7;
      final int precompile_processing___MODEXP___context_row_offset___success = 0xc;
      final int precompile_processing___MODEXP___misc_row_offset___base_extraction = 0x7;
      final int precompile_processing___MODEXP___misc_row_offset___bbs_analysis = 0x2;
      final int precompile_processing___MODEXP___misc_row_offset___cds_analysis = 0x1;
      final int precompile_processing___MODEXP___misc_row_offset___ebs_analysis = 0x3;
      final int precompile_processing___MODEXP___misc_row_offset___exponent_extraction = 0x8;
      final int precompile_processing___MODEXP___misc_row_offset___full_result_extraction = 0xa;
      final int precompile_processing___MODEXP___misc_row_offset___leading_word_analysis = 0x5;
      final int precompile_processing___MODEXP___misc_row_offset___mbs_analysis = 0x4;
      final int precompile_processing___MODEXP___misc_row_offset___modulus_extraction = 0x9;
      final int precompile_processing___MODEXP___misc_row_offset___partial_result_transfer = 0xb;
      final int precompile_processing___MODEXP___misc_row_offset___pricing = 0x6;
      final int precompile_processing___MODEXP_context_row___FKTR___row_offset = 0x7;
      final int precompile_processing___MODEXP_context_row___success___row_offset = 0xc;
      final int precompile_processing___MODEXP_misc_row___cds___row_offset = 0x1;
      final int precompile_processing___MODEXP_misc_row___copy_inputs_base___row_offset = 0x7;
      final int precompile_processing___MODEXP_misc_row___copy_inputs_exponent___row_offset = 0x8;
      final int precompile_processing___MODEXP_misc_row___copy_inputs_modulus___row_offset = 0x9;
      final int precompile_processing___MODEXP_misc_row___extract_bbs___offset = 0x2;
      final int precompile_processing___MODEXP_misc_row___extract_ebs___row_offset = 0x3;
      final int precompile_processing___MODEXP_misc_row___extract_mbs___row_offset = 0x4;
      final int precompile_processing___MODEXP_misc_row___extract_raw_lead___row_offset = 0x5;
      final int precompile_processing___MODEXP_misc_row___full_result_transfer___row_offset = 0xa;
      final int precompile_processing___MODEXP_misc_row___partial_result_copy___row_offset = 0xb;
      final int precompile_processing___MODEXP_misc_row___pricing___row_offset = 0x6;
      final int precompile_processing___MODEXP_nsr_FKTR = 0x8;
      final int precompile_processing___MODEXP_nsr_success = 0xd;
      final int precompile_processing___common___1st_misc_row___row_offset = 0x1;
      final int precompile_processing___common___2nd_misc_row___row_offset = 0x2;
      final int precompile_processing___common___3rd_misc_row___row_offset = 0x3;
      final int precompile_processing___common___context_row_FKTH___row_offset = 0x2;
      final int precompile_processing___common___context_row_success___row_offset = 0x4;
      final int precompile_processing___nsr_BLAKE2f_FKTR = 0x4;
      final int precompile_processing___nsr_BLAKE2f_success = 0x6;
      final int precompile_processing___nsr_IDENTITY_FKTH = 0x3;
      final int precompile_processing___nsr_IDENTITY_success = 0x4;
      final int precompile_processing___nsr_standard_failure = 0x3;
      final int precompile_processing___nsr_standard_success = 0x5;
      final int roff___txn_instruction___exceptional_context_row = 0x2;
      final int roff___txn_instruction___transaction_row = 0x1;
      final int tx_finl___row_offset___ACC___coinbase_reward = 0x1;
      final int tx_finl___row_offset___ACC___sender_gas_refund = 0x0;
      final int tx_finl___row_offset___TXN = 0x2;
      final int tx_init___row_offset___MISC = 0x0;
      final int tx_init___row_offset___TXN = 0x1;
      final int tx_skip___row_offset___coinbase_account = 0x2;
      final int tx_skip___row_offset___recipient_account = 0x1;
      final int tx_skip___row_offset___sender_account = 0x0;
      final int tx_skip___row_offset___transaction_row = 0x3;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Hub absoluteTransactionNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub blkNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub callerContextNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub codeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Hub contextGetsReverted(final boolean val) { throw new IllegalArgumentException(); }
      default Hub contextMayChange(final boolean val) { throw new IllegalArgumentException(); }
      default Hub contextNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub contextNumberNew(final long val) { throw new IllegalArgumentException(); }
      default Hub contextRevertStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub contextSelfReverts(final boolean val) { throw new IllegalArgumentException(); }
      default Hub contextWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub counterNsr(final long val) { throw new IllegalArgumentException(); }
      default Hub counterNsr(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub counterTli(final boolean val) { throw new IllegalArgumentException(); }
      default Hub domStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub exceptionAhoy(final boolean val) { throw new IllegalArgumentException(); }
      default Hub gasActual(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub gasCost(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub gasExpected(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub gasNext(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub height(final long val) { throw new IllegalArgumentException(); }
      default Hub heightNew(final long val) { throw new IllegalArgumentException(); }
      default Hub hubStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub hubStampTransactionEnd(final long val) { throw new IllegalArgumentException(); }
      default Hub logInfoStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub mmuStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub mxpStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub nonStackRows(final long val) { throw new IllegalArgumentException(); }
      default Hub nonStackRows(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub peekAtAccount(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtContext(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtMiscellaneous(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtScenario(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtStack(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtStorage(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtTransaction(final boolean val) { throw new IllegalArgumentException(); }
      default Hub peekAtTransient(final boolean val) { throw new IllegalArgumentException(); }
      default Hub programCounter(final long val) { throw new IllegalArgumentException(); }
      default Hub programCounterNew(final long val) { throw new IllegalArgumentException(); }
      default Hub refundCounter(final long val) { throw new IllegalArgumentException(); }
      default Hub refundCounterNew(final long val) { throw new IllegalArgumentException(); }
      default Hub relativeBlockNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub subStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub sysf(final boolean val) { throw new IllegalArgumentException(); }
      default Hub sysfTxnNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub sysi(final boolean val) { throw new IllegalArgumentException(); }
      default Hub sysiTxnNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub totlTxnNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub twoLineInstruction(final boolean val) { throw new IllegalArgumentException(); }
      default Hub txExec(final boolean val) { throw new IllegalArgumentException(); }
      default Hub txFinl(final boolean val) { throw new IllegalArgumentException(); }
      default Hub txInit(final boolean val) { throw new IllegalArgumentException(); }
      default Hub txSkip(final boolean val) { throw new IllegalArgumentException(); }
      default Hub txWarm(final boolean val) { throw new IllegalArgumentException(); }
      default Hub user(final boolean val) { throw new IllegalArgumentException(); }
      default Hub userTxnNumber(final long val) { throw new IllegalArgumentException(); }
      Hub validateRow();
      Hub fillAndValidateRow();
      default Hub pAccountAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountBalance(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountBalanceNew(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeHashHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeHashHiNew(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeHashLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeHashLoNew(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountCodeSizeNew(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountDeploymentNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountDeploymentNumberNew(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountDeploymentStatus(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountDeploymentStatusNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountExists(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountExistsNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountHadCodeInitially(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountHasCode(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountHasCodeNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountIsPrecompile(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountMarkedForDeletion(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountMarkedForDeletionNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountMarkedForSelfdestruct(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountMarkedForSelfdestructNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountNonce(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountNonceNew(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrDepAddrHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrDepAddrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrKecHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrKecLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrRecipe(final long val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrRecipe(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrSaltHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountRlpaddrSaltLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountRomlexFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountTrmFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountTrmRawAddressHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pAccountWarmth(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pAccountWarmthNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pContextAccountAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextAccountAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pContextAccountDeploymentNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextByteCodeAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextByteCodeAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pContextByteCodeCodeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextByteCodeDeploymentNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextByteCodeDeploymentStatus(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextCallerAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextCallerAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pContextCallDataContextNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextCallDataOffset(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextCallDataSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextCallStackDepth(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextCallValue(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pContextContextNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextIsRoot(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pContextIsStatic(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pContextReturnAtCapacity(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextReturnAtOffset(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextReturnDataContextNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextReturnDataOffset(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextReturnDataSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pContextUpdate(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscCcrsStamp(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscCcsrFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpData3(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpData4(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpData5(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscExpInst(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuAuxId(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuExoSum(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuInst(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuLimb1(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuLimb2(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuPhase(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuRefOffset(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuRefSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuSrcId(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuSrcOffsetHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuSrcOffsetLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuSuccessBit(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuTgtId(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMmuTgtOffsetLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpDeploys(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpGasMxp(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpInst(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpMtntop(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpOffset1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpOffset1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpOffset2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpOffset2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpSize1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpSize1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpSize1NonzeroNoMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpSize2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpSize2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpSize2NonzeroNoMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscMxpWords(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData10(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData3(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData4(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData5(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData6(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData7(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData8(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobData9(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscOobInst(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpExists(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpGasHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpGasLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpGasMxp(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpGasPaidOutOfPocket(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpGasStipend(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpGasUpfrontGasCost(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpInstruction(final long val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpInstruction(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpOogx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpValueHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpValueLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pMiscStpWarmth(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallAbortWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallAbortWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallEoaSuccessCallerWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallEoaSuccessCallerWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallException(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallPrcFailure(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallPrcSuccessCallerWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallPrcSuccessCallerWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallSmcFailureCallerWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallSmcFailureCallerWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallSmcSuccessCallerWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCallSmcSuccessCallerWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateAbort(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateEmptyInitCodeWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateEmptyInitCodeWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateException(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateFailureConditionWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateFailureConditionWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateNonemptyInitCodeFailureWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateNonemptyInitCodeFailureWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateNonemptyInitCodeSuccessWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioCreateNonemptyInitCodeSuccessWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcBlake2f(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcCalleeGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcCallerGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcCdo(final long val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcCds(final long val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcEcadd(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcEcmul(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcEcpairing(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcEcrecover(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcFailureKnownToHub(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcFailureKnownToRam(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcIdentity(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcModexp(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcRac(final long val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcRao(final long val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcReturnGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcRipemd160(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcSha2256(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcSuccessCallerWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioPrcSuccessCallerWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnException(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnFromDeploymentEmptyCodeWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnFromDeploymentEmptyCodeWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnFromDeploymentNonemptyCodeWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnFromDeploymentNonemptyCodeWontRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnFromMessageCallWillTouchRam(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioReturnFromMessageCallWontTouchRam(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioSelfdestructException(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioSelfdestructWillRevert(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioSelfdestructWontRevertAlreadyMarked(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pScenarioSelfdestructWontRevertNotYetMarked(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackAccFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackAddFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackAlpha(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackAlpha(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub pStackBinFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackBtcFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackCallFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackConFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackCopyFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackCreateFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackDecFlag1(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackDecFlag2(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackDecFlag3(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackDecFlag4(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackDelta(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackDelta(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub pStackDupFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackExtFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackHaltFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackHashInfoFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackHashInfoKeccakHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackHashInfoKeccakLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackIcpx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackInstruction(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackInstruction(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Hub pStackInvalidFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackJumpx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackJumpDestinationVettingRequired(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackJumpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackKecFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackLogFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackLogInfoFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackMachineStateFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackMaxcsx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackMcopyFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackModFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackMulFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackMxpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackOogx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackOpcx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackPushpopFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackPushValueHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackPushValueLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackRdcx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackShfFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackSox(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackSstorex(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStackramFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemHeight1(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemHeight2(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemHeight3(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemHeight4(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemPop1(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemPop2(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemPop3(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemPop4(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemStamp1(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemStamp2(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemStamp3(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemStamp4(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueHi1(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueHi2(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueHi3(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueHi4(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueLo1(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueLo2(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueLo3(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStackItemValueLo4(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStackStaticx(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStaticFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackStaticGas(final long val) { throw new IllegalArgumentException(); }
      default Hub pStackStoFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackSux(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackSwapFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackTransFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackTxnFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStackWcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pStorageAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageDeploymentNumber(final long val) { throw new IllegalArgumentException(); }
      default Hub pStorageSloadOperation(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageSstoreOperation(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageStorageKeyHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageStorageKeyLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueCurrChanges(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueCurrHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueCurrIsOrig(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueCurrIsZero(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueCurrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueNextHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueNextIsCurr(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueNextIsOrig(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueNextIsZero(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueNextLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueOrigHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueOrigIsZero(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageValueOrigLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pStorageWarmth(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pStorageWarmthNew(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionBasefee(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionCallDataSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pTransactionCoinbaseAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pTransactionCoinbaseAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionCopyTxcd(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionEip2935(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionEip4788(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionFromAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pTransactionFromAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionGasInitiallyAvailable(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionGasLeftover(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionGasLimit(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionGasPrice(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionInitialBalance(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionInitCodeSize(final long val) { throw new IllegalArgumentException(); }
      default Hub pTransactionIsDeployment(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionIsType2(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionNonce(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionNoop(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionPriorityFeePerGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionRefundCounterInfinity(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionRefundEffective(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionRequiresEvmExecution(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionStatusCode(final boolean val) { throw new IllegalArgumentException(); }
      default Hub pTransactionSystTxnData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionSystTxnData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionSystTxnData3(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionSystTxnData4(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionToAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pTransactionToAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransactionValue(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientAddressHi(final long val) { throw new IllegalArgumentException(); }
      default Hub pTransientAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientStorageKeyHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientStorageKeyLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientValueCurrHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientValueCurrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientValueNextHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Hub pTransientValueNextLo(final Bytes val) { throw new IllegalArgumentException(); }
   }
   public interface Instdecoder {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Instdecoder alpha(final long val) { throw new IllegalArgumentException(); }
      default Instdecoder alpha(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Instdecoder billingPerByte(final long val) { throw new IllegalArgumentException(); }
      default Instdecoder billingPerByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Instdecoder billingPerWord(final long val) { throw new IllegalArgumentException(); }
      default Instdecoder billingPerWord(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Instdecoder delta(final long val) { throw new IllegalArgumentException(); }
      default Instdecoder delta(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Instdecoder familyAccount(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyAdd(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyBatch(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyBin(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyCall(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyContext(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyCopy(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyCreate(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyDup(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyExt(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyHalt(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyInvalid(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyJump(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyKec(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyLog(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyMachineState(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyMcopy(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyMod(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyMul(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyPushPop(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyShf(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyStackRam(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyStorage(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familySwap(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyTransaction(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyTransient(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder familyWcp(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder flag1(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder flag2(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder flag3(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder flag4(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isBytePricing(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isDoubleMaxOffset(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isFixedSize1(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isFixedSize32(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isJumpdest(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isMcopy(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isMsize(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isPush(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isReturn(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isSingleMaxOffset(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder isWordPricing(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder mxpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder mxpType1(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder mxpType2(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder mxpType3(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder mxpType4(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder mxpType5(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder opcode(final long val) { throw new IllegalArgumentException(); }
      default Instdecoder opcode(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Instdecoder staticFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Instdecoder staticGas(final long val) { throw new IllegalArgumentException(); }
      default Instdecoder twoLineInstruction(final boolean val) { throw new IllegalArgumentException(); }
      Instdecoder validateRow();
      Instdecoder fillAndValidateRow();
   }
   public interface Logdata {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Logdata absLogNum(final long val) { throw new IllegalArgumentException(); }
      default Logdata absLogNumMax(final long val) { throw new IllegalArgumentException(); }
      default Logdata index(final long val) { throw new IllegalArgumentException(); }
      default Logdata limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Logdata logsData(final boolean val) { throw new IllegalArgumentException(); }
      default Logdata sizeAcc(final long val) { throw new IllegalArgumentException(); }
      default Logdata sizeLimb(final long val) { throw new IllegalArgumentException(); }
      default Logdata sizeTotal(final long val) { throw new IllegalArgumentException(); }
      Logdata validateRow();
      Logdata fillAndValidateRow();
   }
   public interface Loginfo {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Loginfo absLogNum(final long val) { throw new IllegalArgumentException(); }
      default Loginfo absLogNumMax(final long val) { throw new IllegalArgumentException(); }
      default Loginfo absTxnNum(final long val) { throw new IllegalArgumentException(); }
      default Loginfo absTxnNumMax(final long val) { throw new IllegalArgumentException(); }
      default Loginfo addrHi(final long val) { throw new IllegalArgumentException(); }
      default Loginfo addrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo ct(final long val) { throw new IllegalArgumentException(); }
      default Loginfo ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Loginfo ctMax(final long val) { throw new IllegalArgumentException(); }
      default Loginfo ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Loginfo dataHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo dataLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo dataSize(final long val) { throw new IllegalArgumentException(); }
      default Loginfo inst(final long val) { throw new IllegalArgumentException(); }
      default Loginfo inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Loginfo isLogX0(final boolean val) { throw new IllegalArgumentException(); }
      default Loginfo isLogX1(final boolean val) { throw new IllegalArgumentException(); }
      default Loginfo isLogX2(final boolean val) { throw new IllegalArgumentException(); }
      default Loginfo isLogX3(final boolean val) { throw new IllegalArgumentException(); }
      default Loginfo isLogX4(final boolean val) { throw new IllegalArgumentException(); }
      default Loginfo phase(final long val) { throw new IllegalArgumentException(); }
      default Loginfo topicHi1(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicHi2(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicHi3(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicHi4(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicLo1(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicLo2(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicLo3(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo topicLo4(final Bytes val) { throw new IllegalArgumentException(); }
      default Loginfo txnEmitsLogs(final boolean val) { throw new IllegalArgumentException(); }
      Loginfo validateRow();
      Loginfo fillAndValidateRow();
   }
   public interface Mmio {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Mmio acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio acc3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio acc4(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio accA(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio accB(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio accC(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio accLimb(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio bit1(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio bit2(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio bit3(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio bit4(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio bit5(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio byteA(final long val) { throw new IllegalArgumentException(); }
      default Mmio byteA(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio byteB(final long val) { throw new IllegalArgumentException(); }
      default Mmio byteB(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio byteC(final long val) { throw new IllegalArgumentException(); }
      default Mmio byteC(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio byteLimb(final long val) { throw new IllegalArgumentException(); }
      default Mmio byteLimb(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio cnA(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio cnB(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio cnC(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio contextSource(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio contextTarget(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio counter(final long val) { throw new IllegalArgumentException(); }
      default Mmio counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio exoId(final long val) { throw new IllegalArgumentException(); }
      default Mmio exoIsBlakemodexp(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoIsEcdata(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoIsKec(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoIsLog(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoIsRipsha(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoIsRom(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoIsTxcd(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio exoSum(final long val) { throw new IllegalArgumentException(); }
      default Mmio fast(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio indexA(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio indexB(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio indexC(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio indexX(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio isLimbToRamOneTarget(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isLimbToRamTransplant(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isLimbToRamTwoTarget(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isLimbVanishes(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamExcision(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToLimbOneSource(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToLimbTransplant(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToLimbTwoSource(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToRamPartial(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToRamTransplant(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToRamTwoSource(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamToRamTwoTarget(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio isRamVanishes(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio kecId(final long val) { throw new IllegalArgumentException(); }
      default Mmio limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio mmioInstruction(final long val) { throw new IllegalArgumentException(); }
      default Mmio mmioStamp(final long val) { throw new IllegalArgumentException(); }
      default Mmio phase(final long val) { throw new IllegalArgumentException(); }
      default Mmio pow2561(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio pow2562(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio size(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio slow(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio sourceByteOffset(final long val) { throw new IllegalArgumentException(); }
      default Mmio sourceByteOffset(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio sourceLimbOffset(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio successBit(final boolean val) { throw new IllegalArgumentException(); }
      default Mmio targetByteOffset(final long val) { throw new IllegalArgumentException(); }
      default Mmio targetByteOffset(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmio targetLimbOffset(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio totalSize(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio valA(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio valANew(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio valB(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio valBNew(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio valC(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmio valCNew(final Bytes val) { throw new IllegalArgumentException(); }
      Mmio validateRow();
      Mmio fillAndValidateRow();
   }
   public interface Mmu {
      final int NB_MICRO_ROWS_TOT_BLAKE = 0x2;
      final int NB_MICRO_ROWS_TOT_INVALID_CODE_PREFIX = 0x1;
      final int NB_MICRO_ROWS_TOT_MLOAD = 0x2;
      final int NB_MICRO_ROWS_TOT_MODEXP_DATA = 0x20;
      final int NB_MICRO_ROWS_TOT_MODEXP_ZERO = 0x20;
      final int NB_MICRO_ROWS_TOT_MSTORE = 0x2;
      final int NB_MICRO_ROWS_TOT_MSTORE_EIGHT = 0x1;
      final int NB_MICRO_ROWS_TOT_RIGHT_PADDED_WORD_EXTRACTION = 0x2;
      final int NB_PP_ROWS_ANY_TO_RAM_WITH_PADDING_PURE_PADDING = 0x4;
      final int NB_PP_ROWS_ANY_TO_RAM_WITH_PADDING_PURE_PADDING_PO = 0x5;
      final int NB_PP_ROWS_ANY_TO_RAM_WITH_PADDING_SOME_DATA = 0xa;
      final int NB_PP_ROWS_ANY_TO_RAM_WITH_PADDING_SOME_DATA_PO = 0xb;
      final int NB_PP_ROWS_BLAKE = 0x2;
      final int NB_PP_ROWS_BLAKE_PO = 0x3;
      final int NB_PP_ROWS_BLAKE_PT = 0x4;
      final int NB_PP_ROWS_EXO_TO_RAM_TRANSPLANTS = 0x1;
      final int NB_PP_ROWS_EXO_TO_RAM_TRANSPLANTS_PO = 0x2;
      final int NB_PP_ROWS_INVALID_CODE_PREFIX = 0x1;
      final int NB_PP_ROWS_INVALID_CODE_PREFIX_PO = 0x2;
      final int NB_PP_ROWS_MLOAD = 0x1;
      final int NB_PP_ROWS_MLOAD_PO = 0x2;
      final int NB_PP_ROWS_MLOAD_PT = 0x3;
      final int NB_PP_ROWS_MODEXP_DATA = 0x6;
      final int NB_PP_ROWS_MODEXP_DATA_PO = 0x7;
      final int NB_PP_ROWS_MODEXP_ZERO = 0x1;
      final int NB_PP_ROWS_MODEXP_ZERO_PO = 0x2;
      final int NB_PP_ROWS_MSTORE = 0x1;
      final int NB_PP_ROWS_MSTORE8 = 0x1;
      final int NB_PP_ROWS_MSTORE8_PO = 0x2;
      final int NB_PP_ROWS_MSTORE_PO = 0x2;
      final int NB_PP_ROWS_MSTORE_PT = 0x3;
      final int NB_PP_ROWS_RAM_TO_EXO_WITH_PADDING = 0x4;
      final int NB_PP_ROWS_RAM_TO_EXO_WITH_PADDING_PO = 0x5;
      final int NB_PP_ROWS_RAM_TO_RAM_SANS_PADDING = 0x5;
      final int NB_PP_ROWS_RAM_TO_RAM_SANS_PADDING_PO = 0x6;
      final int NB_PP_ROWS_RIGHT_PADDED_WORD_EXTRACTION = 0x5;
      final int NB_PP_ROWS_RIGHT_PADDED_WORD_EXTRACTION_PO = 0x6;
      final int NB_PP_ROWS_RIGHT_PADDED_WORD_EXTRACTION_PT = 0x7;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Mmu bin1(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu bin2(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu bin3(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu bin4(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu bin5(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isAnyToRamWithPaddingPurePadding(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isAnyToRamWithPaddingSomeData(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isBlake(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isExoToRamTransplants(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isInvalidCodePrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isMload(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isModexpData(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isModexpZero(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isMstore(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isMstore8(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isRamToExoWithPadding(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isRamToRamSansPadding(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu isRightPaddedWordExtraction(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu lzro(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu macro(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu micro(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu mmioStamp(final long val) { throw new IllegalArgumentException(); }
      default Mmu ntFirst(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu ntLast(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu ntMddl(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu ntOnly(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu out1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu out2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu out3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu out4(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu out5(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu prprc(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu rzFirst(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu rzLast(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu rzMddl(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu rzOnly(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu stamp(final long val) { throw new IllegalArgumentException(); }
      default Mmu tot(final long val) { throw new IllegalArgumentException(); }
      default Mmu totlz(final long val) { throw new IllegalArgumentException(); }
      default Mmu totnt(final long val) { throw new IllegalArgumentException(); }
      default Mmu totrz(final long val) { throw new IllegalArgumentException(); }
      Mmu validateRow();
      Mmu fillAndValidateRow();
      default Mmu pMacroAuxId(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroExoSum(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMacroInst(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMacroLimb1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroLimb2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroPhase(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMacroRefOffset(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroRefSize(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroSize(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroSrcId(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroSrcOffsetHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroSrcOffsetLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroSuccessBit(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu pMacroTgtId(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMacroTgtOffsetLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMicroCnS(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMicroCnT(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMicroExoId(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroExoSum(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroInst(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroKecId(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroLimb(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMicroPhase(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroSbo(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroSbo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmu pMicroSize(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroSize(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmu pMicroSlo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMicroSuccessBit(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu pMicroTbo(final long val) { throw new IllegalArgumentException(); }
      default Mmu pMicroTbo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmu pMicroTlo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pMicroTotalSize(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcCt(final long val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcEucA(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcEucB(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcEucCeil(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcEucFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcEucQuot(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcEucRem(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpInst(final long val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mmu pPrprcWcpRes(final boolean val) { throw new IllegalArgumentException(); }
   }
   public interface Mod {
      final BigInteger THETA = new BigInteger("18446744073709551616");
      final BigInteger THETA2 = new BigInteger("340282366920938463463374607431768211456");
      final BigInteger THETA_SQUARED_OVER_TWO = new BigInteger("170141183460469231731687303715884105728");
      int spillage();
      List<ColumnHeader> headers(int length);
      default Mod acc12(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod acc13(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod acc22(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod acc23(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accB0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accB1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accB2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accB3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accDelta0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accDelta1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accDelta2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accDelta3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accH0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accH1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accH2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accQ0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accQ1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accQ2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accQ3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accR0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accR1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accR2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod accR3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod byte12(final long val) { throw new IllegalArgumentException(); }
      default Mod byte12(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byte13(final long val) { throw new IllegalArgumentException(); }
      default Mod byte13(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byte22(final long val) { throw new IllegalArgumentException(); }
      default Mod byte22(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byte23(final long val) { throw new IllegalArgumentException(); }
      default Mod byte23(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteB0(final long val) { throw new IllegalArgumentException(); }
      default Mod byteB0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteB1(final long val) { throw new IllegalArgumentException(); }
      default Mod byteB1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteB2(final long val) { throw new IllegalArgumentException(); }
      default Mod byteB2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteB3(final long val) { throw new IllegalArgumentException(); }
      default Mod byteB3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteDelta0(final long val) { throw new IllegalArgumentException(); }
      default Mod byteDelta0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteDelta1(final long val) { throw new IllegalArgumentException(); }
      default Mod byteDelta1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteDelta2(final long val) { throw new IllegalArgumentException(); }
      default Mod byteDelta2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteDelta3(final long val) { throw new IllegalArgumentException(); }
      default Mod byteDelta3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteH0(final long val) { throw new IllegalArgumentException(); }
      default Mod byteH0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteH1(final long val) { throw new IllegalArgumentException(); }
      default Mod byteH1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteH2(final long val) { throw new IllegalArgumentException(); }
      default Mod byteH2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteQ0(final long val) { throw new IllegalArgumentException(); }
      default Mod byteQ0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteQ1(final long val) { throw new IllegalArgumentException(); }
      default Mod byteQ1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteQ2(final long val) { throw new IllegalArgumentException(); }
      default Mod byteQ2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteQ3(final long val) { throw new IllegalArgumentException(); }
      default Mod byteQ3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteR0(final long val) { throw new IllegalArgumentException(); }
      default Mod byteR0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteR1(final long val) { throw new IllegalArgumentException(); }
      default Mod byteR1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteR2(final long val) { throw new IllegalArgumentException(); }
      default Mod byteR2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod byteR3(final long val) { throw new IllegalArgumentException(); }
      default Mod byteR3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod cmp1(final boolean val) { throw new IllegalArgumentException(); }
      default Mod cmp2(final boolean val) { throw new IllegalArgumentException(); }
      default Mod ct(final long val) { throw new IllegalArgumentException(); }
      default Mod inst(final long val) { throw new IllegalArgumentException(); }
      default Mod inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mod isDiv(final boolean val) { throw new IllegalArgumentException(); }
      default Mod isMod(final boolean val) { throw new IllegalArgumentException(); }
      default Mod isSdiv(final boolean val) { throw new IllegalArgumentException(); }
      default Mod isSmod(final boolean val) { throw new IllegalArgumentException(); }
      default Mod mli(final boolean val) { throw new IllegalArgumentException(); }
      default Mod msb1(final boolean val) { throw new IllegalArgumentException(); }
      default Mod msb2(final boolean val) { throw new IllegalArgumentException(); }
      default Mod oli(final boolean val) { throw new IllegalArgumentException(); }
      default Mod resHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod resLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mod signed(final boolean val) { throw new IllegalArgumentException(); }
      default Mod stamp(final long val) { throw new IllegalArgumentException(); }
      Mod validateRow();
      Mod fillAndValidateRow();
   }
   public interface Mul {
      final int ONETWOEIGHT = 0x80;
      final int ONETWOSEVEN = 0x7f;
      final BigInteger THETA = new BigInteger("18446744073709551616");
      final BigInteger THETA2 = new BigInteger("340282366920938463463374607431768211456");
      int spillage();
      List<ColumnHeader> headers(int length);
      default Mul accA0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accA1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accA2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accA3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accB0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accB1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accB2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accB3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accC0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accC1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accC2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accC3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accH0(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accH1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accH2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul accH3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul bits(final boolean val) { throw new IllegalArgumentException(); }
      default Mul bitNum(final long val) { throw new IllegalArgumentException(); }
      default Mul byteA0(final long val) { throw new IllegalArgumentException(); }
      default Mul byteA0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteA1(final long val) { throw new IllegalArgumentException(); }
      default Mul byteA1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteA2(final long val) { throw new IllegalArgumentException(); }
      default Mul byteA2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteA3(final long val) { throw new IllegalArgumentException(); }
      default Mul byteA3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteB0(final long val) { throw new IllegalArgumentException(); }
      default Mul byteB0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteB1(final long val) { throw new IllegalArgumentException(); }
      default Mul byteB1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteB2(final long val) { throw new IllegalArgumentException(); }
      default Mul byteB2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteB3(final long val) { throw new IllegalArgumentException(); }
      default Mul byteB3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteC0(final long val) { throw new IllegalArgumentException(); }
      default Mul byteC0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteC1(final long val) { throw new IllegalArgumentException(); }
      default Mul byteC1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteC2(final long val) { throw new IllegalArgumentException(); }
      default Mul byteC2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteC3(final long val) { throw new IllegalArgumentException(); }
      default Mul byteC3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteH0(final long val) { throw new IllegalArgumentException(); }
      default Mul byteH0(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteH1(final long val) { throw new IllegalArgumentException(); }
      default Mul byteH1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteH2(final long val) { throw new IllegalArgumentException(); }
      default Mul byteH2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul byteH3(final long val) { throw new IllegalArgumentException(); }
      default Mul byteH3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul counter(final long val) { throw new IllegalArgumentException(); }
      default Mul counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul exponentBit(final boolean val) { throw new IllegalArgumentException(); }
      default Mul exponentBitAccumulator(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul exponentBitSource(final boolean val) { throw new IllegalArgumentException(); }
      default Mul instruction(final long val) { throw new IllegalArgumentException(); }
      default Mul instruction(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mul mulStamp(final long val) { throw new IllegalArgumentException(); }
      default Mul oli(final boolean val) { throw new IllegalArgumentException(); }
      default Mul resultVanishes(final boolean val) { throw new IllegalArgumentException(); }
      default Mul resHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul resLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mul squareAndMultiply(final boolean val) { throw new IllegalArgumentException(); }
      default Mul tinyBase(final boolean val) { throw new IllegalArgumentException(); }
      default Mul tinyExponent(final boolean val) { throw new IllegalArgumentException(); }
      Mul validateRow();
      Mul fillAndValidateRow();
   }
   public interface Mxp {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Mxp acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp acc3(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp acc4(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp accA(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp accQ(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp accW(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp byte1(final long val) { throw new IllegalArgumentException(); }
      default Mxp byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byte2(final long val) { throw new IllegalArgumentException(); }
      default Mxp byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byte3(final long val) { throw new IllegalArgumentException(); }
      default Mxp byte3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byte4(final long val) { throw new IllegalArgumentException(); }
      default Mxp byte4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byteA(final long val) { throw new IllegalArgumentException(); }
      default Mxp byteA(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byteQ(final long val) { throw new IllegalArgumentException(); }
      default Mxp byteQ(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byteQq(final long val) { throw new IllegalArgumentException(); }
      default Mxp byteQq(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byteR(final long val) { throw new IllegalArgumentException(); }
      default Mxp byteR(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp byteW(final long val) { throw new IllegalArgumentException(); }
      default Mxp byteW(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp cn(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp cn(final long val) { throw new IllegalArgumentException(); }
      default Mxp comp(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp computation(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp ct(final long val) { throw new IllegalArgumentException(); }
      default Mxp ctMax(final long val) { throw new IllegalArgumentException(); }
      default Mxp cMem(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp cMemNew(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp decoder(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp deploys(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp expands(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp gasMxp(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp gbyte(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp gword(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp inst(final long val) { throw new IllegalArgumentException(); }
      default Mxp inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp linCost(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp macro(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp maxOffset(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp maxOffset1(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp maxOffset2(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp mtntop(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp mxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp mxpStamp(final long val) { throw new IllegalArgumentException(); }
      default Mxp mxpType1(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp mxpType2(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp mxpType3(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp mxpType4(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp mxpType5(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp noop(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp offset1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp offset1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp offset2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp offset2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp quadCost(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp roob(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp scenario(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp size1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp size1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp size1NonzeroNoMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp size2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp size2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp size2NonzeroNoMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp stamp(final long val) { throw new IllegalArgumentException(); }
      default Mxp words(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp wordsNew(final Bytes val) { throw new IllegalArgumentException(); }
      Mxp validateRow();
      Mxp fillAndValidateRow();
      default Mxp pComputationWcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pComputationEucFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pComputationExoInst(final long val) { throw new IllegalArgumentException(); }
      default Mxp pComputationExoInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp pComputationArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pComputationArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pComputationArg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pComputationArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pComputationResA(final long val) { throw new IllegalArgumentException(); }
      default Mxp pComputationResB(final long val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderInst(final long val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsMsize(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsReturn(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsMcopy(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsFixedSize32(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsFixedSize1(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsSingleMaxOffset(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsDoubleMaxOffset(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsWordPricing(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderIsBytePricing(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderGword(final long val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderGword(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderGbyte(final long val) { throw new IllegalArgumentException(); }
      default Mxp pDecoderGbyte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp pMacroInst(final long val) { throw new IllegalArgumentException(); }
      default Mxp pMacroInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Mxp pMacroDeploying(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pMacroOffset1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroOffset1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroSize1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroSize1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroOffset2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroOffset2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroSize2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroSize2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroRes(final long val) { throw new IllegalArgumentException(); }
      default Mxp pMacroMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pMacroGasMxp(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pMacroS1Nznomxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pMacroS2Nznomxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioMsize(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioTrivial(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioMxpx(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioStateUpdateWordPricing(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioStateUpdateBytePricing(final boolean val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioWords(final long val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioWordsNew(final long val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioCmem(final Bytes val) { throw new IllegalArgumentException(); }
      default Mxp pScenarioCmemNew(final Bytes val) { throw new IllegalArgumentException(); }
   }
   public interface Oob {
      final int CT_MAX_BLAKE2F_CDS = 0x1;
      final int CT_MAX_BLAKE2F_PARAMS = 0x1;
      final int CT_MAX_CALL = 0x2;
      final int CT_MAX_CDL = 0x0;
      final int CT_MAX_DEPLOYMENT = 0x0;
      final int CT_MAX_ECADD = 0x2;
      final int CT_MAX_ECMUL = 0x2;
      final int CT_MAX_ECPAIRING = 0x4;
      final int CT_MAX_ECRECOVER = 0x2;
      final int CT_MAX_IDENTITY = 0x3;
      final int CT_MAX_JUMP = 0x0;
      final int CT_MAX_JUMPI = 0x1;
      final int CT_MAX_MODEXP_CDS = 0x2;
      final int CT_MAX_MODEXP_EXTRACT = 0x3;
      final int CT_MAX_MODEXP_LEAD = 0x3;
      final int CT_MAX_MODEXP_PRICING = 0x5;
      final int CT_MAX_MODEXP_XBS = 0x2;
      final int CT_MAX_RDC = 0x2;
      final int CT_MAX_RIPEMD = 0x3;
      final int CT_MAX_SHA2 = 0x3;
      final int CT_MAX_SSTORE = 0x0;
      final int CT_MAX_XCALL = 0x0;
      final int G_QUADDIVISOR = 0x3;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Oob addFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Oob ct(final long val) { throw new IllegalArgumentException(); }
      default Oob ctMax(final long val) { throw new IllegalArgumentException(); }
      default Oob data1(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data10(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data2(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data3(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data4(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data5(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data6(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data7(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data8(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob data9(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob isBlake2FCds(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isBlake2FParams(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isCall(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isCdl(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isCreate(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isDeployment(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isEcadd(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isEcmul(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isEcpairing(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isEcrecover(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isIdentity(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isJump(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isJumpi(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isModexpCds(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isModexpExtract(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isModexpLead(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isModexpPricing(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isModexpXbs(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isRdc(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isRipemd(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isSha2(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isSstore(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isXcall(final boolean val) { throw new IllegalArgumentException(); }
      default Oob isXcreate(final boolean val) { throw new IllegalArgumentException(); }
      default Oob modFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Oob oobInst(final long val) { throw new IllegalArgumentException(); }
      default Oob outgoingData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob outgoingData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob outgoingData3(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob outgoingData4(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob outgoingInst(final long val) { throw new IllegalArgumentException(); }
      default Oob outgoingInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Oob outgoingResLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Oob stamp(final long val) { throw new IllegalArgumentException(); }
      default Oob wcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      Oob validateRow();
      Oob fillAndValidateRow();
   }
   public interface Power {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Power iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Power exponent(final long val) { throw new IllegalArgumentException(); }
      default Power exponent(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Power power(final Bytes val) { throw new IllegalArgumentException(); }
      Power validateRow();
      Power fillAndValidateRow();
   }
   public interface Rlpaddr {
      final int MAX_CT_CREATE = 0x7;
      final int MAX_CT_CREATE2 = 0x5;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Rlpaddr acc(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr accBytesize(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr accBytesize(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlpaddr addrHi(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr addrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr bit1(final boolean val) { throw new IllegalArgumentException(); }
      default Rlpaddr bitAcc(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr bitAcc(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlpaddr byte1(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlpaddr counter(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlpaddr depAddrHi(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr depAddrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr index(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr index(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlpaddr kecHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr kecLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr lc(final boolean val) { throw new IllegalArgumentException(); }
      default Rlpaddr limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr nonce(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr power(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr rawAddrHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr recipe(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr recipe(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlpaddr recipe1(final boolean val) { throw new IllegalArgumentException(); }
      default Rlpaddr recipe2(final boolean val) { throw new IllegalArgumentException(); }
      default Rlpaddr saltHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr saltLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlpaddr selectorKeccakRes(final boolean val) { throw new IllegalArgumentException(); }
      default Rlpaddr stamp(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr tinyNonZeroNonce(final boolean val) { throw new IllegalArgumentException(); }
      default Rlpaddr nBytes(final long val) { throw new IllegalArgumentException(); }
      default Rlpaddr nBytes(final UnsignedByte val) { throw new IllegalArgumentException(); }
      Rlpaddr validateRow();
      Rlpaddr fillAndValidateRow();
   }
   public interface Rlptxn {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Rlptxn absTxNum(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn absTxNumInfiny(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn accessTupleBytesize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn accBytesize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn addrHi(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn addrLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn bit(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn bitAcc(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn bitAcc(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn byte1(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn byte2(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn cmp(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn codeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn counter(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn ct(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn ctMax(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn dataGasCost(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn dataHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn dataLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn depth1(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn depth2(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn done(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn indexData(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn indexLt(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn indexLx(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn input1(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn input2(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn isAccessList(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isBeta(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isChainId(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isData(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isGasLimit(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isGasPrice(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isMaxFeePerGas(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isMaxPriorityFeePerGas(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isNonce(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseAccessList(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseBeta(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseChainId(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseData(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseGasLimit(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseGasPrice(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseMaxFeePerGas(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseMaxPriorityFeePerGas(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseNonce(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseR(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseRlpPrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseS(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseTo(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseValue(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPhaseY(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isPrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isR(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isRlpPrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isS(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isTo(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isValue(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn isY(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn lcCorrection(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn limbConstructed(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn lt(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn lx(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn phase(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn phaseEnd(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn phaseSize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn power(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn replayProtection(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn requiresEvmExecution(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn rlpLtBytesize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn rlpLxBytesize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn toHashByProver(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn txn(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn type(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn type0(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn type1(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn type2(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn type3(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn type4(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn userTxnNumber(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn yParity(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn nAddr(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn nBytes(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn nKeys(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn nKeysPerAddr(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn nStep(final long val) { throw new IllegalArgumentException(); }
      Rlptxn validateRow();
      Rlptxn fillAndValidateRow();
      default Rlptxn pCmpLimb(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpNbytes(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpNbytes(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTrmFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpRlpUtilsFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpInst(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData3(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData4(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData5(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData6(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData7(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData8(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpExoData8(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpIsPrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpIsAddress(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpIsStorage(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp1(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp2(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp3(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp4(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp5(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp6(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pCmpTmp7(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnTxType(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnTxType(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnIsDeployment(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnChainId(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnNonce(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnGasPrice(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnMaxPriorityFeePerGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnMaxFeePerGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnGasLimit(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnToHi(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnToLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnValue(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnNumberOfZeroBytes(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnNumberOfNonzeroBytes(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnNumberOfPrewarmedAddresses(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnNumberOfPrewarmedStorageKeys(final long val) { throw new IllegalArgumentException(); }
      default Rlptxn pTxnRequiresEvmExecution(final boolean val) { throw new IllegalArgumentException(); }
   }
   public interface Rlptxrcpt {
      final int SUBPHASE_ID_WEIGHT_DEPTH = 0x30;
      final int SUBPHASE_ID_WEIGHT_INDEX_LOCAL = 0x60;
      final int SUBPHASE_ID_WEIGHT_IS_OD = 0x18;
      final int SUBPHASE_ID_WEIGHT_IS_OT = 0xc;
      final int SUBPHASE_ID_WEIGHT_IS_PREFIX = 0x6;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Rlptxrcpt absLogNum(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt absLogNumMax(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt absTxNum(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt absTxNumMax(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt acc3(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt acc4(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt accSize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt bit(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt bitAcc(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt bitAcc(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte1(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte2(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte3(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte4(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt byte4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt counter(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt depth1(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt done(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt index(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt indexLocal(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt input1(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt input2(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt input3(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt input4(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt isData(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt isPrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt isTopic(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt lcCorrection(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt limbConstructed(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt localSize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt logEntrySize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phase1(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phase2(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phase3(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phase4(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phase5(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phaseEnd(final boolean val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phaseId(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt phaseSize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt power(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt txrcptSize(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt nBytes(final long val) { throw new IllegalArgumentException(); }
      default Rlptxrcpt nStep(final long val) { throw new IllegalArgumentException(); }
      Rlptxrcpt validateRow();
      Rlptxrcpt fillAndValidateRow();
   }
   public interface Rlputils {
      final int CT_MAX_INST_INTEGER = 0x2;
      final int CT_MAX_INST_BYTE_STRING_PREFIX = 0x2;
      final int CT_MAX_INST_BYTES32 = 0x0;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Rlputils iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils macro(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils compt(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils ct(final long val) { throw new IllegalArgumentException(); }
      default Rlputils ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils ctMax(final long val) { throw new IllegalArgumentException(); }
      default Rlputils ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils zeroCounter(final long val) { throw new IllegalArgumentException(); }
      default Rlputils nonzCounter(final long val) { throw new IllegalArgumentException(); }
      default Rlputils isInteger(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils isByteStringPrefix(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils isByte32(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils isDataPricing(final boolean val) { throw new IllegalArgumentException(); }
      Rlputils validateRow();
      Rlputils fillAndValidateRow();
      default Rlputils pMacroInst(final long val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData1(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData2(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData3(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData4(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData5(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData6(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData7(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData8(final long val) { throw new IllegalArgumentException(); }
      default Rlputils pMacroData8(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils pComptArg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pComptArg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pComptArg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pComptRes(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils pComptInst(final long val) { throw new IllegalArgumentException(); }
      default Rlputils pComptInst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils pComptWcpCtMax(final long val) { throw new IllegalArgumentException(); }
      default Rlputils pComptWcpCtMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils pComptShfFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Rlputils pComptShfArg(final long val) { throw new IllegalArgumentException(); }
      default Rlputils pComptShfArg(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rlputils pComptShfPower(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pComptAcc(final Bytes val) { throw new IllegalArgumentException(); }
      default Rlputils pComptLimb(final Bytes val) { throw new IllegalArgumentException(); }
   }
   public interface Rom {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Rom acc(final Bytes val) { throw new IllegalArgumentException(); }
      default Rom codesizeReached(final boolean val) { throw new IllegalArgumentException(); }
      default Rom codeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Rom codeFragmentIndexInfty(final long val) { throw new IllegalArgumentException(); }
      default Rom codeSize(final long val) { throw new IllegalArgumentException(); }
      default Rom counter(final long val) { throw new IllegalArgumentException(); }
      default Rom counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom counterMax(final long val) { throw new IllegalArgumentException(); }
      default Rom counterMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom counterPush(final long val) { throw new IllegalArgumentException(); }
      default Rom counterPush(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom index(final long val) { throw new IllegalArgumentException(); }
      default Rom isJumpdest(final boolean val) { throw new IllegalArgumentException(); }
      default Rom isPush(final boolean val) { throw new IllegalArgumentException(); }
      default Rom isPushData(final boolean val) { throw new IllegalArgumentException(); }
      default Rom limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Rom opcode(final long val) { throw new IllegalArgumentException(); }
      default Rom opcode(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom paddedBytecodeByte(final long val) { throw new IllegalArgumentException(); }
      default Rom paddedBytecodeByte(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom programCounter(final long val) { throw new IllegalArgumentException(); }
      default Rom pushFunnelBit(final boolean val) { throw new IllegalArgumentException(); }
      default Rom pushParameter(final long val) { throw new IllegalArgumentException(); }
      default Rom pushParameter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom pushValueAcc(final Bytes val) { throw new IllegalArgumentException(); }
      default Rom pushValueHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Rom pushValueLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Rom nBytes(final long val) { throw new IllegalArgumentException(); }
      default Rom nBytes(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Rom nBytesAcc(final long val) { throw new IllegalArgumentException(); }
      default Rom nBytesAcc(final UnsignedByte val) { throw new IllegalArgumentException(); }
      Rom validateRow();
      Rom fillAndValidateRow();
   }
   public interface Romlex {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Romlex addressHi(final long val) { throw new IllegalArgumentException(); }
      default Romlex addressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Romlex codeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Romlex codeFragmentIndexInfty(final long val) { throw new IllegalArgumentException(); }
      default Romlex codeHashHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Romlex codeHashLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Romlex codeSize(final long val) { throw new IllegalArgumentException(); }
      default Romlex deploymentNumber(final long val) { throw new IllegalArgumentException(); }
      default Romlex deploymentStatus(final boolean val) { throw new IllegalArgumentException(); }
      Romlex validateRow();
      Romlex fillAndValidateRow();
   }
   public interface Shakiradata {
      final int INDEX_MAX_RESULT = 0x1;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Shakiradata id(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata index(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata indexMax(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata isKeccakData(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata isKeccakResult(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata isRipemdData(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata isRipemdResult(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata isSha2Data(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata isSha2Result(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata limb(final Bytes val) { throw new IllegalArgumentException(); }
      default Shakiradata phase(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata phase(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shakiradata selectorKeccakResHi(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata selectorRipemdResHi(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata selectorSha2ResHi(final boolean val) { throw new IllegalArgumentException(); }
      default Shakiradata shakiraStamp(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata totalSize(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata nBytes(final long val) { throw new IllegalArgumentException(); }
      default Shakiradata nBytesAcc(final long val) { throw new IllegalArgumentException(); }
      Shakiradata validateRow();
      Shakiradata fillAndValidateRow();
   }
   public interface Shf {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Shf acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf acc3(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf acc4(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf acc5(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf bits(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bit1(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bit2(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bit3(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bit4(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bitB3(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bitB4(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bitB5(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bitB6(final boolean val) { throw new IllegalArgumentException(); }
      default Shf bitB7(final boolean val) { throw new IllegalArgumentException(); }
      default Shf byte1(final long val) { throw new IllegalArgumentException(); }
      default Shf byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf byte2(final long val) { throw new IllegalArgumentException(); }
      default Shf byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf byte3(final long val) { throw new IllegalArgumentException(); }
      default Shf byte3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf byte4(final long val) { throw new IllegalArgumentException(); }
      default Shf byte4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf byte5(final long val) { throw new IllegalArgumentException(); }
      default Shf byte5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf counter(final long val) { throw new IllegalArgumentException(); }
      default Shf counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf inst(final long val) { throw new IllegalArgumentException(); }
      default Shf inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Shf known(final boolean val) { throw new IllegalArgumentException(); }
      default Shf leftAlignedSuffixHigh(final long val) { throw new IllegalArgumentException(); }
      default Shf leftAlignedSuffixHigh(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf leftAlignedSuffixLow(final long val) { throw new IllegalArgumentException(); }
      default Shf leftAlignedSuffixLow(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf low3(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf microShiftParameter(final long val) { throw new IllegalArgumentException(); }
      default Shf microShiftParameter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf neg(final boolean val) { throw new IllegalArgumentException(); }
      default Shf ones(final long val) { throw new IllegalArgumentException(); }
      default Shf ones(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf oneLineInstruction(final boolean val) { throw new IllegalArgumentException(); }
      default Shf resHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf resLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Shf rightAlignedPrefixHigh(final long val) { throw new IllegalArgumentException(); }
      default Shf rightAlignedPrefixHigh(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf rightAlignedPrefixLow(final long val) { throw new IllegalArgumentException(); }
      default Shf rightAlignedPrefixLow(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb3Hi(final long val) { throw new IllegalArgumentException(); }
      default Shf shb3Hi(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb3Lo(final long val) { throw new IllegalArgumentException(); }
      default Shf shb3Lo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb4Hi(final long val) { throw new IllegalArgumentException(); }
      default Shf shb4Hi(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb4Lo(final long val) { throw new IllegalArgumentException(); }
      default Shf shb4Lo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb5Hi(final long val) { throw new IllegalArgumentException(); }
      default Shf shb5Hi(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb5Lo(final long val) { throw new IllegalArgumentException(); }
      default Shf shb5Lo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb6Hi(final long val) { throw new IllegalArgumentException(); }
      default Shf shb6Hi(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb6Lo(final long val) { throw new IllegalArgumentException(); }
      default Shf shb6Lo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb7Hi(final long val) { throw new IllegalArgumentException(); }
      default Shf shb7Hi(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shb7Lo(final long val) { throw new IllegalArgumentException(); }
      default Shf shb7Lo(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shf shiftDirection(final boolean val) { throw new IllegalArgumentException(); }
      default Shf shiftStamp(final long val) { throw new IllegalArgumentException(); }
      Shf validateRow();
      Shf fillAndValidateRow();
   }
   public interface Shfreftable {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Shfreftable byte1(final long val) { throw new IllegalArgumentException(); }
      default Shfreftable byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shfreftable iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Shfreftable las(final long val) { throw new IllegalArgumentException(); }
      default Shfreftable las(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shfreftable mshp(final long val) { throw new IllegalArgumentException(); }
      default Shfreftable mshp(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shfreftable ones(final long val) { throw new IllegalArgumentException(); }
      default Shfreftable ones(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Shfreftable rap(final long val) { throw new IllegalArgumentException(); }
      default Shfreftable rap(final UnsignedByte val) { throw new IllegalArgumentException(); }
      Shfreftable validateRow();
      Shfreftable fillAndValidateRow();
   }
   public interface Stp {
      final int STP_CT_MAX_CALL = 0x4;
      final int STP_CT_MAX_CALL_OOGX = 0x2;
      final int STP_CT_MAX_CREATE = 0x2;
      final int STP_CT_MAX_CREATE_OOGX = 0x1;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Stp arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp ct(final long val) { throw new IllegalArgumentException(); }
      default Stp ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Stp ctMax(final long val) { throw new IllegalArgumentException(); }
      default Stp ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Stp exists(final boolean val) { throw new IllegalArgumentException(); }
      default Stp exogenousModuleInstruction(final long val) { throw new IllegalArgumentException(); }
      default Stp exogenousModuleInstruction(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Stp gasActual(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp gasHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp gasLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp gasMxp(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp gasOutOfPocket(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp gasStipend(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp gasUpfront(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp instruction(final long val) { throw new IllegalArgumentException(); }
      default Stp instruction(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Stp isCall(final boolean val) { throw new IllegalArgumentException(); }
      default Stp isCallcode(final boolean val) { throw new IllegalArgumentException(); }
      default Stp isCreate(final boolean val) { throw new IllegalArgumentException(); }
      default Stp isCreate2(final boolean val) { throw new IllegalArgumentException(); }
      default Stp isDelegatecall(final boolean val) { throw new IllegalArgumentException(); }
      default Stp isStaticcall(final boolean val) { throw new IllegalArgumentException(); }
      default Stp modFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Stp outOfGasException(final boolean val) { throw new IllegalArgumentException(); }
      default Stp resLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp stamp(final long val) { throw new IllegalArgumentException(); }
      default Stp valHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp valLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Stp warm(final boolean val) { throw new IllegalArgumentException(); }
      default Stp wcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      Stp validateRow();
      Stp fillAndValidateRow();
   }
   public interface Trm {
      final int ROW_OFFSET_ADDRESS = 0x0;
      final int ROW_OFFSET_ADDRESS_TRM = 0x1;
      final int ROW_OFFSET_NON_ZERO_ADDR = 0x2;
      final int ROW_OFFSET_PRC_ADDR = 0x3;
      final int TRM_CT_MAX = 0x3;
      final int TRM_NB_ROWS = 0x4;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Trm arg1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Trm arg1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Trm arg2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Trm arg2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Trm ct(final long val) { throw new IllegalArgumentException(); }
      default Trm first(final boolean val) { throw new IllegalArgumentException(); }
      default Trm inst(final long val) { throw new IllegalArgumentException(); }
      default Trm inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Trm iomf(final boolean val) { throw new IllegalArgumentException(); }
      default Trm isPrecompile(final boolean val) { throw new IllegalArgumentException(); }
      default Trm rawAddressHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Trm rawAddressLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Trm res(final boolean val) { throw new IllegalArgumentException(); }
      default Trm trmAddressHi(final long val) { throw new IllegalArgumentException(); }
      Trm validateRow();
      Trm fillAndValidateRow();
   }
   public interface Txndata {
      final int COMMON_RLP_TXN_PHASE_NUMBER_0 = 0x1;
      final int COMMON_RLP_TXN_PHASE_NUMBER_1 = 0x8;
      final int COMMON_RLP_TXN_PHASE_NUMBER_2 = 0x3;
      final int COMMON_RLP_TXN_PHASE_NUMBER_3 = 0x9;
      final int COMMON_RLP_TXN_PHASE_NUMBER_4 = 0xa;
      final int COMMON_RLP_TXN_PHASE_NUMBER_5 = 0x7;
      final int TYPE_0_RLP_TXN_PHASE_NUMBER_6 = 0x4;
      final int TYPE_1_RLP_TXN_PHASE_NUMBER_6 = 0x4;
      final int TYPE_1_RLP_TXN_PHASE_NUMBER_7 = 0xb;
      final int TYPE_2_RLP_TXN_PHASE_NUMBER_6 = 0x6;
      final int TYPE_2_RLP_TXN_PHASE_NUMBER_7 = 0xb;
      final int row_offset___initial_balance_comparison = 0x1;
      final int row_offset___nonce_comparison = 0x0;
      int spillage();
      List<ColumnHeader> headers(int length);
      default Txndata absTxNum(final long val) { throw new IllegalArgumentException(); }
      default Txndata absTxNumMax(final long val) { throw new IllegalArgumentException(); }
      default Txndata argOneLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata argTwoLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata basefee(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata blockGasLimit(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata callDataSize(final long val) { throw new IllegalArgumentException(); }
      default Txndata codeFragmentIndex(final long val) { throw new IllegalArgumentException(); }
      default Txndata coinbaseHi(final long val) { throw new IllegalArgumentException(); }
      default Txndata coinbaseLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata copyTxcd(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata ct(final long val) { throw new IllegalArgumentException(); }
      default Txndata ct(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Txndata eucFlag(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata fromHi(final long val) { throw new IllegalArgumentException(); }
      default Txndata fromLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata gasCumulative(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata gasInitiallyAvailable(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata gasLeftover(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata gasLimit(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata gasPrice(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata initialBalance(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata initCodeSize(final long val) { throw new IllegalArgumentException(); }
      default Txndata inst(final long val) { throw new IllegalArgumentException(); }
      default Txndata inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Txndata isDep(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata isLastTxOfBlock(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata nonce(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata outgoingHi(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata outgoingLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata outgoingRlpTxnrcpt(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata phaseRlpTxn(final long val) { throw new IllegalArgumentException(); }
      default Txndata phaseRlpTxn(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Txndata phaseRlpTxnrcpt(final long val) { throw new IllegalArgumentException(); }
      default Txndata phaseRlpTxnrcpt(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Txndata priorityFeePerGas(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata refundCounter(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata refundEffective(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata relBlock(final long val) { throw new IllegalArgumentException(); }
      default Txndata relTxNum(final long val) { throw new IllegalArgumentException(); }
      default Txndata relTxNumMax(final long val) { throw new IllegalArgumentException(); }
      default Txndata requiresEvmExecution(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata res(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata statusCode(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata toHi(final long val) { throw new IllegalArgumentException(); }
      default Txndata toLo(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata type0(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata type1(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata type2(final boolean val) { throw new IllegalArgumentException(); }
      default Txndata value(final Bytes val) { throw new IllegalArgumentException(); }
      default Txndata wcpFlag(final boolean val) { throw new IllegalArgumentException(); }
      Txndata validateRow();
      Txndata fillAndValidateRow();
   }
   public interface Wcp {
      int spillage();
      List<ColumnHeader> headers(int length);
      default Wcp acc1(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp acc2(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp acc3(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp acc4(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp acc5(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp acc6(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp argument1Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp argument1Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp argument2Hi(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp argument2Lo(final Bytes val) { throw new IllegalArgumentException(); }
      default Wcp bits(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp bit1(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp bit2(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp bit3(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp bit4(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp byte1(final long val) { throw new IllegalArgumentException(); }
      default Wcp byte1(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp byte2(final long val) { throw new IllegalArgumentException(); }
      default Wcp byte2(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp byte3(final long val) { throw new IllegalArgumentException(); }
      default Wcp byte3(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp byte4(final long val) { throw new IllegalArgumentException(); }
      default Wcp byte4(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp byte5(final long val) { throw new IllegalArgumentException(); }
      default Wcp byte5(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp byte6(final long val) { throw new IllegalArgumentException(); }
      default Wcp byte6(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp counter(final long val) { throw new IllegalArgumentException(); }
      default Wcp counter(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp ctMax(final long val) { throw new IllegalArgumentException(); }
      default Wcp ctMax(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp inst(final long val) { throw new IllegalArgumentException(); }
      default Wcp inst(final UnsignedByte val) { throw new IllegalArgumentException(); }
      default Wcp isEq(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isGeq(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isGt(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isIszero(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isLeq(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isLt(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isSgt(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp isSlt(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp neg1(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp neg2(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp oneLineInstruction(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp result(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp variableLengthInstruction(final boolean val) { throw new IllegalArgumentException(); }
      default Wcp wordComparisonStamp(final long val) { throw new IllegalArgumentException(); }
      Wcp validateRow();
      Wcp fillAndValidateRow();
   }

   /**
    * ColumnHeader contains information about a given column in the resulting trace file.
    *
    * @param name Name of the column, as found in the trace file.
    * @param bytesPerElement Bytes required for each element in the column.
    */
   public record ColumnHeader(String name, int register, long bytesPerElement, long length) { }

  /**
   * Add an item of metadata to this trace.
   */
  public void addMetadata(String key, Object value);

  /**
   * Construct a new trace which will be written to a given file.
   *
   * @param file File into which the trace will be written.  Observe any previous contents of this file will be lost.
   * @return Trace object to use for writing column data.
   *
   * @throws IOException If an I/O error occurs.
   */
  public void open(RandomAccessFile file, List<ColumnHeader> rawHeaders) throws IOException;
}
