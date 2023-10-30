/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.hub;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
  @JsonProperty("ABSOLUTE_TRANSACTION_NUMBER") List<BigInteger> absoluteTransactionNumber,
  @JsonProperty("ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_CCRS_STAMP_xor_HASH_INFO___KEC_HI_xor_ADDRESS_HI_xor_BASEFEE") List<BigInteger> addrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee,
  @JsonProperty("ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_EXP___DYNCOST_xor_HASH_INFO___KEC_LO_xor_ADDRESS_LO_xor_CALL_DATA_SIZE") List<BigInteger> addrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize,
  @JsonProperty("BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_EXP___EXPONENT_LO_xor_HEIGHT_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO") List<BigInteger> balanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo,
  @JsonProperty("BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_EXP___EXPONENT_HI_xor_HASH_INFO___SIZE_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI") List<BigInteger> balanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi,
  @JsonProperty("BATCH_NUMBER") List<BigInteger> batchNumber,
  @JsonProperty("CALLER_CONTEXT_NUMBER") List<BigInteger> callerContextNumber,
  @JsonProperty("CODE_ADDRESS_HI") List<BigInteger> codeAddressHi,
  @JsonProperty("CODE_ADDRESS_LO") List<BigInteger> codeAddressLo,
  @JsonProperty("CODE_DEPLOYMENT_NUMBER") List<BigInteger> codeDeploymentNumber,
  @JsonProperty("CODE_DEPLOYMENT_STATUS") List<Boolean> codeDeploymentStatus,
  @JsonProperty("CODE_FRAGMENT_INDEX") List<BigInteger> codeFragmentIndex,
  @JsonProperty("CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_MMU___INST_xor_HEIGHT_OVER_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO") List<BigInteger> codeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo,
  @JsonProperty("CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_MMU___EXO_SUM_xor_HEIGHT_NEW_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI") List<BigInteger> codeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi,
  @JsonProperty("CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_MMU___OFFSET_2_HI_xor_INST_xor_VAL_NEXT_HI_xor_GAS_PRICE") List<BigInteger> codeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice,
  @JsonProperty("CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_MMU___OFFSET_1_LO_xor_HEIGHT_UNDER_xor_VAL_CURR_LO_xor_GAS_LIMIT") List<BigInteger> codeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit,
  @JsonProperty("CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_MMU___PARAM_1_xor_PUSH_VALUE_LO_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL") List<BigInteger> codeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal,
  @JsonProperty("CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_MMU___OFFSET_2_LO_xor_PUSH_VALUE_HI_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT") List<BigInteger> codeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount,
  @JsonProperty("CONTEXT_GETS_REVERTED_FLAG") List<Boolean> contextGetsRevertedFlag,
  @JsonProperty("CONTEXT_MAY_CHANGE_FLAG") List<Boolean> contextMayChangeFlag,
  @JsonProperty("CONTEXT_NUMBER") List<BigInteger> contextNumber,
  @JsonProperty("CONTEXT_NUMBER_NEW") List<BigInteger> contextNumberNew,
  @JsonProperty("CONTEXT_REVERT_STAMP") List<BigInteger> contextRevertStamp,
  @JsonProperty("CONTEXT_SELF_REVERTS_FLAG") List<Boolean> contextSelfRevertsFlag,
  @JsonProperty("CONTEXT_WILL_REVERT_FLAG") List<Boolean> contextWillRevertFlag,
  @JsonProperty("COUNTER_NSR") List<BigInteger> counterNsr,
  @JsonProperty("COUNTER_TLI") List<Boolean> counterTli,
  @JsonProperty("DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_MMU___REF_SIZE_xor_STACK_ITEM_HEIGHT_3_xor_INIT_GAS") List<BigInteger> depNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas,
  @JsonProperty("DEP_NUM_xor_CALL_DATA_SIZE_xor_MMU___REF_OFFSET_xor_STACK_ITEM_HEIGHT_2_xor_INIT_CODE_SIZE") List<BigInteger> depNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize,
  @JsonProperty("DEP_STATUS_NEW_xor_EXP___FLAG_xor_CALL_EOA_SUCCESS_CALLER_WILL_REVERT_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE") List<Boolean> depStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode,
  @JsonProperty("DEP_STATUS_xor_CCSR_FLAG_xor_CALL_ABORT_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559") List<Boolean> depStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559,
  @JsonProperty("DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_MMU___PARAM_2_xor_STACK_ITEM_HEIGHT_1_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE") List<BigInteger> deploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance,
  @JsonProperty("DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ABORT_FLAG_xor_BLAKE2f_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT") List<Boolean> deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment,
  @JsonProperty("DOM_STAMP") List<BigInteger> domStamp,
  @JsonProperty("EXCEPTION_AHOY_FLAG") List<Boolean> exceptionAhoyFlag,
  @JsonProperty("EXISTS_NEW_xor_MMU___FLAG_xor_CALL_PRC_FAILURE_CALLER_WILL_REVERT_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG") List<Boolean> existsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig,
  @JsonProperty("EXISTS_xor_FCOND_FLAG_xor_CALL_EOA_SUCCESS_CALLER_WONT_REVERT_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION") List<Boolean> existsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution,
  @JsonProperty("GAS_ACTUAL") List<BigInteger> gasActual,
  @JsonProperty("GAS_COST") List<BigInteger> gasCost,
  @JsonProperty("GAS_EXPECTED") List<BigInteger> gasExpected,
  @JsonProperty("GAS_NEXT") List<BigInteger> gasNext,
  @JsonProperty("GAS_REFUND") List<BigInteger> gasRefund,
  @JsonProperty("GAS_REFUND_NEW") List<BigInteger> gasRefundNew,
  @JsonProperty("HAS_CODE_NEW_xor_MXP___DEPLOYS_xor_CALL_PRC_SUCCESS_CALLER_WILL_REVERT_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO") List<Boolean> hasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero,
  @JsonProperty("HAS_CODE_xor_MMU___INFO_xor_CALL_PRC_FAILURE_CALLER_WONT_REVERT_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO") List<Boolean> hasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero,
  @JsonProperty("HASH_INFO_STAMP") List<BigInteger> hashInfoStamp,
  @JsonProperty("HUB_STAMP") List<BigInteger> hubStamp,
  @JsonProperty("HUB_STAMP_TRANSACTION_END") List<BigInteger> hubStampTransactionEnd,
  @JsonProperty("IS_BLAKE2f_xor_MXP___FLAG_xor_CALL_PRC_SUCCESS_CALLER_WONT_REVERT_xor_CREATE_FLAG_xor_WARM") List<Boolean> isBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm,
  @JsonProperty("IS_ECADD_xor_MXP___MXPX_xor_CALL_SMC_FAILURE_CALLER_WILL_REVERT_xor_DECODED_FLAG_1_xor_WARM_NEW") List<Boolean> isEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew,
  @JsonProperty("IS_ECMUL_xor_OOB___EVENT_1_xor_CALL_SMC_FAILURE_CALLER_WONT_REVERT_xor_DECODED_FLAG_2") List<Boolean> isEcmulXorOobEvent1XorCallSmcFailureCallerWontRevertXorDecodedFlag2,
  @JsonProperty("IS_ECPAIRING_xor_OOB___EVENT_2_xor_CALL_SMC_SUCCESS_CALLER_WILL_REVERT_xor_DECODED_FLAG_3") List<Boolean> isEcpairingXorOobEvent2XorCallSmcSuccessCallerWillRevertXorDecodedFlag3,
  @JsonProperty("IS_ECRECOVER_xor_OOB___FLAG_xor_CALL_SMC_SUCCESS_CALLER_WONT_REVERT_xor_DECODED_FLAG_4") List<Boolean> isEcrecoverXorOobFlagXorCallSmcSuccessCallerWontRevertXorDecodedFlag4,
  @JsonProperty("IS_IDENTITY_xor_PRECINFO___FLAG_xor_CODEDEPOSIT_xor_DUP_FLAG") List<Boolean> isIdentityXorPrecinfoFlagXorCodedepositXorDupFlag,
  @JsonProperty("IS_MODEXP_xor_STP___EXISTS_xor_CODEDEPOSIT_INVALID_CODE_PREFIX_xor_EXT_FLAG") List<Boolean> isModexpXorStpExistsXorCodedepositInvalidCodePrefixXorExtFlag,
  @JsonProperty("IS_PRECOMPILE_xor_STP___FLAG_xor_CODEDEPOSIT_VALID_CODE_PREFIX_xor_HALT_FLAG") List<Boolean> isPrecompileXorStpFlagXorCodedepositValidCodePrefixXorHaltFlag,
  @JsonProperty("IS_RIPEMDsub160_xor_STP___OOGX_xor_ECADD_xor_HASH_INFO_FLAG") List<Boolean> isRipemDsub160XorStpOogxXorEcaddXorHashInfoFlag,
  @JsonProperty("IS_SHA2sub256_xor_STP___WARM_xor_ECMUL_xor_INVALID_FLAG") List<Boolean> isSha2Sub256XorStpWarmXorEcmulXorInvalidFlag,
  @JsonProperty("MMU_STAMP") List<BigInteger> mmuStamp,
  @JsonProperty("MXP___SIZE_1_HI_xor_STACK_ITEM_VALUE_LO_2") List<BigInteger> mxpSize1HiXorStackItemValueLo2,
  @JsonProperty("MXP___SIZE_1_LO_xor_STACK_ITEM_VALUE_LO_3") List<BigInteger> mxpSize1LoXorStackItemValueLo3,
  @JsonProperty("MXP___SIZE_2_HI_xor_STACK_ITEM_VALUE_LO_4") List<BigInteger> mxpSize2HiXorStackItemValueLo4,
  @JsonProperty("MXP___SIZE_2_LO_xor_STATIC_GAS") List<BigInteger> mxpSize2LoXorStaticGas,
  @JsonProperty("MXP_STAMP") List<BigInteger> mxpStamp,
  @JsonProperty("MXP___WORDS") List<BigInteger> mxpWords,
  @JsonProperty("NONCE_NEW_xor_CONTEXT_NUMBER_xor_MMU___SIZE_xor_STACK_ITEM_STAMP_1_xor_NONCE") List<BigInteger> nonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce,
  @JsonProperty("NONCE_xor_CALL_VALUE_xor_MMU___RETURNER_xor_STACK_ITEM_HEIGHT_4_xor_LEFTOVER_GAS") List<BigInteger> nonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas,
  @JsonProperty("NUMBER_OF_NON_STACK_ROWS") List<BigInteger> numberOfNonStackRows,
  @JsonProperty("OOB___INST") List<BigInteger> oobInst,
  @JsonProperty("OOB___OUTGOING_DATA_1") List<BigInteger> oobOutgoingData1,
  @JsonProperty("OOB___OUTGOING_DATA_2") List<BigInteger> oobOutgoingData2,
  @JsonProperty("OOB___OUTGOING_DATA_3") List<BigInteger> oobOutgoingData3,
  @JsonProperty("OOB___OUTGOING_DATA_4") List<BigInteger> oobOutgoingData4,
  @JsonProperty("OOB___OUTGOING_DATA_5") List<BigInteger> oobOutgoingData5,
  @JsonProperty("OOB___OUTGOING_DATA_6") List<BigInteger> oobOutgoingData6,
  @JsonProperty("PEEK_AT_ACCOUNT") List<Boolean> peekAtAccount,
  @JsonProperty("PEEK_AT_CONTEXT") List<Boolean> peekAtContext,
  @JsonProperty("PEEK_AT_MISCELLANEOUS") List<Boolean> peekAtMiscellaneous,
  @JsonProperty("PEEK_AT_SCENARIO") List<Boolean> peekAtScenario,
  @JsonProperty("PEEK_AT_STACK") List<Boolean> peekAtStack,
  @JsonProperty("PEEK_AT_STORAGE") List<Boolean> peekAtStorage,
  @JsonProperty("PEEK_AT_TRANSACTION") List<Boolean> peekAtTransaction,
  @JsonProperty("PRECINFO___ADDR_LO") List<BigInteger> precinfoAddrLo,
  @JsonProperty("PRECINFO___CDS") List<BigInteger> precinfoCds,
  @JsonProperty("PRECINFO___EXEC_COST") List<BigInteger> precinfoExecCost,
  @JsonProperty("PRECINFO___PROVIDES_RETURN_DATA") List<BigInteger> precinfoProvidesReturnData,
  @JsonProperty("PRECINFO___RDS") List<BigInteger> precinfoRds,
  @JsonProperty("PRECINFO___SUCCESS") List<BigInteger> precinfoSuccess,
  @JsonProperty("PRECINFO___TOUCHES_RAM") List<BigInteger> precinfoTouchesRam,
  @JsonProperty("PROGRAM_COUNTER") List<BigInteger> programCounter,
  @JsonProperty("PROGRAM_COUNTER_NEW") List<BigInteger> programCounterNew,
  @JsonProperty("PUSHPOP_FLAG") List<Boolean> pushpopFlag,
  @JsonProperty("RDCX") List<Boolean> rdcx,
  @JsonProperty("RIPEMDsub160_xor_KEC_FLAG") List<Boolean> ripemDsub160XorKecFlag,
  @JsonProperty("RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_MMU___STACK_VAL_HI_xor_STACK_ITEM_STAMP_2_xor_TO_ADDRESS_HI") List<BigInteger> rlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi,
  @JsonProperty("RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_MMU___STACK_VAL_LO_xor_STACK_ITEM_STAMP_3_xor_TO_ADDRESS_LO") List<BigInteger> rlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo,
  @JsonProperty("RLPADDR___FLAG_xor_ECPAIRING_xor_INVPREX") List<Boolean> rlpaddrFlagXorEcpairingXorInvprex,
  @JsonProperty("RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_MXP___GAS_MXP_xor_STACK_ITEM_STAMP_4_xor_VALUE") List<BigInteger> rlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue,
  @JsonProperty("RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_MXP___INST_xor_STACK_ITEM_VALUE_HI_1") List<BigInteger> rlpaddrKecLoXorReturnAtOffsetXorMxpInstXorStackItemValueHi1,
  @JsonProperty("RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_MXP___OFFSET_1_HI_xor_STACK_ITEM_VALUE_HI_2") List<BigInteger> rlpaddrRecipeXorReturnAtSizeXorMxpOffset1HiXorStackItemValueHi2,
  @JsonProperty("RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_MXP___OFFSET_1_LO_xor_STACK_ITEM_VALUE_HI_3") List<BigInteger> rlpaddrSaltHiXorReturnDataOffsetXorMxpOffset1LoXorStackItemValueHi3,
  @JsonProperty("RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_MXP___OFFSET_2_HI_xor_STACK_ITEM_VALUE_HI_4") List<BigInteger> rlpaddrSaltLoXorReturnDataSizeXorMxpOffset2HiXorStackItemValueHi4,
  @JsonProperty("SCN_FAILURE_1_xor_LOG_FLAG") List<Boolean> scnFailure1XorLogFlag,
  @JsonProperty("SCN_FAILURE_2_xor_MACHINE_STATE_FLAG") List<Boolean> scnFailure2XorMachineStateFlag,
  @JsonProperty("SCN_FAILURE_3_xor_MAXCSX") List<Boolean> scnFailure3XorMaxcsx,
  @JsonProperty("SCN_FAILURE_4_xor_MOD_FLAG") List<Boolean> scnFailure4XorModFlag,
  @JsonProperty("SCN_SUCCESS_1_xor_MUL_FLAG") List<Boolean> scnSuccess1XorMulFlag,
  @JsonProperty("SCN_SUCCESS_2_xor_MXPX") List<Boolean> scnSuccess2XorMxpx,
  @JsonProperty("SCN_SUCCESS_3_xor_MXP_FLAG") List<Boolean> scnSuccess3XorMxpFlag,
  @JsonProperty("SCN_SUCCESS_4_xor_OOB_FLAG") List<Boolean> scnSuccess4XorOobFlag,
  @JsonProperty("SELFDESTRUCT_xor_OOGX") List<Boolean> selfdestructXorOogx,
  @JsonProperty("SHA2sub256_xor_OPCX") List<Boolean> sha2Sub256XorOpcx,
  @JsonProperty("SHF_FLAG") List<Boolean> shfFlag,
  @JsonProperty("SOX") List<Boolean> sox,
  @JsonProperty("SSTOREX") List<Boolean> sstorex,
  @JsonProperty("STACK_ITEM_POP_1") List<Boolean> stackItemPop1,
  @JsonProperty("STACK_ITEM_POP_2") List<Boolean> stackItemPop2,
  @JsonProperty("STACK_ITEM_POP_3") List<Boolean> stackItemPop3,
  @JsonProperty("STACK_ITEM_POP_4") List<Boolean> stackItemPop4,
  @JsonProperty("STACKRAM_FLAG") List<Boolean> stackramFlag,
  @JsonProperty("STATIC_FLAG") List<Boolean> staticFlag,
  @JsonProperty("STATICX") List<Boolean> staticx,
  @JsonProperty("STO_FLAG") List<Boolean> stoFlag,
  @JsonProperty("STP___GAS_HI") List<BigInteger> stpGasHi,
  @JsonProperty("STP___GAS_LO") List<BigInteger> stpGasLo,
  @JsonProperty("STP___GAS_OOPKT") List<BigInteger> stpGasOopkt,
  @JsonProperty("STP___GAS_STPD") List<BigInteger> stpGasStpd,
  @JsonProperty("STP___INST") List<BigInteger> stpInst,
  @JsonProperty("STP___VAL_HI") List<BigInteger> stpValHi,
  @JsonProperty("STP___VAL_LO") List<BigInteger> stpValLo,
  @JsonProperty("SUB_STAMP") List<BigInteger> subStamp,
  @JsonProperty("SUX") List<Boolean> sux,
  @JsonProperty("SWAP_FLAG") List<Boolean> swapFlag,
  @JsonProperty("TRANSACTION_REVERTS") List<Boolean> transactionReverts,
  @JsonProperty("TRM_FLAG") List<Boolean> trmFlag,
  @JsonProperty("TRM___FLAG_xor_ECRECOVER_xor_JUMPX") List<Boolean> trmFlagXorEcrecoverXorJumpx,
  @JsonProperty("TRM___RAW_ADDR_HI_xor_MXP___OFFSET_2_LO_xor_STACK_ITEM_VALUE_LO_1") List<BigInteger> trmRawAddrHiXorMxpOffset2LoXorStackItemValueLo1,
  @JsonProperty("TWO_LINE_INSTRUCTION") List<Boolean> twoLineInstruction,
  @JsonProperty("TX_EXEC") List<Boolean> txExec,
  @JsonProperty("TX_FINL") List<Boolean> txFinl,
  @JsonProperty("TX_INIT") List<Boolean> txInit,
  @JsonProperty("TX_SKIP") List<Boolean> txSkip,
  @JsonProperty("TX_WARM") List<Boolean> txWarm,
  @JsonProperty("TXN_FLAG") List<Boolean> txnFlag,
  @JsonProperty("WARM_NEW_xor_MODEXP_xor_JUMP_FLAG") List<Boolean> warmNewXorModexpXorJumpFlag,
  @JsonProperty("WARM_xor_IDENTITY_xor_JUMP_DESTINATION_VETTING_REQUIRED") List<Boolean> warmXorIdentityXorJumpDestinationVettingRequired,
  @JsonProperty("WCP_FLAG") List<Boolean> wcpFlag) { 


  public int size() {
      return this.absoluteTransactionNumber.size();
  }

  public static List<ColumnHeader> headers(int size) {
    return List.of(
      new ColumnHeader("hub.ABSOLUTE_TRANSACTION_NUMBER", 32, size),
      new ColumnHeader("hub.ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_CCRS_STAMP_xor_HASH_INFO___KEC_HI_xor_ADDRESS_HI_xor_BASEFEE", 32, size),
      new ColumnHeader("hub.ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_EXP___DYNCOST_xor_HASH_INFO___KEC_LO_xor_ADDRESS_LO_xor_CALL_DATA_SIZE", 32, size),
      new ColumnHeader("hub.BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_EXP___EXPONENT_LO_xor_HEIGHT_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO", 32, size),
      new ColumnHeader("hub.BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_EXP___EXPONENT_HI_xor_HASH_INFO___SIZE_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI", 32, size),
      new ColumnHeader("hub.BATCH_NUMBER", 32, size),
      new ColumnHeader("hub.CALLER_CONTEXT_NUMBER", 32, size),
      new ColumnHeader("hub.CODE_ADDRESS_HI", 32, size),
      new ColumnHeader("hub.CODE_ADDRESS_LO", 32, size),
      new ColumnHeader("hub.CODE_DEPLOYMENT_NUMBER", 32, size),
      new ColumnHeader("hub.CODE_DEPLOYMENT_STATUS", 1, size),
      new ColumnHeader("hub.CODE_FRAGMENT_INDEX", 32, size),
      new ColumnHeader("hub.CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_MMU___INST_xor_HEIGHT_OVER_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO", 32, size),
      new ColumnHeader("hub.CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_MMU___EXO_SUM_xor_HEIGHT_NEW_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI", 32, size),
      new ColumnHeader("hub.CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_MMU___OFFSET_2_HI_xor_INST_xor_VAL_NEXT_HI_xor_GAS_PRICE", 32, size),
      new ColumnHeader("hub.CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_MMU___OFFSET_1_LO_xor_HEIGHT_UNDER_xor_VAL_CURR_LO_xor_GAS_LIMIT", 32, size),
      new ColumnHeader("hub.CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_MMU___PARAM_1_xor_PUSH_VALUE_LO_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL", 32, size),
      new ColumnHeader("hub.CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_MMU___OFFSET_2_LO_xor_PUSH_VALUE_HI_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT", 32, size),
      new ColumnHeader("hub.CONTEXT_GETS_REVERTED_FLAG", 1, size),
      new ColumnHeader("hub.CONTEXT_MAY_CHANGE_FLAG", 1, size),
      new ColumnHeader("hub.CONTEXT_NUMBER", 32, size),
      new ColumnHeader("hub.CONTEXT_NUMBER_NEW", 32, size),
      new ColumnHeader("hub.CONTEXT_REVERT_STAMP", 32, size),
      new ColumnHeader("hub.CONTEXT_SELF_REVERTS_FLAG", 1, size),
      new ColumnHeader("hub.CONTEXT_WILL_REVERT_FLAG", 1, size),
      new ColumnHeader("hub.COUNTER_NSR", 32, size),
      new ColumnHeader("hub.COUNTER_TLI", 1, size),
      new ColumnHeader("hub.DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_MMU___REF_SIZE_xor_STACK_ITEM_HEIGHT_3_xor_INIT_GAS", 32, size),
      new ColumnHeader("hub.DEP_NUM_xor_CALL_DATA_SIZE_xor_MMU___REF_OFFSET_xor_STACK_ITEM_HEIGHT_2_xor_INIT_CODE_SIZE", 32, size),
      new ColumnHeader("hub.DEP_STATUS_NEW_xor_EXP___FLAG_xor_CALL_EOA_SUCCESS_CALLER_WILL_REVERT_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE", 1, size),
      new ColumnHeader("hub.DEP_STATUS_xor_CCSR_FLAG_xor_CALL_ABORT_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559", 1, size),
      new ColumnHeader("hub.DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_MMU___PARAM_2_xor_STACK_ITEM_HEIGHT_1_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE", 32, size),
      new ColumnHeader("hub.DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ABORT_FLAG_xor_BLAKE2f_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT", 1, size),
      new ColumnHeader("hub.DOM_STAMP", 32, size),
      new ColumnHeader("hub.EXCEPTION_AHOY_FLAG", 1, size),
      new ColumnHeader("hub.EXISTS_NEW_xor_MMU___FLAG_xor_CALL_PRC_FAILURE_CALLER_WILL_REVERT_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG", 1, size),
      new ColumnHeader("hub.EXISTS_xor_FCOND_FLAG_xor_CALL_EOA_SUCCESS_CALLER_WONT_REVERT_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION", 1, size),
      new ColumnHeader("hub.GAS_ACTUAL", 32, size),
      new ColumnHeader("hub.GAS_COST", 32, size),
      new ColumnHeader("hub.GAS_EXPECTED", 32, size),
      new ColumnHeader("hub.GAS_NEXT", 32, size),
      new ColumnHeader("hub.GAS_REFUND", 32, size),
      new ColumnHeader("hub.GAS_REFUND_NEW", 32, size),
      new ColumnHeader("hub.HAS_CODE_NEW_xor_MXP___DEPLOYS_xor_CALL_PRC_SUCCESS_CALLER_WILL_REVERT_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO", 1, size),
      new ColumnHeader("hub.HAS_CODE_xor_MMU___INFO_xor_CALL_PRC_FAILURE_CALLER_WONT_REVERT_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO", 1, size),
      new ColumnHeader("hub.HASH_INFO_STAMP", 32, size),
      new ColumnHeader("hub.HUB_STAMP", 32, size),
      new ColumnHeader("hub.HUB_STAMP_TRANSACTION_END", 32, size),
      new ColumnHeader("hub.IS_BLAKE2f_xor_MXP___FLAG_xor_CALL_PRC_SUCCESS_CALLER_WONT_REVERT_xor_CREATE_FLAG_xor_WARM", 1, size),
      new ColumnHeader("hub.IS_ECADD_xor_MXP___MXPX_xor_CALL_SMC_FAILURE_CALLER_WILL_REVERT_xor_DECODED_FLAG_1_xor_WARM_NEW", 1, size),
      new ColumnHeader("hub.IS_ECMUL_xor_OOB___EVENT_1_xor_CALL_SMC_FAILURE_CALLER_WONT_REVERT_xor_DECODED_FLAG_2", 1, size),
      new ColumnHeader("hub.IS_ECPAIRING_xor_OOB___EVENT_2_xor_CALL_SMC_SUCCESS_CALLER_WILL_REVERT_xor_DECODED_FLAG_3", 1, size),
      new ColumnHeader("hub.IS_ECRECOVER_xor_OOB___FLAG_xor_CALL_SMC_SUCCESS_CALLER_WONT_REVERT_xor_DECODED_FLAG_4", 1, size),
      new ColumnHeader("hub.IS_IDENTITY_xor_PRECINFO___FLAG_xor_CODEDEPOSIT_xor_DUP_FLAG", 1, size),
      new ColumnHeader("hub.IS_MODEXP_xor_STP___EXISTS_xor_CODEDEPOSIT_INVALID_CODE_PREFIX_xor_EXT_FLAG", 1, size),
      new ColumnHeader("hub.IS_PRECOMPILE_xor_STP___FLAG_xor_CODEDEPOSIT_VALID_CODE_PREFIX_xor_HALT_FLAG", 1, size),
      new ColumnHeader("hub.IS_RIPEMDsub160_xor_STP___OOGX_xor_ECADD_xor_HASH_INFO_FLAG", 1, size),
      new ColumnHeader("hub.IS_SHA2sub256_xor_STP___WARM_xor_ECMUL_xor_INVALID_FLAG", 1, size),
      new ColumnHeader("hub.MMU_STAMP", 32, size),
      new ColumnHeader("hub.MXP___SIZE_1_HI_xor_STACK_ITEM_VALUE_LO_2", 32, size),
      new ColumnHeader("hub.MXP___SIZE_1_LO_xor_STACK_ITEM_VALUE_LO_3", 32, size),
      new ColumnHeader("hub.MXP___SIZE_2_HI_xor_STACK_ITEM_VALUE_LO_4", 32, size),
      new ColumnHeader("hub.MXP___SIZE_2_LO_xor_STATIC_GAS", 32, size),
      new ColumnHeader("hub.MXP_STAMP", 32, size),
      new ColumnHeader("hub.MXP___WORDS", 32, size),
      new ColumnHeader("hub.NONCE_NEW_xor_CONTEXT_NUMBER_xor_MMU___SIZE_xor_STACK_ITEM_STAMP_1_xor_NONCE", 32, size),
      new ColumnHeader("hub.NONCE_xor_CALL_VALUE_xor_MMU___RETURNER_xor_STACK_ITEM_HEIGHT_4_xor_LEFTOVER_GAS", 32, size),
      new ColumnHeader("hub.NUMBER_OF_NON_STACK_ROWS", 32, size),
      new ColumnHeader("hub.OOB___INST", 32, size),
      new ColumnHeader("hub.OOB___OUTGOING_DATA_1", 32, size),
      new ColumnHeader("hub.OOB___OUTGOING_DATA_2", 32, size),
      new ColumnHeader("hub.OOB___OUTGOING_DATA_3", 32, size),
      new ColumnHeader("hub.OOB___OUTGOING_DATA_4", 32, size),
      new ColumnHeader("hub.OOB___OUTGOING_DATA_5", 32, size),
      new ColumnHeader("hub.OOB___OUTGOING_DATA_6", 32, size),
      new ColumnHeader("hub.PEEK_AT_ACCOUNT", 1, size),
      new ColumnHeader("hub.PEEK_AT_CONTEXT", 1, size),
      new ColumnHeader("hub.PEEK_AT_MISCELLANEOUS", 1, size),
      new ColumnHeader("hub.PEEK_AT_SCENARIO", 1, size),
      new ColumnHeader("hub.PEEK_AT_STACK", 1, size),
      new ColumnHeader("hub.PEEK_AT_STORAGE", 1, size),
      new ColumnHeader("hub.PEEK_AT_TRANSACTION", 1, size),
      new ColumnHeader("hub.PRECINFO___ADDR_LO", 32, size),
      new ColumnHeader("hub.PRECINFO___CDS", 32, size),
      new ColumnHeader("hub.PRECINFO___EXEC_COST", 32, size),
      new ColumnHeader("hub.PRECINFO___PROVIDES_RETURN_DATA", 32, size),
      new ColumnHeader("hub.PRECINFO___RDS", 32, size),
      new ColumnHeader("hub.PRECINFO___SUCCESS", 32, size),
      new ColumnHeader("hub.PRECINFO___TOUCHES_RAM", 32, size),
      new ColumnHeader("hub.PROGRAM_COUNTER", 32, size),
      new ColumnHeader("hub.PROGRAM_COUNTER_NEW", 32, size),
      new ColumnHeader("hub.PUSHPOP_FLAG", 1, size),
      new ColumnHeader("hub.RDCX", 1, size),
      new ColumnHeader("hub.RIPEMDsub160_xor_KEC_FLAG", 1, size),
      new ColumnHeader("hub.RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_MMU___STACK_VAL_HI_xor_STACK_ITEM_STAMP_2_xor_TO_ADDRESS_HI", 32, size),
      new ColumnHeader("hub.RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_MMU___STACK_VAL_LO_xor_STACK_ITEM_STAMP_3_xor_TO_ADDRESS_LO", 32, size),
      new ColumnHeader("hub.RLPADDR___FLAG_xor_ECPAIRING_xor_INVPREX", 1, size),
      new ColumnHeader("hub.RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_MXP___GAS_MXP_xor_STACK_ITEM_STAMP_4_xor_VALUE", 32, size),
      new ColumnHeader("hub.RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_MXP___INST_xor_STACK_ITEM_VALUE_HI_1", 32, size),
      new ColumnHeader("hub.RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_MXP___OFFSET_1_HI_xor_STACK_ITEM_VALUE_HI_2", 32, size),
      new ColumnHeader("hub.RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_MXP___OFFSET_1_LO_xor_STACK_ITEM_VALUE_HI_3", 32, size),
      new ColumnHeader("hub.RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_MXP___OFFSET_2_HI_xor_STACK_ITEM_VALUE_HI_4", 32, size),
      new ColumnHeader("hub.SCN_FAILURE_1_xor_LOG_FLAG", 1, size),
      new ColumnHeader("hub.SCN_FAILURE_2_xor_MACHINE_STATE_FLAG", 1, size),
      new ColumnHeader("hub.SCN_FAILURE_3_xor_MAXCSX", 1, size),
      new ColumnHeader("hub.SCN_FAILURE_4_xor_MOD_FLAG", 1, size),
      new ColumnHeader("hub.SCN_SUCCESS_1_xor_MUL_FLAG", 1, size),
      new ColumnHeader("hub.SCN_SUCCESS_2_xor_MXPX", 1, size),
      new ColumnHeader("hub.SCN_SUCCESS_3_xor_MXP_FLAG", 1, size),
      new ColumnHeader("hub.SCN_SUCCESS_4_xor_OOB_FLAG", 1, size),
      new ColumnHeader("hub.SELFDESTRUCT_xor_OOGX", 1, size),
      new ColumnHeader("hub.SHA2sub256_xor_OPCX", 1, size),
      new ColumnHeader("hub.SHF_FLAG", 1, size),
      new ColumnHeader("hub.SOX", 1, size),
      new ColumnHeader("hub.SSTOREX", 1, size),
      new ColumnHeader("hub.STACK_ITEM_POP_1", 1, size),
      new ColumnHeader("hub.STACK_ITEM_POP_2", 1, size),
      new ColumnHeader("hub.STACK_ITEM_POP_3", 1, size),
      new ColumnHeader("hub.STACK_ITEM_POP_4", 1, size),
      new ColumnHeader("hub.STACKRAM_FLAG", 1, size),
      new ColumnHeader("hub.STATIC_FLAG", 1, size),
      new ColumnHeader("hub.STATICX", 1, size),
      new ColumnHeader("hub.STO_FLAG", 1, size),
      new ColumnHeader("hub.STP___GAS_HI", 32, size),
      new ColumnHeader("hub.STP___GAS_LO", 32, size),
      new ColumnHeader("hub.STP___GAS_OOPKT", 32, size),
      new ColumnHeader("hub.STP___GAS_STPD", 32, size),
      new ColumnHeader("hub.STP___INST", 32, size),
      new ColumnHeader("hub.STP___VAL_HI", 32, size),
      new ColumnHeader("hub.STP___VAL_LO", 32, size),
      new ColumnHeader("hub.SUB_STAMP", 32, size),
      new ColumnHeader("hub.SUX", 1, size),
      new ColumnHeader("hub.SWAP_FLAG", 1, size),
      new ColumnHeader("hub.TRANSACTION_REVERTS", 1, size),
      new ColumnHeader("hub.TRM_FLAG", 1, size),
      new ColumnHeader("hub.TRM___FLAG_xor_ECRECOVER_xor_JUMPX", 1, size),
      new ColumnHeader("hub.TRM___RAW_ADDR_HI_xor_MXP___OFFSET_2_LO_xor_STACK_ITEM_VALUE_LO_1", 32, size),
      new ColumnHeader("hub.TWO_LINE_INSTRUCTION", 1, size),
      new ColumnHeader("hub.TX_EXEC", 1, size),
      new ColumnHeader("hub.TX_FINL", 1, size),
      new ColumnHeader("hub.TX_INIT", 1, size),
      new ColumnHeader("hub.TX_SKIP", 1, size),
      new ColumnHeader("hub.TX_WARM", 1, size),
      new ColumnHeader("hub.TXN_FLAG", 1, size),
      new ColumnHeader("hub.WARM_NEW_xor_MODEXP_xor_JUMP_FLAG", 1, size),
      new ColumnHeader("hub.WARM_xor_IDENTITY_xor_JUMP_DESTINATION_VETTING_REQUIRED", 1, size),
      new ColumnHeader("hub.WCP_FLAG", 1, size)
      );
  }

  public static class TraceBuilder {
    private final BitSet filled = new BitSet();
    private final int length;
    private int currentLength = 0;
    private final ByteBuffer target;

    public TraceBuilder(ByteBuffer target, int length) {
      this.length = length;
      this.target = target;
    }


    private void writeabsoluteTransactionNumber(final BigInteger b) {
      this.target.put(0 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(final BigInteger b) {
      this.target.put(935 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(final BigInteger b) {
      this.target.put(967 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(final BigInteger b) {
      this.target.put(1031 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(final BigInteger b) {
      this.target.put(999 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writebatchNumber(final BigInteger b) {
      this.target.put(32 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecallerContextNumber(final BigInteger b) {
      this.target.put(64 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeAddressHi(final BigInteger b) {
      this.target.put(96 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeAddressLo(final BigInteger b) {
      this.target.put(128 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeDeploymentNumber(final BigInteger b) {
      this.target.put(160 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeDeploymentStatus(final Boolean b) {
      this.target.put(192 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writecodeFragmentIndex(final BigInteger b) {
      this.target.put(193 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(final BigInteger b) {
      this.target.put(1095 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(final BigInteger b) {
      this.target.put(1063 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(final BigInteger b) {
      this.target.put(1159 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(final BigInteger b) {
      this.target.put(1127 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(final BigInteger b) {
      this.target.put(1223 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(final BigInteger b) {
      this.target.put(1191 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecontextGetsRevertedFlag(final Boolean b) {
      this.target.put(225 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writecontextMayChangeFlag(final Boolean b) {
      this.target.put(226 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writecontextNumber(final BigInteger b) {
      this.target.put(227 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecontextNumberNew(final BigInteger b) {
      this.target.put(259 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecontextRevertStamp(final BigInteger b) {
      this.target.put(291 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecontextSelfRevertsFlag(final Boolean b) {
      this.target.put(323 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writecontextWillRevertFlag(final Boolean b) {
      this.target.put(324 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writecounterNsr(final BigInteger b) {
      this.target.put(325 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecounterTli(final Boolean b) {
      this.target.put(357 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writedepNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas(final BigInteger b) {
      this.target.put(1319 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writedepNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize(final BigInteger b) {
      this.target.put(1287 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(final Boolean b) {
      this.target.put(887 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(final Boolean b) {
      this.target.put(886 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(final BigInteger b) {
      this.target.put(1255 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(final Boolean b) {
      this.target.put(885 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writedomStamp(final BigInteger b) {
      this.target.put(358 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeexceptionAhoyFlag(final Boolean b) {
      this.target.put(390 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeexistsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig(final Boolean b) {
      this.target.put(889 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(final Boolean b) {
      this.target.put(888 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writegasActual(final BigInteger b) {
      this.target.put(391 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegasCost(final BigInteger b) {
      this.target.put(423 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegasExpected(final BigInteger b) {
      this.target.put(455 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegasNext(final BigInteger b) {
      this.target.put(487 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegasRefund(final BigInteger b) {
      this.target.put(519 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegasRefundNew(final BigInteger b) {
      this.target.put(551 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writehasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero(final Boolean b) {
      this.target.put(891 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writehasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero(final Boolean b) {
      this.target.put(890 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writehashInfoStamp(final BigInteger b) {
      this.target.put(583 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writehubStamp(final BigInteger b) {
      this.target.put(615 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writehubStampTransactionEnd(final BigInteger b) {
      this.target.put(647 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeisBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm(final Boolean b) {
      this.target.put(892 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew(final Boolean b) {
      this.target.put(893 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisEcmulXorOobEvent1XorCallSmcFailureCallerWontRevertXorDecodedFlag2(final Boolean b) {
      this.target.put(894 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisEcpairingXorOobEvent2XorCallSmcSuccessCallerWillRevertXorDecodedFlag3(final Boolean b) {
      this.target.put(895 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisEcrecoverXorOobFlagXorCallSmcSuccessCallerWontRevertXorDecodedFlag4(final Boolean b) {
      this.target.put(896 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisIdentityXorPrecinfoFlagXorCodedepositXorDupFlag(final Boolean b) {
      this.target.put(897 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisModexpXorStpExistsXorCodedepositInvalidCodePrefixXorExtFlag(final Boolean b) {
      this.target.put(898 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisPrecompileXorStpFlagXorCodedepositValidCodePrefixXorHaltFlag(final Boolean b) {
      this.target.put(899 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisRipemDsub160XorStpOogxXorEcaddXorHashInfoFlag(final Boolean b) {
      this.target.put(900 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisSha2Sub256XorStpWarmXorEcmulXorInvalidFlag(final Boolean b) {
      this.target.put(901 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writemmuStamp(final BigInteger b) {
      this.target.put(679 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpSize1HiXorStackItemValueLo2(final BigInteger b) {
      this.target.put(1671 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpSize1LoXorStackItemValueLo3(final BigInteger b) {
      this.target.put(1703 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpSize2HiXorStackItemValueLo4(final BigInteger b) {
      this.target.put(1735 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpSize2LoXorStaticGas(final BigInteger b) {
      this.target.put(1767 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpStamp(final BigInteger b) {
      this.target.put(711 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpWords(final BigInteger b) {
      this.target.put(1799 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writenonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce(final BigInteger b) {
      this.target.put(1383 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writenonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas(final BigInteger b) {
      this.target.put(1351 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writenumberOfNonStackRows(final BigInteger b) {
      this.target.put(743 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobInst(final BigInteger b) {
      this.target.put(1831 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobOutgoingData1(final BigInteger b) {
      this.target.put(1863 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobOutgoingData2(final BigInteger b) {
      this.target.put(1895 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobOutgoingData3(final BigInteger b) {
      this.target.put(1927 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobOutgoingData4(final BigInteger b) {
      this.target.put(1959 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobOutgoingData5(final BigInteger b) {
      this.target.put(1991 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoobOutgoingData6(final BigInteger b) {
      this.target.put(2023 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writepeekAtAccount(final Boolean b) {
      this.target.put(775 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepeekAtContext(final Boolean b) {
      this.target.put(776 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepeekAtMiscellaneous(final Boolean b) {
      this.target.put(777 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepeekAtScenario(final Boolean b) {
      this.target.put(778 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepeekAtStack(final Boolean b) {
      this.target.put(779 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepeekAtStorage(final Boolean b) {
      this.target.put(780 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepeekAtTransaction(final Boolean b) {
      this.target.put(781 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeprecinfoAddrLo(final BigInteger b) {
      this.target.put(2055 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprecinfoCds(final BigInteger b) {
      this.target.put(2087 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprecinfoExecCost(final BigInteger b) {
      this.target.put(2119 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprecinfoProvidesReturnData(final BigInteger b) {
      this.target.put(2151 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprecinfoRds(final BigInteger b) {
      this.target.put(2183 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprecinfoSuccess(final BigInteger b) {
      this.target.put(2215 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprecinfoTouchesRam(final BigInteger b) {
      this.target.put(2247 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprogramCounter(final BigInteger b) {
      this.target.put(782 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeprogramCounterNew(final BigInteger b) {
      this.target.put(814 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writepushpopFlag(final Boolean b) {
      this.target.put(917 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writerdcx(final Boolean b) {
      this.target.put(918 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeripemDsub160XorKecFlag(final Boolean b) {
      this.target.put(906 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writerlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi(final BigInteger b) {
      this.target.put(1415 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo(final BigInteger b) {
      this.target.put(1447 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerlpaddrFlagXorEcpairingXorInvprex(final Boolean b) {
      this.target.put(902 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writerlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue(final BigInteger b) {
      this.target.put(1479 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerlpaddrKecLoXorReturnAtOffsetXorMxpInstXorStackItemValueHi1(final BigInteger b) {
      this.target.put(1511 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerlpaddrRecipeXorReturnAtSizeXorMxpOffset1HiXorStackItemValueHi2(final BigInteger b) {
      this.target.put(1543 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerlpaddrSaltHiXorReturnDataOffsetXorMxpOffset1LoXorStackItemValueHi3(final BigInteger b) {
      this.target.put(1575 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerlpaddrSaltLoXorReturnDataSizeXorMxpOffset2HiXorStackItemValueHi4(final BigInteger b) {
      this.target.put(1607 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writescnFailure1XorLogFlag(final Boolean b) {
      this.target.put(907 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnFailure2XorMachineStateFlag(final Boolean b) {
      this.target.put(908 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnFailure3XorMaxcsx(final Boolean b) {
      this.target.put(909 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnFailure4XorModFlag(final Boolean b) {
      this.target.put(910 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnSuccess1XorMulFlag(final Boolean b) {
      this.target.put(911 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnSuccess2XorMxpx(final Boolean b) {
      this.target.put(912 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnSuccess3XorMxpFlag(final Boolean b) {
      this.target.put(913 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writescnSuccess4XorOobFlag(final Boolean b) {
      this.target.put(914 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeselfdestructXorOogx(final Boolean b) {
      this.target.put(915 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writesha2Sub256XorOpcx(final Boolean b) {
      this.target.put(916 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeshfFlag(final Boolean b) {
      this.target.put(919 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writesox(final Boolean b) {
      this.target.put(920 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writesstorex(final Boolean b) {
      this.target.put(921 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestackItemPop1(final Boolean b) {
      this.target.put(923 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestackItemPop2(final Boolean b) {
      this.target.put(924 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestackItemPop3(final Boolean b) {
      this.target.put(925 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestackItemPop4(final Boolean b) {
      this.target.put(926 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestackramFlag(final Boolean b) {
      this.target.put(922 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestaticFlag(final Boolean b) {
      this.target.put(928 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestaticx(final Boolean b) {
      this.target.put(927 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestoFlag(final Boolean b) {
      this.target.put(929 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writestpGasHi(final BigInteger b) {
      this.target.put(2279 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestpGasLo(final BigInteger b) {
      this.target.put(2311 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestpGasOopkt(final BigInteger b) {
      this.target.put(2343 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestpGasStpd(final BigInteger b) {
      this.target.put(2375 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestpInst(final BigInteger b) {
      this.target.put(2407 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestpValHi(final BigInteger b) {
      this.target.put(2439 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestpValLo(final BigInteger b) {
      this.target.put(2471 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writesubStamp(final BigInteger b) {
      this.target.put(846 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writesux(final Boolean b) {
      this.target.put(930 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeswapFlag(final Boolean b) {
      this.target.put(931 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetransactionReverts(final Boolean b) {
      this.target.put(878 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetrmFlag(final Boolean b) {
      this.target.put(932 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetrmFlagXorEcrecoverXorJumpx(final Boolean b) {
      this.target.put(903 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetrmRawAddrHiXorMxpOffset2LoXorStackItemValueLo1(final BigInteger b) {
      this.target.put(1639 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writetwoLineInstruction(final Boolean b) {
      this.target.put(879 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetxExec(final Boolean b) {
      this.target.put(880 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetxFinl(final Boolean b) {
      this.target.put(881 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetxInit(final Boolean b) {
      this.target.put(882 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetxSkip(final Boolean b) {
      this.target.put(883 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetxWarm(final Boolean b) {
      this.target.put(884 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writetxnFlag(final Boolean b) {
      this.target.put(933 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writewarmNewXorModexpXorJumpFlag(final Boolean b) {
      this.target.put(905 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writewarmXorIdentityXorJumpDestinationVettingRequired(final Boolean b) {
      this.target.put(904 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writewcpFlag(final Boolean b) {
      this.target.put(934 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }


    public TraceBuilder absoluteTransactionNumber(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ABSOLUTE_TRANSACTION_NUMBER already set");
      } else {
        filled.set(0);
      }

      this.writeabsoluteTransactionNumber(b);

      return this;
    }
    public TraceBuilder batchNumber(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("BATCH_NUMBER already set");
      } else {
        filled.set(1);
      }

      this.writebatchNumber(b);

      return this;
    }
    public TraceBuilder callerContextNumber(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER already set");
      } else {
        filled.set(2);
      }

      this.writecallerContextNumber(b);

      return this;
    }
    public TraceBuilder codeAddressHi(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("CODE_ADDRESS_HI already set");
      } else {
        filled.set(3);
      }

      this.writecodeAddressHi(b);

      return this;
    }
    public TraceBuilder codeAddressLo(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("CODE_ADDRESS_LO already set");
      } else {
        filled.set(4);
      }

      this.writecodeAddressLo(b);

      return this;
    }
    public TraceBuilder codeDeploymentNumber(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(5);
      }

      this.writecodeDeploymentNumber(b);

      return this;
    }
    public TraceBuilder codeDeploymentStatus(final Boolean b) {
      if (filled.get(6)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_STATUS already set");
      } else {
        filled.set(6);
      }

      this.writecodeDeploymentStatus(b);

      return this;
    }
    public TraceBuilder codeFragmentIndex(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
      } else {
        filled.set(7);
      }

      this.writecodeFragmentIndex(b);

      return this;
    }
    public TraceBuilder contextGetsRevertedFlag(final Boolean b) {
      if (filled.get(8)) {
        throw new IllegalStateException("CONTEXT_GETS_REVERTED_FLAG already set");
      } else {
        filled.set(8);
      }

      this.writecontextGetsRevertedFlag(b);

      return this;
    }
    public TraceBuilder contextMayChangeFlag(final Boolean b) {
      if (filled.get(9)) {
        throw new IllegalStateException("CONTEXT_MAY_CHANGE_FLAG already set");
      } else {
        filled.set(9);
      }

      this.writecontextMayChangeFlag(b);

      return this;
    }
    public TraceBuilder contextNumber(final BigInteger b) {
      if (filled.get(10)) {
        throw new IllegalStateException("CONTEXT_NUMBER already set");
      } else {
        filled.set(10);
      }

      this.writecontextNumber(b);

      return this;
    }
    public TraceBuilder contextNumberNew(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("CONTEXT_NUMBER_NEW already set");
      } else {
        filled.set(11);
      }

      this.writecontextNumberNew(b);

      return this;
    }
    public TraceBuilder contextRevertStamp(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("CONTEXT_REVERT_STAMP already set");
      } else {
        filled.set(12);
      }

      this.writecontextRevertStamp(b);

      return this;
    }
    public TraceBuilder contextSelfRevertsFlag(final Boolean b) {
      if (filled.get(13)) {
        throw new IllegalStateException("CONTEXT_SELF_REVERTS_FLAG already set");
      } else {
        filled.set(13);
      }

      this.writecontextSelfRevertsFlag(b);

      return this;
    }
    public TraceBuilder contextWillRevertFlag(final Boolean b) {
      if (filled.get(14)) {
        throw new IllegalStateException("CONTEXT_WILL_REVERT_FLAG already set");
      } else {
        filled.set(14);
      }

      this.writecontextWillRevertFlag(b);

      return this;
    }
    public TraceBuilder counterNsr(final BigInteger b) {
      if (filled.get(15)) {
        throw new IllegalStateException("COUNTER_NSR already set");
      } else {
        filled.set(15);
      }

      this.writecounterNsr(b);

      return this;
    }
    public TraceBuilder counterTli(final Boolean b) {
      if (filled.get(16)) {
        throw new IllegalStateException("COUNTER_TLI already set");
      } else {
        filled.set(16);
      }

      this.writecounterTli(b);

      return this;
    }
    public TraceBuilder domStamp(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("DOM_STAMP already set");
      } else {
        filled.set(17);
      }

      this.writedomStamp(b);

      return this;
    }
    public TraceBuilder exceptionAhoyFlag(final Boolean b) {
      if (filled.get(18)) {
        throw new IllegalStateException("EXCEPTION_AHOY_FLAG already set");
      } else {
        filled.set(18);
      }

      this.writeexceptionAhoyFlag(b);

      return this;
    }
    public TraceBuilder gasActual(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("GAS_ACTUAL already set");
      } else {
        filled.set(19);
      }

      this.writegasActual(b);

      return this;
    }
    public TraceBuilder gasCost(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("GAS_COST already set");
      } else {
        filled.set(20);
      }

      this.writegasCost(b);

      return this;
    }
    public TraceBuilder gasExpected(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("GAS_EXPECTED already set");
      } else {
        filled.set(21);
      }

      this.writegasExpected(b);

      return this;
    }
    public TraceBuilder gasNext(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("GAS_NEXT already set");
      } else {
        filled.set(22);
      }

      this.writegasNext(b);

      return this;
    }
    public TraceBuilder gasRefund(final BigInteger b) {
      if (filled.get(23)) {
        throw new IllegalStateException("GAS_REFUND already set");
      } else {
        filled.set(23);
      }

      this.writegasRefund(b);

      return this;
    }
    public TraceBuilder gasRefundNew(final BigInteger b) {
      if (filled.get(24)) {
        throw new IllegalStateException("GAS_REFUND_NEW already set");
      } else {
        filled.set(24);
      }

      this.writegasRefundNew(b);

      return this;
    }
    public TraceBuilder hashInfoStamp(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("HASH_INFO_STAMP already set");
      } else {
        filled.set(25);
      }

      this.writehashInfoStamp(b);

      return this;
    }
    public TraceBuilder hubStamp(final BigInteger b) {
      if (filled.get(26)) {
        throw new IllegalStateException("HUB_STAMP already set");
      } else {
        filled.set(26);
      }

      this.writehubStamp(b);

      return this;
    }
    public TraceBuilder hubStampTransactionEnd(final BigInteger b) {
      if (filled.get(27)) {
        throw new IllegalStateException("HUB_STAMP_TRANSACTION_END already set");
      } else {
        filled.set(27);
      }

      this.writehubStampTransactionEnd(b);

      return this;
    }
    public TraceBuilder mmuStamp(final BigInteger b) {
      if (filled.get(28)) {
        throw new IllegalStateException("MMU_STAMP already set");
      } else {
        filled.set(28);
      }

      this.writemmuStamp(b);

      return this;
    }
    public TraceBuilder mxpStamp(final BigInteger b) {
      if (filled.get(29)) {
        throw new IllegalStateException("MXP_STAMP already set");
      } else {
        filled.set(29);
      }

      this.writemxpStamp(b);

      return this;
    }
    public TraceBuilder numberOfNonStackRows(final BigInteger b) {
      if (filled.get(30)) {
        throw new IllegalStateException("NUMBER_OF_NON_STACK_ROWS already set");
      } else {
        filled.set(30);
      }

      this.writenumberOfNonStackRows(b);

      return this;
    }
    public TraceBuilder pAccountAddrHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("ADDR_HI already set");
      } else {
        filled.set(98);
      }

      this.writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(b);

      return this;
    }
    public TraceBuilder pAccountAddrLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("ADDR_LO already set");
      } else {
        filled.set(99);
      }

      this.writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(b);

      return this;
    }
    public TraceBuilder pAccountBalance(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("BALANCE already set");
      } else {
        filled.set(100);
      }

      this.writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(b);

      return this;
    }
    public TraceBuilder pAccountBalanceNew(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("BALANCE_NEW already set");
      } else {
        filled.set(101);
      }

      this.writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(b);

      return this;
    }
    public TraceBuilder pAccountCodeHashHi(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("CODE_HASH_HI already set");
      } else {
        filled.set(102);
      }

      this.writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(b);

      return this;
    }
    public TraceBuilder pAccountCodeHashHiNew(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("CODE_HASH_HI_NEW already set");
      } else {
        filled.set(103);
      }

      this.writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(b);

      return this;
    }
    public TraceBuilder pAccountCodeHashLo(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("CODE_HASH_LO already set");
      } else {
        filled.set(104);
      }

      this.writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(b);

      return this;
    }
    public TraceBuilder pAccountCodeHashLoNew(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("CODE_HASH_LO_NEW already set");
      } else {
        filled.set(105);
      }

      this.writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(b);

      return this;
    }
    public TraceBuilder pAccountCodeSize(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("CODE_SIZE already set");
      } else {
        filled.set(106);
      }

      this.writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(b);

      return this;
    }
    public TraceBuilder pAccountCodeSizeNew(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("CODE_SIZE_NEW already set");
      } else {
        filled.set(107);
      }

      this.writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(b);

      return this;
    }
    public TraceBuilder pAccountDepNum(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("DEP_NUM already set");
      } else {
        filled.set(109);
      }

      this.writedepNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize(b);

      return this;
    }
    public TraceBuilder pAccountDepNumNew(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("DEP_NUM_NEW already set");
      } else {
        filled.set(110);
      }

      this.writedepNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas(b);

      return this;
    }
    public TraceBuilder pAccountDepStatus(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("DEP_STATUS already set");
      } else {
        filled.set(49);
      }

      this.writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(b);

      return this;
    }
    public TraceBuilder pAccountDepStatusNew(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("DEP_STATUS_NEW already set");
      } else {
        filled.set(50);
      }

      this.writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(b);

      return this;
    }
    public TraceBuilder pAccountDeploymentNumberInfty(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER_INFTY already set");
      } else {
        filled.set(108);
      }

      this.writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(b);

      return this;
    }
    public TraceBuilder pAccountDeploymentStatusInfty(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("DEPLOYMENT_STATUS_INFTY already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pAccountExists(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("EXISTS already set");
      } else {
        filled.set(51);
      }

      this.writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(b);

      return this;
    }
    public TraceBuilder pAccountExistsNew(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("EXISTS_NEW already set");
      } else {
        filled.set(52);
      }

      this.writeexistsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig(b);

      return this;
    }
    public TraceBuilder pAccountHasCode(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("HAS_CODE already set");
      } else {
        filled.set(53);
      }

      this.writehasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero(b);

      return this;
    }
    public TraceBuilder pAccountHasCodeNew(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("HAS_CODE_NEW already set");
      } else {
        filled.set(54);
      }

      this.writehasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero(b);

      return this;
    }
    public TraceBuilder pAccountIsBlake2F(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("IS_BLAKE2f already set");
      } else {
        filled.set(55);
      }

      this.writeisBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm(b);

      return this;
    }
    public TraceBuilder pAccountIsEcadd(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("IS_ECADD already set");
      } else {
        filled.set(56);
      }

      this.writeisEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew(b);

      return this;
    }
    public TraceBuilder pAccountIsEcmul(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("IS_ECMUL already set");
      } else {
        filled.set(57);
      }

      this.writeisEcmulXorOobEvent1XorCallSmcFailureCallerWontRevertXorDecodedFlag2(b);

      return this;
    }
    public TraceBuilder pAccountIsEcpairing(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("IS_ECPAIRING already set");
      } else {
        filled.set(58);
      }

      this.writeisEcpairingXorOobEvent2XorCallSmcSuccessCallerWillRevertXorDecodedFlag3(b);

      return this;
    }
    public TraceBuilder pAccountIsEcrecover(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("IS_ECRECOVER already set");
      } else {
        filled.set(59);
      }

      this.writeisEcrecoverXorOobFlagXorCallSmcSuccessCallerWontRevertXorDecodedFlag4(b);

      return this;
    }
    public TraceBuilder pAccountIsIdentity(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("IS_IDENTITY already set");
      } else {
        filled.set(60);
      }

      this.writeisIdentityXorPrecinfoFlagXorCodedepositXorDupFlag(b);

      return this;
    }
    public TraceBuilder pAccountIsModexp(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("IS_MODEXP already set");
      } else {
        filled.set(61);
      }

      this.writeisModexpXorStpExistsXorCodedepositInvalidCodePrefixXorExtFlag(b);

      return this;
    }
    public TraceBuilder pAccountIsPrecompile(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("IS_PRECOMPILE already set");
      } else {
        filled.set(62);
      }

      this.writeisPrecompileXorStpFlagXorCodedepositValidCodePrefixXorHaltFlag(b);

      return this;
    }
    public TraceBuilder pAccountIsRipemd160(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("IS_RIPEMD-160 already set");
      } else {
        filled.set(63);
      }

      this.writeisRipemDsub160XorStpOogxXorEcaddXorHashInfoFlag(b);

      return this;
    }
    public TraceBuilder pAccountIsSha2256(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("IS_SHA2-256 already set");
      } else {
        filled.set(64);
      }

      this.writeisSha2Sub256XorStpWarmXorEcmulXorInvalidFlag(b);

      return this;
    }
    public TraceBuilder pAccountNonce(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("NONCE already set");
      } else {
        filled.set(111);
      }

      this.writenonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas(b);

      return this;
    }
    public TraceBuilder pAccountNonceNew(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("NONCE_NEW already set");
      } else {
        filled.set(112);
      }

      this.writenonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrDepAddrHi(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_HI already set");
      } else {
        filled.set(113);
      }

      this.writerlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrDepAddrLo(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_LO already set");
      } else {
        filled.set(114);
      }

      this.writerlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrFlag(final Boolean b) {
      if (filled.get(65)) {
        throw new IllegalStateException("RLPADDR___FLAG already set");
      } else {
        filled.set(65);
      }

      this.writerlpaddrFlagXorEcpairingXorInvprex(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrKecHi(final BigInteger b) {
      if (filled.get(115)) {
        throw new IllegalStateException("RLPADDR___KEC_HI already set");
      } else {
        filled.set(115);
      }

      this.writerlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrKecLo(final BigInteger b) {
      if (filled.get(116)) {
        throw new IllegalStateException("RLPADDR___KEC_LO already set");
      } else {
        filled.set(116);
      }

      this.writerlpaddrKecLoXorReturnAtOffsetXorMxpInstXorStackItemValueHi1(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrRecipe(final BigInteger b) {
      if (filled.get(117)) {
        throw new IllegalStateException("RLPADDR___RECIPE already set");
      } else {
        filled.set(117);
      }

      this.writerlpaddrRecipeXorReturnAtSizeXorMxpOffset1HiXorStackItemValueHi2(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrSaltHi(final BigInteger b) {
      if (filled.get(118)) {
        throw new IllegalStateException("RLPADDR___SALT_HI already set");
      } else {
        filled.set(118);
      }

      this.writerlpaddrSaltHiXorReturnDataOffsetXorMxpOffset1LoXorStackItemValueHi3(b);

      return this;
    }
    public TraceBuilder pAccountRlpaddrSaltLo(final BigInteger b) {
      if (filled.get(119)) {
        throw new IllegalStateException("RLPADDR___SALT_LO already set");
      } else {
        filled.set(119);
      }

      this.writerlpaddrSaltLoXorReturnDataSizeXorMxpOffset2HiXorStackItemValueHi4(b);

      return this;
    }
    public TraceBuilder pAccountTrmFlag(final Boolean b) {
      if (filled.get(66)) {
        throw new IllegalStateException("TRM___FLAG already set");
      } else {
        filled.set(66);
      }

      this.writetrmFlagXorEcrecoverXorJumpx(b);

      return this;
    }
    public TraceBuilder pAccountTrmRawAddrHi(final BigInteger b) {
      if (filled.get(120)) {
        throw new IllegalStateException("TRM___RAW_ADDR_HI already set");
      } else {
        filled.set(120);
      }

      this.writetrmRawAddrHiXorMxpOffset2LoXorStackItemValueLo1(b);

      return this;
    }
    public TraceBuilder pAccountWarm(final Boolean b) {
      if (filled.get(67)) {
        throw new IllegalStateException("WARM already set");
      } else {
        filled.set(67);
      }

      this.writewarmXorIdentityXorJumpDestinationVettingRequired(b);

      return this;
    }
    public TraceBuilder pAccountWarmNew(final Boolean b) {
      if (filled.get(68)) {
        throw new IllegalStateException("WARM_NEW already set");
      } else {
        filled.set(68);
      }

      this.writewarmNewXorModexpXorJumpFlag(b);

      return this;
    }
    public TraceBuilder pContextAccountAddressHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("ACCOUNT_ADDRESS_HI already set");
      } else {
        filled.set(98);
      }

      this.writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(b);

      return this;
    }
    public TraceBuilder pContextAccountAddressLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("ACCOUNT_ADDRESS_LO already set");
      } else {
        filled.set(99);
      }

      this.writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(b);

      return this;
    }
    public TraceBuilder pContextAccountDeploymentNumber(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("ACCOUNT_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(100);
      }

      this.writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(b);

      return this;
    }
    public TraceBuilder pContextByteCodeAddressHi(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("BYTE_CODE_ADDRESS_HI already set");
      } else {
        filled.set(101);
      }

      this.writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(b);

      return this;
    }
    public TraceBuilder pContextByteCodeAddressLo(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("BYTE_CODE_ADDRESS_LO already set");
      } else {
        filled.set(102);
      }

      this.writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(b);

      return this;
    }
    public TraceBuilder pContextByteCodeDeploymentNumber(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("BYTE_CODE_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(103);
      }

      this.writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(b);

      return this;
    }
    public TraceBuilder pContextByteCodeDeploymentStatus(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("BYTE_CODE_DEPLOYMENT_STATUS already set");
      } else {
        filled.set(104);
      }

      this.writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(b);

      return this;
    }
    public TraceBuilder pContextCallDataOffset(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("CALL_DATA_OFFSET already set");
      } else {
        filled.set(108);
      }

      this.writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(b);

      return this;
    }
    public TraceBuilder pContextCallDataSize(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("CALL_DATA_SIZE already set");
      } else {
        filled.set(109);
      }

      this.writedepNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize(b);

      return this;
    }
    public TraceBuilder pContextCallStackDepth(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("CALL_STACK_DEPTH already set");
      } else {
        filled.set(110);
      }

      this.writedepNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas(b);

      return this;
    }
    public TraceBuilder pContextCallValue(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("CALL_VALUE already set");
      } else {
        filled.set(111);
      }

      this.writenonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas(b);

      return this;
    }
    public TraceBuilder pContextCallerAddressHi(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("CALLER_ADDRESS_HI already set");
      } else {
        filled.set(105);
      }

      this.writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(b);

      return this;
    }
    public TraceBuilder pContextCallerAddressLo(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("CALLER_ADDRESS_LO already set");
      } else {
        filled.set(106);
      }

      this.writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(b);

      return this;
    }
    public TraceBuilder pContextCallerContextNumber(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER already set");
      } else {
        filled.set(107);
      }

      this.writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(b);

      return this;
    }
    public TraceBuilder pContextContextNumber(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("CONTEXT_NUMBER already set");
      } else {
        filled.set(112);
      }

      this.writenonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce(b);

      return this;
    }
    public TraceBuilder pContextIsStatic(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("IS_STATIC already set");
      } else {
        filled.set(113);
      }

      this.writerlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi(b);

      return this;
    }
    public TraceBuilder pContextReturnAtOffset(final BigInteger b) {
      if (filled.get(116)) {
        throw new IllegalStateException("RETURN_AT_OFFSET already set");
      } else {
        filled.set(116);
      }

      this.writerlpaddrKecLoXorReturnAtOffsetXorMxpInstXorStackItemValueHi1(b);

      return this;
    }
    public TraceBuilder pContextReturnAtSize(final BigInteger b) {
      if (filled.get(117)) {
        throw new IllegalStateException("RETURN_AT_SIZE already set");
      } else {
        filled.set(117);
      }

      this.writerlpaddrRecipeXorReturnAtSizeXorMxpOffset1HiXorStackItemValueHi2(b);

      return this;
    }
    public TraceBuilder pContextReturnDataOffset(final BigInteger b) {
      if (filled.get(118)) {
        throw new IllegalStateException("RETURN_DATA_OFFSET already set");
      } else {
        filled.set(118);
      }

      this.writerlpaddrSaltHiXorReturnDataOffsetXorMxpOffset1LoXorStackItemValueHi3(b);

      return this;
    }
    public TraceBuilder pContextReturnDataSize(final BigInteger b) {
      if (filled.get(119)) {
        throw new IllegalStateException("RETURN_DATA_SIZE already set");
      } else {
        filled.set(119);
      }

      this.writerlpaddrSaltLoXorReturnDataSizeXorMxpOffset2HiXorStackItemValueHi4(b);

      return this;
    }
    public TraceBuilder pContextReturnerContextNumber(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("RETURNER_CONTEXT_NUMBER already set");
      } else {
        filled.set(114);
      }

      this.writerlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo(b);

      return this;
    }
    public TraceBuilder pContextReturnerIsPrecompile(final BigInteger b) {
      if (filled.get(115)) {
        throw new IllegalStateException("RETURNER_IS_PRECOMPILE already set");
      } else {
        filled.set(115);
      }

      this.writerlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue(b);

      return this;
    }
    public TraceBuilder pContextUpdate(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("UPDATE already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pMiscellaneousAbortFlag(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("ABORT_FLAG already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pMiscellaneousCcrsStamp(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("CCRS_STAMP already set");
      } else {
        filled.set(98);
      }

      this.writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(b);

      return this;
    }
    public TraceBuilder pMiscellaneousCcsrFlag(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("CCSR_FLAG already set");
      } else {
        filled.set(49);
      }

      this.writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(b);

      return this;
    }
    public TraceBuilder pMiscellaneousExpDyncost(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("EXP___DYNCOST already set");
      } else {
        filled.set(99);
      }

      this.writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(b);

      return this;
    }
    public TraceBuilder pMiscellaneousExpExponentHi(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("EXP___EXPONENT_HI already set");
      } else {
        filled.set(100);
      }

      this.writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(b);

      return this;
    }
    public TraceBuilder pMiscellaneousExpExponentLo(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("EXP___EXPONENT_LO already set");
      } else {
        filled.set(101);
      }

      this.writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(b);

      return this;
    }
    public TraceBuilder pMiscellaneousExpFlag(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("EXP___FLAG already set");
      } else {
        filled.set(50);
      }

      this.writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(b);

      return this;
    }
    public TraceBuilder pMiscellaneousFcondFlag(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("FCOND_FLAG already set");
      } else {
        filled.set(51);
      }

      this.writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuExoSum(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("MMU___EXO_SUM already set");
      } else {
        filled.set(102);
      }

      this.writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuFlag(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("MMU___FLAG already set");
      } else {
        filled.set(52);
      }

      this.writeexistsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuInfo(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("MMU___INFO already set");
      } else {
        filled.set(53);
      }

      this.writehasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuInst(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("MMU___INST already set");
      } else {
        filled.set(103);
      }

      this.writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuOffset1Lo(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("MMU___OFFSET_1_LO already set");
      } else {
        filled.set(104);
      }

      this.writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuOffset2Hi(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("MMU___OFFSET_2_HI already set");
      } else {
        filled.set(105);
      }

      this.writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuOffset2Lo(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("MMU___OFFSET_2_LO already set");
      } else {
        filled.set(106);
      }

      this.writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuParam1(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("MMU___PARAM_1 already set");
      } else {
        filled.set(107);
      }

      this.writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuParam2(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("MMU___PARAM_2 already set");
      } else {
        filled.set(108);
      }

      this.writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuRefOffset(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("MMU___REF_OFFSET already set");
      } else {
        filled.set(109);
      }

      this.writedepNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuRefSize(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("MMU___REF_SIZE already set");
      } else {
        filled.set(110);
      }

      this.writedepNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuReturner(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("MMU___RETURNER already set");
      } else {
        filled.set(111);
      }

      this.writenonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuSize(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("MMU___SIZE already set");
      } else {
        filled.set(112);
      }

      this.writenonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuStackValHi(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("MMU___STACK_VAL_HI already set");
      } else {
        filled.set(113);
      }

      this.writerlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMmuStackValLo(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("MMU___STACK_VAL_LO already set");
      } else {
        filled.set(114);
      }

      this.writerlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpDeploys(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("MXP___DEPLOYS already set");
      } else {
        filled.set(54);
      }

      this.writehasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpFlag(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("MXP___FLAG already set");
      } else {
        filled.set(55);
      }

      this.writeisBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpGasMxp(final BigInteger b) {
      if (filled.get(115)) {
        throw new IllegalStateException("MXP___GAS_MXP already set");
      } else {
        filled.set(115);
      }

      this.writerlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpInst(final BigInteger b) {
      if (filled.get(116)) {
        throw new IllegalStateException("MXP___INST already set");
      } else {
        filled.set(116);
      }

      this.writerlpaddrKecLoXorReturnAtOffsetXorMxpInstXorStackItemValueHi1(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpMxpx(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("MXP___MXPX already set");
      } else {
        filled.set(56);
      }

      this.writeisEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpOffset1Hi(final BigInteger b) {
      if (filled.get(117)) {
        throw new IllegalStateException("MXP___OFFSET_1_HI already set");
      } else {
        filled.set(117);
      }

      this.writerlpaddrRecipeXorReturnAtSizeXorMxpOffset1HiXorStackItemValueHi2(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpOffset1Lo(final BigInteger b) {
      if (filled.get(118)) {
        throw new IllegalStateException("MXP___OFFSET_1_LO already set");
      } else {
        filled.set(118);
      }

      this.writerlpaddrSaltHiXorReturnDataOffsetXorMxpOffset1LoXorStackItemValueHi3(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpOffset2Hi(final BigInteger b) {
      if (filled.get(119)) {
        throw new IllegalStateException("MXP___OFFSET_2_HI already set");
      } else {
        filled.set(119);
      }

      this.writerlpaddrSaltLoXorReturnDataSizeXorMxpOffset2HiXorStackItemValueHi4(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpOffset2Lo(final BigInteger b) {
      if (filled.get(120)) {
        throw new IllegalStateException("MXP___OFFSET_2_LO already set");
      } else {
        filled.set(120);
      }

      this.writetrmRawAddrHiXorMxpOffset2LoXorStackItemValueLo1(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpSize1Hi(final BigInteger b) {
      if (filled.get(121)) {
        throw new IllegalStateException("MXP___SIZE_1_HI already set");
      } else {
        filled.set(121);
      }

      this.writemxpSize1HiXorStackItemValueLo2(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpSize1Lo(final BigInteger b) {
      if (filled.get(122)) {
        throw new IllegalStateException("MXP___SIZE_1_LO already set");
      } else {
        filled.set(122);
      }

      this.writemxpSize1LoXorStackItemValueLo3(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpSize2Hi(final BigInteger b) {
      if (filled.get(123)) {
        throw new IllegalStateException("MXP___SIZE_2_HI already set");
      } else {
        filled.set(123);
      }

      this.writemxpSize2HiXorStackItemValueLo4(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpSize2Lo(final BigInteger b) {
      if (filled.get(124)) {
        throw new IllegalStateException("MXP___SIZE_2_LO already set");
      } else {
        filled.set(124);
      }

      this.writemxpSize2LoXorStaticGas(b);

      return this;
    }
    public TraceBuilder pMiscellaneousMxpWords(final BigInteger b) {
      if (filled.get(125)) {
        throw new IllegalStateException("MXP___WORDS already set");
      } else {
        filled.set(125);
      }

      this.writemxpWords(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobEvent1(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("OOB___EVENT_1 already set");
      } else {
        filled.set(57);
      }

      this.writeisEcmulXorOobEvent1XorCallSmcFailureCallerWontRevertXorDecodedFlag2(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobEvent2(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("OOB___EVENT_2 already set");
      } else {
        filled.set(58);
      }

      this.writeisEcpairingXorOobEvent2XorCallSmcSuccessCallerWillRevertXorDecodedFlag3(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobFlag(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("OOB___FLAG already set");
      } else {
        filled.set(59);
      }

      this.writeisEcrecoverXorOobFlagXorCallSmcSuccessCallerWontRevertXorDecodedFlag4(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobInst(final BigInteger b) {
      if (filled.get(126)) {
        throw new IllegalStateException("OOB___INST already set");
      } else {
        filled.set(126);
      }

      this.writeoobInst(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobOutgoingData1(final BigInteger b) {
      if (filled.get(127)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_1 already set");
      } else {
        filled.set(127);
      }

      this.writeoobOutgoingData1(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobOutgoingData2(final BigInteger b) {
      if (filled.get(128)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_2 already set");
      } else {
        filled.set(128);
      }

      this.writeoobOutgoingData2(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobOutgoingData3(final BigInteger b) {
      if (filled.get(129)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_3 already set");
      } else {
        filled.set(129);
      }

      this.writeoobOutgoingData3(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobOutgoingData4(final BigInteger b) {
      if (filled.get(130)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_4 already set");
      } else {
        filled.set(130);
      }

      this.writeoobOutgoingData4(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobOutgoingData5(final BigInteger b) {
      if (filled.get(131)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_5 already set");
      } else {
        filled.set(131);
      }

      this.writeoobOutgoingData5(b);

      return this;
    }
    public TraceBuilder pMiscellaneousOobOutgoingData6(final BigInteger b) {
      if (filled.get(132)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_6 already set");
      } else {
        filled.set(132);
      }

      this.writeoobOutgoingData6(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoAddrLo(final BigInteger b) {
      if (filled.get(133)) {
        throw new IllegalStateException("PRECINFO___ADDR_LO already set");
      } else {
        filled.set(133);
      }

      this.writeprecinfoAddrLo(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoCds(final BigInteger b) {
      if (filled.get(134)) {
        throw new IllegalStateException("PRECINFO___CDS already set");
      } else {
        filled.set(134);
      }

      this.writeprecinfoCds(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoExecCost(final BigInteger b) {
      if (filled.get(135)) {
        throw new IllegalStateException("PRECINFO___EXEC_COST already set");
      } else {
        filled.set(135);
      }

      this.writeprecinfoExecCost(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoFlag(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("PRECINFO___FLAG already set");
      } else {
        filled.set(60);
      }

      this.writeisIdentityXorPrecinfoFlagXorCodedepositXorDupFlag(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoProvidesReturnData(final BigInteger b) {
      if (filled.get(136)) {
        throw new IllegalStateException("PRECINFO___PROVIDES_RETURN_DATA already set");
      } else {
        filled.set(136);
      }

      this.writeprecinfoProvidesReturnData(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoRds(final BigInteger b) {
      if (filled.get(137)) {
        throw new IllegalStateException("PRECINFO___RDS already set");
      } else {
        filled.set(137);
      }

      this.writeprecinfoRds(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoSuccess(final BigInteger b) {
      if (filled.get(138)) {
        throw new IllegalStateException("PRECINFO___SUCCESS already set");
      } else {
        filled.set(138);
      }

      this.writeprecinfoSuccess(b);

      return this;
    }
    public TraceBuilder pMiscellaneousPrecinfoTouchesRam(final BigInteger b) {
      if (filled.get(139)) {
        throw new IllegalStateException("PRECINFO___TOUCHES_RAM already set");
      } else {
        filled.set(139);
      }

      this.writeprecinfoTouchesRam(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpExists(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("STP___EXISTS already set");
      } else {
        filled.set(61);
      }

      this.writeisModexpXorStpExistsXorCodedepositInvalidCodePrefixXorExtFlag(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpFlag(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("STP___FLAG already set");
      } else {
        filled.set(62);
      }

      this.writeisPrecompileXorStpFlagXorCodedepositValidCodePrefixXorHaltFlag(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpGasHi(final BigInteger b) {
      if (filled.get(140)) {
        throw new IllegalStateException("STP___GAS_HI already set");
      } else {
        filled.set(140);
      }

      this.writestpGasHi(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpGasLo(final BigInteger b) {
      if (filled.get(141)) {
        throw new IllegalStateException("STP___GAS_LO already set");
      } else {
        filled.set(141);
      }

      this.writestpGasLo(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpGasOopkt(final BigInteger b) {
      if (filled.get(142)) {
        throw new IllegalStateException("STP___GAS_OOPKT already set");
      } else {
        filled.set(142);
      }

      this.writestpGasOopkt(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpGasStpd(final BigInteger b) {
      if (filled.get(143)) {
        throw new IllegalStateException("STP___GAS_STPD already set");
      } else {
        filled.set(143);
      }

      this.writestpGasStpd(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpInst(final BigInteger b) {
      if (filled.get(144)) {
        throw new IllegalStateException("STP___INST already set");
      } else {
        filled.set(144);
      }

      this.writestpInst(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpOogx(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("STP___OOGX already set");
      } else {
        filled.set(63);
      }

      this.writeisRipemDsub160XorStpOogxXorEcaddXorHashInfoFlag(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpValHi(final BigInteger b) {
      if (filled.get(145)) {
        throw new IllegalStateException("STP___VAL_HI already set");
      } else {
        filled.set(145);
      }

      this.writestpValHi(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpValLo(final BigInteger b) {
      if (filled.get(146)) {
        throw new IllegalStateException("STP___VAL_LO already set");
      } else {
        filled.set(146);
      }

      this.writestpValLo(b);

      return this;
    }
    public TraceBuilder pMiscellaneousStpWarm(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("STP___WARM already set");
      } else {
        filled.set(64);
      }

      this.writeisSha2Sub256XorStpWarmXorEcmulXorInvalidFlag(b);

      return this;
    }
    public TraceBuilder pScenarioBlake2F(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("BLAKE2f already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pScenarioCallAbort(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("CALL_ABORT already set");
      } else {
        filled.set(49);
      }

      this.writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(b);

      return this;
    }
    public TraceBuilder pScenarioCallEoaSuccessCallerWillRevert(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("CALL_EOA_SUCCESS_CALLER_WILL_REVERT already set");
      } else {
        filled.set(50);
      }

      this.writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(b);

      return this;
    }
    public TraceBuilder pScenarioCallEoaSuccessCallerWontRevert(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("CALL_EOA_SUCCESS_CALLER_WONT_REVERT already set");
      } else {
        filled.set(51);
      }

      this.writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(b);

      return this;
    }
    public TraceBuilder pScenarioCallPrcFailureCallerWillRevert(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("CALL_PRC_FAILURE_CALLER_WILL_REVERT already set");
      } else {
        filled.set(52);
      }

      this.writeexistsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig(b);

      return this;
    }
    public TraceBuilder pScenarioCallPrcFailureCallerWontRevert(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("CALL_PRC_FAILURE_CALLER_WONT_REVERT already set");
      } else {
        filled.set(53);
      }

      this.writehasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero(b);

      return this;
    }
    public TraceBuilder pScenarioCallPrcSuccessCallerWillRevert(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("CALL_PRC_SUCCESS_CALLER_WILL_REVERT already set");
      } else {
        filled.set(54);
      }

      this.writehasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero(b);

      return this;
    }
    public TraceBuilder pScenarioCallPrcSuccessCallerWontRevert(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("CALL_PRC_SUCCESS_CALLER_WONT_REVERT already set");
      } else {
        filled.set(55);
      }

      this.writeisBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm(b);

      return this;
    }
    public TraceBuilder pScenarioCallSmcFailureCallerWillRevert(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("CALL_SMC_FAILURE_CALLER_WILL_REVERT already set");
      } else {
        filled.set(56);
      }

      this.writeisEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew(b);

      return this;
    }
    public TraceBuilder pScenarioCallSmcFailureCallerWontRevert(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("CALL_SMC_FAILURE_CALLER_WONT_REVERT already set");
      } else {
        filled.set(57);
      }

      this.writeisEcmulXorOobEvent1XorCallSmcFailureCallerWontRevertXorDecodedFlag2(b);

      return this;
    }
    public TraceBuilder pScenarioCallSmcSuccessCallerWillRevert(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("CALL_SMC_SUCCESS_CALLER_WILL_REVERT already set");
      } else {
        filled.set(58);
      }

      this.writeisEcpairingXorOobEvent2XorCallSmcSuccessCallerWillRevertXorDecodedFlag3(b);

      return this;
    }
    public TraceBuilder pScenarioCallSmcSuccessCallerWontRevert(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("CALL_SMC_SUCCESS_CALLER_WONT_REVERT already set");
      } else {
        filled.set(59);
      }

      this.writeisEcrecoverXorOobFlagXorCallSmcSuccessCallerWontRevertXorDecodedFlag4(b);

      return this;
    }
    public TraceBuilder pScenarioCodedeposit(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("CODEDEPOSIT already set");
      } else {
        filled.set(60);
      }

      this.writeisIdentityXorPrecinfoFlagXorCodedepositXorDupFlag(b);

      return this;
    }
    public TraceBuilder pScenarioCodedepositInvalidCodePrefix(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("CODEDEPOSIT_INVALID_CODE_PREFIX already set");
      } else {
        filled.set(61);
      }

      this.writeisModexpXorStpExistsXorCodedepositInvalidCodePrefixXorExtFlag(b);

      return this;
    }
    public TraceBuilder pScenarioCodedepositValidCodePrefix(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("CODEDEPOSIT_VALID_CODE_PREFIX already set");
      } else {
        filled.set(62);
      }

      this.writeisPrecompileXorStpFlagXorCodedepositValidCodePrefixXorHaltFlag(b);

      return this;
    }
    public TraceBuilder pScenarioEcadd(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("ECADD already set");
      } else {
        filled.set(63);
      }

      this.writeisRipemDsub160XorStpOogxXorEcaddXorHashInfoFlag(b);

      return this;
    }
    public TraceBuilder pScenarioEcmul(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("ECMUL already set");
      } else {
        filled.set(64);
      }

      this.writeisSha2Sub256XorStpWarmXorEcmulXorInvalidFlag(b);

      return this;
    }
    public TraceBuilder pScenarioEcpairing(final Boolean b) {
      if (filled.get(65)) {
        throw new IllegalStateException("ECPAIRING already set");
      } else {
        filled.set(65);
      }

      this.writerlpaddrFlagXorEcpairingXorInvprex(b);

      return this;
    }
    public TraceBuilder pScenarioEcrecover(final Boolean b) {
      if (filled.get(66)) {
        throw new IllegalStateException("ECRECOVER already set");
      } else {
        filled.set(66);
      }

      this.writetrmFlagXorEcrecoverXorJumpx(b);

      return this;
    }
    public TraceBuilder pScenarioIdentity(final Boolean b) {
      if (filled.get(67)) {
        throw new IllegalStateException("IDENTITY already set");
      } else {
        filled.set(67);
      }

      this.writewarmXorIdentityXorJumpDestinationVettingRequired(b);

      return this;
    }
    public TraceBuilder pScenarioModexp(final Boolean b) {
      if (filled.get(68)) {
        throw new IllegalStateException("MODEXP already set");
      } else {
        filled.set(68);
      }

      this.writewarmNewXorModexpXorJumpFlag(b);

      return this;
    }
    public TraceBuilder pScenarioRipemd160(final Boolean b) {
      if (filled.get(69)) {
        throw new IllegalStateException("RIPEMD-160 already set");
      } else {
        filled.set(69);
      }

      this.writeripemDsub160XorKecFlag(b);

      return this;
    }
    public TraceBuilder pScenarioScnFailure1(final Boolean b) {
      if (filled.get(70)) {
        throw new IllegalStateException("SCN_FAILURE_1 already set");
      } else {
        filled.set(70);
      }

      this.writescnFailure1XorLogFlag(b);

      return this;
    }
    public TraceBuilder pScenarioScnFailure2(final Boolean b) {
      if (filled.get(71)) {
        throw new IllegalStateException("SCN_FAILURE_2 already set");
      } else {
        filled.set(71);
      }

      this.writescnFailure2XorMachineStateFlag(b);

      return this;
    }
    public TraceBuilder pScenarioScnFailure3(final Boolean b) {
      if (filled.get(72)) {
        throw new IllegalStateException("SCN_FAILURE_3 already set");
      } else {
        filled.set(72);
      }

      this.writescnFailure3XorMaxcsx(b);

      return this;
    }
    public TraceBuilder pScenarioScnFailure4(final Boolean b) {
      if (filled.get(73)) {
        throw new IllegalStateException("SCN_FAILURE_4 already set");
      } else {
        filled.set(73);
      }

      this.writescnFailure4XorModFlag(b);

      return this;
    }
    public TraceBuilder pScenarioScnSuccess1(final Boolean b) {
      if (filled.get(74)) {
        throw new IllegalStateException("SCN_SUCCESS_1 already set");
      } else {
        filled.set(74);
      }

      this.writescnSuccess1XorMulFlag(b);

      return this;
    }
    public TraceBuilder pScenarioScnSuccess2(final Boolean b) {
      if (filled.get(75)) {
        throw new IllegalStateException("SCN_SUCCESS_2 already set");
      } else {
        filled.set(75);
      }

      this.writescnSuccess2XorMxpx(b);

      return this;
    }
    public TraceBuilder pScenarioScnSuccess3(final Boolean b) {
      if (filled.get(76)) {
        throw new IllegalStateException("SCN_SUCCESS_3 already set");
      } else {
        filled.set(76);
      }

      this.writescnSuccess3XorMxpFlag(b);

      return this;
    }
    public TraceBuilder pScenarioScnSuccess4(final Boolean b) {
      if (filled.get(77)) {
        throw new IllegalStateException("SCN_SUCCESS_4 already set");
      } else {
        filled.set(77);
      }

      this.writescnSuccess4XorOobFlag(b);

      return this;
    }
    public TraceBuilder pScenarioSelfdestruct(final Boolean b) {
      if (filled.get(78)) {
        throw new IllegalStateException("SELFDESTRUCT already set");
      } else {
        filled.set(78);
      }

      this.writeselfdestructXorOogx(b);

      return this;
    }
    public TraceBuilder pScenarioSha2256(final Boolean b) {
      if (filled.get(79)) {
        throw new IllegalStateException("SHA2-256 already set");
      } else {
        filled.set(79);
      }

      this.writesha2Sub256XorOpcx(b);

      return this;
    }
    public TraceBuilder pStackAccFlag(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("ACC_FLAG already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pStackAddFlag(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("ADD_FLAG already set");
      } else {
        filled.set(49);
      }

      this.writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(b);

      return this;
    }
    public TraceBuilder pStackBinFlag(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("BIN_FLAG already set");
      } else {
        filled.set(50);
      }

      this.writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(b);

      return this;
    }
    public TraceBuilder pStackBtcFlag(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("BTC_FLAG already set");
      } else {
        filled.set(51);
      }

      this.writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(b);

      return this;
    }
    public TraceBuilder pStackCallFlag(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("CALL_FLAG already set");
      } else {
        filled.set(52);
      }

      this.writeexistsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig(b);

      return this;
    }
    public TraceBuilder pStackConFlag(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("CON_FLAG already set");
      } else {
        filled.set(53);
      }

      this.writehasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero(b);

      return this;
    }
    public TraceBuilder pStackCopyFlag(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("COPY_FLAG already set");
      } else {
        filled.set(54);
      }

      this.writehasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero(b);

      return this;
    }
    public TraceBuilder pStackCreateFlag(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("CREATE_FLAG already set");
      } else {
        filled.set(55);
      }

      this.writeisBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm(b);

      return this;
    }
    public TraceBuilder pStackDecodedFlag1(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("DECODED_FLAG_1 already set");
      } else {
        filled.set(56);
      }

      this.writeisEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew(b);

      return this;
    }
    public TraceBuilder pStackDecodedFlag2(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("DECODED_FLAG_2 already set");
      } else {
        filled.set(57);
      }

      this.writeisEcmulXorOobEvent1XorCallSmcFailureCallerWontRevertXorDecodedFlag2(b);

      return this;
    }
    public TraceBuilder pStackDecodedFlag3(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("DECODED_FLAG_3 already set");
      } else {
        filled.set(58);
      }

      this.writeisEcpairingXorOobEvent2XorCallSmcSuccessCallerWillRevertXorDecodedFlag3(b);

      return this;
    }
    public TraceBuilder pStackDecodedFlag4(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("DECODED_FLAG_4 already set");
      } else {
        filled.set(59);
      }

      this.writeisEcrecoverXorOobFlagXorCallSmcSuccessCallerWontRevertXorDecodedFlag4(b);

      return this;
    }
    public TraceBuilder pStackDupFlag(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("DUP_FLAG already set");
      } else {
        filled.set(60);
      }

      this.writeisIdentityXorPrecinfoFlagXorCodedepositXorDupFlag(b);

      return this;
    }
    public TraceBuilder pStackExtFlag(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("EXT_FLAG already set");
      } else {
        filled.set(61);
      }

      this.writeisModexpXorStpExistsXorCodedepositInvalidCodePrefixXorExtFlag(b);

      return this;
    }
    public TraceBuilder pStackHaltFlag(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("HALT_FLAG already set");
      } else {
        filled.set(62);
      }

      this.writeisPrecompileXorStpFlagXorCodedepositValidCodePrefixXorHaltFlag(b);

      return this;
    }
    public TraceBuilder pStackHashInfoFlag(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("HASH_INFO_FLAG already set");
      } else {
        filled.set(63);
      }

      this.writeisRipemDsub160XorStpOogxXorEcaddXorHashInfoFlag(b);

      return this;
    }
    public TraceBuilder pStackHashInfoKecHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("HASH_INFO___KEC_HI already set");
      } else {
        filled.set(98);
      }

      this.writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(b);

      return this;
    }
    public TraceBuilder pStackHashInfoKecLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("HASH_INFO___KEC_LO already set");
      } else {
        filled.set(99);
      }

      this.writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(b);

      return this;
    }
    public TraceBuilder pStackHashInfoSize(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("HASH_INFO___SIZE already set");
      } else {
        filled.set(100);
      }

      this.writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(b);

      return this;
    }
    public TraceBuilder pStackHeight(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("HEIGHT already set");
      } else {
        filled.set(101);
      }

      this.writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(b);

      return this;
    }
    public TraceBuilder pStackHeightNew(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("HEIGHT_NEW already set");
      } else {
        filled.set(102);
      }

      this.writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(b);

      return this;
    }
    public TraceBuilder pStackHeightOver(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("HEIGHT_OVER already set");
      } else {
        filled.set(103);
      }

      this.writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(b);

      return this;
    }
    public TraceBuilder pStackHeightUnder(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("HEIGHT_UNDER already set");
      } else {
        filled.set(104);
      }

      this.writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(b);

      return this;
    }
    public TraceBuilder pStackInst(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(105);
      }

      this.writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(b);

      return this;
    }
    public TraceBuilder pStackInvalidFlag(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("INVALID_FLAG already set");
      } else {
        filled.set(64);
      }

      this.writeisSha2Sub256XorStpWarmXorEcmulXorInvalidFlag(b);

      return this;
    }
    public TraceBuilder pStackInvprex(final Boolean b) {
      if (filled.get(65)) {
        throw new IllegalStateException("INVPREX already set");
      } else {
        filled.set(65);
      }

      this.writerlpaddrFlagXorEcpairingXorInvprex(b);

      return this;
    }
    public TraceBuilder pStackJumpDestinationVettingRequired(final Boolean b) {
      if (filled.get(67)) {
        throw new IllegalStateException("JUMP_DESTINATION_VETTING_REQUIRED already set");
      } else {
        filled.set(67);
      }

      this.writewarmXorIdentityXorJumpDestinationVettingRequired(b);

      return this;
    }
    public TraceBuilder pStackJumpFlag(final Boolean b) {
      if (filled.get(68)) {
        throw new IllegalStateException("JUMP_FLAG already set");
      } else {
        filled.set(68);
      }

      this.writewarmNewXorModexpXorJumpFlag(b);

      return this;
    }
    public TraceBuilder pStackJumpx(final Boolean b) {
      if (filled.get(66)) {
        throw new IllegalStateException("JUMPX already set");
      } else {
        filled.set(66);
      }

      this.writetrmFlagXorEcrecoverXorJumpx(b);

      return this;
    }
    public TraceBuilder pStackKecFlag(final Boolean b) {
      if (filled.get(69)) {
        throw new IllegalStateException("KEC_FLAG already set");
      } else {
        filled.set(69);
      }

      this.writeripemDsub160XorKecFlag(b);

      return this;
    }
    public TraceBuilder pStackLogFlag(final Boolean b) {
      if (filled.get(70)) {
        throw new IllegalStateException("LOG_FLAG already set");
      } else {
        filled.set(70);
      }

      this.writescnFailure1XorLogFlag(b);

      return this;
    }
    public TraceBuilder pStackMachineStateFlag(final Boolean b) {
      if (filled.get(71)) {
        throw new IllegalStateException("MACHINE_STATE_FLAG already set");
      } else {
        filled.set(71);
      }

      this.writescnFailure2XorMachineStateFlag(b);

      return this;
    }
    public TraceBuilder pStackMaxcsx(final Boolean b) {
      if (filled.get(72)) {
        throw new IllegalStateException("MAXCSX already set");
      } else {
        filled.set(72);
      }

      this.writescnFailure3XorMaxcsx(b);

      return this;
    }
    public TraceBuilder pStackModFlag(final Boolean b) {
      if (filled.get(73)) {
        throw new IllegalStateException("MOD_FLAG already set");
      } else {
        filled.set(73);
      }

      this.writescnFailure4XorModFlag(b);

      return this;
    }
    public TraceBuilder pStackMulFlag(final Boolean b) {
      if (filled.get(74)) {
        throw new IllegalStateException("MUL_FLAG already set");
      } else {
        filled.set(74);
      }

      this.writescnSuccess1XorMulFlag(b);

      return this;
    }
    public TraceBuilder pStackMxpFlag(final Boolean b) {
      if (filled.get(76)) {
        throw new IllegalStateException("MXP_FLAG already set");
      } else {
        filled.set(76);
      }

      this.writescnSuccess3XorMxpFlag(b);

      return this;
    }
    public TraceBuilder pStackMxpx(final Boolean b) {
      if (filled.get(75)) {
        throw new IllegalStateException("MXPX already set");
      } else {
        filled.set(75);
      }

      this.writescnSuccess2XorMxpx(b);

      return this;
    }
    public TraceBuilder pStackOobFlag(final Boolean b) {
      if (filled.get(77)) {
        throw new IllegalStateException("OOB_FLAG already set");
      } else {
        filled.set(77);
      }

      this.writescnSuccess4XorOobFlag(b);

      return this;
    }
    public TraceBuilder pStackOogx(final Boolean b) {
      if (filled.get(78)) {
        throw new IllegalStateException("OOGX already set");
      } else {
        filled.set(78);
      }

      this.writeselfdestructXorOogx(b);

      return this;
    }
    public TraceBuilder pStackOpcx(final Boolean b) {
      if (filled.get(79)) {
        throw new IllegalStateException("OPCX already set");
      } else {
        filled.set(79);
      }

      this.writesha2Sub256XorOpcx(b);

      return this;
    }
    public TraceBuilder pStackPushValueHi(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("PUSH_VALUE_HI already set");
      } else {
        filled.set(106);
      }

      this.writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(b);

      return this;
    }
    public TraceBuilder pStackPushValueLo(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("PUSH_VALUE_LO already set");
      } else {
        filled.set(107);
      }

      this.writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(b);

      return this;
    }
    public TraceBuilder pStackPushpopFlag(final Boolean b) {
      if (filled.get(80)) {
        throw new IllegalStateException("PUSHPOP_FLAG already set");
      } else {
        filled.set(80);
      }

      this.writepushpopFlag(b);

      return this;
    }
    public TraceBuilder pStackRdcx(final Boolean b) {
      if (filled.get(81)) {
        throw new IllegalStateException("RDCX already set");
      } else {
        filled.set(81);
      }

      this.writerdcx(b);

      return this;
    }
    public TraceBuilder pStackShfFlag(final Boolean b) {
      if (filled.get(82)) {
        throw new IllegalStateException("SHF_FLAG already set");
      } else {
        filled.set(82);
      }

      this.writeshfFlag(b);

      return this;
    }
    public TraceBuilder pStackSox(final Boolean b) {
      if (filled.get(83)) {
        throw new IllegalStateException("SOX already set");
      } else {
        filled.set(83);
      }

      this.writesox(b);

      return this;
    }
    public TraceBuilder pStackSstorex(final Boolean b) {
      if (filled.get(84)) {
        throw new IllegalStateException("SSTOREX already set");
      } else {
        filled.set(84);
      }

      this.writesstorex(b);

      return this;
    }
    public TraceBuilder pStackStackItemHeight1(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_1 already set");
      } else {
        filled.set(108);
      }

      this.writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(b);

      return this;
    }
    public TraceBuilder pStackStackItemHeight2(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_2 already set");
      } else {
        filled.set(109);
      }

      this.writedepNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize(b);

      return this;
    }
    public TraceBuilder pStackStackItemHeight3(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_3 already set");
      } else {
        filled.set(110);
      }

      this.writedepNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas(b);

      return this;
    }
    public TraceBuilder pStackStackItemHeight4(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_4 already set");
      } else {
        filled.set(111);
      }

      this.writenonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas(b);

      return this;
    }
    public TraceBuilder pStackStackItemPop1(final Boolean b) {
      if (filled.get(86)) {
        throw new IllegalStateException("STACK_ITEM_POP_1 already set");
      } else {
        filled.set(86);
      }

      this.writestackItemPop1(b);

      return this;
    }
    public TraceBuilder pStackStackItemPop2(final Boolean b) {
      if (filled.get(87)) {
        throw new IllegalStateException("STACK_ITEM_POP_2 already set");
      } else {
        filled.set(87);
      }

      this.writestackItemPop2(b);

      return this;
    }
    public TraceBuilder pStackStackItemPop3(final Boolean b) {
      if (filled.get(88)) {
        throw new IllegalStateException("STACK_ITEM_POP_3 already set");
      } else {
        filled.set(88);
      }

      this.writestackItemPop3(b);

      return this;
    }
    public TraceBuilder pStackStackItemPop4(final Boolean b) {
      if (filled.get(89)) {
        throw new IllegalStateException("STACK_ITEM_POP_4 already set");
      } else {
        filled.set(89);
      }

      this.writestackItemPop4(b);

      return this;
    }
    public TraceBuilder pStackStackItemStamp1(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_1 already set");
      } else {
        filled.set(112);
      }

      this.writenonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce(b);

      return this;
    }
    public TraceBuilder pStackStackItemStamp2(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_2 already set");
      } else {
        filled.set(113);
      }

      this.writerlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi(b);

      return this;
    }
    public TraceBuilder pStackStackItemStamp3(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_3 already set");
      } else {
        filled.set(114);
      }

      this.writerlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo(b);

      return this;
    }
    public TraceBuilder pStackStackItemStamp4(final BigInteger b) {
      if (filled.get(115)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_4 already set");
      } else {
        filled.set(115);
      }

      this.writerlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueHi1(final BigInteger b) {
      if (filled.get(116)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_1 already set");
      } else {
        filled.set(116);
      }

      this.writerlpaddrKecLoXorReturnAtOffsetXorMxpInstXorStackItemValueHi1(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueHi2(final BigInteger b) {
      if (filled.get(117)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_2 already set");
      } else {
        filled.set(117);
      }

      this.writerlpaddrRecipeXorReturnAtSizeXorMxpOffset1HiXorStackItemValueHi2(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueHi3(final BigInteger b) {
      if (filled.get(118)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_3 already set");
      } else {
        filled.set(118);
      }

      this.writerlpaddrSaltHiXorReturnDataOffsetXorMxpOffset1LoXorStackItemValueHi3(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueHi4(final BigInteger b) {
      if (filled.get(119)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_4 already set");
      } else {
        filled.set(119);
      }

      this.writerlpaddrSaltLoXorReturnDataSizeXorMxpOffset2HiXorStackItemValueHi4(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueLo1(final BigInteger b) {
      if (filled.get(120)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_1 already set");
      } else {
        filled.set(120);
      }

      this.writetrmRawAddrHiXorMxpOffset2LoXorStackItemValueLo1(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueLo2(final BigInteger b) {
      if (filled.get(121)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_2 already set");
      } else {
        filled.set(121);
      }

      this.writemxpSize1HiXorStackItemValueLo2(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueLo3(final BigInteger b) {
      if (filled.get(122)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_3 already set");
      } else {
        filled.set(122);
      }

      this.writemxpSize1LoXorStackItemValueLo3(b);

      return this;
    }
    public TraceBuilder pStackStackItemValueLo4(final BigInteger b) {
      if (filled.get(123)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_4 already set");
      } else {
        filled.set(123);
      }

      this.writemxpSize2HiXorStackItemValueLo4(b);

      return this;
    }
    public TraceBuilder pStackStackramFlag(final Boolean b) {
      if (filled.get(85)) {
        throw new IllegalStateException("STACKRAM_FLAG already set");
      } else {
        filled.set(85);
      }

      this.writestackramFlag(b);

      return this;
    }
    public TraceBuilder pStackStaticFlag(final Boolean b) {
      if (filled.get(91)) {
        throw new IllegalStateException("STATIC_FLAG already set");
      } else {
        filled.set(91);
      }

      this.writestaticFlag(b);

      return this;
    }
    public TraceBuilder pStackStaticGas(final BigInteger b) {
      if (filled.get(124)) {
        throw new IllegalStateException("STATIC_GAS already set");
      } else {
        filled.set(124);
      }

      this.writemxpSize2LoXorStaticGas(b);

      return this;
    }
    public TraceBuilder pStackStaticx(final Boolean b) {
      if (filled.get(90)) {
        throw new IllegalStateException("STATICX already set");
      } else {
        filled.set(90);
      }

      this.writestaticx(b);

      return this;
    }
    public TraceBuilder pStackStoFlag(final Boolean b) {
      if (filled.get(92)) {
        throw new IllegalStateException("STO_FLAG already set");
      } else {
        filled.set(92);
      }

      this.writestoFlag(b);

      return this;
    }
    public TraceBuilder pStackSux(final Boolean b) {
      if (filled.get(93)) {
        throw new IllegalStateException("SUX already set");
      } else {
        filled.set(93);
      }

      this.writesux(b);

      return this;
    }
    public TraceBuilder pStackSwapFlag(final Boolean b) {
      if (filled.get(94)) {
        throw new IllegalStateException("SWAP_FLAG already set");
      } else {
        filled.set(94);
      }

      this.writeswapFlag(b);

      return this;
    }
    public TraceBuilder pStackTrmFlag(final Boolean b) {
      if (filled.get(95)) {
        throw new IllegalStateException("TRM_FLAG already set");
      } else {
        filled.set(95);
      }

      this.writetrmFlag(b);

      return this;
    }
    public TraceBuilder pStackTxnFlag(final Boolean b) {
      if (filled.get(96)) {
        throw new IllegalStateException("TXN_FLAG already set");
      } else {
        filled.set(96);
      }

      this.writetxnFlag(b);

      return this;
    }
    public TraceBuilder pStackWcpFlag(final Boolean b) {
      if (filled.get(97)) {
        throw new IllegalStateException("WCP_FLAG already set");
      } else {
        filled.set(97);
      }

      this.writewcpFlag(b);

      return this;
    }
    public TraceBuilder pStorageAddressHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("ADDRESS_HI already set");
      } else {
        filled.set(98);
      }

      this.writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(b);

      return this;
    }
    public TraceBuilder pStorageAddressLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("ADDRESS_LO already set");
      } else {
        filled.set(99);
      }

      this.writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(b);

      return this;
    }
    public TraceBuilder pStorageDeploymentNumber(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(100);
      }

      this.writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(b);

      return this;
    }
    public TraceBuilder pStorageStorageKeyHi(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("STORAGE_KEY_HI already set");
      } else {
        filled.set(101);
      }

      this.writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(b);

      return this;
    }
    public TraceBuilder pStorageStorageKeyLo(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("STORAGE_KEY_LO already set");
      } else {
        filled.set(102);
      }

      this.writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(b);

      return this;
    }
    public TraceBuilder pStorageValCurrChanges(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("VAL_CURR_CHANGES already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pStorageValCurrHi(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("VAL_CURR_HI already set");
      } else {
        filled.set(103);
      }

      this.writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(b);

      return this;
    }
    public TraceBuilder pStorageValCurrIsOrig(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("VAL_CURR_IS_ORIG already set");
      } else {
        filled.set(49);
      }

      this.writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(b);

      return this;
    }
    public TraceBuilder pStorageValCurrIsZero(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("VAL_CURR_IS_ZERO already set");
      } else {
        filled.set(50);
      }

      this.writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(b);

      return this;
    }
    public TraceBuilder pStorageValCurrLo(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("VAL_CURR_LO already set");
      } else {
        filled.set(104);
      }

      this.writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(b);

      return this;
    }
    public TraceBuilder pStorageValNextHi(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("VAL_NEXT_HI already set");
      } else {
        filled.set(105);
      }

      this.writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(b);

      return this;
    }
    public TraceBuilder pStorageValNextIsCurr(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("VAL_NEXT_IS_CURR already set");
      } else {
        filled.set(51);
      }

      this.writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(b);

      return this;
    }
    public TraceBuilder pStorageValNextIsOrig(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("VAL_NEXT_IS_ORIG already set");
      } else {
        filled.set(52);
      }

      this.writeexistsNewXorMmuFlagXorCallPrcFailureCallerWillRevertXorCallFlagXorValNextIsOrig(b);

      return this;
    }
    public TraceBuilder pStorageValNextIsZero(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("VAL_NEXT_IS_ZERO already set");
      } else {
        filled.set(53);
      }

      this.writehasCodeXorMmuInfoXorCallPrcFailureCallerWontRevertXorConFlagXorValNextIsZero(b);

      return this;
    }
    public TraceBuilder pStorageValNextLo(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("VAL_NEXT_LO already set");
      } else {
        filled.set(106);
      }

      this.writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(b);

      return this;
    }
    public TraceBuilder pStorageValOrigHi(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("VAL_ORIG_HI already set");
      } else {
        filled.set(107);
      }

      this.writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(b);

      return this;
    }
    public TraceBuilder pStorageValOrigIsZero(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("VAL_ORIG_IS_ZERO already set");
      } else {
        filled.set(54);
      }

      this.writehasCodeNewXorMxpDeploysXorCallPrcSuccessCallerWillRevertXorCopyFlagXorValOrigIsZero(b);

      return this;
    }
    public TraceBuilder pStorageValOrigLo(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("VAL_ORIG_LO already set");
      } else {
        filled.set(108);
      }

      this.writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(b);

      return this;
    }
    public TraceBuilder pStorageWarm(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("WARM already set");
      } else {
        filled.set(55);
      }

      this.writeisBlake2FXorMxpFlagXorCallPrcSuccessCallerWontRevertXorCreateFlagXorWarm(b);

      return this;
    }
    public TraceBuilder pStorageWarmNew(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("WARM_NEW already set");
      } else {
        filled.set(56);
      }

      this.writeisEcaddXorMxpMxpxXorCallSmcFailureCallerWillRevertXorDecodedFlag1XorWarmNew(b);

      return this;
    }
    public TraceBuilder pTransactionBasefee(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("BASEFEE already set");
      } else {
        filled.set(98);
      }

      this.writeaddrHiXorAccountAddressHiXorCcrsStampXorHashInfoKecHiXorAddressHiXorBasefee(b);

      return this;
    }
    public TraceBuilder pTransactionCallDataSize(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("CALL_DATA_SIZE already set");
      } else {
        filled.set(99);
      }

      this.writeaddrLoXorAccountAddressLoXorExpDyncostXorHashInfoKecLoXorAddressLoXorCallDataSize(b);

      return this;
    }
    public TraceBuilder pTransactionCoinbaseAddressHi(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("COINBASE_ADDRESS_HI already set");
      } else {
        filled.set(100);
      }

      this.writebalanceXorAccountDeploymentNumberXorExpExponentHiXorHashInfoSizeXorDeploymentNumberXorCoinbaseAddressHi(b);

      return this;
    }
    public TraceBuilder pTransactionCoinbaseAddressLo(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("COINBASE_ADDRESS_LO already set");
      } else {
        filled.set(101);
      }

      this.writebalanceNewXorByteCodeAddressHiXorExpExponentLoXorHeightXorStorageKeyHiXorCoinbaseAddressLo(b);

      return this;
    }
    public TraceBuilder pTransactionFromAddressHi(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("FROM_ADDRESS_HI already set");
      } else {
        filled.set(102);
      }

      this.writecodeHashHiXorByteCodeAddressLoXorMmuExoSumXorHeightNewXorStorageKeyLoXorFromAddressHi(b);

      return this;
    }
    public TraceBuilder pTransactionFromAddressLo(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("FROM_ADDRESS_LO already set");
      } else {
        filled.set(103);
      }

      this.writecodeHashHiNewXorByteCodeDeploymentNumberXorMmuInstXorHeightOverXorValCurrHiXorFromAddressLo(b);

      return this;
    }
    public TraceBuilder pTransactionGasLimit(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("GAS_LIMIT already set");
      } else {
        filled.set(104);
      }

      this.writecodeHashLoXorByteCodeDeploymentStatusXorMmuOffset1LoXorHeightUnderXorValCurrLoXorGasLimit(b);

      return this;
    }
    public TraceBuilder pTransactionGasPrice(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("GAS_PRICE already set");
      } else {
        filled.set(105);
      }

      this.writecodeHashLoNewXorCallerAddressHiXorMmuOffset2HiXorInstXorValNextHiXorGasPrice(b);

      return this;
    }
    public TraceBuilder pTransactionGasRefundAmount(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("GAS_REFUND_AMOUNT already set");
      } else {
        filled.set(106);
      }

      this.writecodeSizeXorCallerAddressLoXorMmuOffset2LoXorPushValueHiXorValNextLoXorGasRefundAmount(b);

      return this;
    }
    public TraceBuilder pTransactionGasRefundCounterFinal(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("GAS_REFUND_COUNTER_FINAL already set");
      } else {
        filled.set(107);
      }

      this.writecodeSizeNewXorCallerContextNumberXorMmuParam1XorPushValueLoXorValOrigHiXorGasRefundCounterFinal(b);

      return this;
    }
    public TraceBuilder pTransactionInitCodeSize(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("INIT_CODE_SIZE already set");
      } else {
        filled.set(109);
      }

      this.writedepNumXorCallDataSizeXorMmuRefOffsetXorStackItemHeight2XorInitCodeSize(b);

      return this;
    }
    public TraceBuilder pTransactionInitGas(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("INIT_GAS already set");
      } else {
        filled.set(110);
      }

      this.writedepNumNewXorCallStackDepthXorMmuRefSizeXorStackItemHeight3XorInitGas(b);

      return this;
    }
    public TraceBuilder pTransactionInitialBalance(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("INITIAL_BALANCE already set");
      } else {
        filled.set(108);
      }

      this.writedeploymentNumberInftyXorCallDataOffsetXorMmuParam2XorStackItemHeight1XorValOrigLoXorInitialBalance(b);

      return this;
    }
    public TraceBuilder pTransactionIsDeployment(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("IS_DEPLOYMENT already set");
      } else {
        filled.set(48);
      }

      this.writedeploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment(b);

      return this;
    }
    public TraceBuilder pTransactionIsEip1559(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("IS_EIP1559 already set");
      } else {
        filled.set(49);
      }

      this.writedepStatusXorCcsrFlagXorCallAbortXorAddFlagXorValCurrIsOrigXorIsEip1559(b);

      return this;
    }
    public TraceBuilder pTransactionLeftoverGas(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("LEFTOVER_GAS already set");
      } else {
        filled.set(111);
      }

      this.writenonceXorCallValueXorMmuReturnerXorStackItemHeight4XorLeftoverGas(b);

      return this;
    }
    public TraceBuilder pTransactionNonce(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("NONCE already set");
      } else {
        filled.set(112);
      }

      this.writenonceNewXorContextNumberXorMmuSizeXorStackItemStamp1XorNonce(b);

      return this;
    }
    public TraceBuilder pTransactionStatusCode(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("STATUS_CODE already set");
      } else {
        filled.set(50);
      }

      this.writedepStatusNewXorExpFlagXorCallEoaSuccessCallerWillRevertXorBinFlagXorValCurrIsZeroXorStatusCode(b);

      return this;
    }
    public TraceBuilder pTransactionToAddressHi(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("TO_ADDRESS_HI already set");
      } else {
        filled.set(113);
      }

      this.writerlpaddrDepAddrHiXorIsStaticXorMmuStackValHiXorStackItemStamp2XorToAddressHi(b);

      return this;
    }
    public TraceBuilder pTransactionToAddressLo(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("TO_ADDRESS_LO already set");
      } else {
        filled.set(114);
      }

      this.writerlpaddrDepAddrLoXorReturnerContextNumberXorMmuStackValLoXorStackItemStamp3XorToAddressLo(b);

      return this;
    }
    public TraceBuilder pTransactionTxnRequiresEvmExecution(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("TXN_REQUIRES_EVM_EXECUTION already set");
      } else {
        filled.set(51);
      }

      this.writeexistsXorFcondFlagXorCallEoaSuccessCallerWontRevertXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution(b);

      return this;
    }
    public TraceBuilder pTransactionValue(final BigInteger b) {
      if (filled.get(115)) {
        throw new IllegalStateException("VALUE already set");
      } else {
        filled.set(115);
      }

      this.writerlpaddrKecHiXorReturnerIsPrecompileXorMxpGasMxpXorStackItemStamp4XorValue(b);

      return this;
    }
    public TraceBuilder peekAtAccount(final Boolean b) {
      if (filled.get(31)) {
        throw new IllegalStateException("PEEK_AT_ACCOUNT already set");
      } else {
        filled.set(31);
      }

      this.writepeekAtAccount(b);

      return this;
    }
    public TraceBuilder peekAtContext(final Boolean b) {
      if (filled.get(32)) {
        throw new IllegalStateException("PEEK_AT_CONTEXT already set");
      } else {
        filled.set(32);
      }

      this.writepeekAtContext(b);

      return this;
    }
    public TraceBuilder peekAtMiscellaneous(final Boolean b) {
      if (filled.get(33)) {
        throw new IllegalStateException("PEEK_AT_MISCELLANEOUS already set");
      } else {
        filled.set(33);
      }

      this.writepeekAtMiscellaneous(b);

      return this;
    }
    public TraceBuilder peekAtScenario(final Boolean b) {
      if (filled.get(34)) {
        throw new IllegalStateException("PEEK_AT_SCENARIO already set");
      } else {
        filled.set(34);
      }

      this.writepeekAtScenario(b);

      return this;
    }
    public TraceBuilder peekAtStack(final Boolean b) {
      if (filled.get(35)) {
        throw new IllegalStateException("PEEK_AT_STACK already set");
      } else {
        filled.set(35);
      }

      this.writepeekAtStack(b);

      return this;
    }
    public TraceBuilder peekAtStorage(final Boolean b) {
      if (filled.get(36)) {
        throw new IllegalStateException("PEEK_AT_STORAGE already set");
      } else {
        filled.set(36);
      }

      this.writepeekAtStorage(b);

      return this;
    }
    public TraceBuilder peekAtTransaction(final Boolean b) {
      if (filled.get(37)) {
        throw new IllegalStateException("PEEK_AT_TRANSACTION already set");
      } else {
        filled.set(37);
      }

      this.writepeekAtTransaction(b);

      return this;
    }
    public TraceBuilder programCounter(final BigInteger b) {
      if (filled.get(38)) {
        throw new IllegalStateException("PROGRAM_COUNTER already set");
      } else {
        filled.set(38);
      }

      this.writeprogramCounter(b);

      return this;
    }
    public TraceBuilder programCounterNew(final BigInteger b) {
      if (filled.get(39)) {
        throw new IllegalStateException("PROGRAM_COUNTER_NEW already set");
      } else {
        filled.set(39);
      }

      this.writeprogramCounterNew(b);

      return this;
    }
    public TraceBuilder subStamp(final BigInteger b) {
      if (filled.get(40)) {
        throw new IllegalStateException("SUB_STAMP already set");
      } else {
        filled.set(40);
      }

      this.writesubStamp(b);

      return this;
    }
    public TraceBuilder transactionReverts(final Boolean b) {
      if (filled.get(41)) {
        throw new IllegalStateException("TRANSACTION_REVERTS already set");
      } else {
        filled.set(41);
      }

      this.writetransactionReverts(b);

      return this;
    }
    public TraceBuilder twoLineInstruction(final Boolean b) {
      if (filled.get(42)) {
        throw new IllegalStateException("TWO_LINE_INSTRUCTION already set");
      } else {
        filled.set(42);
      }

      this.writetwoLineInstruction(b);

      return this;
    }
    public TraceBuilder txExec(final Boolean b) {
      if (filled.get(43)) {
        throw new IllegalStateException("TX_EXEC already set");
      } else {
        filled.set(43);
      }

      this.writetxExec(b);

      return this;
    }
    public TraceBuilder txFinl(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("TX_FINL already set");
      } else {
        filled.set(44);
      }

      this.writetxFinl(b);

      return this;
    }
    public TraceBuilder txInit(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("TX_INIT already set");
      } else {
        filled.set(45);
      }

      this.writetxInit(b);

      return this;
    }
    public TraceBuilder txSkip(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("TX_SKIP already set");
      } else {
        filled.set(46);
      }

      this.writetxSkip(b);

      return this;
    }
    public TraceBuilder txWarm(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("TX_WARM already set");
      } else {
        filled.set(47);
      }

      this.writetxWarm(b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ABSOLUTE_TRANSACTION_NUMBER has not been filled");
      }

      if (!filled.get(98)) {
        throw new IllegalStateException("ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_CCRS_STAMP_xor_HASH_INFO___KEC_HI_xor_ADDRESS_HI_xor_BASEFEE has not been filled");
      }

      if (!filled.get(99)) {
        throw new IllegalStateException("ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_EXP___DYNCOST_xor_HASH_INFO___KEC_LO_xor_ADDRESS_LO_xor_CALL_DATA_SIZE has not been filled");
      }

      if (!filled.get(101)) {
        throw new IllegalStateException("BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_EXP___EXPONENT_LO_xor_HEIGHT_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO has not been filled");
      }

      if (!filled.get(100)) {
        throw new IllegalStateException("BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_EXP___EXPONENT_HI_xor_HASH_INFO___SIZE_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("BATCH_NUMBER has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("CODE_ADDRESS_HI has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("CODE_ADDRESS_LO has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_NUMBER has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_STATUS has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
      }

      if (!filled.get(103)) {
        throw new IllegalStateException("CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_MMU___INST_xor_HEIGHT_OVER_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO has not been filled");
      }

      if (!filled.get(102)) {
        throw new IllegalStateException("CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_MMU___EXO_SUM_xor_HEIGHT_NEW_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI has not been filled");
      }

      if (!filled.get(105)) {
        throw new IllegalStateException("CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_MMU___OFFSET_2_HI_xor_INST_xor_VAL_NEXT_HI_xor_GAS_PRICE has not been filled");
      }

      if (!filled.get(104)) {
        throw new IllegalStateException("CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_MMU___OFFSET_1_LO_xor_HEIGHT_UNDER_xor_VAL_CURR_LO_xor_GAS_LIMIT has not been filled");
      }

      if (!filled.get(107)) {
        throw new IllegalStateException("CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_MMU___PARAM_1_xor_PUSH_VALUE_LO_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL has not been filled");
      }

      if (!filled.get(106)) {
        throw new IllegalStateException("CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_MMU___OFFSET_2_LO_xor_PUSH_VALUE_HI_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("CONTEXT_GETS_REVERTED_FLAG has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("CONTEXT_MAY_CHANGE_FLAG has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("CONTEXT_NUMBER has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("CONTEXT_NUMBER_NEW has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("CONTEXT_REVERT_STAMP has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("CONTEXT_SELF_REVERTS_FLAG has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("CONTEXT_WILL_REVERT_FLAG has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("COUNTER_NSR has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("COUNTER_TLI has not been filled");
      }

      if (!filled.get(110)) {
        throw new IllegalStateException("DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_MMU___REF_SIZE_xor_STACK_ITEM_HEIGHT_3_xor_INIT_GAS has not been filled");
      }

      if (!filled.get(109)) {
        throw new IllegalStateException("DEP_NUM_xor_CALL_DATA_SIZE_xor_MMU___REF_OFFSET_xor_STACK_ITEM_HEIGHT_2_xor_INIT_CODE_SIZE has not been filled");
      }

      if (!filled.get(50)) {
        throw new IllegalStateException("DEP_STATUS_NEW_xor_EXP___FLAG_xor_CALL_EOA_SUCCESS_CALLER_WILL_REVERT_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE has not been filled");
      }

      if (!filled.get(49)) {
        throw new IllegalStateException("DEP_STATUS_xor_CCSR_FLAG_xor_CALL_ABORT_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559 has not been filled");
      }

      if (!filled.get(108)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_MMU___PARAM_2_xor_STACK_ITEM_HEIGHT_1_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE has not been filled");
      }

      if (!filled.get(48)) {
        throw new IllegalStateException("DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ABORT_FLAG_xor_BLAKE2f_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("DOM_STAMP has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("EXCEPTION_AHOY_FLAG has not been filled");
      }

      if (!filled.get(52)) {
        throw new IllegalStateException("EXISTS_NEW_xor_MMU___FLAG_xor_CALL_PRC_FAILURE_CALLER_WILL_REVERT_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG has not been filled");
      }

      if (!filled.get(51)) {
        throw new IllegalStateException("EXISTS_xor_FCOND_FLAG_xor_CALL_EOA_SUCCESS_CALLER_WONT_REVERT_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("GAS_ACTUAL has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("GAS_COST has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("GAS_EXPECTED has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("GAS_NEXT has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("GAS_REFUND has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("GAS_REFUND_NEW has not been filled");
      }

      if (!filled.get(54)) {
        throw new IllegalStateException("HAS_CODE_NEW_xor_MXP___DEPLOYS_xor_CALL_PRC_SUCCESS_CALLER_WILL_REVERT_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO has not been filled");
      }

      if (!filled.get(53)) {
        throw new IllegalStateException("HAS_CODE_xor_MMU___INFO_xor_CALL_PRC_FAILURE_CALLER_WONT_REVERT_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("HASH_INFO_STAMP has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("HUB_STAMP has not been filled");
      }

      if (!filled.get(27)) {
        throw new IllegalStateException("HUB_STAMP_TRANSACTION_END has not been filled");
      }

      if (!filled.get(55)) {
        throw new IllegalStateException("IS_BLAKE2f_xor_MXP___FLAG_xor_CALL_PRC_SUCCESS_CALLER_WONT_REVERT_xor_CREATE_FLAG_xor_WARM has not been filled");
      }

      if (!filled.get(56)) {
        throw new IllegalStateException("IS_ECADD_xor_MXP___MXPX_xor_CALL_SMC_FAILURE_CALLER_WILL_REVERT_xor_DECODED_FLAG_1_xor_WARM_NEW has not been filled");
      }

      if (!filled.get(57)) {
        throw new IllegalStateException("IS_ECMUL_xor_OOB___EVENT_1_xor_CALL_SMC_FAILURE_CALLER_WONT_REVERT_xor_DECODED_FLAG_2 has not been filled");
      }

      if (!filled.get(58)) {
        throw new IllegalStateException("IS_ECPAIRING_xor_OOB___EVENT_2_xor_CALL_SMC_SUCCESS_CALLER_WILL_REVERT_xor_DECODED_FLAG_3 has not been filled");
      }

      if (!filled.get(59)) {
        throw new IllegalStateException("IS_ECRECOVER_xor_OOB___FLAG_xor_CALL_SMC_SUCCESS_CALLER_WONT_REVERT_xor_DECODED_FLAG_4 has not been filled");
      }

      if (!filled.get(60)) {
        throw new IllegalStateException("IS_IDENTITY_xor_PRECINFO___FLAG_xor_CODEDEPOSIT_xor_DUP_FLAG has not been filled");
      }

      if (!filled.get(61)) {
        throw new IllegalStateException("IS_MODEXP_xor_STP___EXISTS_xor_CODEDEPOSIT_INVALID_CODE_PREFIX_xor_EXT_FLAG has not been filled");
      }

      if (!filled.get(62)) {
        throw new IllegalStateException("IS_PRECOMPILE_xor_STP___FLAG_xor_CODEDEPOSIT_VALID_CODE_PREFIX_xor_HALT_FLAG has not been filled");
      }

      if (!filled.get(63)) {
        throw new IllegalStateException("IS_RIPEMDsub160_xor_STP___OOGX_xor_ECADD_xor_HASH_INFO_FLAG has not been filled");
      }

      if (!filled.get(64)) {
        throw new IllegalStateException("IS_SHA2sub256_xor_STP___WARM_xor_ECMUL_xor_INVALID_FLAG has not been filled");
      }

      if (!filled.get(28)) {
        throw new IllegalStateException("MMU_STAMP has not been filled");
      }

      if (!filled.get(121)) {
        throw new IllegalStateException("MXP___SIZE_1_HI_xor_STACK_ITEM_VALUE_LO_2 has not been filled");
      }

      if (!filled.get(122)) {
        throw new IllegalStateException("MXP___SIZE_1_LO_xor_STACK_ITEM_VALUE_LO_3 has not been filled");
      }

      if (!filled.get(123)) {
        throw new IllegalStateException("MXP___SIZE_2_HI_xor_STACK_ITEM_VALUE_LO_4 has not been filled");
      }

      if (!filled.get(124)) {
        throw new IllegalStateException("MXP___SIZE_2_LO_xor_STATIC_GAS has not been filled");
      }

      if (!filled.get(29)) {
        throw new IllegalStateException("MXP_STAMP has not been filled");
      }

      if (!filled.get(125)) {
        throw new IllegalStateException("MXP___WORDS has not been filled");
      }

      if (!filled.get(112)) {
        throw new IllegalStateException("NONCE_NEW_xor_CONTEXT_NUMBER_xor_MMU___SIZE_xor_STACK_ITEM_STAMP_1_xor_NONCE has not been filled");
      }

      if (!filled.get(111)) {
        throw new IllegalStateException("NONCE_xor_CALL_VALUE_xor_MMU___RETURNER_xor_STACK_ITEM_HEIGHT_4_xor_LEFTOVER_GAS has not been filled");
      }

      if (!filled.get(30)) {
        throw new IllegalStateException("NUMBER_OF_NON_STACK_ROWS has not been filled");
      }

      if (!filled.get(126)) {
        throw new IllegalStateException("OOB___INST has not been filled");
      }

      if (!filled.get(127)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_1 has not been filled");
      }

      if (!filled.get(128)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_2 has not been filled");
      }

      if (!filled.get(129)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_3 has not been filled");
      }

      if (!filled.get(130)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_4 has not been filled");
      }

      if (!filled.get(131)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_5 has not been filled");
      }

      if (!filled.get(132)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_6 has not been filled");
      }

      if (!filled.get(31)) {
        throw new IllegalStateException("PEEK_AT_ACCOUNT has not been filled");
      }

      if (!filled.get(32)) {
        throw new IllegalStateException("PEEK_AT_CONTEXT has not been filled");
      }

      if (!filled.get(33)) {
        throw new IllegalStateException("PEEK_AT_MISCELLANEOUS has not been filled");
      }

      if (!filled.get(34)) {
        throw new IllegalStateException("PEEK_AT_SCENARIO has not been filled");
      }

      if (!filled.get(35)) {
        throw new IllegalStateException("PEEK_AT_STACK has not been filled");
      }

      if (!filled.get(36)) {
        throw new IllegalStateException("PEEK_AT_STORAGE has not been filled");
      }

      if (!filled.get(37)) {
        throw new IllegalStateException("PEEK_AT_TRANSACTION has not been filled");
      }

      if (!filled.get(133)) {
        throw new IllegalStateException("PRECINFO___ADDR_LO has not been filled");
      }

      if (!filled.get(134)) {
        throw new IllegalStateException("PRECINFO___CDS has not been filled");
      }

      if (!filled.get(135)) {
        throw new IllegalStateException("PRECINFO___EXEC_COST has not been filled");
      }

      if (!filled.get(136)) {
        throw new IllegalStateException("PRECINFO___PROVIDES_RETURN_DATA has not been filled");
      }

      if (!filled.get(137)) {
        throw new IllegalStateException("PRECINFO___RDS has not been filled");
      }

      if (!filled.get(138)) {
        throw new IllegalStateException("PRECINFO___SUCCESS has not been filled");
      }

      if (!filled.get(139)) {
        throw new IllegalStateException("PRECINFO___TOUCHES_RAM has not been filled");
      }

      if (!filled.get(38)) {
        throw new IllegalStateException("PROGRAM_COUNTER has not been filled");
      }

      if (!filled.get(39)) {
        throw new IllegalStateException("PROGRAM_COUNTER_NEW has not been filled");
      }

      if (!filled.get(80)) {
        throw new IllegalStateException("PUSHPOP_FLAG has not been filled");
      }

      if (!filled.get(81)) {
        throw new IllegalStateException("RDCX has not been filled");
      }

      if (!filled.get(69)) {
        throw new IllegalStateException("RIPEMDsub160_xor_KEC_FLAG has not been filled");
      }

      if (!filled.get(113)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_MMU___STACK_VAL_HI_xor_STACK_ITEM_STAMP_2_xor_TO_ADDRESS_HI has not been filled");
      }

      if (!filled.get(114)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_MMU___STACK_VAL_LO_xor_STACK_ITEM_STAMP_3_xor_TO_ADDRESS_LO has not been filled");
      }

      if (!filled.get(65)) {
        throw new IllegalStateException("RLPADDR___FLAG_xor_ECPAIRING_xor_INVPREX has not been filled");
      }

      if (!filled.get(115)) {
        throw new IllegalStateException("RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_MXP___GAS_MXP_xor_STACK_ITEM_STAMP_4_xor_VALUE has not been filled");
      }

      if (!filled.get(116)) {
        throw new IllegalStateException("RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_MXP___INST_xor_STACK_ITEM_VALUE_HI_1 has not been filled");
      }

      if (!filled.get(117)) {
        throw new IllegalStateException("RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_MXP___OFFSET_1_HI_xor_STACK_ITEM_VALUE_HI_2 has not been filled");
      }

      if (!filled.get(118)) {
        throw new IllegalStateException("RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_MXP___OFFSET_1_LO_xor_STACK_ITEM_VALUE_HI_3 has not been filled");
      }

      if (!filled.get(119)) {
        throw new IllegalStateException("RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_MXP___OFFSET_2_HI_xor_STACK_ITEM_VALUE_HI_4 has not been filled");
      }

      if (!filled.get(70)) {
        throw new IllegalStateException("SCN_FAILURE_1_xor_LOG_FLAG has not been filled");
      }

      if (!filled.get(71)) {
        throw new IllegalStateException("SCN_FAILURE_2_xor_MACHINE_STATE_FLAG has not been filled");
      }

      if (!filled.get(72)) {
        throw new IllegalStateException("SCN_FAILURE_3_xor_MAXCSX has not been filled");
      }

      if (!filled.get(73)) {
        throw new IllegalStateException("SCN_FAILURE_4_xor_MOD_FLAG has not been filled");
      }

      if (!filled.get(74)) {
        throw new IllegalStateException("SCN_SUCCESS_1_xor_MUL_FLAG has not been filled");
      }

      if (!filled.get(75)) {
        throw new IllegalStateException("SCN_SUCCESS_2_xor_MXPX has not been filled");
      }

      if (!filled.get(76)) {
        throw new IllegalStateException("SCN_SUCCESS_3_xor_MXP_FLAG has not been filled");
      }

      if (!filled.get(77)) {
        throw new IllegalStateException("SCN_SUCCESS_4_xor_OOB_FLAG has not been filled");
      }

      if (!filled.get(78)) {
        throw new IllegalStateException("SELFDESTRUCT_xor_OOGX has not been filled");
      }

      if (!filled.get(79)) {
        throw new IllegalStateException("SHA2sub256_xor_OPCX has not been filled");
      }

      if (!filled.get(82)) {
        throw new IllegalStateException("SHF_FLAG has not been filled");
      }

      if (!filled.get(83)) {
        throw new IllegalStateException("SOX has not been filled");
      }

      if (!filled.get(84)) {
        throw new IllegalStateException("SSTOREX has not been filled");
      }

      if (!filled.get(86)) {
        throw new IllegalStateException("STACK_ITEM_POP_1 has not been filled");
      }

      if (!filled.get(87)) {
        throw new IllegalStateException("STACK_ITEM_POP_2 has not been filled");
      }

      if (!filled.get(88)) {
        throw new IllegalStateException("STACK_ITEM_POP_3 has not been filled");
      }

      if (!filled.get(89)) {
        throw new IllegalStateException("STACK_ITEM_POP_4 has not been filled");
      }

      if (!filled.get(85)) {
        throw new IllegalStateException("STACKRAM_FLAG has not been filled");
      }

      if (!filled.get(91)) {
        throw new IllegalStateException("STATIC_FLAG has not been filled");
      }

      if (!filled.get(90)) {
        throw new IllegalStateException("STATICX has not been filled");
      }

      if (!filled.get(92)) {
        throw new IllegalStateException("STO_FLAG has not been filled");
      }

      if (!filled.get(140)) {
        throw new IllegalStateException("STP___GAS_HI has not been filled");
      }

      if (!filled.get(141)) {
        throw new IllegalStateException("STP___GAS_LO has not been filled");
      }

      if (!filled.get(142)) {
        throw new IllegalStateException("STP___GAS_OOPKT has not been filled");
      }

      if (!filled.get(143)) {
        throw new IllegalStateException("STP___GAS_STPD has not been filled");
      }

      if (!filled.get(144)) {
        throw new IllegalStateException("STP___INST has not been filled");
      }

      if (!filled.get(145)) {
        throw new IllegalStateException("STP___VAL_HI has not been filled");
      }

      if (!filled.get(146)) {
        throw new IllegalStateException("STP___VAL_LO has not been filled");
      }

      if (!filled.get(40)) {
        throw new IllegalStateException("SUB_STAMP has not been filled");
      }

      if (!filled.get(93)) {
        throw new IllegalStateException("SUX has not been filled");
      }

      if (!filled.get(94)) {
        throw new IllegalStateException("SWAP_FLAG has not been filled");
      }

      if (!filled.get(41)) {
        throw new IllegalStateException("TRANSACTION_REVERTS has not been filled");
      }

      if (!filled.get(95)) {
        throw new IllegalStateException("TRM_FLAG has not been filled");
      }

      if (!filled.get(66)) {
        throw new IllegalStateException("TRM___FLAG_xor_ECRECOVER_xor_JUMPX has not been filled");
      }

      if (!filled.get(120)) {
        throw new IllegalStateException("TRM___RAW_ADDR_HI_xor_MXP___OFFSET_2_LO_xor_STACK_ITEM_VALUE_LO_1 has not been filled");
      }

      if (!filled.get(42)) {
        throw new IllegalStateException("TWO_LINE_INSTRUCTION has not been filled");
      }

      if (!filled.get(43)) {
        throw new IllegalStateException("TX_EXEC has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException("TX_FINL has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException("TX_INIT has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException("TX_SKIP has not been filled");
      }

      if (!filled.get(47)) {
        throw new IllegalStateException("TX_WARM has not been filled");
      }

      if (!filled.get(96)) {
        throw new IllegalStateException("TXN_FLAG has not been filled");
      }

      if (!filled.get(68)) {
        throw new IllegalStateException("WARM_NEW_xor_MODEXP_xor_JUMP_FLAG has not been filled");
      }

      if (!filled.get(67)) {
        throw new IllegalStateException("WARM_xor_IDENTITY_xor_JUMP_DESTINATION_VETTING_REQUIRED has not been filled");
      }

      if (!filled.get(97)) {
        throw new IllegalStateException("WCP_FLAG has not been filled");
      }


      filled.clear();
      this.currentLength++;

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      filled.clear();
      this.currentLength++;
      return this;
    }
  }

}
