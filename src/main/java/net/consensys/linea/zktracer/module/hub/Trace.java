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
import net.consensys.linea.zktracer.bytes.UnsignedByte;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
  @JsonProperty("ABORT_FLAG") List<Boolean> abortFlag,
  @JsonProperty("ABSOLUTE_TRANSACTION_NUMBER") List<BigInteger> absoluteTransactionNumber,
  @JsonProperty("ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_HEIGHT_xor_ADDRESS_HI_xor_BASEFEE") List<BigInteger> addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee,
  @JsonProperty("ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_HEIGHT_NEW_xor_ADDRESS_LO_xor_CALL_DATA_SIZE") List<BigInteger> addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize,
  @JsonProperty("BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_HEIGHT_UNDER_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO") List<BigInteger> balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo,
  @JsonProperty("BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_HEIGHT_OVER_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI") List<BigInteger> balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi,
  @JsonProperty("BATCH_NUMBER") List<BigInteger> batchNumber,
  @JsonProperty("CALLER_CONTEXT_NUMBER") List<BigInteger> callerContextNumber,
  @JsonProperty("CODE_ADDRESS_HI") List<BigInteger> codeAddressHi,
  @JsonProperty("CODE_ADDRESS_LO") List<BigInteger> codeAddressLo,
  @JsonProperty("CODE_DEPLOYMENT_NUMBER") List<BigInteger> codeDeploymentNumber,
  @JsonProperty("CODE_DEPLOYMENT_STATUS") List<Boolean> codeDeploymentStatus,
  @JsonProperty("CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_PUSH_VALUE_HI_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO") List<BigInteger> codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo,
  @JsonProperty("CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_INSTRUCTION_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI") List<BigInteger> codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi,
  @JsonProperty("CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_STACK_ITEM_HEIGHT_1_xor_VAL_NEXT_HI_xor_GAS_PRICE") List<BigInteger> codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice,
  @JsonProperty("CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_PUSH_VALUE_LO_xor_VAL_CURR_LO_xor_GAS_LIMIT") List<BigInteger> codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit,
  @JsonProperty("CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_STACK_ITEM_HEIGHT_3_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL") List<BigInteger> codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal,
  @JsonProperty("CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_STACK_ITEM_HEIGHT_2_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT") List<BigInteger> codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount,
  @JsonProperty("CONTEXT_GETS_REVRTD_FLAG") List<Boolean> contextGetsRevrtdFlag,
  @JsonProperty("CONTEXT_MAY_CHANGE_FLAG") List<Boolean> contextMayChangeFlag,
  @JsonProperty("CONTEXT_NUMBER") List<BigInteger> contextNumber,
  @JsonProperty("CONTEXT_NUMBER_NEW") List<BigInteger> contextNumberNew,
  @JsonProperty("CONTEXT_REVERT_STAMP") List<BigInteger> contextRevertStamp,
  @JsonProperty("CONTEXT_SELF_REVRTS_FLAG") List<Boolean> contextSelfRevrtsFlag,
  @JsonProperty("CONTEXT_WILL_REVERT_FLAG") List<Boolean> contextWillRevertFlag,
  @JsonProperty("COUNTER_NSR") List<BigInteger> counterNsr,
  @JsonProperty("COUNTER_TLI") List<Boolean> counterTli,
  @JsonProperty("DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_STACK_ITEM_STAMP_2_xor_INIT_GAS") List<BigInteger> depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas,
  @JsonProperty("DEP_NUM_xor_CALL_DATA_SIZE_xor_STACK_ITEM_STAMP_1_xor_INIT_CODE_SIZE") List<BigInteger> depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize,
  @JsonProperty("DEP_STATUS_NEW_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE") List<Boolean> depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode,
  @JsonProperty("DEP_STATUS_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559") List<Boolean> depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559,
  @JsonProperty("DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_STACK_ITEM_HEIGHT_4_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE") List<BigInteger> deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance,
  @JsonProperty("DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT") List<Boolean> deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment,
  @JsonProperty("EXCEPTION_AHOY_FLAG") List<Boolean> exceptionAhoyFlag,
  @JsonProperty("EXISTS_NEW_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG") List<Boolean> existsNewXorCallFlagXorValNextIsOrig,
  @JsonProperty("EXISTS_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION") List<Boolean> existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution,
  @JsonProperty("FAILURE_CONDITION_FLAG") List<Boolean> failureConditionFlag,
  @JsonProperty("GAS_ACTUAL") List<BigInteger> gasActual,
  @JsonProperty("GAS_COST") List<BigInteger> gasCost,
  @JsonProperty("GAS_EXPECTED") List<BigInteger> gasExpected,
  @JsonProperty("GAS_MEMORY_EXPANSION") List<BigInteger> gasMemoryExpansion,
  @JsonProperty("GAS_NEXT") List<BigInteger> gasNext,
  @JsonProperty("GAS_REFUND") List<BigInteger> gasRefund,
  @JsonProperty("HAS_CODE_NEW_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO") List<Boolean> hasCodeNewXorCopyFlagXorValOrigIsZero,
  @JsonProperty("HAS_CODE_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO") List<Boolean> hasCodeXorConFlagXorValNextIsZero,
  @JsonProperty("HUB_STAMP") List<BigInteger> hubStamp,
  @JsonProperty("IS_BLAKE2f_xor_CREATE_FLAG_xor_WARM") List<Boolean> isBlake2FXorCreateFlagXorWarm,
  @JsonProperty("IS_ECADD_xor_DECODED_FLAG_1_xor_WARM_NEW") List<Boolean> isEcaddXorDecodedFlag1XorWarmNew,
  @JsonProperty("IS_ECMUL_xor_DECODED_FLAG_2") List<Boolean> isEcmulXorDecodedFlag2,
  @JsonProperty("IS_ECPAIRING_xor_DECODED_FLAG_3") List<Boolean> isEcpairingXorDecodedFlag3,
  @JsonProperty("IS_ECRECOVER_xor_DECODED_FLAG_4") List<Boolean> isEcrecoverXorDecodedFlag4,
  @JsonProperty("IS_IDENTITY_xor_DUP_FLAG") List<Boolean> isIdentityXorDupFlag,
  @JsonProperty("IS_MODEXP_xor_EXT_FLAG") List<Boolean> isModexpXorExtFlag,
  @JsonProperty("IS_PRECOMPILE_xor_HALT_FLAG") List<Boolean> isPrecompileXorHaltFlag,
  @JsonProperty("IS_RIPEMDsub160_xor_INVALID_FLAG") List<Boolean> isRipemDsub160XorInvalidFlag,
  @JsonProperty("IS_SHA2sub256_xor_INVPREX") List<Boolean> isSha2Sub256XorInvprex,
  @JsonProperty("MAXCSX") List<Boolean> maxcsx,
  @JsonProperty("MOD_FLAG") List<Boolean> modFlag,
  @JsonProperty("MUL_FLAG") List<Boolean> mulFlag,
  @JsonProperty("MXP_FLAG") List<Boolean> mxpFlag,
  @JsonProperty("MXPX") List<Boolean> mxpx,
  @JsonProperty("NONCE_NEW_xor_CONTEXT_NUMBER_xor_STACK_ITEM_STAMP_4_xor_NONCE") List<BigInteger> nonceNewXorContextNumberXorStackItemStamp4XorNonce,
  @JsonProperty("NONCE_xor_CALL_VALUE_xor_STACK_ITEM_STAMP_3_xor_LEFTOVER_GAS") List<BigInteger> nonceXorCallValueXorStackItemStamp3XorLeftoverGas,
  @JsonProperty("NUMBER_OF_NON_STACK_ROWS") List<BigInteger> numberOfNonStackRows,
  @JsonProperty("OOB_FLAG") List<Boolean> oobFlag,
  @JsonProperty("OOGX") List<Boolean> oogx,
  @JsonProperty("OPCX") List<Boolean> opcx,
  @JsonProperty("PEEK_AT_ACCOUNT") List<Boolean> peekAtAccount,
  @JsonProperty("PEEK_AT_CONTEXT") List<Boolean> peekAtContext,
  @JsonProperty("PEEK_AT_MISCELLANEOUS") List<Boolean> peekAtMiscellaneous,
  @JsonProperty("PEEK_AT_SCENARIO") List<Boolean> peekAtScenario,
  @JsonProperty("PEEK_AT_STACK") List<Boolean> peekAtStack,
  @JsonProperty("PEEK_AT_STORAGE") List<Boolean> peekAtStorage,
  @JsonProperty("PEEK_AT_TRANSACTION") List<Boolean> peekAtTransaction,
  @JsonProperty("PROGRAM_COUNTER") List<BigInteger> programCounter,
  @JsonProperty("PROGRAM_COUNTER_NEW") List<BigInteger> programCounterNew,
  @JsonProperty("PUSHPOP_FLAG") List<Boolean> pushpopFlag,
  @JsonProperty("RDCX") List<Boolean> rdcx,
  @JsonProperty("RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_STACK_ITEM_VALUE_HI_1_xor_TO_ADDRESS_HI") List<BigInteger> rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi,
  @JsonProperty("RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_STACK_ITEM_VALUE_HI_2_xor_TO_ADDRESS_LO") List<BigInteger> rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo,
  @JsonProperty("RLPADDR___FLAG_xor_JUMPX") List<Boolean> rlpaddrFlagXorJumpx,
  @JsonProperty("RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_STACK_ITEM_VALUE_HI_3_xor_VALUE") List<BigInteger> rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue,
  @JsonProperty("RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_STACK_ITEM_VALUE_HI_4") List<BigInteger> rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4,
  @JsonProperty("RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_STACK_ITEM_VALUE_LO_1") List<BigInteger> rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1,
  @JsonProperty("RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_STACK_ITEM_VALUE_LO_2") List<BigInteger> rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2,
  @JsonProperty("RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_STACK_ITEM_VALUE_LO_3") List<BigInteger> rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3,
  @JsonProperty("SHF_FLAG") List<Boolean> shfFlag,
  @JsonProperty("SOX") List<Boolean> sox,
  @JsonProperty("SSTOREX") List<Boolean> sstorex,
  @JsonProperty("STACK_ITEM_POP_1") List<Boolean> stackItemPop1,
  @JsonProperty("STACK_ITEM_POP_2") List<Boolean> stackItemPop2,
  @JsonProperty("STACK_ITEM_POP_3") List<Boolean> stackItemPop3,
  @JsonProperty("STACK_ITEM_POP_4") List<Boolean> stackItemPop4,
  @JsonProperty("STACKRAM_FLAG") List<Boolean> stackramFlag,
  @JsonProperty("STATIC_FLAG") List<Boolean> staticFlag,
  @JsonProperty("STATIC_GAS") List<BigInteger> staticGas,
  @JsonProperty("STATICX") List<Boolean> staticx,
  @JsonProperty("STO_FLAG") List<Boolean> stoFlag,
  @JsonProperty("SUX") List<Boolean> sux,
  @JsonProperty("SWAP_FLAG") List<Boolean> swapFlag,
  @JsonProperty("TRANSACTION_END_STAMP") List<BigInteger> transactionEndStamp,
  @JsonProperty("TRANSACTION_REVERTS") List<BigInteger> transactionReverts,
  @JsonProperty("TRM_FLAG") List<Boolean> trmFlag,
  @JsonProperty("TRM___FLAG_xor_JUMP_FLAG") List<Boolean> trmFlagXorJumpFlag,
  @JsonProperty("TRM___RAW_ADDR_HI_xor_STACK_ITEM_VALUE_LO_4") List<BigInteger> trmRawAddrHiXorStackItemValueLo4,
  @JsonProperty("TWO_LINE_INSTRUCTION") List<Boolean> twoLineInstruction,
  @JsonProperty("TX_EXEC") List<Boolean> txExec,
  @JsonProperty("TX_FINL") List<Boolean> txFinl,
  @JsonProperty("TX_INIT") List<Boolean> txInit,
  @JsonProperty("TX_SKIP") List<Boolean> txSkip,
  @JsonProperty("TX_WARM") List<Boolean> txWarm,
  @JsonProperty("TXN_FLAG") List<Boolean> txnFlag,
  @JsonProperty("WARM_NEW_xor_LOG_FLAG") List<Boolean> warmNewXorLogFlag,
  @JsonProperty("WARM_xor_KEC_FLAG") List<Boolean> warmXorKecFlag,
  @JsonProperty("WCP_FLAG") List<Boolean> wcpFlag) { 
  static TraceBuilder builder() {
    return new TraceBuilder();
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ABORT_FLAG")
    private final List<Boolean> abortFlag = new ArrayList<>();
    @JsonProperty("ABSOLUTE_TRANSACTION_NUMBER")
    private final List<BigInteger> absoluteTransactionNumber = new ArrayList<>();
    @JsonProperty("ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_HEIGHT_xor_ADDRESS_HI_xor_BASEFEE")
    private final List<BigInteger> addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee = new ArrayList<>();
    @JsonProperty("ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_HEIGHT_NEW_xor_ADDRESS_LO_xor_CALL_DATA_SIZE")
    private final List<BigInteger> addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize = new ArrayList<>();
    @JsonProperty("BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_HEIGHT_UNDER_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO")
    private final List<BigInteger> balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo = new ArrayList<>();
    @JsonProperty("BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_HEIGHT_OVER_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI")
    private final List<BigInteger> balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi = new ArrayList<>();
    @JsonProperty("BATCH_NUMBER")
    private final List<BigInteger> batchNumber = new ArrayList<>();
    @JsonProperty("CALLER_CONTEXT_NUMBER")
    private final List<BigInteger> callerContextNumber = new ArrayList<>();
    @JsonProperty("CODE_ADDRESS_HI")
    private final List<BigInteger> codeAddressHi = new ArrayList<>();
    @JsonProperty("CODE_ADDRESS_LO")
    private final List<BigInteger> codeAddressLo = new ArrayList<>();
    @JsonProperty("CODE_DEPLOYMENT_NUMBER")
    private final List<BigInteger> codeDeploymentNumber = new ArrayList<>();
    @JsonProperty("CODE_DEPLOYMENT_STATUS")
    private final List<Boolean> codeDeploymentStatus = new ArrayList<>();
    @JsonProperty("CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_PUSH_VALUE_HI_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO")
    private final List<BigInteger> codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo = new ArrayList<>();
    @JsonProperty("CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_INSTRUCTION_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI")
    private final List<BigInteger> codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi = new ArrayList<>();
    @JsonProperty("CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_STACK_ITEM_HEIGHT_1_xor_VAL_NEXT_HI_xor_GAS_PRICE")
    private final List<BigInteger> codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice = new ArrayList<>();
    @JsonProperty("CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_PUSH_VALUE_LO_xor_VAL_CURR_LO_xor_GAS_LIMIT")
    private final List<BigInteger> codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit = new ArrayList<>();
    @JsonProperty("CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_STACK_ITEM_HEIGHT_3_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL")
    private final List<BigInteger> codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal = new ArrayList<>();
    @JsonProperty("CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_STACK_ITEM_HEIGHT_2_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT")
    private final List<BigInteger> codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount = new ArrayList<>();
    @JsonProperty("CONTEXT_GETS_REVRTD_FLAG")
    private final List<Boolean> contextGetsRevrtdFlag = new ArrayList<>();
    @JsonProperty("CONTEXT_MAY_CHANGE_FLAG")
    private final List<Boolean> contextMayChangeFlag = new ArrayList<>();
    @JsonProperty("CONTEXT_NUMBER")
    private final List<BigInteger> contextNumber = new ArrayList<>();
    @JsonProperty("CONTEXT_NUMBER_NEW")
    private final List<BigInteger> contextNumberNew = new ArrayList<>();
    @JsonProperty("CONTEXT_REVERT_STAMP")
    private final List<BigInteger> contextRevertStamp = new ArrayList<>();
    @JsonProperty("CONTEXT_SELF_REVRTS_FLAG")
    private final List<Boolean> contextSelfRevrtsFlag = new ArrayList<>();
    @JsonProperty("CONTEXT_WILL_REVERT_FLAG")
    private final List<Boolean> contextWillRevertFlag = new ArrayList<>();
    @JsonProperty("COUNTER_NSR")
    private final List<BigInteger> counterNsr = new ArrayList<>();
    @JsonProperty("COUNTER_TLI")
    private final List<Boolean> counterTli = new ArrayList<>();
    @JsonProperty("DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_STACK_ITEM_STAMP_2_xor_INIT_GAS")
    private final List<BigInteger> depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas = new ArrayList<>();
    @JsonProperty("DEP_NUM_xor_CALL_DATA_SIZE_xor_STACK_ITEM_STAMP_1_xor_INIT_CODE_SIZE")
    private final List<BigInteger> depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize = new ArrayList<>();
    @JsonProperty("DEP_STATUS_NEW_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE")
    private final List<Boolean> depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode = new ArrayList<>();
    @JsonProperty("DEP_STATUS_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559")
    private final List<Boolean> depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559 = new ArrayList<>();
    @JsonProperty("DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_STACK_ITEM_HEIGHT_4_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE")
    private final List<BigInteger> deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance = new ArrayList<>();
    @JsonProperty("DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT")
    private final List<Boolean> deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment = new ArrayList<>();
    @JsonProperty("EXCEPTION_AHOY_FLAG")
    private final List<Boolean> exceptionAhoyFlag = new ArrayList<>();
    @JsonProperty("EXISTS_NEW_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG")
    private final List<Boolean> existsNewXorCallFlagXorValNextIsOrig = new ArrayList<>();
    @JsonProperty("EXISTS_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION")
    private final List<Boolean> existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution = new ArrayList<>();
    @JsonProperty("FAILURE_CONDITION_FLAG")
    private final List<Boolean> failureConditionFlag = new ArrayList<>();
    @JsonProperty("GAS_ACTUAL")
    private final List<BigInteger> gasActual = new ArrayList<>();
    @JsonProperty("GAS_COST")
    private final List<BigInteger> gasCost = new ArrayList<>();
    @JsonProperty("GAS_EXPECTED")
    private final List<BigInteger> gasExpected = new ArrayList<>();
    @JsonProperty("GAS_MEMORY_EXPANSION")
    private final List<BigInteger> gasMemoryExpansion = new ArrayList<>();
    @JsonProperty("GAS_NEXT")
    private final List<BigInteger> gasNext = new ArrayList<>();
    @JsonProperty("GAS_REFUND")
    private final List<BigInteger> gasRefund = new ArrayList<>();
    @JsonProperty("HAS_CODE_NEW_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO")
    private final List<Boolean> hasCodeNewXorCopyFlagXorValOrigIsZero = new ArrayList<>();
    @JsonProperty("HAS_CODE_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO")
    private final List<Boolean> hasCodeXorConFlagXorValNextIsZero = new ArrayList<>();
    @JsonProperty("HUB_STAMP")
    private final List<BigInteger> hubStamp = new ArrayList<>();
    @JsonProperty("IS_BLAKE2f_xor_CREATE_FLAG_xor_WARM")
    private final List<Boolean> isBlake2FXorCreateFlagXorWarm = new ArrayList<>();
    @JsonProperty("IS_ECADD_xor_DECODED_FLAG_1_xor_WARM_NEW")
    private final List<Boolean> isEcaddXorDecodedFlag1XorWarmNew = new ArrayList<>();
    @JsonProperty("IS_ECMUL_xor_DECODED_FLAG_2")
    private final List<Boolean> isEcmulXorDecodedFlag2 = new ArrayList<>();
    @JsonProperty("IS_ECPAIRING_xor_DECODED_FLAG_3")
    private final List<Boolean> isEcpairingXorDecodedFlag3 = new ArrayList<>();
    @JsonProperty("IS_ECRECOVER_xor_DECODED_FLAG_4")
    private final List<Boolean> isEcrecoverXorDecodedFlag4 = new ArrayList<>();
    @JsonProperty("IS_IDENTITY_xor_DUP_FLAG")
    private final List<Boolean> isIdentityXorDupFlag = new ArrayList<>();
    @JsonProperty("IS_MODEXP_xor_EXT_FLAG")
    private final List<Boolean> isModexpXorExtFlag = new ArrayList<>();
    @JsonProperty("IS_PRECOMPILE_xor_HALT_FLAG")
    private final List<Boolean> isPrecompileXorHaltFlag = new ArrayList<>();
    @JsonProperty("IS_RIPEMDsub160_xor_INVALID_FLAG")
    private final List<Boolean> isRipemDsub160XorInvalidFlag = new ArrayList<>();
    @JsonProperty("IS_SHA2sub256_xor_INVPREX")
    private final List<Boolean> isSha2Sub256XorInvprex = new ArrayList<>();
    @JsonProperty("MAXCSX")
    private final List<Boolean> maxcsx = new ArrayList<>();
    @JsonProperty("MOD_FLAG")
    private final List<Boolean> modFlag = new ArrayList<>();
    @JsonProperty("MUL_FLAG")
    private final List<Boolean> mulFlag = new ArrayList<>();
    @JsonProperty("MXP_FLAG")
    private final List<Boolean> mxpFlag = new ArrayList<>();
    @JsonProperty("MXPX")
    private final List<Boolean> mxpx = new ArrayList<>();
    @JsonProperty("NONCE_NEW_xor_CONTEXT_NUMBER_xor_STACK_ITEM_STAMP_4_xor_NONCE")
    private final List<BigInteger> nonceNewXorContextNumberXorStackItemStamp4XorNonce = new ArrayList<>();
    @JsonProperty("NONCE_xor_CALL_VALUE_xor_STACK_ITEM_STAMP_3_xor_LEFTOVER_GAS")
    private final List<BigInteger> nonceXorCallValueXorStackItemStamp3XorLeftoverGas = new ArrayList<>();
    @JsonProperty("NUMBER_OF_NON_STACK_ROWS")
    private final List<BigInteger> numberOfNonStackRows = new ArrayList<>();
    @JsonProperty("OOB_FLAG")
    private final List<Boolean> oobFlag = new ArrayList<>();
    @JsonProperty("OOGX")
    private final List<Boolean> oogx = new ArrayList<>();
    @JsonProperty("OPCX")
    private final List<Boolean> opcx = new ArrayList<>();
    @JsonProperty("PEEK_AT_ACCOUNT")
    private final List<Boolean> peekAtAccount = new ArrayList<>();
    @JsonProperty("PEEK_AT_CONTEXT")
    private final List<Boolean> peekAtContext = new ArrayList<>();
    @JsonProperty("PEEK_AT_MISCELLANEOUS")
    private final List<Boolean> peekAtMiscellaneous = new ArrayList<>();
    @JsonProperty("PEEK_AT_SCENARIO")
    private final List<Boolean> peekAtScenario = new ArrayList<>();
    @JsonProperty("PEEK_AT_STACK")
    private final List<Boolean> peekAtStack = new ArrayList<>();
    @JsonProperty("PEEK_AT_STORAGE")
    private final List<Boolean> peekAtStorage = new ArrayList<>();
    @JsonProperty("PEEK_AT_TRANSACTION")
    private final List<Boolean> peekAtTransaction = new ArrayList<>();
    @JsonProperty("PROGRAM_COUNTER")
    private final List<BigInteger> programCounter = new ArrayList<>();
    @JsonProperty("PROGRAM_COUNTER_NEW")
    private final List<BigInteger> programCounterNew = new ArrayList<>();
    @JsonProperty("PUSHPOP_FLAG")
    private final List<Boolean> pushpopFlag = new ArrayList<>();
    @JsonProperty("RDCX")
    private final List<Boolean> rdcx = new ArrayList<>();
    @JsonProperty("RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_STACK_ITEM_VALUE_HI_1_xor_TO_ADDRESS_HI")
    private final List<BigInteger> rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi = new ArrayList<>();
    @JsonProperty("RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_STACK_ITEM_VALUE_HI_2_xor_TO_ADDRESS_LO")
    private final List<BigInteger> rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo = new ArrayList<>();
    @JsonProperty("RLPADDR___FLAG_xor_JUMPX")
    private final List<Boolean> rlpaddrFlagXorJumpx = new ArrayList<>();
    @JsonProperty("RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_STACK_ITEM_VALUE_HI_3_xor_VALUE")
    private final List<BigInteger> rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue = new ArrayList<>();
    @JsonProperty("RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_STACK_ITEM_VALUE_HI_4")
    private final List<BigInteger> rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4 = new ArrayList<>();
    @JsonProperty("RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_STACK_ITEM_VALUE_LO_1")
    private final List<BigInteger> rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1 = new ArrayList<>();
    @JsonProperty("RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_STACK_ITEM_VALUE_LO_2")
    private final List<BigInteger> rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2 = new ArrayList<>();
    @JsonProperty("RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_STACK_ITEM_VALUE_LO_3")
    private final List<BigInteger> rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3 = new ArrayList<>();
    @JsonProperty("SHF_FLAG")
    private final List<Boolean> shfFlag = new ArrayList<>();
    @JsonProperty("SOX")
    private final List<Boolean> sox = new ArrayList<>();
    @JsonProperty("SSTOREX")
    private final List<Boolean> sstorex = new ArrayList<>();
    @JsonProperty("STACK_ITEM_POP_1")
    private final List<Boolean> stackItemPop1 = new ArrayList<>();
    @JsonProperty("STACK_ITEM_POP_2")
    private final List<Boolean> stackItemPop2 = new ArrayList<>();
    @JsonProperty("STACK_ITEM_POP_3")
    private final List<Boolean> stackItemPop3 = new ArrayList<>();
    @JsonProperty("STACK_ITEM_POP_4")
    private final List<Boolean> stackItemPop4 = new ArrayList<>();
    @JsonProperty("STACKRAM_FLAG")
    private final List<Boolean> stackramFlag = new ArrayList<>();
    @JsonProperty("STATIC_FLAG")
    private final List<Boolean> staticFlag = new ArrayList<>();
    @JsonProperty("STATIC_GAS")
    private final List<BigInteger> staticGas = new ArrayList<>();
    @JsonProperty("STATICX")
    private final List<Boolean> staticx = new ArrayList<>();
    @JsonProperty("STO_FLAG")
    private final List<Boolean> stoFlag = new ArrayList<>();
    @JsonProperty("SUX")
    private final List<Boolean> sux = new ArrayList<>();
    @JsonProperty("SWAP_FLAG")
    private final List<Boolean> swapFlag = new ArrayList<>();
    @JsonProperty("TRANSACTION_END_STAMP")
    private final List<BigInteger> transactionEndStamp = new ArrayList<>();
    @JsonProperty("TRANSACTION_REVERTS")
    private final List<BigInteger> transactionReverts = new ArrayList<>();
    @JsonProperty("TRM_FLAG")
    private final List<Boolean> trmFlag = new ArrayList<>();
    @JsonProperty("TRM___FLAG_xor_JUMP_FLAG")
    private final List<Boolean> trmFlagXorJumpFlag = new ArrayList<>();
    @JsonProperty("TRM___RAW_ADDR_HI_xor_STACK_ITEM_VALUE_LO_4")
    private final List<BigInteger> trmRawAddrHiXorStackItemValueLo4 = new ArrayList<>();
    @JsonProperty("TWO_LINE_INSTRUCTION")
    private final List<Boolean> twoLineInstruction = new ArrayList<>();
    @JsonProperty("TX_EXEC")
    private final List<Boolean> txExec = new ArrayList<>();
    @JsonProperty("TX_FINL")
    private final List<Boolean> txFinl = new ArrayList<>();
    @JsonProperty("TX_INIT")
    private final List<Boolean> txInit = new ArrayList<>();
    @JsonProperty("TX_SKIP")
    private final List<Boolean> txSkip = new ArrayList<>();
    @JsonProperty("TX_WARM")
    private final List<Boolean> txWarm = new ArrayList<>();
    @JsonProperty("TXN_FLAG")
    private final List<Boolean> txnFlag = new ArrayList<>();
    @JsonProperty("WARM_NEW_xor_LOG_FLAG")
    private final List<Boolean> warmNewXorLogFlag = new ArrayList<>();
    @JsonProperty("WARM_xor_KEC_FLAG")
    private final List<Boolean> warmXorKecFlag = new ArrayList<>();
    @JsonProperty("WCP_FLAG")
    private final List<Boolean> wcpFlag = new ArrayList<>();

    private TraceBuilder() {}

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.abortFlag.size();
    }

    public TraceBuilder abortFlag(final Boolean b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ABORT_FLAG already set");
      } else {
        filled.set(0);
      }

      abortFlag.add(b);

      return this;
    }

    public TraceBuilder absoluteTransactionNumber(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ABSOLUTE_TRANSACTION_NUMBER already set");
      } else {
        filled.set(1);
      }

      absoluteTransactionNumber.add(b);

      return this;
    }

    public TraceBuilder batchNumber(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("BATCH_NUMBER already set");
      } else {
        filled.set(2);
      }

      batchNumber.add(b);

      return this;
    }

    public TraceBuilder callerContextNumber(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER already set");
      } else {
        filled.set(3);
      }

      callerContextNumber.add(b);

      return this;
    }

    public TraceBuilder codeAddressHi(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("CODE_ADDRESS_HI already set");
      } else {
        filled.set(4);
      }

      codeAddressHi.add(b);

      return this;
    }

    public TraceBuilder codeAddressLo(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("CODE_ADDRESS_LO already set");
      } else {
        filled.set(5);
      }

      codeAddressLo.add(b);

      return this;
    }

    public TraceBuilder codeDeploymentNumber(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(6);
      }

      codeDeploymentNumber.add(b);

      return this;
    }

    public TraceBuilder codeDeploymentStatus(final Boolean b) {
      if (filled.get(7)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_STATUS already set");
      } else {
        filled.set(7);
      }

      codeDeploymentStatus.add(b);

      return this;
    }

    public TraceBuilder contextGetsRevrtdFlag(final Boolean b) {
      if (filled.get(8)) {
        throw new IllegalStateException("CONTEXT_GETS_REVRTD_FLAG already set");
      } else {
        filled.set(8);
      }

      contextGetsRevrtdFlag.add(b);

      return this;
    }

    public TraceBuilder contextMayChangeFlag(final Boolean b) {
      if (filled.get(9)) {
        throw new IllegalStateException("CONTEXT_MAY_CHANGE_FLAG already set");
      } else {
        filled.set(9);
      }

      contextMayChangeFlag.add(b);

      return this;
    }

    public TraceBuilder contextNumber(final BigInteger b) {
      if (filled.get(10)) {
        throw new IllegalStateException("CONTEXT_NUMBER already set");
      } else {
        filled.set(10);
      }

      contextNumber.add(b);

      return this;
    }

    public TraceBuilder contextNumberNew(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("CONTEXT_NUMBER_NEW already set");
      } else {
        filled.set(11);
      }

      contextNumberNew.add(b);

      return this;
    }

    public TraceBuilder contextRevertStamp(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("CONTEXT_REVERT_STAMP already set");
      } else {
        filled.set(12);
      }

      contextRevertStamp.add(b);

      return this;
    }

    public TraceBuilder contextSelfRevrtsFlag(final Boolean b) {
      if (filled.get(13)) {
        throw new IllegalStateException("CONTEXT_SELF_REVRTS_FLAG already set");
      } else {
        filled.set(13);
      }

      contextSelfRevrtsFlag.add(b);

      return this;
    }

    public TraceBuilder contextWillRevertFlag(final Boolean b) {
      if (filled.get(14)) {
        throw new IllegalStateException("CONTEXT_WILL_REVERT_FLAG already set");
      } else {
        filled.set(14);
      }

      contextWillRevertFlag.add(b);

      return this;
    }

    public TraceBuilder counterNsr(final BigInteger b) {
      if (filled.get(15)) {
        throw new IllegalStateException("COUNTER_NSR already set");
      } else {
        filled.set(15);
      }

      counterNsr.add(b);

      return this;
    }

    public TraceBuilder counterTli(final Boolean b) {
      if (filled.get(16)) {
        throw new IllegalStateException("COUNTER_TLI already set");
      } else {
        filled.set(16);
      }

      counterTli.add(b);

      return this;
    }

    public TraceBuilder exceptionAhoyFlag(final Boolean b) {
      if (filled.get(17)) {
        throw new IllegalStateException("EXCEPTION_AHOY_FLAG already set");
      } else {
        filled.set(17);
      }

      exceptionAhoyFlag.add(b);

      return this;
    }

    public TraceBuilder failureConditionFlag(final Boolean b) {
      if (filled.get(18)) {
        throw new IllegalStateException("FAILURE_CONDITION_FLAG already set");
      } else {
        filled.set(18);
      }

      failureConditionFlag.add(b);

      return this;
    }

    public TraceBuilder gasActual(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("GAS_ACTUAL already set");
      } else {
        filled.set(19);
      }

      gasActual.add(b);

      return this;
    }

    public TraceBuilder gasCost(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("GAS_COST already set");
      } else {
        filled.set(20);
      }

      gasCost.add(b);

      return this;
    }

    public TraceBuilder gasExpected(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("GAS_EXPECTED already set");
      } else {
        filled.set(21);
      }

      gasExpected.add(b);

      return this;
    }

    public TraceBuilder gasMemoryExpansion(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("GAS_MEMORY_EXPANSION already set");
      } else {
        filled.set(22);
      }

      gasMemoryExpansion.add(b);

      return this;
    }

    public TraceBuilder gasNext(final BigInteger b) {
      if (filled.get(23)) {
        throw new IllegalStateException("GAS_NEXT already set");
      } else {
        filled.set(23);
      }

      gasNext.add(b);

      return this;
    }

    public TraceBuilder gasRefund(final BigInteger b) {
      if (filled.get(24)) {
        throw new IllegalStateException("GAS_REFUND already set");
      } else {
        filled.set(24);
      }

      gasRefund.add(b);

      return this;
    }

    public TraceBuilder hubStamp(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("HUB_STAMP already set");
      } else {
        filled.set(25);
      }

      hubStamp.add(b);

      return this;
    }

    public TraceBuilder numberOfNonStackRows(final BigInteger b) {
      if (filled.get(26)) {
        throw new IllegalStateException("NUMBER_OF_NON_STACK_ROWS already set");
      } else {
        filled.set(26);
      }

      numberOfNonStackRows.add(b);

      return this;
    }

    public TraceBuilder pAccountAddrHi(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("ADDR_HI already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pAccountAddrLo(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("ADDR_LO already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pAccountBalance(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("BALANCE already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.add(b);

      return this;
    }

    public TraceBuilder pAccountBalanceNew(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("BALANCE_NEW already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashHi(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("CODE_HASH_HI already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashHiNew(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("CODE_HASH_HI_NEW already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashLo(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("CODE_HASH_LO already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashLoNew(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("CODE_HASH_LO_NEW already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeSize(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("CODE_SIZE already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeSizeNew(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("CODE_SIZE_NEW already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.add(b);

      return this;
    }

    public TraceBuilder pAccountDepNum(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("DEP_NUM already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pAccountDepNumNew(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("DEP_NUM_NEW already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pAccountDepStatus(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("DEP_STATUS already set");
      } else {
        filled.set(45);
      }

      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pAccountDepStatusNew(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("DEP_STATUS_NEW already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pAccountDeploymentNumberInfty(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER_INFTY already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.add(b);

      return this;
    }

    public TraceBuilder pAccountDeploymentStatusInfty(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("DEPLOYMENT_STATUS_INFTY already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.add(b);

      return this;
    }

    public TraceBuilder pAccountExists(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("EXISTS already set");
      } else {
        filled.set(47);
      }

      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pAccountExistsNew(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("EXISTS_NEW already set");
      } else {
        filled.set(48);
      }

      existsNewXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pAccountHasCode(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("HAS_CODE already set");
      } else {
        filled.set(49);
      }

      hasCodeXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pAccountHasCodeNew(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("HAS_CODE_NEW already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pAccountIsBlake2F(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("IS_BLAKE2f already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcadd(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("IS_ECADD already set");
      } else {
        filled.set(52);
      }

      isEcaddXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcmul(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("IS_ECMUL already set");
      } else {
        filled.set(53);
      }

      isEcmulXorDecodedFlag2.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcpairing(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("IS_ECPAIRING already set");
      } else {
        filled.set(54);
      }

      isEcpairingXorDecodedFlag3.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcrecover(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("IS_ECRECOVER already set");
      } else {
        filled.set(55);
      }

      isEcrecoverXorDecodedFlag4.add(b);

      return this;
    }

    public TraceBuilder pAccountIsIdentity(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("IS_IDENTITY already set");
      } else {
        filled.set(56);
      }

      isIdentityXorDupFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsModexp(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("IS_MODEXP already set");
      } else {
        filled.set(57);
      }

      isModexpXorExtFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsPrecompile(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("IS_PRECOMPILE already set");
      } else {
        filled.set(58);
      }

      isPrecompileXorHaltFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsRipemd160(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("IS_RIPEMD-160 already set");
      } else {
        filled.set(59);
      }

      isRipemDsub160XorInvalidFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsSha2256(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("IS_SHA2-256 already set");
      } else {
        filled.set(60);
      }

      isSha2Sub256XorInvprex.add(b);

      return this;
    }

    public TraceBuilder pAccountNonce(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("NONCE already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pAccountNonceNew(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("NONCE_NEW already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrDepAddrHi(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_HI already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrDepAddrLo(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_LO already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrFlag(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("RLPADDR___FLAG already set");
      } else {
        filled.set(61);
      }

      rlpaddrFlagXorJumpx.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrKecHi(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("RLPADDR___KEC_HI already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrKecLo(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("RLPADDR___KEC_LO already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrRecipe(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("RLPADDR___RECIPE already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrSaltHi(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("RLPADDR___SALT_HI already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrSaltLo(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("RLPADDR___SALT_LO already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pAccountTrmFlag(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("TRM___FLAG already set");
      } else {
        filled.set(62);
      }

      trmFlagXorJumpFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountTrmRawAddrHi(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("TRM___RAW_ADDR_HI already set");
      } else {
        filled.set(113);
      }

      trmRawAddrHiXorStackItemValueLo4.add(b);

      return this;
    }

    public TraceBuilder pAccountWarm(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("WARM already set");
      } else {
        filled.set(63);
      }

      warmXorKecFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountWarmNew(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("WARM_NEW already set");
      } else {
        filled.set(64);
      }

      warmNewXorLogFlag.add(b);

      return this;
    }

    public TraceBuilder pContextAccountAddressHi(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("ACCOUNT_ADDRESS_HI already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pContextAccountAddressLo(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("ACCOUNT_ADDRESS_LO already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pContextAccountDeploymentNumber(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("ACCOUNT_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeAddressHi(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("BYTE_CODE_ADDRESS_HI already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeAddressLo(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("BYTE_CODE_ADDRESS_LO already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeDeploymentNumber(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("BYTE_CODE_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeDeploymentStatus(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("BYTE_CODE_DEPLOYMENT_STATUS already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pContextCallDataOffset(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("CALL_DATA_OFFSET already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.add(b);

      return this;
    }

    public TraceBuilder pContextCallDataSize(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("CALL_DATA_SIZE already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pContextCallStackDepth(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("CALL_STACK_DEPTH already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pContextCallValue(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("CALL_VALUE already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pContextCallerAddressHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("CALLER_ADDRESS_HI already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pContextCallerAddressLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("CALLER_ADDRESS_LO already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.add(b);

      return this;
    }

    public TraceBuilder pContextCallerContextNumber(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.add(b);

      return this;
    }

    public TraceBuilder pContextContextNumber(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("CONTEXT_NUMBER already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pContextIsStatic(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("IS_STATIC already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pContextReturnAtOffset(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("RETURN_AT_OFFSET already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pContextReturnAtSize(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("RETURN_AT_SIZE already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pContextReturnDataOffset(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("RETURN_DATA_OFFSET already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pContextReturnDataSize(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("RETURN_DATA_SIZE already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pContextReturnerContextNumber(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("RETURNER_CONTEXT_NUMBER already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.add(b);

      return this;
    }

    public TraceBuilder pContextReturnerIsPrecompile(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("RETURNER_IS_PRECOMPILE already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pContextUpdate(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("UPDATE already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.add(b);

      return this;
    }

    public TraceBuilder pStackAccFlag(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("ACC_FLAG already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.add(b);

      return this;
    }

    public TraceBuilder pStackAddFlag(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("ADD_FLAG already set");
      } else {
        filled.set(45);
      }

      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pStackBinFlag(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("BIN_FLAG already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pStackBtcFlag(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("BTC_FLAG already set");
      } else {
        filled.set(47);
      }

      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pStackCallFlag(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("CALL_FLAG already set");
      } else {
        filled.set(48);
      }

      existsNewXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pStackConFlag(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("CON_FLAG already set");
      } else {
        filled.set(49);
      }

      hasCodeXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pStackCopyFlag(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("COPY_FLAG already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pStackCreateFlag(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("CREATE_FLAG already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag1(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("DECODED_FLAG_1 already set");
      } else {
        filled.set(52);
      }

      isEcaddXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag2(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("DECODED_FLAG_2 already set");
      } else {
        filled.set(53);
      }

      isEcmulXorDecodedFlag2.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag3(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("DECODED_FLAG_3 already set");
      } else {
        filled.set(54);
      }

      isEcpairingXorDecodedFlag3.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag4(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("DECODED_FLAG_4 already set");
      } else {
        filled.set(55);
      }

      isEcrecoverXorDecodedFlag4.add(b);

      return this;
    }

    public TraceBuilder pStackDupFlag(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("DUP_FLAG already set");
      } else {
        filled.set(56);
      }

      isIdentityXorDupFlag.add(b);

      return this;
    }

    public TraceBuilder pStackExtFlag(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("EXT_FLAG already set");
      } else {
        filled.set(57);
      }

      isModexpXorExtFlag.add(b);

      return this;
    }

    public TraceBuilder pStackHaltFlag(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("HALT_FLAG already set");
      } else {
        filled.set(58);
      }

      isPrecompileXorHaltFlag.add(b);

      return this;
    }

    public TraceBuilder pStackHeight(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("HEIGHT already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pStackHeightNew(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("HEIGHT_NEW already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pStackHeightOver(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("HEIGHT_OVER already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStackHeightUnder(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("HEIGHT_UNDER already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(b);

      return this;
    }

    public TraceBuilder pStackInstruction(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("INSTRUCTION already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStackInvalidFlag(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("INVALID_FLAG already set");
      } else {
        filled.set(59);
      }

      isRipemDsub160XorInvalidFlag.add(b);

      return this;
    }

    public TraceBuilder pStackInvprex(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("INVPREX already set");
      } else {
        filled.set(60);
      }

      isSha2Sub256XorInvprex.add(b);

      return this;
    }

    public TraceBuilder pStackJumpFlag(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("JUMP_FLAG already set");
      } else {
        filled.set(62);
      }

      trmFlagXorJumpFlag.add(b);

      return this;
    }

    public TraceBuilder pStackJumpx(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("JUMPX already set");
      } else {
        filled.set(61);
      }

      rlpaddrFlagXorJumpx.add(b);

      return this;
    }

    public TraceBuilder pStackKecFlag(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("KEC_FLAG already set");
      } else {
        filled.set(63);
      }

      warmXorKecFlag.add(b);

      return this;
    }

    public TraceBuilder pStackLogFlag(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("LOG_FLAG already set");
      } else {
        filled.set(64);
      }

      warmNewXorLogFlag.add(b);

      return this;
    }

    public TraceBuilder pStackMaxcsx(final Boolean b) {
      if (filled.get(65)) {
        throw new IllegalStateException("MAXCSX already set");
      } else {
        filled.set(65);
      }

      maxcsx.add(b);

      return this;
    }

    public TraceBuilder pStackModFlag(final Boolean b) {
      if (filled.get(66)) {
        throw new IllegalStateException("MOD_FLAG already set");
      } else {
        filled.set(66);
      }

      modFlag.add(b);

      return this;
    }

    public TraceBuilder pStackMulFlag(final Boolean b) {
      if (filled.get(67)) {
        throw new IllegalStateException("MUL_FLAG already set");
      } else {
        filled.set(67);
      }

      mulFlag.add(b);

      return this;
    }

    public TraceBuilder pStackMxpFlag(final Boolean b) {
      if (filled.get(69)) {
        throw new IllegalStateException("MXP_FLAG already set");
      } else {
        filled.set(69);
      }

      mxpFlag.add(b);

      return this;
    }

    public TraceBuilder pStackMxpx(final Boolean b) {
      if (filled.get(68)) {
        throw new IllegalStateException("MXPX already set");
      } else {
        filled.set(68);
      }

      mxpx.add(b);

      return this;
    }

    public TraceBuilder pStackOobFlag(final Boolean b) {
      if (filled.get(70)) {
        throw new IllegalStateException("OOB_FLAG already set");
      } else {
        filled.set(70);
      }

      oobFlag.add(b);

      return this;
    }

    public TraceBuilder pStackOogx(final Boolean b) {
      if (filled.get(71)) {
        throw new IllegalStateException("OOGX already set");
      } else {
        filled.set(71);
      }

      oogx.add(b);

      return this;
    }

    public TraceBuilder pStackOpcx(final Boolean b) {
      if (filled.get(72)) {
        throw new IllegalStateException("OPCX already set");
      } else {
        filled.set(72);
      }

      opcx.add(b);

      return this;
    }

    public TraceBuilder pStackPushValueHi(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("PUSH_VALUE_HI already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.add(b);

      return this;
    }

    public TraceBuilder pStackPushValueLo(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("PUSH_VALUE_LO already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pStackPushpopFlag(final Boolean b) {
      if (filled.get(73)) {
        throw new IllegalStateException("PUSHPOP_FLAG already set");
      } else {
        filled.set(73);
      }

      pushpopFlag.add(b);

      return this;
    }

    public TraceBuilder pStackRdcx(final Boolean b) {
      if (filled.get(74)) {
        throw new IllegalStateException("RDCX already set");
      } else {
        filled.set(74);
      }

      rdcx.add(b);

      return this;
    }

    public TraceBuilder pStackShfFlag(final Boolean b) {
      if (filled.get(75)) {
        throw new IllegalStateException("SHF_FLAG already set");
      } else {
        filled.set(75);
      }

      shfFlag.add(b);

      return this;
    }

    public TraceBuilder pStackSox(final Boolean b) {
      if (filled.get(76)) {
        throw new IllegalStateException("SOX already set");
      } else {
        filled.set(76);
      }

      sox.add(b);

      return this;
    }

    public TraceBuilder pStackSstorex(final Boolean b) {
      if (filled.get(77)) {
        throw new IllegalStateException("SSTOREX already set");
      } else {
        filled.set(77);
      }

      sstorex.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight1(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_1 already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight2(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_2 already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight3(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_3 already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight4(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_4 already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemPop1(final Boolean b) {
      if (filled.get(79)) {
        throw new IllegalStateException("STACK_ITEM_POP_1 already set");
      } else {
        filled.set(79);
      }

      stackItemPop1.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemPop2(final Boolean b) {
      if (filled.get(80)) {
        throw new IllegalStateException("STACK_ITEM_POP_2 already set");
      } else {
        filled.set(80);
      }

      stackItemPop2.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemPop3(final Boolean b) {
      if (filled.get(81)) {
        throw new IllegalStateException("STACK_ITEM_POP_3 already set");
      } else {
        filled.set(81);
      }

      stackItemPop3.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemPop4(final Boolean b) {
      if (filled.get(82)) {
        throw new IllegalStateException("STACK_ITEM_POP_4 already set");
      } else {
        filled.set(82);
      }

      stackItemPop4.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp1(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_1 already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp2(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_2 already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp3(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_3 already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp4(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_4 already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi1(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_1 already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi2(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_2 already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi3(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_3 already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi4(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_4 already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo1(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_1 already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo2(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_2 already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo3(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_3 already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo4(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_4 already set");
      } else {
        filled.set(113);
      }

      trmRawAddrHiXorStackItemValueLo4.add(b);

      return this;
    }

    public TraceBuilder pStackStackramFlag(final Boolean b) {
      if (filled.get(78)) {
        throw new IllegalStateException("STACKRAM_FLAG already set");
      } else {
        filled.set(78);
      }

      stackramFlag.add(b);

      return this;
    }

    public TraceBuilder pStackStaticFlag(final Boolean b) {
      if (filled.get(84)) {
        throw new IllegalStateException("STATIC_FLAG already set");
      } else {
        filled.set(84);
      }

      staticFlag.add(b);

      return this;
    }

    public TraceBuilder pStackStaticGas(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("STATIC_GAS already set");
      } else {
        filled.set(114);
      }

      staticGas.add(b);

      return this;
    }

    public TraceBuilder pStackStaticx(final Boolean b) {
      if (filled.get(83)) {
        throw new IllegalStateException("STATICX already set");
      } else {
        filled.set(83);
      }

      staticx.add(b);

      return this;
    }

    public TraceBuilder pStackStoFlag(final Boolean b) {
      if (filled.get(85)) {
        throw new IllegalStateException("STO_FLAG already set");
      } else {
        filled.set(85);
      }

      stoFlag.add(b);

      return this;
    }

    public TraceBuilder pStackSux(final Boolean b) {
      if (filled.get(86)) {
        throw new IllegalStateException("SUX already set");
      } else {
        filled.set(86);
      }

      sux.add(b);

      return this;
    }

    public TraceBuilder pStackSwapFlag(final Boolean b) {
      if (filled.get(87)) {
        throw new IllegalStateException("SWAP_FLAG already set");
      } else {
        filled.set(87);
      }

      swapFlag.add(b);

      return this;
    }

    public TraceBuilder pStackTrmFlag(final Boolean b) {
      if (filled.get(88)) {
        throw new IllegalStateException("TRM_FLAG already set");
      } else {
        filled.set(88);
      }

      trmFlag.add(b);

      return this;
    }

    public TraceBuilder pStackTxnFlag(final Boolean b) {
      if (filled.get(89)) {
        throw new IllegalStateException("TXN_FLAG already set");
      } else {
        filled.set(89);
      }

      txnFlag.add(b);

      return this;
    }

    public TraceBuilder pStackWcpFlag(final Boolean b) {
      if (filled.get(90)) {
        throw new IllegalStateException("WCP_FLAG already set");
      } else {
        filled.set(90);
      }

      wcpFlag.add(b);

      return this;
    }

    public TraceBuilder pStorageAddressHi(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("ADDRESS_HI already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pStorageAddressLo(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("ADDRESS_LO already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pStorageDeploymentNumber(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStorageStorageKeyHi(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("STORAGE_KEY_HI already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(b);

      return this;
    }

    public TraceBuilder pStorageStorageKeyLo(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("STORAGE_KEY_LO already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrChanges(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("VAL_CURR_CHANGES already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrHi(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("VAL_CURR_HI already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrIsOrig(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("VAL_CURR_IS_ORIG already set");
      } else {
        filled.set(45);
      }

      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrIsZero(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("VAL_CURR_IS_ZERO already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrLo(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("VAL_CURR_LO already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("VAL_NEXT_HI already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextIsCurr(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("VAL_NEXT_IS_CURR already set");
      } else {
        filled.set(47);
      }

      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextIsOrig(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("VAL_NEXT_IS_ORIG already set");
      } else {
        filled.set(48);
      }

      existsNewXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextIsZero(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("VAL_NEXT_IS_ZERO already set");
      } else {
        filled.set(49);
      }

      hasCodeXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("VAL_NEXT_LO already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.add(b);

      return this;
    }

    public TraceBuilder pStorageValOrigHi(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("VAL_ORIG_HI already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.add(b);

      return this;
    }

    public TraceBuilder pStorageValOrigIsZero(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("VAL_ORIG_IS_ZERO already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pStorageValOrigLo(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("VAL_ORIG_LO already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.add(b);

      return this;
    }

    public TraceBuilder pStorageWarm(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("WARM already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pStorageWarmNew(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("WARM_NEW already set");
      } else {
        filled.set(52);
      }

      isEcaddXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pTransactionBasefee(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("BASEFEE already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pTransactionCallDataSize(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("CALL_DATA_SIZE already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pTransactionCoinbaseAddressHi(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("COINBASE_ADDRESS_HI already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.add(b);

      return this;
    }

    public TraceBuilder pTransactionCoinbaseAddressLo(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("COINBASE_ADDRESS_LO already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(b);

      return this;
    }

    public TraceBuilder pTransactionFromAddressHi(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("FROM_ADDRESS_HI already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pTransactionFromAddressLo(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("FROM_ADDRESS_LO already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.add(b);

      return this;
    }

    public TraceBuilder pTransactionGasLimit(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("GAS_LIMIT already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pTransactionGasPrice(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("GAS_PRICE already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pTransactionGasRefundAmount(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("GAS_REFUND_AMOUNT already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.add(b);

      return this;
    }

    public TraceBuilder pTransactionGasRefundCounterFinal(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("GAS_REFUND_COUNTER_FINAL already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.add(b);

      return this;
    }

    public TraceBuilder pTransactionInitCodeSize(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("INIT_CODE_SIZE already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pTransactionInitGas(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("INIT_GAS already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pTransactionInitialBalance(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("INITIAL_BALANCE already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.add(b);

      return this;
    }

    public TraceBuilder pTransactionIsDeployment(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("IS_DEPLOYMENT already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.add(b);

      return this;
    }

    public TraceBuilder pTransactionIsEip1559(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("IS_EIP1559 already set");
      } else {
        filled.set(45);
      }

      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pTransactionLeftoverGas(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("LEFTOVER_GAS already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pTransactionNonce(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("NONCE already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pTransactionStatusCode(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("STATUS_CODE already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pTransactionToAddressHi(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("TO_ADDRESS_HI already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pTransactionToAddressLo(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("TO_ADDRESS_LO already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.add(b);

      return this;
    }

    public TraceBuilder pTransactionTxnRequiresEvmExecution(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("TXN_REQUIRES_EVM_EXECUTION already set");
      } else {
        filled.set(47);
      }

      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pTransactionValue(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("VALUE already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder peekAtAccount(final Boolean b) {
      if (filled.get(27)) {
        throw new IllegalStateException("PEEK_AT_ACCOUNT already set");
      } else {
        filled.set(27);
      }

      peekAtAccount.add(b);

      return this;
    }

    public TraceBuilder peekAtContext(final Boolean b) {
      if (filled.get(28)) {
        throw new IllegalStateException("PEEK_AT_CONTEXT already set");
      } else {
        filled.set(28);
      }

      peekAtContext.add(b);

      return this;
    }

    public TraceBuilder peekAtMiscellaneous(final Boolean b) {
      if (filled.get(29)) {
        throw new IllegalStateException("PEEK_AT_MISCELLANEOUS already set");
      } else {
        filled.set(29);
      }

      peekAtMiscellaneous.add(b);

      return this;
    }

    public TraceBuilder peekAtScenario(final Boolean b) {
      if (filled.get(30)) {
        throw new IllegalStateException("PEEK_AT_SCENARIO already set");
      } else {
        filled.set(30);
      }

      peekAtScenario.add(b);

      return this;
    }

    public TraceBuilder peekAtStack(final Boolean b) {
      if (filled.get(31)) {
        throw new IllegalStateException("PEEK_AT_STACK already set");
      } else {
        filled.set(31);
      }

      peekAtStack.add(b);

      return this;
    }

    public TraceBuilder peekAtStorage(final Boolean b) {
      if (filled.get(32)) {
        throw new IllegalStateException("PEEK_AT_STORAGE already set");
      } else {
        filled.set(32);
      }

      peekAtStorage.add(b);

      return this;
    }

    public TraceBuilder peekAtTransaction(final Boolean b) {
      if (filled.get(33)) {
        throw new IllegalStateException("PEEK_AT_TRANSACTION already set");
      } else {
        filled.set(33);
      }

      peekAtTransaction.add(b);

      return this;
    }

    public TraceBuilder programCounter(final BigInteger b) {
      if (filled.get(34)) {
        throw new IllegalStateException("PROGRAM_COUNTER already set");
      } else {
        filled.set(34);
      }

      programCounter.add(b);

      return this;
    }

    public TraceBuilder programCounterNew(final BigInteger b) {
      if (filled.get(35)) {
        throw new IllegalStateException("PROGRAM_COUNTER_NEW already set");
      } else {
        filled.set(35);
      }

      programCounterNew.add(b);

      return this;
    }

    public TraceBuilder transactionEndStamp(final BigInteger b) {
      if (filled.get(36)) {
        throw new IllegalStateException("TRANSACTION_END_STAMP already set");
      } else {
        filled.set(36);
      }

      transactionEndStamp.add(b);

      return this;
    }

    public TraceBuilder transactionReverts(final BigInteger b) {
      if (filled.get(37)) {
        throw new IllegalStateException("TRANSACTION_REVERTS already set");
      } else {
        filled.set(37);
      }

      transactionReverts.add(b);

      return this;
    }

    public TraceBuilder twoLineInstruction(final Boolean b) {
      if (filled.get(38)) {
        throw new IllegalStateException("TWO_LINE_INSTRUCTION already set");
      } else {
        filled.set(38);
      }

      twoLineInstruction.add(b);

      return this;
    }

    public TraceBuilder txExec(final Boolean b) {
      if (filled.get(39)) {
        throw new IllegalStateException("TX_EXEC already set");
      } else {
        filled.set(39);
      }

      txExec.add(b);

      return this;
    }

    public TraceBuilder txFinl(final Boolean b) {
      if (filled.get(40)) {
        throw new IllegalStateException("TX_FINL already set");
      } else {
        filled.set(40);
      }

      txFinl.add(b);

      return this;
    }

    public TraceBuilder txInit(final Boolean b) {
      if (filled.get(41)) {
        throw new IllegalStateException("TX_INIT already set");
      } else {
        filled.set(41);
      }

      txInit.add(b);

      return this;
    }

    public TraceBuilder txSkip(final Boolean b) {
      if (filled.get(42)) {
        throw new IllegalStateException("TX_SKIP already set");
      } else {
        filled.set(42);
      }

      txSkip.add(b);

      return this;
    }

    public TraceBuilder txWarm(final Boolean b) {
      if (filled.get(43)) {
        throw new IllegalStateException("TX_WARM already set");
      } else {
        filled.set(43);
      }

      txWarm.add(b);

      return this;
    }

    public TraceBuilder setAbortFlagAt(final Boolean b, int i) {
      abortFlag.set(i, b);

      return this;
    }

    public TraceBuilder setAbsoluteTransactionNumberAt(final BigInteger b, int i) {
      absoluteTransactionNumber.set(i, b);

      return this;
    }

    public TraceBuilder setBatchNumberAt(final BigInteger b, int i) {
      batchNumber.set(i, b);

      return this;
    }

    public TraceBuilder setCallerContextNumberAt(final BigInteger b, int i) {
      callerContextNumber.set(i, b);

      return this;
    }

    public TraceBuilder setCodeAddressHiAt(final BigInteger b, int i) {
      codeAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setCodeAddressLoAt(final BigInteger b, int i) {
      codeAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setCodeDeploymentNumberAt(final BigInteger b, int i) {
      codeDeploymentNumber.set(i, b);

      return this;
    }

    public TraceBuilder setCodeDeploymentStatusAt(final Boolean b, int i) {
      codeDeploymentStatus.set(i, b);

      return this;
    }

    public TraceBuilder setContextGetsRevrtdFlagAt(final Boolean b, int i) {
      contextGetsRevrtdFlag.set(i, b);

      return this;
    }

    public TraceBuilder setContextMayChangeFlagAt(final Boolean b, int i) {
      contextMayChangeFlag.set(i, b);

      return this;
    }

    public TraceBuilder setContextNumberAt(final BigInteger b, int i) {
      contextNumber.set(i, b);

      return this;
    }

    public TraceBuilder setContextNumberNewAt(final BigInteger b, int i) {
      contextNumberNew.set(i, b);

      return this;
    }

    public TraceBuilder setContextRevertStampAt(final BigInteger b, int i) {
      contextRevertStamp.set(i, b);

      return this;
    }

    public TraceBuilder setContextSelfRevrtsFlagAt(final Boolean b, int i) {
      contextSelfRevrtsFlag.set(i, b);

      return this;
    }

    public TraceBuilder setContextWillRevertFlagAt(final Boolean b, int i) {
      contextWillRevertFlag.set(i, b);

      return this;
    }

    public TraceBuilder setCounterNsrAt(final BigInteger b, int i) {
      counterNsr.set(i, b);

      return this;
    }

    public TraceBuilder setCounterTliAt(final Boolean b, int i) {
      counterTli.set(i, b);

      return this;
    }

    public TraceBuilder setExceptionAhoyFlagAt(final Boolean b, int i) {
      exceptionAhoyFlag.set(i, b);

      return this;
    }

    public TraceBuilder setFailureConditionFlagAt(final Boolean b, int i) {
      failureConditionFlag.set(i, b);

      return this;
    }

    public TraceBuilder setGasActualAt(final BigInteger b, int i) {
      gasActual.set(i, b);

      return this;
    }

    public TraceBuilder setGasCostAt(final BigInteger b, int i) {
      gasCost.set(i, b);

      return this;
    }

    public TraceBuilder setGasExpectedAt(final BigInteger b, int i) {
      gasExpected.set(i, b);

      return this;
    }

    public TraceBuilder setGasMemoryExpansionAt(final BigInteger b, int i) {
      gasMemoryExpansion.set(i, b);

      return this;
    }

    public TraceBuilder setGasNextAt(final BigInteger b, int i) {
      gasNext.set(i, b);

      return this;
    }

    public TraceBuilder setGasRefundAt(final BigInteger b, int i) {
      gasRefund.set(i, b);

      return this;
    }

    public TraceBuilder setHubStampAt(final BigInteger b, int i) {
      hubStamp.set(i, b);

      return this;
    }

    public TraceBuilder setNumberOfNonStackRowsAt(final BigInteger b, int i) {
      numberOfNonStackRows.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountAddrHiAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountAddrLoAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountBalanceAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountBalanceNewAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiNewAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoNewAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeNewAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumNewAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusAt(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusNewAt(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentNumberInftyAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentStatusInftyAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountExistsAt(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountExistsNewAt(final Boolean b, int i) {
      existsNewXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeAt(final Boolean b, int i) {
      hasCodeXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeNewAt(final Boolean b, int i) {
      hasCodeNewXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsBlake2FAt(final Boolean b, int i) {
      isBlake2FXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcaddAt(final Boolean b, int i) {
      isEcaddXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcmulAt(final Boolean b, int i) {
      isEcmulXorDecodedFlag2.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcpairingAt(final Boolean b, int i) {
      isEcpairingXorDecodedFlag3.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcrecoverAt(final Boolean b, int i) {
      isEcrecoverXorDecodedFlag4.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsIdentityAt(final Boolean b, int i) {
      isIdentityXorDupFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsModexpAt(final Boolean b, int i) {
      isModexpXorExtFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsPrecompileAt(final Boolean b, int i) {
      isPrecompileXorHaltFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsRipemd160At(final Boolean b, int i) {
      isRipemDsub160XorInvalidFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsSha2256At(final Boolean b, int i) {
      isSha2Sub256XorInvprex.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceAt(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceNewAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrHiAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrLoAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrFlagAt(final Boolean b, int i) {
      rlpaddrFlagXorJumpx.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecHiAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecLoAt(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrRecipeAt(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltHiAt(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltLoAt(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmFlagAt(final Boolean b, int i) {
      trmFlagXorJumpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmRawAddrHiAt(final BigInteger b, int i) {
      trmRawAddrHiXorStackItemValueLo4.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmAt(final Boolean b, int i) {
      warmXorKecFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmNewAt(final Boolean b, int i) {
      warmNewXorLogFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressHiAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressLoAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPContextAccountDeploymentNumberAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressHiAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressLoAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentNumberAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentStatusAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallDataOffsetAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallDataSizeAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallStackDepthAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallValueAt(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressHiAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressLoAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallerContextNumberAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(i, b);

      return this;
    }

    public TraceBuilder setPContextContextNumberAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPContextIsStaticAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtOffsetAt(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtSizeAt(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataOffsetAt(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataSizeAt(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerContextNumberAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerIsPrecompileAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPContextUpdateAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(i, b);

      return this;
    }

    public TraceBuilder setPStackAccFlagAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(i, b);

      return this;
    }

    public TraceBuilder setPStackAddFlagAt(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPStackBinFlagAt(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPStackBtcFlagAt(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPStackCallFlagAt(final Boolean b, int i) {
      existsNewXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPStackConFlagAt(final Boolean b, int i) {
      hasCodeXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStackCopyFlagAt(final Boolean b, int i) {
      hasCodeNewXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStackCreateFlagAt(final Boolean b, int i) {
      isBlake2FXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag1At(final Boolean b, int i) {
      isEcaddXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag2At(final Boolean b, int i) {
      isEcmulXorDecodedFlag2.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag3At(final Boolean b, int i) {
      isEcpairingXorDecodedFlag3.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag4At(final Boolean b, int i) {
      isEcrecoverXorDecodedFlag4.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDupFlagAt(final Boolean b, int i) {
      isIdentityXorDupFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackExtFlagAt(final Boolean b, int i) {
      isModexpXorExtFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHaltFlagAt(final Boolean b, int i) {
      isPrecompileXorHaltFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightNewAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightOverAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightUnderAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPStackInstructionAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPStackInvalidFlagAt(final Boolean b, int i) {
      isRipemDsub160XorInvalidFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackInvprexAt(final Boolean b, int i) {
      isSha2Sub256XorInvprex.set(i, b);

      return this;
    }

    public TraceBuilder setPStackJumpFlagAt(final Boolean b, int i) {
      trmFlagXorJumpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackJumpxAt(final Boolean b, int i) {
      rlpaddrFlagXorJumpx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackKecFlagAt(final Boolean b, int i) {
      warmXorKecFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackLogFlagAt(final Boolean b, int i) {
      warmNewXorLogFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMaxcsxAt(final Boolean b, int i) {
      maxcsx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackModFlagAt(final Boolean b, int i) {
      modFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMulFlagAt(final Boolean b, int i) {
      mulFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMxpFlagAt(final Boolean b, int i) {
      mxpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMxpxAt(final Boolean b, int i) {
      mxpx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackOobFlagAt(final Boolean b, int i) {
      oobFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackOogxAt(final Boolean b, int i) {
      oogx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackOpcxAt(final Boolean b, int i) {
      opcx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackPushValueHiAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPStackPushValueLoAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(i, b);

      return this;
    }

    public TraceBuilder setPStackPushpopFlagAt(final Boolean b, int i) {
      pushpopFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackRdcxAt(final Boolean b, int i) {
      rdcx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackShfFlagAt(final Boolean b, int i) {
      shfFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackSoxAt(final Boolean b, int i) {
      sox.set(i, b);

      return this;
    }

    public TraceBuilder setPStackSstorexAt(final Boolean b, int i) {
      sstorex.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight1At(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight2At(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight3At(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight4At(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop1At(final Boolean b, int i) {
      stackItemPop1.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop2At(final Boolean b, int i) {
      stackItemPop2.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop3At(final Boolean b, int i) {
      stackItemPop3.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop4At(final Boolean b, int i) {
      stackItemPop4.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp1At(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp2At(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp3At(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp4At(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi1At(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi2At(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi3At(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi4At(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo1At(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo2At(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo3At(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo4At(final BigInteger b, int i) {
      trmRawAddrHiXorStackItemValueLo4.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackramFlagAt(final Boolean b, int i) {
      stackramFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStaticFlagAt(final Boolean b, int i) {
      staticFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStaticGasAt(final BigInteger b, int i) {
      staticGas.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStaticxAt(final Boolean b, int i) {
      staticx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStoFlagAt(final Boolean b, int i) {
      stoFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackSuxAt(final Boolean b, int i) {
      sux.set(i, b);

      return this;
    }

    public TraceBuilder setPStackSwapFlagAt(final Boolean b, int i) {
      swapFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackTrmFlagAt(final Boolean b, int i) {
      trmFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackTxnFlagAt(final Boolean b, int i) {
      txnFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackWcpFlagAt(final Boolean b, int i) {
      wcpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageAddressHiAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageAddressLoAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageDeploymentNumberAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyHiAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyLoAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrChangesAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrHiAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsOrigAt(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsZeroAt(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrLoAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextHiAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsCurrAt(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsOrigAt(final Boolean b, int i) {
      existsNewXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsZeroAt(final Boolean b, int i) {
      hasCodeXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextLoAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigHiAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigIsZeroAt(final Boolean b, int i) {
      hasCodeNewXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigLoAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmAt(final Boolean b, int i) {
      isBlake2FXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmNewAt(final Boolean b, int i) {
      isEcaddXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionBasefeeAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionCallDataSizeAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressHiAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressLoAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressHiAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressLoAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasLimitAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasPriceAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundAmountAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundCounterFinalAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitCodeSizeAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitGasAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitialBalanceAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionIsDeploymentAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionIsEip1559At(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionLeftoverGasAt(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionNonceAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionStatusCodeAt(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressHiAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressLoAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionTxnRequiresEvmExecutionAt(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionValueAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtAccountAt(final Boolean b, int i) {
      peekAtAccount.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtContextAt(final Boolean b, int i) {
      peekAtContext.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtMiscellaneousAt(final Boolean b, int i) {
      peekAtMiscellaneous.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtScenarioAt(final Boolean b, int i) {
      peekAtScenario.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtStackAt(final Boolean b, int i) {
      peekAtStack.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtStorageAt(final Boolean b, int i) {
      peekAtStorage.set(i, b);

      return this;
    }

    public TraceBuilder setPeekAtTransactionAt(final Boolean b, int i) {
      peekAtTransaction.set(i, b);

      return this;
    }

    public TraceBuilder setProgramCounterAt(final BigInteger b, int i) {
      programCounter.set(i, b);

      return this;
    }

    public TraceBuilder setProgramCounterNewAt(final BigInteger b, int i) {
      programCounterNew.set(i, b);

      return this;
    }

    public TraceBuilder setTransactionEndStampAt(final BigInteger b, int i) {
      transactionEndStamp.set(i, b);

      return this;
    }

    public TraceBuilder setTransactionRevertsAt(final BigInteger b, int i) {
      transactionReverts.set(i, b);

      return this;
    }

    public TraceBuilder setTwoLineInstructionAt(final Boolean b, int i) {
      twoLineInstruction.set(i, b);

      return this;
    }

    public TraceBuilder setTxExecAt(final Boolean b, int i) {
      txExec.set(i, b);

      return this;
    }

    public TraceBuilder setTxFinlAt(final Boolean b, int i) {
      txFinl.set(i, b);

      return this;
    }

    public TraceBuilder setTxInitAt(final Boolean b, int i) {
      txInit.set(i, b);

      return this;
    }

    public TraceBuilder setTxSkipAt(final Boolean b, int i) {
      txSkip.set(i, b);

      return this;
    }

    public TraceBuilder setTxWarmAt(final Boolean b, int i) {
      txWarm.set(i, b);

      return this;
    }

    public TraceBuilder setAbortFlagRelative(final Boolean b, int i) {
      abortFlag.set(abortFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAbsoluteTransactionNumberRelative(final BigInteger b, int i) {
      absoluteTransactionNumber.set(absoluteTransactionNumber.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setBatchNumberRelative(final BigInteger b, int i) {
      batchNumber.set(batchNumber.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCallerContextNumberRelative(final BigInteger b, int i) {
      callerContextNumber.set(callerContextNumber.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCodeAddressHiRelative(final BigInteger b, int i) {
      codeAddressHi.set(codeAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCodeAddressLoRelative(final BigInteger b, int i) {
      codeAddressLo.set(codeAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCodeDeploymentNumberRelative(final BigInteger b, int i) {
      codeDeploymentNumber.set(codeDeploymentNumber.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCodeDeploymentStatusRelative(final Boolean b, int i) {
      codeDeploymentStatus.set(codeDeploymentStatus.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextGetsRevrtdFlagRelative(final Boolean b, int i) {
      contextGetsRevrtdFlag.set(contextGetsRevrtdFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextMayChangeFlagRelative(final Boolean b, int i) {
      contextMayChangeFlag.set(contextMayChangeFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextNumberRelative(final BigInteger b, int i) {
      contextNumber.set(contextNumber.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextNumberNewRelative(final BigInteger b, int i) {
      contextNumberNew.set(contextNumberNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextRevertStampRelative(final BigInteger b, int i) {
      contextRevertStamp.set(contextRevertStamp.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextSelfRevrtsFlagRelative(final Boolean b, int i) {
      contextSelfRevrtsFlag.set(contextSelfRevrtsFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setContextWillRevertFlagRelative(final Boolean b, int i) {
      contextWillRevertFlag.set(contextWillRevertFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCounterNsrRelative(final BigInteger b, int i) {
      counterNsr.set(counterNsr.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCounterTliRelative(final Boolean b, int i) {
      counterTli.set(counterTli.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setExceptionAhoyFlagRelative(final Boolean b, int i) {
      exceptionAhoyFlag.set(exceptionAhoyFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setFailureConditionFlagRelative(final Boolean b, int i) {
      failureConditionFlag.set(failureConditionFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setGasActualRelative(final BigInteger b, int i) {
      gasActual.set(gasActual.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setGasCostRelative(final BigInteger b, int i) {
      gasCost.set(gasCost.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setGasExpectedRelative(final BigInteger b, int i) {
      gasExpected.set(gasExpected.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setGasMemoryExpansionRelative(final BigInteger b, int i) {
      gasMemoryExpansion.set(gasMemoryExpansion.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setGasNextRelative(final BigInteger b, int i) {
      gasNext.set(gasNext.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setGasRefundRelative(final BigInteger b, int i) {
      gasRefund.set(gasRefund.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setHubStampRelative(final BigInteger b, int i) {
      hubStamp.set(hubStamp.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setNumberOfNonStackRowsRelative(final BigInteger b, int i) {
      numberOfNonStackRows.set(numberOfNonStackRows.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountAddrHiRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountAddrLoRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountBalanceRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountBalanceNewRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiNewRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoNewRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeNewRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumNewRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusRelative(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusNewRelative(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentNumberInftyRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentStatusInftyRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountExistsRelative(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountExistsNewRelative(final Boolean b, int i) {
      existsNewXorCallFlagXorValNextIsOrig.set(existsNewXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeRelative(final Boolean b, int i) {
      hasCodeXorConFlagXorValNextIsZero.set(hasCodeXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeNewRelative(final Boolean b, int i) {
      hasCodeNewXorCopyFlagXorValOrigIsZero.set(hasCodeNewXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsBlake2FRelative(final Boolean b, int i) {
      isBlake2FXorCreateFlagXorWarm.set(isBlake2FXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcaddRelative(final Boolean b, int i) {
      isEcaddXorDecodedFlag1XorWarmNew.set(isEcaddXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcmulRelative(final Boolean b, int i) {
      isEcmulXorDecodedFlag2.set(isEcmulXorDecodedFlag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcpairingRelative(final Boolean b, int i) {
      isEcpairingXorDecodedFlag3.set(isEcpairingXorDecodedFlag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcrecoverRelative(final Boolean b, int i) {
      isEcrecoverXorDecodedFlag4.set(isEcrecoverXorDecodedFlag4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsIdentityRelative(final Boolean b, int i) {
      isIdentityXorDupFlag.set(isIdentityXorDupFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsModexpRelative(final Boolean b, int i) {
      isModexpXorExtFlag.set(isModexpXorExtFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsPrecompileRelative(final Boolean b, int i) {
      isPrecompileXorHaltFlag.set(isPrecompileXorHaltFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsRipemd160Relative(final Boolean b, int i) {
      isRipemDsub160XorInvalidFlag.set(isRipemDsub160XorInvalidFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsSha2256Relative(final Boolean b, int i) {
      isSha2Sub256XorInvprex.set(isSha2Sub256XorInvprex.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceRelative(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(nonceXorCallValueXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceNewRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(nonceNewXorContextNumberXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrHiRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrLoRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrFlagRelative(final Boolean b, int i) {
      rlpaddrFlagXorJumpx.set(rlpaddrFlagXorJumpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecHiRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecLoRelative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.set(rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrRecipeRelative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.set(rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltHiRelative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.set(rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltLoRelative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.set(rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmFlagRelative(final Boolean b, int i) {
      trmFlagXorJumpFlag.set(trmFlagXorJumpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmRawAddrHiRelative(final BigInteger b, int i) {
      trmRawAddrHiXorStackItemValueLo4.set(trmRawAddrHiXorStackItemValueLo4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmRelative(final Boolean b, int i) {
      warmXorKecFlag.set(warmXorKecFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmNewRelative(final Boolean b, int i) {
      warmNewXorLogFlag.set(warmNewXorLogFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressHiRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressLoRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextAccountDeploymentNumberRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressHiRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressLoRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentNumberRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentStatusRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallDataOffsetRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallDataSizeRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallStackDepthRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallValueRelative(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(nonceXorCallValueXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressHiRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressLoRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallerContextNumberRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextContextNumberRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(nonceNewXorContextNumberXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextIsStaticRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtOffsetRelative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.set(rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtSizeRelative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.set(rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataOffsetRelative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.set(rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataSizeRelative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.set(rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerContextNumberRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerIsPrecompileRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextUpdateRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackAccFlagRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackAddFlagRelative(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackBinFlagRelative(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackBtcFlagRelative(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackCallFlagRelative(final Boolean b, int i) {
      existsNewXorCallFlagXorValNextIsOrig.set(existsNewXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackConFlagRelative(final Boolean b, int i) {
      hasCodeXorConFlagXorValNextIsZero.set(hasCodeXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackCopyFlagRelative(final Boolean b, int i) {
      hasCodeNewXorCopyFlagXorValOrigIsZero.set(hasCodeNewXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackCreateFlagRelative(final Boolean b, int i) {
      isBlake2FXorCreateFlagXorWarm.set(isBlake2FXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag1Relative(final Boolean b, int i) {
      isEcaddXorDecodedFlag1XorWarmNew.set(isEcaddXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag2Relative(final Boolean b, int i) {
      isEcmulXorDecodedFlag2.set(isEcmulXorDecodedFlag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag3Relative(final Boolean b, int i) {
      isEcpairingXorDecodedFlag3.set(isEcpairingXorDecodedFlag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag4Relative(final Boolean b, int i) {
      isEcrecoverXorDecodedFlag4.set(isEcrecoverXorDecodedFlag4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDupFlagRelative(final Boolean b, int i) {
      isIdentityXorDupFlag.set(isIdentityXorDupFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackExtFlagRelative(final Boolean b, int i) {
      isModexpXorExtFlag.set(isModexpXorExtFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHaltFlagRelative(final Boolean b, int i) {
      isPrecompileXorHaltFlag.set(isPrecompileXorHaltFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHeightRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHeightNewRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHeightOverRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHeightUnderRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackInstructionRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackInvalidFlagRelative(final Boolean b, int i) {
      isRipemDsub160XorInvalidFlag.set(isRipemDsub160XorInvalidFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackInvprexRelative(final Boolean b, int i) {
      isSha2Sub256XorInvprex.set(isSha2Sub256XorInvprex.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackJumpFlagRelative(final Boolean b, int i) {
      trmFlagXorJumpFlag.set(trmFlagXorJumpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackJumpxRelative(final Boolean b, int i) {
      rlpaddrFlagXorJumpx.set(rlpaddrFlagXorJumpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackKecFlagRelative(final Boolean b, int i) {
      warmXorKecFlag.set(warmXorKecFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackLogFlagRelative(final Boolean b, int i) {
      warmNewXorLogFlag.set(warmNewXorLogFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMaxcsxRelative(final Boolean b, int i) {
      maxcsx.set(maxcsx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackModFlagRelative(final Boolean b, int i) {
      modFlag.set(modFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMulFlagRelative(final Boolean b, int i) {
      mulFlag.set(mulFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMxpFlagRelative(final Boolean b, int i) {
      mxpFlag.set(mxpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMxpxRelative(final Boolean b, int i) {
      mxpx.set(mxpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackOobFlagRelative(final Boolean b, int i) {
      oobFlag.set(oobFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackOogxRelative(final Boolean b, int i) {
      oogx.set(oogx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackOpcxRelative(final Boolean b, int i) {
      opcx.set(opcx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackPushValueHiRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackPushValueLoRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackPushpopFlagRelative(final Boolean b, int i) {
      pushpopFlag.set(pushpopFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackRdcxRelative(final Boolean b, int i) {
      rdcx.set(rdcx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackShfFlagRelative(final Boolean b, int i) {
      shfFlag.set(shfFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackSoxRelative(final Boolean b, int i) {
      sox.set(sox.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackSstorexRelative(final Boolean b, int i) {
      sstorex.set(sstorex.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight1Relative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight2Relative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight3Relative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight4Relative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop1Relative(final Boolean b, int i) {
      stackItemPop1.set(stackItemPop1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop2Relative(final Boolean b, int i) {
      stackItemPop2.set(stackItemPop2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop3Relative(final Boolean b, int i) {
      stackItemPop3.set(stackItemPop3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemPop4Relative(final Boolean b, int i) {
      stackItemPop4.set(stackItemPop4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp1Relative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp2Relative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp3Relative(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(nonceXorCallValueXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp4Relative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(nonceNewXorContextNumberXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi1Relative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi2Relative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi3Relative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi4Relative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.set(rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo1Relative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.set(rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo2Relative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.set(rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo3Relative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.set(rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo4Relative(final BigInteger b, int i) {
      trmRawAddrHiXorStackItemValueLo4.set(trmRawAddrHiXorStackItemValueLo4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackramFlagRelative(final Boolean b, int i) {
      stackramFlag.set(stackramFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStaticFlagRelative(final Boolean b, int i) {
      staticFlag.set(staticFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStaticGasRelative(final BigInteger b, int i) {
      staticGas.set(staticGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStaticxRelative(final Boolean b, int i) {
      staticx.set(staticx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStoFlagRelative(final Boolean b, int i) {
      stoFlag.set(stoFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackSuxRelative(final Boolean b, int i) {
      sux.set(sux.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackSwapFlagRelative(final Boolean b, int i) {
      swapFlag.set(swapFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackTrmFlagRelative(final Boolean b, int i) {
      trmFlag.set(trmFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackTxnFlagRelative(final Boolean b, int i) {
      txnFlag.set(txnFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackWcpFlagRelative(final Boolean b, int i) {
      wcpFlag.set(wcpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageAddressHiRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageAddressLoRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageDeploymentNumberRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyHiRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyLoRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrChangesRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrHiRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsOrigRelative(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsZeroRelative(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrLoRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextHiRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsCurrRelative(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsOrigRelative(final Boolean b, int i) {
      existsNewXorCallFlagXorValNextIsOrig.set(existsNewXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsZeroRelative(final Boolean b, int i) {
      hasCodeXorConFlagXorValNextIsZero.set(hasCodeXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextLoRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigHiRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigIsZeroRelative(final Boolean b, int i) {
      hasCodeNewXorCopyFlagXorValOrigIsZero.set(hasCodeNewXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigLoRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmRelative(final Boolean b, int i) {
      isBlake2FXorCreateFlagXorWarm.set(isBlake2FXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmNewRelative(final Boolean b, int i) {
      isEcaddXorDecodedFlag1XorWarmNew.set(isEcaddXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionBasefeeRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.set(addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionCallDataSizeRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.set(addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressHiRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.set(balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressLoRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressHiRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.set(codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressLoRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.set(codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasLimitRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.set(codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasPriceRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.set(codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundAmountRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.set(codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundCounterFinalRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.set(codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitCodeSizeRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.set(depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitGasRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.set(depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitialBalanceRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.set(deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionIsDeploymentRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.set(deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionIsEip1559Relative(final Boolean b, int i) {
      depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.set(depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionLeftoverGasRelative(final BigInteger b, int i) {
      nonceXorCallValueXorStackItemStamp3XorLeftoverGas.set(nonceXorCallValueXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionNonceRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorStackItemStamp4XorNonce.set(nonceNewXorContextNumberXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionStatusCodeRelative(final Boolean b, int i) {
      depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.set(depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressHiRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.set(rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressLoRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.set(rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionTxnRequiresEvmExecutionRelative(final Boolean b, int i) {
      existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionValueRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.set(rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtAccountRelative(final Boolean b, int i) {
      peekAtAccount.set(peekAtAccount.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtContextRelative(final Boolean b, int i) {
      peekAtContext.set(peekAtContext.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtMiscellaneousRelative(final Boolean b, int i) {
      peekAtMiscellaneous.set(peekAtMiscellaneous.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtScenarioRelative(final Boolean b, int i) {
      peekAtScenario.set(peekAtScenario.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtStackRelative(final Boolean b, int i) {
      peekAtStack.set(peekAtStack.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtStorageRelative(final Boolean b, int i) {
      peekAtStorage.set(peekAtStorage.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPeekAtTransactionRelative(final Boolean b, int i) {
      peekAtTransaction.set(peekAtTransaction.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setProgramCounterRelative(final BigInteger b, int i) {
      programCounter.set(programCounter.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setProgramCounterNewRelative(final BigInteger b, int i) {
      programCounterNew.set(programCounterNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTransactionEndStampRelative(final BigInteger b, int i) {
      transactionEndStamp.set(transactionEndStamp.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTransactionRevertsRelative(final BigInteger b, int i) {
      transactionReverts.set(transactionReverts.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTwoLineInstructionRelative(final Boolean b, int i) {
      twoLineInstruction.set(twoLineInstruction.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTxExecRelative(final Boolean b, int i) {
      txExec.set(txExec.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTxFinlRelative(final Boolean b, int i) {
      txFinl.set(txFinl.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTxInitRelative(final Boolean b, int i) {
      txInit.set(txInit.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTxSkipRelative(final Boolean b, int i) {
      txSkip.set(txSkip.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTxWarmRelative(final Boolean b, int i) {
      txWarm.set(txWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ABORT_FLAG has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("ABSOLUTE_TRANSACTION_NUMBER has not been filled");
      }

      if (!filled.get(91)) {
        throw new IllegalStateException("ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_HEIGHT_xor_ADDRESS_HI_xor_BASEFEE has not been filled");
      }

      if (!filled.get(92)) {
        throw new IllegalStateException("ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_HEIGHT_NEW_xor_ADDRESS_LO_xor_CALL_DATA_SIZE has not been filled");
      }

      if (!filled.get(94)) {
        throw new IllegalStateException("BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_HEIGHT_UNDER_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO has not been filled");
      }

      if (!filled.get(93)) {
        throw new IllegalStateException("BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_HEIGHT_OVER_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("BATCH_NUMBER has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("CODE_ADDRESS_HI has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("CODE_ADDRESS_LO has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_NUMBER has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("CODE_DEPLOYMENT_STATUS has not been filled");
      }

      if (!filled.get(96)) {
        throw new IllegalStateException("CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_PUSH_VALUE_HI_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO has not been filled");
      }

      if (!filled.get(95)) {
        throw new IllegalStateException("CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_INSTRUCTION_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI has not been filled");
      }

      if (!filled.get(98)) {
        throw new IllegalStateException("CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_STACK_ITEM_HEIGHT_1_xor_VAL_NEXT_HI_xor_GAS_PRICE has not been filled");
      }

      if (!filled.get(97)) {
        throw new IllegalStateException("CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_PUSH_VALUE_LO_xor_VAL_CURR_LO_xor_GAS_LIMIT has not been filled");
      }

      if (!filled.get(100)) {
        throw new IllegalStateException("CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_STACK_ITEM_HEIGHT_3_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL has not been filled");
      }

      if (!filled.get(99)) {
        throw new IllegalStateException("CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_STACK_ITEM_HEIGHT_2_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("CONTEXT_GETS_REVRTD_FLAG has not been filled");
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
        throw new IllegalStateException("CONTEXT_SELF_REVRTS_FLAG has not been filled");
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

      if (!filled.get(103)) {
        throw new IllegalStateException("DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_STACK_ITEM_STAMP_2_xor_INIT_GAS has not been filled");
      }

      if (!filled.get(102)) {
        throw new IllegalStateException("DEP_NUM_xor_CALL_DATA_SIZE_xor_STACK_ITEM_STAMP_1_xor_INIT_CODE_SIZE has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException("DEP_STATUS_NEW_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException("DEP_STATUS_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559 has not been filled");
      }

      if (!filled.get(101)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_STACK_ITEM_HEIGHT_4_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException("DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("EXCEPTION_AHOY_FLAG has not been filled");
      }

      if (!filled.get(48)) {
        throw new IllegalStateException("EXISTS_NEW_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG has not been filled");
      }

      if (!filled.get(47)) {
        throw new IllegalStateException("EXISTS_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("FAILURE_CONDITION_FLAG has not been filled");
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
        throw new IllegalStateException("GAS_MEMORY_EXPANSION has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("GAS_NEXT has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("GAS_REFUND has not been filled");
      }

      if (!filled.get(50)) {
        throw new IllegalStateException("HAS_CODE_NEW_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO has not been filled");
      }

      if (!filled.get(49)) {
        throw new IllegalStateException("HAS_CODE_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("HUB_STAMP has not been filled");
      }

      if (!filled.get(51)) {
        throw new IllegalStateException("IS_BLAKE2f_xor_CREATE_FLAG_xor_WARM has not been filled");
      }

      if (!filled.get(52)) {
        throw new IllegalStateException("IS_ECADD_xor_DECODED_FLAG_1_xor_WARM_NEW has not been filled");
      }

      if (!filled.get(53)) {
        throw new IllegalStateException("IS_ECMUL_xor_DECODED_FLAG_2 has not been filled");
      }

      if (!filled.get(54)) {
        throw new IllegalStateException("IS_ECPAIRING_xor_DECODED_FLAG_3 has not been filled");
      }

      if (!filled.get(55)) {
        throw new IllegalStateException("IS_ECRECOVER_xor_DECODED_FLAG_4 has not been filled");
      }

      if (!filled.get(56)) {
        throw new IllegalStateException("IS_IDENTITY_xor_DUP_FLAG has not been filled");
      }

      if (!filled.get(57)) {
        throw new IllegalStateException("IS_MODEXP_xor_EXT_FLAG has not been filled");
      }

      if (!filled.get(58)) {
        throw new IllegalStateException("IS_PRECOMPILE_xor_HALT_FLAG has not been filled");
      }

      if (!filled.get(59)) {
        throw new IllegalStateException("IS_RIPEMDsub160_xor_INVALID_FLAG has not been filled");
      }

      if (!filled.get(60)) {
        throw new IllegalStateException("IS_SHA2sub256_xor_INVPREX has not been filled");
      }

      if (!filled.get(65)) {
        throw new IllegalStateException("MAXCSX has not been filled");
      }

      if (!filled.get(66)) {
        throw new IllegalStateException("MOD_FLAG has not been filled");
      }

      if (!filled.get(67)) {
        throw new IllegalStateException("MUL_FLAG has not been filled");
      }

      if (!filled.get(69)) {
        throw new IllegalStateException("MXP_FLAG has not been filled");
      }

      if (!filled.get(68)) {
        throw new IllegalStateException("MXPX has not been filled");
      }

      if (!filled.get(105)) {
        throw new IllegalStateException("NONCE_NEW_xor_CONTEXT_NUMBER_xor_STACK_ITEM_STAMP_4_xor_NONCE has not been filled");
      }

      if (!filled.get(104)) {
        throw new IllegalStateException("NONCE_xor_CALL_VALUE_xor_STACK_ITEM_STAMP_3_xor_LEFTOVER_GAS has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("NUMBER_OF_NON_STACK_ROWS has not been filled");
      }

      if (!filled.get(70)) {
        throw new IllegalStateException("OOB_FLAG has not been filled");
      }

      if (!filled.get(71)) {
        throw new IllegalStateException("OOGX has not been filled");
      }

      if (!filled.get(72)) {
        throw new IllegalStateException("OPCX has not been filled");
      }

      if (!filled.get(27)) {
        throw new IllegalStateException("PEEK_AT_ACCOUNT has not been filled");
      }

      if (!filled.get(28)) {
        throw new IllegalStateException("PEEK_AT_CONTEXT has not been filled");
      }

      if (!filled.get(29)) {
        throw new IllegalStateException("PEEK_AT_MISCELLANEOUS has not been filled");
      }

      if (!filled.get(30)) {
        throw new IllegalStateException("PEEK_AT_SCENARIO has not been filled");
      }

      if (!filled.get(31)) {
        throw new IllegalStateException("PEEK_AT_STACK has not been filled");
      }

      if (!filled.get(32)) {
        throw new IllegalStateException("PEEK_AT_STORAGE has not been filled");
      }

      if (!filled.get(33)) {
        throw new IllegalStateException("PEEK_AT_TRANSACTION has not been filled");
      }

      if (!filled.get(34)) {
        throw new IllegalStateException("PROGRAM_COUNTER has not been filled");
      }

      if (!filled.get(35)) {
        throw new IllegalStateException("PROGRAM_COUNTER_NEW has not been filled");
      }

      if (!filled.get(73)) {
        throw new IllegalStateException("PUSHPOP_FLAG has not been filled");
      }

      if (!filled.get(74)) {
        throw new IllegalStateException("RDCX has not been filled");
      }

      if (!filled.get(106)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_STACK_ITEM_VALUE_HI_1_xor_TO_ADDRESS_HI has not been filled");
      }

      if (!filled.get(107)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_STACK_ITEM_VALUE_HI_2_xor_TO_ADDRESS_LO has not been filled");
      }

      if (!filled.get(61)) {
        throw new IllegalStateException("RLPADDR___FLAG_xor_JUMPX has not been filled");
      }

      if (!filled.get(108)) {
        throw new IllegalStateException("RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_STACK_ITEM_VALUE_HI_3_xor_VALUE has not been filled");
      }

      if (!filled.get(109)) {
        throw new IllegalStateException("RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_STACK_ITEM_VALUE_HI_4 has not been filled");
      }

      if (!filled.get(110)) {
        throw new IllegalStateException("RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_STACK_ITEM_VALUE_LO_1 has not been filled");
      }

      if (!filled.get(111)) {
        throw new IllegalStateException("RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_STACK_ITEM_VALUE_LO_2 has not been filled");
      }

      if (!filled.get(112)) {
        throw new IllegalStateException("RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_STACK_ITEM_VALUE_LO_3 has not been filled");
      }

      if (!filled.get(75)) {
        throw new IllegalStateException("SHF_FLAG has not been filled");
      }

      if (!filled.get(76)) {
        throw new IllegalStateException("SOX has not been filled");
      }

      if (!filled.get(77)) {
        throw new IllegalStateException("SSTOREX has not been filled");
      }

      if (!filled.get(79)) {
        throw new IllegalStateException("STACK_ITEM_POP_1 has not been filled");
      }

      if (!filled.get(80)) {
        throw new IllegalStateException("STACK_ITEM_POP_2 has not been filled");
      }

      if (!filled.get(81)) {
        throw new IllegalStateException("STACK_ITEM_POP_3 has not been filled");
      }

      if (!filled.get(82)) {
        throw new IllegalStateException("STACK_ITEM_POP_4 has not been filled");
      }

      if (!filled.get(78)) {
        throw new IllegalStateException("STACKRAM_FLAG has not been filled");
      }

      if (!filled.get(84)) {
        throw new IllegalStateException("STATIC_FLAG has not been filled");
      }

      if (!filled.get(114)) {
        throw new IllegalStateException("STATIC_GAS has not been filled");
      }

      if (!filled.get(83)) {
        throw new IllegalStateException("STATICX has not been filled");
      }

      if (!filled.get(85)) {
        throw new IllegalStateException("STO_FLAG has not been filled");
      }

      if (!filled.get(86)) {
        throw new IllegalStateException("SUX has not been filled");
      }

      if (!filled.get(87)) {
        throw new IllegalStateException("SWAP_FLAG has not been filled");
      }

      if (!filled.get(36)) {
        throw new IllegalStateException("TRANSACTION_END_STAMP has not been filled");
      }

      if (!filled.get(37)) {
        throw new IllegalStateException("TRANSACTION_REVERTS has not been filled");
      }

      if (!filled.get(88)) {
        throw new IllegalStateException("TRM_FLAG has not been filled");
      }

      if (!filled.get(62)) {
        throw new IllegalStateException("TRM___FLAG_xor_JUMP_FLAG has not been filled");
      }

      if (!filled.get(113)) {
        throw new IllegalStateException("TRM___RAW_ADDR_HI_xor_STACK_ITEM_VALUE_LO_4 has not been filled");
      }

      if (!filled.get(38)) {
        throw new IllegalStateException("TWO_LINE_INSTRUCTION has not been filled");
      }

      if (!filled.get(39)) {
        throw new IllegalStateException("TX_EXEC has not been filled");
      }

      if (!filled.get(40)) {
        throw new IllegalStateException("TX_FINL has not been filled");
      }

      if (!filled.get(41)) {
        throw new IllegalStateException("TX_INIT has not been filled");
      }

      if (!filled.get(42)) {
        throw new IllegalStateException("TX_SKIP has not been filled");
      }

      if (!filled.get(43)) {
        throw new IllegalStateException("TX_WARM has not been filled");
      }

      if (!filled.get(89)) {
        throw new IllegalStateException("TXN_FLAG has not been filled");
      }

      if (!filled.get(64)) {
        throw new IllegalStateException("WARM_NEW_xor_LOG_FLAG has not been filled");
      }

      if (!filled.get(63)) {
        throw new IllegalStateException("WARM_xor_KEC_FLAG has not been filled");
      }

      if (!filled.get(90)) {
        throw new IllegalStateException("WCP_FLAG has not been filled");
      }


      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
          abortFlag.add(false);
          this.filled.set(0);
      }
      if (!filled.get(1)) {
          absoluteTransactionNumber.add(BigInteger.ZERO);
          this.filled.set(1);
      }
      if (!filled.get(91)) {
          addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee.add(BigInteger.ZERO);
          this.filled.set(91);
      }
      if (!filled.get(92)) {
          addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize.add(BigInteger.ZERO);
          this.filled.set(92);
      }
      if (!filled.get(94)) {
          balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(BigInteger.ZERO);
          this.filled.set(94);
      }
      if (!filled.get(93)) {
          balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi.add(BigInteger.ZERO);
          this.filled.set(93);
      }
      if (!filled.get(2)) {
          batchNumber.add(BigInteger.ZERO);
          this.filled.set(2);
      }
      if (!filled.get(3)) {
          callerContextNumber.add(BigInteger.ZERO);
          this.filled.set(3);
      }
      if (!filled.get(4)) {
          codeAddressHi.add(BigInteger.ZERO);
          this.filled.set(4);
      }
      if (!filled.get(5)) {
          codeAddressLo.add(BigInteger.ZERO);
          this.filled.set(5);
      }
      if (!filled.get(6)) {
          codeDeploymentNumber.add(BigInteger.ZERO);
          this.filled.set(6);
      }
      if (!filled.get(7)) {
          codeDeploymentStatus.add(false);
          this.filled.set(7);
      }
      if (!filled.get(96)) {
          codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo.add(BigInteger.ZERO);
          this.filled.set(96);
      }
      if (!filled.get(95)) {
          codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi.add(BigInteger.ZERO);
          this.filled.set(95);
      }
      if (!filled.get(98)) {
          codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice.add(BigInteger.ZERO);
          this.filled.set(98);
      }
      if (!filled.get(97)) {
          codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit.add(BigInteger.ZERO);
          this.filled.set(97);
      }
      if (!filled.get(100)) {
          codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal.add(BigInteger.ZERO);
          this.filled.set(100);
      }
      if (!filled.get(99)) {
          codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount.add(BigInteger.ZERO);
          this.filled.set(99);
      }
      if (!filled.get(8)) {
          contextGetsRevrtdFlag.add(false);
          this.filled.set(8);
      }
      if (!filled.get(9)) {
          contextMayChangeFlag.add(false);
          this.filled.set(9);
      }
      if (!filled.get(10)) {
          contextNumber.add(BigInteger.ZERO);
          this.filled.set(10);
      }
      if (!filled.get(11)) {
          contextNumberNew.add(BigInteger.ZERO);
          this.filled.set(11);
      }
      if (!filled.get(12)) {
          contextRevertStamp.add(BigInteger.ZERO);
          this.filled.set(12);
      }
      if (!filled.get(13)) {
          contextSelfRevrtsFlag.add(false);
          this.filled.set(13);
      }
      if (!filled.get(14)) {
          contextWillRevertFlag.add(false);
          this.filled.set(14);
      }
      if (!filled.get(15)) {
          counterNsr.add(BigInteger.ZERO);
          this.filled.set(15);
      }
      if (!filled.get(16)) {
          counterTli.add(false);
          this.filled.set(16);
      }
      if (!filled.get(103)) {
          depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas.add(BigInteger.ZERO);
          this.filled.set(103);
      }
      if (!filled.get(102)) {
          depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize.add(BigInteger.ZERO);
          this.filled.set(102);
      }
      if (!filled.get(46)) {
          depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode.add(false);
          this.filled.set(46);
      }
      if (!filled.get(45)) {
          depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559.add(false);
          this.filled.set(45);
      }
      if (!filled.get(101)) {
          deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance.add(BigInteger.ZERO);
          this.filled.set(101);
      }
      if (!filled.get(44)) {
          deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment.add(false);
          this.filled.set(44);
      }
      if (!filled.get(17)) {
          exceptionAhoyFlag.add(false);
          this.filled.set(17);
      }
      if (!filled.get(48)) {
          existsNewXorCallFlagXorValNextIsOrig.add(false);
          this.filled.set(48);
      }
      if (!filled.get(47)) {
          existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(false);
          this.filled.set(47);
      }
      if (!filled.get(18)) {
          failureConditionFlag.add(false);
          this.filled.set(18);
      }
      if (!filled.get(19)) {
          gasActual.add(BigInteger.ZERO);
          this.filled.set(19);
      }
      if (!filled.get(20)) {
          gasCost.add(BigInteger.ZERO);
          this.filled.set(20);
      }
      if (!filled.get(21)) {
          gasExpected.add(BigInteger.ZERO);
          this.filled.set(21);
      }
      if (!filled.get(22)) {
          gasMemoryExpansion.add(BigInteger.ZERO);
          this.filled.set(22);
      }
      if (!filled.get(23)) {
          gasNext.add(BigInteger.ZERO);
          this.filled.set(23);
      }
      if (!filled.get(24)) {
          gasRefund.add(BigInteger.ZERO);
          this.filled.set(24);
      }
      if (!filled.get(50)) {
          hasCodeNewXorCopyFlagXorValOrigIsZero.add(false);
          this.filled.set(50);
      }
      if (!filled.get(49)) {
          hasCodeXorConFlagXorValNextIsZero.add(false);
          this.filled.set(49);
      }
      if (!filled.get(25)) {
          hubStamp.add(BigInteger.ZERO);
          this.filled.set(25);
      }
      if (!filled.get(51)) {
          isBlake2FXorCreateFlagXorWarm.add(false);
          this.filled.set(51);
      }
      if (!filled.get(52)) {
          isEcaddXorDecodedFlag1XorWarmNew.add(false);
          this.filled.set(52);
      }
      if (!filled.get(53)) {
          isEcmulXorDecodedFlag2.add(false);
          this.filled.set(53);
      }
      if (!filled.get(54)) {
          isEcpairingXorDecodedFlag3.add(false);
          this.filled.set(54);
      }
      if (!filled.get(55)) {
          isEcrecoverXorDecodedFlag4.add(false);
          this.filled.set(55);
      }
      if (!filled.get(56)) {
          isIdentityXorDupFlag.add(false);
          this.filled.set(56);
      }
      if (!filled.get(57)) {
          isModexpXorExtFlag.add(false);
          this.filled.set(57);
      }
      if (!filled.get(58)) {
          isPrecompileXorHaltFlag.add(false);
          this.filled.set(58);
      }
      if (!filled.get(59)) {
          isRipemDsub160XorInvalidFlag.add(false);
          this.filled.set(59);
      }
      if (!filled.get(60)) {
          isSha2Sub256XorInvprex.add(false);
          this.filled.set(60);
      }
      if (!filled.get(65)) {
          maxcsx.add(false);
          this.filled.set(65);
      }
      if (!filled.get(66)) {
          modFlag.add(false);
          this.filled.set(66);
      }
      if (!filled.get(67)) {
          mulFlag.add(false);
          this.filled.set(67);
      }
      if (!filled.get(69)) {
          mxpFlag.add(false);
          this.filled.set(69);
      }
      if (!filled.get(68)) {
          mxpx.add(false);
          this.filled.set(68);
      }
      if (!filled.get(105)) {
          nonceNewXorContextNumberXorStackItemStamp4XorNonce.add(BigInteger.ZERO);
          this.filled.set(105);
      }
      if (!filled.get(104)) {
          nonceXorCallValueXorStackItemStamp3XorLeftoverGas.add(BigInteger.ZERO);
          this.filled.set(104);
      }
      if (!filled.get(26)) {
          numberOfNonStackRows.add(BigInteger.ZERO);
          this.filled.set(26);
      }
      if (!filled.get(70)) {
          oobFlag.add(false);
          this.filled.set(70);
      }
      if (!filled.get(71)) {
          oogx.add(false);
          this.filled.set(71);
      }
      if (!filled.get(72)) {
          opcx.add(false);
          this.filled.set(72);
      }
      if (!filled.get(27)) {
          peekAtAccount.add(false);
          this.filled.set(27);
      }
      if (!filled.get(28)) {
          peekAtContext.add(false);
          this.filled.set(28);
      }
      if (!filled.get(29)) {
          peekAtMiscellaneous.add(false);
          this.filled.set(29);
      }
      if (!filled.get(30)) {
          peekAtScenario.add(false);
          this.filled.set(30);
      }
      if (!filled.get(31)) {
          peekAtStack.add(false);
          this.filled.set(31);
      }
      if (!filled.get(32)) {
          peekAtStorage.add(false);
          this.filled.set(32);
      }
      if (!filled.get(33)) {
          peekAtTransaction.add(false);
          this.filled.set(33);
      }
      if (!filled.get(34)) {
          programCounter.add(BigInteger.ZERO);
          this.filled.set(34);
      }
      if (!filled.get(35)) {
          programCounterNew.add(BigInteger.ZERO);
          this.filled.set(35);
      }
      if (!filled.get(73)) {
          pushpopFlag.add(false);
          this.filled.set(73);
      }
      if (!filled.get(74)) {
          rdcx.add(false);
          this.filled.set(74);
      }
      if (!filled.get(106)) {
          rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi.add(BigInteger.ZERO);
          this.filled.set(106);
      }
      if (!filled.get(107)) {
          rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo.add(BigInteger.ZERO);
          this.filled.set(107);
      }
      if (!filled.get(61)) {
          rlpaddrFlagXorJumpx.add(false);
          this.filled.set(61);
      }
      if (!filled.get(108)) {
          rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue.add(BigInteger.ZERO);
          this.filled.set(108);
      }
      if (!filled.get(109)) {
          rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4.add(BigInteger.ZERO);
          this.filled.set(109);
      }
      if (!filled.get(110)) {
          rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1.add(BigInteger.ZERO);
          this.filled.set(110);
      }
      if (!filled.get(111)) {
          rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2.add(BigInteger.ZERO);
          this.filled.set(111);
      }
      if (!filled.get(112)) {
          rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3.add(BigInteger.ZERO);
          this.filled.set(112);
      }
      if (!filled.get(75)) {
          shfFlag.add(false);
          this.filled.set(75);
      }
      if (!filled.get(76)) {
          sox.add(false);
          this.filled.set(76);
      }
      if (!filled.get(77)) {
          sstorex.add(false);
          this.filled.set(77);
      }
      if (!filled.get(79)) {
          stackItemPop1.add(false);
          this.filled.set(79);
      }
      if (!filled.get(80)) {
          stackItemPop2.add(false);
          this.filled.set(80);
      }
      if (!filled.get(81)) {
          stackItemPop3.add(false);
          this.filled.set(81);
      }
      if (!filled.get(82)) {
          stackItemPop4.add(false);
          this.filled.set(82);
      }
      if (!filled.get(78)) {
          stackramFlag.add(false);
          this.filled.set(78);
      }
      if (!filled.get(84)) {
          staticFlag.add(false);
          this.filled.set(84);
      }
      if (!filled.get(114)) {
          staticGas.add(BigInteger.ZERO);
          this.filled.set(114);
      }
      if (!filled.get(83)) {
          staticx.add(false);
          this.filled.set(83);
      }
      if (!filled.get(85)) {
          stoFlag.add(false);
          this.filled.set(85);
      }
      if (!filled.get(86)) {
          sux.add(false);
          this.filled.set(86);
      }
      if (!filled.get(87)) {
          swapFlag.add(false);
          this.filled.set(87);
      }
      if (!filled.get(36)) {
          transactionEndStamp.add(BigInteger.ZERO);
          this.filled.set(36);
      }
      if (!filled.get(37)) {
          transactionReverts.add(BigInteger.ZERO);
          this.filled.set(37);
      }
      if (!filled.get(88)) {
          trmFlag.add(false);
          this.filled.set(88);
      }
      if (!filled.get(62)) {
          trmFlagXorJumpFlag.add(false);
          this.filled.set(62);
      }
      if (!filled.get(113)) {
          trmRawAddrHiXorStackItemValueLo4.add(BigInteger.ZERO);
          this.filled.set(113);
      }
      if (!filled.get(38)) {
          twoLineInstruction.add(false);
          this.filled.set(38);
      }
      if (!filled.get(39)) {
          txExec.add(false);
          this.filled.set(39);
      }
      if (!filled.get(40)) {
          txFinl.add(false);
          this.filled.set(40);
      }
      if (!filled.get(41)) {
          txInit.add(false);
          this.filled.set(41);
      }
      if (!filled.get(42)) {
          txSkip.add(false);
          this.filled.set(42);
      }
      if (!filled.get(43)) {
          txWarm.add(false);
          this.filled.set(43);
      }
      if (!filled.get(89)) {
          txnFlag.add(false);
          this.filled.set(89);
      }
      if (!filled.get(64)) {
          warmNewXorLogFlag.add(false);
          this.filled.set(64);
      }
      if (!filled.get(63)) {
          warmXorKecFlag.add(false);
          this.filled.set(63);
      }
      if (!filled.get(90)) {
          wcpFlag.add(false);
          this.filled.set(90);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
        abortFlag,
        absoluteTransactionNumber,
        addrHiXorAccountAddressHiXorHeightXorAddressHiXorBasefee,
        addrLoXorAccountAddressLoXorHeightNewXorAddressLoXorCallDataSize,
        balanceNewXorByteCodeAddressHiXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo,
        balanceXorAccountDeploymentNumberXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi,
        batchNumber,
        callerContextNumber,
        codeAddressHi,
        codeAddressLo,
        codeDeploymentNumber,
        codeDeploymentStatus,
        codeHashHiNewXorByteCodeDeploymentNumberXorPushValueHiXorValCurrHiXorFromAddressLo,
        codeHashHiXorByteCodeAddressLoXorInstructionXorStorageKeyLoXorFromAddressHi,
        codeHashLoNewXorCallerAddressHiXorStackItemHeight1XorValNextHiXorGasPrice,
        codeHashLoXorByteCodeDeploymentStatusXorPushValueLoXorValCurrLoXorGasLimit,
        codeSizeNewXorCallerContextNumberXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal,
        codeSizeXorCallerAddressLoXorStackItemHeight2XorValNextLoXorGasRefundAmount,
        contextGetsRevrtdFlag,
        contextMayChangeFlag,
        contextNumber,
        contextNumberNew,
        contextRevertStamp,
        contextSelfRevrtsFlag,
        contextWillRevertFlag,
        counterNsr,
        counterTli,
        depNumNewXorCallStackDepthXorStackItemStamp2XorInitGas,
        depNumXorCallDataSizeXorStackItemStamp1XorInitCodeSize,
        depStatusNewXorBinFlagXorValCurrIsZeroXorStatusCode,
        depStatusXorAddFlagXorValCurrIsOrigXorIsEip1559,
        deploymentNumberInftyXorCallDataOffsetXorStackItemHeight4XorValOrigLoXorInitialBalance,
        deploymentStatusInftyXorUpdateXorAccFlagXorValCurrChangesXorIsDeployment,
        exceptionAhoyFlag,
        existsNewXorCallFlagXorValNextIsOrig,
        existsXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution,
        failureConditionFlag,
        gasActual,
        gasCost,
        gasExpected,
        gasMemoryExpansion,
        gasNext,
        gasRefund,
        hasCodeNewXorCopyFlagXorValOrigIsZero,
        hasCodeXorConFlagXorValNextIsZero,
        hubStamp,
        isBlake2FXorCreateFlagXorWarm,
        isEcaddXorDecodedFlag1XorWarmNew,
        isEcmulXorDecodedFlag2,
        isEcpairingXorDecodedFlag3,
        isEcrecoverXorDecodedFlag4,
        isIdentityXorDupFlag,
        isModexpXorExtFlag,
        isPrecompileXorHaltFlag,
        isRipemDsub160XorInvalidFlag,
        isSha2Sub256XorInvprex,
        maxcsx,
        modFlag,
        mulFlag,
        mxpFlag,
        mxpx,
        nonceNewXorContextNumberXorStackItemStamp4XorNonce,
        nonceXorCallValueXorStackItemStamp3XorLeftoverGas,
        numberOfNonStackRows,
        oobFlag,
        oogx,
        opcx,
        peekAtAccount,
        peekAtContext,
        peekAtMiscellaneous,
        peekAtScenario,
        peekAtStack,
        peekAtStorage,
        peekAtTransaction,
        programCounter,
        programCounterNew,
        pushpopFlag,
        rdcx,
        rlpaddrDepAddrHiXorIsStaticXorStackItemValueHi1XorToAddressHi,
        rlpaddrDepAddrLoXorReturnerContextNumberXorStackItemValueHi2XorToAddressLo,
        rlpaddrFlagXorJumpx,
        rlpaddrKecHiXorReturnerIsPrecompileXorStackItemValueHi3XorValue,
        rlpaddrKecLoXorReturnAtOffsetXorStackItemValueHi4,
        rlpaddrRecipeXorReturnAtSizeXorStackItemValueLo1,
        rlpaddrSaltHiXorReturnDataOffsetXorStackItemValueLo2,
        rlpaddrSaltLoXorReturnDataSizeXorStackItemValueLo3,
        shfFlag,
        sox,
        sstorex,
        stackItemPop1,
        stackItemPop2,
        stackItemPop3,
        stackItemPop4,
        stackramFlag,
        staticFlag,
        staticGas,
        staticx,
        stoFlag,
        sux,
        swapFlag,
        transactionEndStamp,
        transactionReverts,
        trmFlag,
        trmFlagXorJumpFlag,
        trmRawAddrHiXorStackItemValueLo4,
        twoLineInstruction,
        txExec,
        txFinl,
        txInit,
        txSkip,
        txWarm,
        txnFlag,
        warmNewXorLogFlag,
        warmXorKecFlag,
        wcpFlag);
    }
  }
}
