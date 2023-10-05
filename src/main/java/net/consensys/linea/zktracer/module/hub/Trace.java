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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
    @JsonProperty("ABORT_FLAG") List<Boolean> abortFlag,
    @JsonProperty("ABSOLUTE_TRANSACTION_NUMBER") List<BigInteger> absoluteTransactionNumber,
    @JsonProperty(
            "ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_CCRS_STAMP_xor_HEIGHT_xor_ADDRESS_HI_xor_BASEFEE")
        List<BigInteger> addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee,
    @JsonProperty(
            "ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_MMU___CN_S_xor_HEIGHT_NEW_xor_ADDRESS_LO_xor_CALL_DATA_SIZE")
        List<BigInteger> addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize,
    @JsonProperty(
            "BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_MMU___INST_xor_HEIGHT_UNDER_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO")
        List<BigInteger>
            balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo,
    @JsonProperty(
            "BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_MMU___CN_T_xor_HEIGHT_OVER_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI")
        List<BigInteger>
            balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi,
    @JsonProperty("BATCH_NUMBER") List<BigInteger> batchNumber,
    @JsonProperty("CALLER_CONTEXT_NUMBER") List<BigInteger> callerContextNumber,
    @JsonProperty("CODE_ADDRESS_HI") List<BigInteger> codeAddressHi,
    @JsonProperty("CODE_ADDRESS_LO") List<BigInteger> codeAddressLo,
    @JsonProperty("CODE_DEPLOYMENT_NUMBER") List<BigInteger> codeDeploymentNumber,
    @JsonProperty("CODE_DEPLOYMENT_STATUS") List<Boolean> codeDeploymentStatus,
    @JsonProperty(
            "CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_MMU___NUM_2_xor_PUSH_VALUE_HI_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO")
        List<BigInteger>
            codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo,
    @JsonProperty(
            "CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_MMU___NUM_1_xor_INSTRUCTION_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI")
        List<BigInteger>
            codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi,
    @JsonProperty(
            "CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_MMU___SIZE_xor_STACK_ITEM_HEIGHT_1_xor_VAL_NEXT_HI_xor_GAS_PRICE")
        List<BigInteger>
            codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice,
    @JsonProperty(
            "CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_MMU___REFS_xor_PUSH_VALUE_LO_xor_VAL_CURR_LO_xor_GAS_LIMIT")
        List<BigInteger>
            codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit,
    @JsonProperty(
            "CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_MMU___SOURCE_OFFSET_LO_xor_STACK_ITEM_HEIGHT_3_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL")
        List<BigInteger>
            codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal,
    @JsonProperty(
            "CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_MMU___SOURCE_OFFSET_HI_xor_STACK_ITEM_HEIGHT_2_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT")
        List<BigInteger>
            codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount,
    @JsonProperty("CONTEXT_GETS_REVRTD_FLAG") List<Boolean> contextGetsRevrtdFlag,
    @JsonProperty("CONTEXT_MAY_CHANGE_FLAG") List<Boolean> contextMayChangeFlag,
    @JsonProperty("CONTEXT_NUMBER") List<BigInteger> contextNumber,
    @JsonProperty("CONTEXT_NUMBER_NEW") List<BigInteger> contextNumberNew,
    @JsonProperty("CONTEXT_REVERT_STAMP") List<BigInteger> contextRevertStamp,
    @JsonProperty("CONTEXT_SELF_REVRTS_FLAG") List<Boolean> contextSelfRevrtsFlag,
    @JsonProperty("CONTEXT_WILL_REVERT_FLAG") List<Boolean> contextWillRevertFlag,
    @JsonProperty("COUNTER_NSR") List<BigInteger> counterNsr,
    @JsonProperty("COUNTER_TLI") List<Boolean> counterTli,
    @JsonProperty(
            "DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_MMU___TARGET_OFFSET_HI_xor_STACK_ITEM_STAMP_2_xor_INIT_GAS")
        List<BigInteger> depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas,
    @JsonProperty(
            "DEP_NUM_xor_CALL_DATA_SIZE_xor_MMU___STACK_VAL_LO_xor_STACK_ITEM_STAMP_1_xor_INIT_CODE_SIZE")
        List<BigInteger> depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize,
    @JsonProperty(
            "DEP_STATUS_NEW_xor_FCOND_FLAG_xor_CODEDEPOSIT_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE")
        List<Boolean> depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode,
    @JsonProperty(
            "DEP_STATUS_xor_CCSR_FLAG_xor_CALL_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559")
        List<Boolean> depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559,
    @JsonProperty(
            "DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_MMU___STACK_VAL_HI_xor_STACK_ITEM_HEIGHT_4_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE")
        List<BigInteger>
            deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance,
    @JsonProperty(
            "DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ABORT_FLAG_xor_BLAKE2f_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT")
        List<Boolean>
            deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment,
    @JsonProperty("EXCEPTION_AHOY_FLAG") List<Boolean> exceptionAhoyFlag,
    @JsonProperty("EXISTS_NEW_xor_MXP___DEPLOYS_xor_ECADD_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG")
        List<Boolean> existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig,
    @JsonProperty(
            "EXISTS_xor_MMU___FLAG_xor_CREATE_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION")
        List<Boolean> existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution,
    @JsonProperty("FAILURE_CONDITION_FLAG") List<Boolean> failureConditionFlag,
    @JsonProperty("GAS_ACTUAL") List<BigInteger> gasActual,
    @JsonProperty("GAS_COST") List<BigInteger> gasCost,
    @JsonProperty("GAS_EXPECTED") List<BigInteger> gasExpected,
    @JsonProperty("GAS_MEMORY_EXPANSION") List<BigInteger> gasMemoryExpansion,
    @JsonProperty("GAS_NEXT") List<BigInteger> gasNext,
    @JsonProperty("GAS_REFUND") List<BigInteger> gasRefund,
    @JsonProperty("HAS_CODE_NEW_xor_MXP___MXPX_xor_ECPAIRING_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO")
        List<Boolean> hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero,
    @JsonProperty("HAS_CODE_xor_MXP___FLAG_xor_ECMUL_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO")
        List<Boolean> hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero,
    @JsonProperty("HUB_STAMP") List<BigInteger> hubStamp,
    @JsonProperty("IS_BLAKE2f_xor_OOB___EVENT_1_xor_ECRECOVER_xor_CREATE_FLAG_xor_WARM")
        List<Boolean> isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm,
    @JsonProperty("IS_ECADD_xor_OOB___EVENT_2_xor_IDENTITY_xor_DECODED_FLAG_1_xor_WARM_NEW")
        List<Boolean> isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew,
    @JsonProperty("IS_ECMUL_xor_OOB___FLAG_xor_MODEXP_xor_DECODED_FLAG_2")
        List<Boolean> isEcmulXorOobFlagXorModexpXorDecodedFlag2,
    @JsonProperty("IS_ECPAIRING_xor_PRECINFO___FLAG_xor_RIPEMDsub160_xor_DECODED_FLAG_3")
        List<Boolean> isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3,
    @JsonProperty("IS_ECRECOVER_xor_STP___EXISTS_xor_SCN_1_xor_DECODED_FLAG_4")
        List<Boolean> isEcrecoverXorStpExistsXorScn1XorDecodedFlag4,
    @JsonProperty("IS_IDENTITY_xor_STP___FLAG_xor_SCN_10_xor_DUP_FLAG")
        List<Boolean> isIdentityXorStpFlagXorScn10XorDupFlag,
    @JsonProperty("IS_MODEXP_xor_STP___OOGX_xor_SCN_11_xor_EXT_FLAG")
        List<Boolean> isModexpXorStpOogxXorScn11XorExtFlag,
    @JsonProperty("IS_PRECOMPILE_xor_STP___WARM_xor_SCN_12_xor_HALT_FLAG")
        List<Boolean> isPrecompileXorStpWarmXorScn12XorHaltFlag,
    @JsonProperty("IS_RIPEMDsub160_xor_SCN_2_xor_INVALID_FLAG")
        List<Boolean> isRipemDsub160XorScn2XorInvalidFlag,
    @JsonProperty("IS_SHA2sub256_xor_SCN_3_xor_INVPREX")
        List<Boolean> isSha2Sub256XorScn3XorInvprex,
    @JsonProperty("MXP_FLAG") List<Boolean> mxpFlag,
    @JsonProperty("MXP___SIZE_2_LO_xor_STATIC_GAS") List<BigInteger> mxpSize2LoXorStaticGas,
    @JsonProperty("MXP___WORDS") List<BigInteger> mxpWords,
    @JsonProperty("NONCE_NEW_xor_CONTEXT_NUMBER_xor_MXP___GAS_MXP_xor_STACK_ITEM_STAMP_4_xor_NONCE")
        List<BigInteger> nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce,
    @JsonProperty(
            "NONCE_xor_CALL_VALUE_xor_MMU___TARGET_OFFSET_LO_xor_STACK_ITEM_STAMP_3_xor_LEFTOVER_GAS")
        List<BigInteger> nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas,
    @JsonProperty("NUMBER_OF_NON_STACK_ROWS") List<BigInteger> numberOfNonStackRows,
    @JsonProperty("OOB_FLAG") List<Boolean> oobFlag,
    @JsonProperty("OOB___INST") List<BigInteger> oobInst,
    @JsonProperty("OOB___OUTGOING_DATA_1") List<BigInteger> oobOutgoingData1,
    @JsonProperty("OOB___OUTGOING_DATA_2") List<BigInteger> oobOutgoingData2,
    @JsonProperty("OOB___OUTGOING_DATA_3") List<BigInteger> oobOutgoingData3,
    @JsonProperty("OOB___OUTGOING_DATA_4") List<BigInteger> oobOutgoingData4,
    @JsonProperty("OOB___OUTGOING_DATA_5") List<BigInteger> oobOutgoingData5,
    @JsonProperty("OOB___OUTGOING_DATA_6") List<BigInteger> oobOutgoingData6,
    @JsonProperty("OOGX") List<Boolean> oogx,
    @JsonProperty("OPCX") List<Boolean> opcx,
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
    @JsonProperty(
            "RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_MXP___INST_xor_STACK_ITEM_VALUE_HI_1_xor_TO_ADDRESS_HI")
        List<BigInteger> rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi,
    @JsonProperty(
            "RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_MXP___OFFSET_1_HI_xor_STACK_ITEM_VALUE_HI_2_xor_TO_ADDRESS_LO")
        List<BigInteger>
            rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo,
    @JsonProperty("RLPADDR___FLAG_xor_SCN_4_xor_JUMPX") List<Boolean> rlpaddrFlagXorScn4XorJumpx,
    @JsonProperty(
            "RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_MXP___OFFSET_1_LO_xor_STACK_ITEM_VALUE_HI_3_xor_VALUE")
        List<BigInteger>
            rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue,
    @JsonProperty(
            "RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_MXP___OFFSET_2_HI_xor_STACK_ITEM_VALUE_HI_4")
        List<BigInteger> rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4,
    @JsonProperty(
            "RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_MXP___OFFSET_2_LO_xor_STACK_ITEM_VALUE_LO_1")
        List<BigInteger> rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1,
    @JsonProperty(
            "RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_MXP___SIZE_1_HI_xor_STACK_ITEM_VALUE_LO_2")
        List<BigInteger> rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2,
    @JsonProperty(
            "RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_MXP___SIZE_1_LO_xor_STACK_ITEM_VALUE_LO_3")
        List<BigInteger> rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3,
    @JsonProperty("SCN_8_xor_MAXCSX") List<Boolean> scn8XorMaxcsx,
    @JsonProperty("SCN_9_xor_MOD_FLAG") List<Boolean> scn9XorModFlag,
    @JsonProperty("SELFDESTRUCT_xor_MUL_FLAG") List<Boolean> selfdestructXorMulFlag,
    @JsonProperty("SHA2sub256_xor_MXPX") List<Boolean> sha2Sub256XorMxpx,
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
    @JsonProperty("SUX") List<Boolean> sux,
    @JsonProperty("SWAP_FLAG") List<Boolean> swapFlag,
    @JsonProperty("TRANSACTION_END_STAMP") List<BigInteger> transactionEndStamp,
    @JsonProperty("TRANSACTION_REVERTS") List<BigInteger> transactionReverts,
    @JsonProperty("TRM_FLAG") List<Boolean> trmFlag,
    @JsonProperty("TRM___FLAG_xor_SCN_5_xor_JUMP_FLAG") List<Boolean> trmFlagXorScn5XorJumpFlag,
    @JsonProperty("TRM___RAW_ADDR_HI_xor_MXP___SIZE_2_HI_xor_STACK_ITEM_VALUE_LO_4")
        List<BigInteger> trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4,
    @JsonProperty("TWO_LINE_INSTRUCTION") List<Boolean> twoLineInstruction,
    @JsonProperty("TX_EXEC") List<Boolean> txExec,
    @JsonProperty("TX_FINL") List<Boolean> txFinl,
    @JsonProperty("TX_INIT") List<Boolean> txInit,
    @JsonProperty("TX_SKIP") List<Boolean> txSkip,
    @JsonProperty("TX_WARM") List<Boolean> txWarm,
    @JsonProperty("TXN_FLAG") List<Boolean> txnFlag,
    @JsonProperty("WARM_NEW_xor_SCN_7_xor_LOG_FLAG") List<Boolean> warmNewXorScn7XorLogFlag,
    @JsonProperty("WARM_xor_SCN_6_xor_KEC_FLAG") List<Boolean> warmXorScn6XorKecFlag,
    @JsonProperty("WCP_FLAG") List<Boolean> wcpFlag) {
  public static TraceBuilder builder() {
    return new TraceBuilder();
  }

  public static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ABORT_FLAG")
    private final List<Boolean> abortFlag = new ArrayList<>();

    @JsonProperty("ABSOLUTE_TRANSACTION_NUMBER")
    private final List<BigInteger> absoluteTransactionNumber = new ArrayList<>();

    @JsonProperty(
        "ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_CCRS_STAMP_xor_HEIGHT_xor_ADDRESS_HI_xor_BASEFEE")
    private final List<BigInteger>
        addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee = new ArrayList<>();

    @JsonProperty(
        "ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_MMU___CN_S_xor_HEIGHT_NEW_xor_ADDRESS_LO_xor_CALL_DATA_SIZE")
    private final List<BigInteger>
        addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize =
            new ArrayList<>();

    @JsonProperty(
        "BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_MMU___INST_xor_HEIGHT_UNDER_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO")
    private final List<BigInteger>
        balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo =
            new ArrayList<>();

    @JsonProperty(
        "BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_MMU___CN_T_xor_HEIGHT_OVER_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI")
    private final List<BigInteger>
        balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi =
            new ArrayList<>();

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

    @JsonProperty(
        "CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_MMU___NUM_2_xor_PUSH_VALUE_HI_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO")
    private final List<BigInteger>
        codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo =
            new ArrayList<>();

    @JsonProperty(
        "CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_MMU___NUM_1_xor_INSTRUCTION_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI")
    private final List<BigInteger>
        codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi =
            new ArrayList<>();

    @JsonProperty(
        "CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_MMU___SIZE_xor_STACK_ITEM_HEIGHT_1_xor_VAL_NEXT_HI_xor_GAS_PRICE")
    private final List<BigInteger>
        codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice =
            new ArrayList<>();

    @JsonProperty(
        "CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_MMU___REFS_xor_PUSH_VALUE_LO_xor_VAL_CURR_LO_xor_GAS_LIMIT")
    private final List<BigInteger>
        codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit =
            new ArrayList<>();

    @JsonProperty(
        "CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_MMU___SOURCE_OFFSET_LO_xor_STACK_ITEM_HEIGHT_3_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL")
    private final List<BigInteger>
        codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal =
            new ArrayList<>();

    @JsonProperty(
        "CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_MMU___SOURCE_OFFSET_HI_xor_STACK_ITEM_HEIGHT_2_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT")
    private final List<BigInteger>
        codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount =
            new ArrayList<>();

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

    @JsonProperty(
        "DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_MMU___TARGET_OFFSET_HI_xor_STACK_ITEM_STAMP_2_xor_INIT_GAS")
    private final List<BigInteger>
        depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas =
            new ArrayList<>();

    @JsonProperty(
        "DEP_NUM_xor_CALL_DATA_SIZE_xor_MMU___STACK_VAL_LO_xor_STACK_ITEM_STAMP_1_xor_INIT_CODE_SIZE")
    private final List<BigInteger>
        depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize = new ArrayList<>();

    @JsonProperty(
        "DEP_STATUS_NEW_xor_FCOND_FLAG_xor_CODEDEPOSIT_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE")
    private final List<Boolean>
        depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode =
            new ArrayList<>();

    @JsonProperty(
        "DEP_STATUS_xor_CCSR_FLAG_xor_CALL_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559")
    private final List<Boolean> depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559 =
        new ArrayList<>();

    @JsonProperty(
        "DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_MMU___STACK_VAL_HI_xor_STACK_ITEM_HEIGHT_4_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE")
    private final List<BigInteger>
        deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance =
            new ArrayList<>();

    @JsonProperty(
        "DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ABORT_FLAG_xor_BLAKE2f_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT")
    private final List<Boolean>
        deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment =
            new ArrayList<>();

    @JsonProperty("EXCEPTION_AHOY_FLAG")
    private final List<Boolean> exceptionAhoyFlag = new ArrayList<>();

    @JsonProperty("EXISTS_NEW_xor_MXP___DEPLOYS_xor_ECADD_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG")
    private final List<Boolean> existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig =
        new ArrayList<>();

    @JsonProperty(
        "EXISTS_xor_MMU___FLAG_xor_CREATE_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION")
    private final List<Boolean>
        existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution =
            new ArrayList<>();

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

    @JsonProperty("HAS_CODE_NEW_xor_MXP___MXPX_xor_ECPAIRING_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO")
    private final List<Boolean> hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero =
        new ArrayList<>();

    @JsonProperty("HAS_CODE_xor_MXP___FLAG_xor_ECMUL_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO")
    private final List<Boolean> hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero =
        new ArrayList<>();

    @JsonProperty("HUB_STAMP")
    private final List<BigInteger> hubStamp = new ArrayList<>();

    @JsonProperty("IS_BLAKE2f_xor_OOB___EVENT_1_xor_ECRECOVER_xor_CREATE_FLAG_xor_WARM")
    private final List<Boolean> isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm =
        new ArrayList<>();

    @JsonProperty("IS_ECADD_xor_OOB___EVENT_2_xor_IDENTITY_xor_DECODED_FLAG_1_xor_WARM_NEW")
    private final List<Boolean> isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew =
        new ArrayList<>();

    @JsonProperty("IS_ECMUL_xor_OOB___FLAG_xor_MODEXP_xor_DECODED_FLAG_2")
    private final List<Boolean> isEcmulXorOobFlagXorModexpXorDecodedFlag2 = new ArrayList<>();

    @JsonProperty("IS_ECPAIRING_xor_PRECINFO___FLAG_xor_RIPEMDsub160_xor_DECODED_FLAG_3")
    private final List<Boolean> isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3 =
        new ArrayList<>();

    @JsonProperty("IS_ECRECOVER_xor_STP___EXISTS_xor_SCN_1_xor_DECODED_FLAG_4")
    private final List<Boolean> isEcrecoverXorStpExistsXorScn1XorDecodedFlag4 = new ArrayList<>();

    @JsonProperty("IS_IDENTITY_xor_STP___FLAG_xor_SCN_10_xor_DUP_FLAG")
    private final List<Boolean> isIdentityXorStpFlagXorScn10XorDupFlag = new ArrayList<>();

    @JsonProperty("IS_MODEXP_xor_STP___OOGX_xor_SCN_11_xor_EXT_FLAG")
    private final List<Boolean> isModexpXorStpOogxXorScn11XorExtFlag = new ArrayList<>();

    @JsonProperty("IS_PRECOMPILE_xor_STP___WARM_xor_SCN_12_xor_HALT_FLAG")
    private final List<Boolean> isPrecompileXorStpWarmXorScn12XorHaltFlag = new ArrayList<>();

    @JsonProperty("IS_RIPEMDsub160_xor_SCN_2_xor_INVALID_FLAG")
    private final List<Boolean> isRipemDsub160XorScn2XorInvalidFlag = new ArrayList<>();

    @JsonProperty("IS_SHA2sub256_xor_SCN_3_xor_INVPREX")
    private final List<Boolean> isSha2Sub256XorScn3XorInvprex = new ArrayList<>();

    @JsonProperty("MXP_FLAG")
    private final List<Boolean> mxpFlag = new ArrayList<>();

    @JsonProperty("MXP___SIZE_2_LO_xor_STATIC_GAS")
    private final List<BigInteger> mxpSize2LoXorStaticGas = new ArrayList<>();

    @JsonProperty("MXP___WORDS")
    private final List<BigInteger> mxpWords = new ArrayList<>();

    @JsonProperty("NONCE_NEW_xor_CONTEXT_NUMBER_xor_MXP___GAS_MXP_xor_STACK_ITEM_STAMP_4_xor_NONCE")
    private final List<BigInteger> nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce =
        new ArrayList<>();

    @JsonProperty(
        "NONCE_xor_CALL_VALUE_xor_MMU___TARGET_OFFSET_LO_xor_STACK_ITEM_STAMP_3_xor_LEFTOVER_GAS")
    private final List<BigInteger>
        nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas = new ArrayList<>();

    @JsonProperty("NUMBER_OF_NON_STACK_ROWS")
    private final List<BigInteger> numberOfNonStackRows = new ArrayList<>();

    @JsonProperty("OOB_FLAG")
    private final List<Boolean> oobFlag = new ArrayList<>();

    @JsonProperty("OOB___INST")
    private final List<BigInteger> oobInst = new ArrayList<>();

    @JsonProperty("OOB___OUTGOING_DATA_1")
    private final List<BigInteger> oobOutgoingData1 = new ArrayList<>();

    @JsonProperty("OOB___OUTGOING_DATA_2")
    private final List<BigInteger> oobOutgoingData2 = new ArrayList<>();

    @JsonProperty("OOB___OUTGOING_DATA_3")
    private final List<BigInteger> oobOutgoingData3 = new ArrayList<>();

    @JsonProperty("OOB___OUTGOING_DATA_4")
    private final List<BigInteger> oobOutgoingData4 = new ArrayList<>();

    @JsonProperty("OOB___OUTGOING_DATA_5")
    private final List<BigInteger> oobOutgoingData5 = new ArrayList<>();

    @JsonProperty("OOB___OUTGOING_DATA_6")
    private final List<BigInteger> oobOutgoingData6 = new ArrayList<>();

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

    @JsonProperty("PRECINFO___ADDR_LO")
    private final List<BigInteger> precinfoAddrLo = new ArrayList<>();

    @JsonProperty("PRECINFO___CDS")
    private final List<BigInteger> precinfoCds = new ArrayList<>();

    @JsonProperty("PRECINFO___EXEC_COST")
    private final List<BigInteger> precinfoExecCost = new ArrayList<>();

    @JsonProperty("PRECINFO___PROVIDES_RETURN_DATA")
    private final List<BigInteger> precinfoProvidesReturnData = new ArrayList<>();

    @JsonProperty("PRECINFO___RDS")
    private final List<BigInteger> precinfoRds = new ArrayList<>();

    @JsonProperty("PRECINFO___SUCCESS")
    private final List<BigInteger> precinfoSuccess = new ArrayList<>();

    @JsonProperty("PRECINFO___TOUCHES_RAM")
    private final List<BigInteger> precinfoTouchesRam = new ArrayList<>();

    @JsonProperty("PROGRAM_COUNTER")
    private final List<BigInteger> programCounter = new ArrayList<>();

    @JsonProperty("PROGRAM_COUNTER_NEW")
    private final List<BigInteger> programCounterNew = new ArrayList<>();

    @JsonProperty("PUSHPOP_FLAG")
    private final List<Boolean> pushpopFlag = new ArrayList<>();

    @JsonProperty("RDCX")
    private final List<Boolean> rdcx = new ArrayList<>();

    @JsonProperty(
        "RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_MXP___INST_xor_STACK_ITEM_VALUE_HI_1_xor_TO_ADDRESS_HI")
    private final List<BigInteger>
        rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi = new ArrayList<>();

    @JsonProperty(
        "RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_MXP___OFFSET_1_HI_xor_STACK_ITEM_VALUE_HI_2_xor_TO_ADDRESS_LO")
    private final List<BigInteger>
        rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo =
            new ArrayList<>();

    @JsonProperty("RLPADDR___FLAG_xor_SCN_4_xor_JUMPX")
    private final List<Boolean> rlpaddrFlagXorScn4XorJumpx = new ArrayList<>();

    @JsonProperty(
        "RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_MXP___OFFSET_1_LO_xor_STACK_ITEM_VALUE_HI_3_xor_VALUE")
    private final List<BigInteger>
        rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue =
            new ArrayList<>();

    @JsonProperty(
        "RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_MXP___OFFSET_2_HI_xor_STACK_ITEM_VALUE_HI_4")
    private final List<BigInteger>
        rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4 = new ArrayList<>();

    @JsonProperty(
        "RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_MXP___OFFSET_2_LO_xor_STACK_ITEM_VALUE_LO_1")
    private final List<BigInteger> rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1 =
        new ArrayList<>();

    @JsonProperty(
        "RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_MXP___SIZE_1_HI_xor_STACK_ITEM_VALUE_LO_2")
    private final List<BigInteger>
        rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2 = new ArrayList<>();

    @JsonProperty(
        "RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_MXP___SIZE_1_LO_xor_STACK_ITEM_VALUE_LO_3")
    private final List<BigInteger> rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3 =
        new ArrayList<>();

    @JsonProperty("SCN_8_xor_MAXCSX")
    private final List<Boolean> scn8XorMaxcsx = new ArrayList<>();

    @JsonProperty("SCN_9_xor_MOD_FLAG")
    private final List<Boolean> scn9XorModFlag = new ArrayList<>();

    @JsonProperty("SELFDESTRUCT_xor_MUL_FLAG")
    private final List<Boolean> selfdestructXorMulFlag = new ArrayList<>();

    @JsonProperty("SHA2sub256_xor_MXPX")
    private final List<Boolean> sha2Sub256XorMxpx = new ArrayList<>();

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

    @JsonProperty("STATICX")
    private final List<Boolean> staticx = new ArrayList<>();

    @JsonProperty("STO_FLAG")
    private final List<Boolean> stoFlag = new ArrayList<>();

    @JsonProperty("STP___GAS_HI")
    private final List<BigInteger> stpGasHi = new ArrayList<>();

    @JsonProperty("STP___GAS_LO")
    private final List<BigInteger> stpGasLo = new ArrayList<>();

    @JsonProperty("STP___GAS_OOPKT")
    private final List<BigInteger> stpGasOopkt = new ArrayList<>();

    @JsonProperty("STP___GAS_STPD")
    private final List<BigInteger> stpGasStpd = new ArrayList<>();

    @JsonProperty("STP___INST")
    private final List<BigInteger> stpInst = new ArrayList<>();

    @JsonProperty("STP___VAL_HI")
    private final List<BigInteger> stpValHi = new ArrayList<>();

    @JsonProperty("STP___VAL_LO")
    private final List<BigInteger> stpValLo = new ArrayList<>();

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

    @JsonProperty("TRM___FLAG_xor_SCN_5_xor_JUMP_FLAG")
    private final List<Boolean> trmFlagXorScn5XorJumpFlag = new ArrayList<>();

    @JsonProperty("TRM___RAW_ADDR_HI_xor_MXP___SIZE_2_HI_xor_STACK_ITEM_VALUE_LO_4")
    private final List<BigInteger> trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4 =
        new ArrayList<>();

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

    @JsonProperty("WARM_NEW_xor_SCN_7_xor_LOG_FLAG")
    private final List<Boolean> warmNewXorScn7XorLogFlag = new ArrayList<>();

    @JsonProperty("WARM_xor_SCN_6_xor_KEC_FLAG")
    private final List<Boolean> warmXorScn6XorKecFlag = new ArrayList<>();

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

      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pAccountAddrLo(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("ADDR_LO already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pAccountBalance(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("BALANCE already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .add(b);

      return this;
    }

    public TraceBuilder pAccountBalanceNew(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("BALANCE_NEW already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pAccountCodeHashHi(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("CODE_HASH_HI already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashHiNew(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("CODE_HASH_HI_NEW already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashLo(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("CODE_HASH_LO already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeHashLoNew(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("CODE_HASH_LO_NEW already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pAccountCodeSize(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("CODE_SIZE already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .add(b);

      return this;
    }

    public TraceBuilder pAccountCodeSizeNew(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("CODE_SIZE_NEW already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .add(b);

      return this;
    }

    public TraceBuilder pAccountDepNum(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("DEP_NUM already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pAccountDepNumNew(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("DEP_NUM_NEW already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pAccountDepStatus(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("DEP_STATUS already set");
      } else {
        filled.set(45);
      }

      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pAccountDepStatusNew(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("DEP_STATUS_NEW already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pAccountDeploymentNumberInfty(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER_INFTY already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .add(b);

      return this;
    }

    public TraceBuilder pAccountDeploymentStatusInfty(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("DEPLOYMENT_STATUS_INFTY already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pAccountExists(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("EXISTS already set");
      } else {
        filled.set(47);
      }

      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pAccountExistsNew(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("EXISTS_NEW already set");
      } else {
        filled.set(48);
      }

      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pAccountHasCode(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("HAS_CODE already set");
      } else {
        filled.set(49);
      }

      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pAccountHasCodeNew(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("HAS_CODE_NEW already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pAccountIsBlake2F(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("IS_BLAKE2f already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcadd(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("IS_ECADD already set");
      } else {
        filled.set(52);
      }

      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcmul(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("IS_ECMUL already set");
      } else {
        filled.set(53);
      }

      isEcmulXorOobFlagXorModexpXorDecodedFlag2.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcpairing(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("IS_ECPAIRING already set");
      } else {
        filled.set(54);
      }

      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.add(b);

      return this;
    }

    public TraceBuilder pAccountIsEcrecover(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("IS_ECRECOVER already set");
      } else {
        filled.set(55);
      }

      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.add(b);

      return this;
    }

    public TraceBuilder pAccountIsIdentity(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("IS_IDENTITY already set");
      } else {
        filled.set(56);
      }

      isIdentityXorStpFlagXorScn10XorDupFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsModexp(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("IS_MODEXP already set");
      } else {
        filled.set(57);
      }

      isModexpXorStpOogxXorScn11XorExtFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsPrecompile(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("IS_PRECOMPILE already set");
      } else {
        filled.set(58);
      }

      isPrecompileXorStpWarmXorScn12XorHaltFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsRipemd160(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("IS_RIPEMD-160 already set");
      } else {
        filled.set(59);
      }

      isRipemDsub160XorScn2XorInvalidFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountIsSha2256(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("IS_SHA2-256 already set");
      } else {
        filled.set(60);
      }

      isSha2Sub256XorScn3XorInvprex.add(b);

      return this;
    }

    public TraceBuilder pAccountNonce(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("NONCE already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pAccountNonceNew(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("NONCE_NEW already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrDepAddrHi(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_HI already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrDepAddrLo(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("RLPADDR___DEP_ADDR_LO already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrFlag(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("RLPADDR___FLAG already set");
      } else {
        filled.set(61);
      }

      rlpaddrFlagXorScn4XorJumpx.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrKecHi(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("RLPADDR___KEC_HI already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrKecLo(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("RLPADDR___KEC_LO already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrRecipe(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("RLPADDR___RECIPE already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrSaltHi(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("RLPADDR___SALT_HI already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pAccountRlpaddrSaltLo(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("RLPADDR___SALT_LO already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pAccountTrmFlag(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("TRM___FLAG already set");
      } else {
        filled.set(62);
      }

      trmFlagXorScn5XorJumpFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountTrmRawAddrHi(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("TRM___RAW_ADDR_HI already set");
      } else {
        filled.set(113);
      }

      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.add(b);

      return this;
    }

    public TraceBuilder pAccountWarm(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("WARM already set");
      } else {
        filled.set(63);
      }

      warmXorScn6XorKecFlag.add(b);

      return this;
    }

    public TraceBuilder pAccountWarmNew(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("WARM_NEW already set");
      } else {
        filled.set(64);
      }

      warmNewXorScn7XorLogFlag.add(b);

      return this;
    }

    public TraceBuilder pContextAccountAddressHi(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("ACCOUNT_ADDRESS_HI already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pContextAccountAddressLo(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("ACCOUNT_ADDRESS_LO already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pContextAccountDeploymentNumber(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("ACCOUNT_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeAddressHi(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("BYTE_CODE_ADDRESS_HI already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pContextByteCodeAddressLo(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("BYTE_CODE_ADDRESS_LO already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeDeploymentNumber(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("BYTE_CODE_DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .add(b);

      return this;
    }

    public TraceBuilder pContextByteCodeDeploymentStatus(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("BYTE_CODE_DEPLOYMENT_STATUS already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pContextCallDataOffset(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("CALL_DATA_OFFSET already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .add(b);

      return this;
    }

    public TraceBuilder pContextCallDataSize(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("CALL_DATA_SIZE already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pContextCallStackDepth(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("CALL_STACK_DEPTH already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pContextCallValue(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("CALL_VALUE already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pContextCallerAddressHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("CALLER_ADDRESS_HI already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pContextCallerAddressLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("CALLER_ADDRESS_LO already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .add(b);

      return this;
    }

    public TraceBuilder pContextCallerContextNumber(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("CALLER_CONTEXT_NUMBER already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .add(b);

      return this;
    }

    public TraceBuilder pContextContextNumber(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("CONTEXT_NUMBER already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pContextIsStatic(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("IS_STATIC already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pContextReturnAtOffset(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("RETURN_AT_OFFSET already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pContextReturnAtSize(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("RETURN_AT_SIZE already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pContextReturnDataOffset(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("RETURN_DATA_OFFSET already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pContextReturnDataSize(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("RETURN_DATA_SIZE already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pContextReturnerContextNumber(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("RETURNER_CONTEXT_NUMBER already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pContextReturnerIsPrecompile(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("RETURNER_IS_PRECOMPILE already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pContextUpdate(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("UPDATE already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousAbortFlag(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("ABORT_FLAG already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousCcrsStamp(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("CCRS_STAMP already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousCcsrFlag(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("CCSR_FLAG already set");
      } else {
        filled.set(45);
      }

      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousFcondFlag(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("FCOND_FLAG already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuCnS(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("MMU___CN_S already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuCnT(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("MMU___CN_T already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuFlag(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("MMU___FLAG already set");
      } else {
        filled.set(47);
      }

      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuInst(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("MMU___INST already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuNum1(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("MMU___NUM_1 already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuNum2(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("MMU___NUM_2 already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuRefs(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("MMU___REFS already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuSize(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("MMU___SIZE already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuSourceOffsetHi(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("MMU___SOURCE_OFFSET_HI already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuSourceOffsetLo(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("MMU___SOURCE_OFFSET_LO already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuStackValHi(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("MMU___STACK_VAL_HI already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuStackValLo(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("MMU___STACK_VAL_LO already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuTargetOffsetHi(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("MMU___TARGET_OFFSET_HI already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMmuTargetOffsetLo(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("MMU___TARGET_OFFSET_LO already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpDeploys(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("MXP___DEPLOYS already set");
      } else {
        filled.set(48);
      }

      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpFlag(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("MXP___FLAG already set");
      } else {
        filled.set(49);
      }

      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpGasMxp(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("MXP___GAS_MXP already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpInst(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("MXP___INST already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpMxpx(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("MXP___MXPX already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpOffset1Hi(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("MXP___OFFSET_1_HI already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpOffset1Lo(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("MXP___OFFSET_1_LO already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpOffset2Hi(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("MXP___OFFSET_2_HI already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpOffset2Lo(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("MXP___OFFSET_2_LO already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpSize1Hi(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("MXP___SIZE_1_HI already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpSize1Lo(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("MXP___SIZE_1_LO already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpSize2Hi(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("MXP___SIZE_2_HI already set");
      } else {
        filled.set(113);
      }

      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpSize2Lo(final BigInteger b) {
      if (filled.get(114)) {
        throw new IllegalStateException("MXP___SIZE_2_LO already set");
      } else {
        filled.set(114);
      }

      mxpSize2LoXorStaticGas.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousMxpWords(final BigInteger b) {
      if (filled.get(115)) {
        throw new IllegalStateException("MXP___WORDS already set");
      } else {
        filled.set(115);
      }

      mxpWords.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobEvent1(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("OOB___EVENT_1 already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobEvent2(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("OOB___EVENT_2 already set");
      } else {
        filled.set(52);
      }

      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobFlag(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("OOB___FLAG already set");
      } else {
        filled.set(53);
      }

      isEcmulXorOobFlagXorModexpXorDecodedFlag2.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobInst(final BigInteger b) {
      if (filled.get(116)) {
        throw new IllegalStateException("OOB___INST already set");
      } else {
        filled.set(116);
      }

      oobInst.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobOutgoingData1(final BigInteger b) {
      if (filled.get(117)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_1 already set");
      } else {
        filled.set(117);
      }

      oobOutgoingData1.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobOutgoingData2(final BigInteger b) {
      if (filled.get(118)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_2 already set");
      } else {
        filled.set(118);
      }

      oobOutgoingData2.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobOutgoingData3(final BigInteger b) {
      if (filled.get(119)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_3 already set");
      } else {
        filled.set(119);
      }

      oobOutgoingData3.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobOutgoingData4(final BigInteger b) {
      if (filled.get(120)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_4 already set");
      } else {
        filled.set(120);
      }

      oobOutgoingData4.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobOutgoingData5(final BigInteger b) {
      if (filled.get(121)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_5 already set");
      } else {
        filled.set(121);
      }

      oobOutgoingData5.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousOobOutgoingData6(final BigInteger b) {
      if (filled.get(122)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_6 already set");
      } else {
        filled.set(122);
      }

      oobOutgoingData6.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoAddrLo(final BigInteger b) {
      if (filled.get(123)) {
        throw new IllegalStateException("PRECINFO___ADDR_LO already set");
      } else {
        filled.set(123);
      }

      precinfoAddrLo.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoCds(final BigInteger b) {
      if (filled.get(124)) {
        throw new IllegalStateException("PRECINFO___CDS already set");
      } else {
        filled.set(124);
      }

      precinfoCds.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoExecCost(final BigInteger b) {
      if (filled.get(125)) {
        throw new IllegalStateException("PRECINFO___EXEC_COST already set");
      } else {
        filled.set(125);
      }

      precinfoExecCost.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoFlag(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("PRECINFO___FLAG already set");
      } else {
        filled.set(54);
      }

      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoProvidesReturnData(final BigInteger b) {
      if (filled.get(126)) {
        throw new IllegalStateException("PRECINFO___PROVIDES_RETURN_DATA already set");
      } else {
        filled.set(126);
      }

      precinfoProvidesReturnData.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoRds(final BigInteger b) {
      if (filled.get(127)) {
        throw new IllegalStateException("PRECINFO___RDS already set");
      } else {
        filled.set(127);
      }

      precinfoRds.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoSuccess(final BigInteger b) {
      if (filled.get(128)) {
        throw new IllegalStateException("PRECINFO___SUCCESS already set");
      } else {
        filled.set(128);
      }

      precinfoSuccess.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousPrecinfoTouchesRam(final BigInteger b) {
      if (filled.get(129)) {
        throw new IllegalStateException("PRECINFO___TOUCHES_RAM already set");
      } else {
        filled.set(129);
      }

      precinfoTouchesRam.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpExists(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("STP___EXISTS already set");
      } else {
        filled.set(55);
      }

      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpFlag(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("STP___FLAG already set");
      } else {
        filled.set(56);
      }

      isIdentityXorStpFlagXorScn10XorDupFlag.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpGasHi(final BigInteger b) {
      if (filled.get(130)) {
        throw new IllegalStateException("STP___GAS_HI already set");
      } else {
        filled.set(130);
      }

      stpGasHi.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpGasLo(final BigInteger b) {
      if (filled.get(131)) {
        throw new IllegalStateException("STP___GAS_LO already set");
      } else {
        filled.set(131);
      }

      stpGasLo.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpGasOopkt(final BigInteger b) {
      if (filled.get(132)) {
        throw new IllegalStateException("STP___GAS_OOPKT already set");
      } else {
        filled.set(132);
      }

      stpGasOopkt.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpGasStpd(final BigInteger b) {
      if (filled.get(133)) {
        throw new IllegalStateException("STP___GAS_STPD already set");
      } else {
        filled.set(133);
      }

      stpGasStpd.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpInst(final BigInteger b) {
      if (filled.get(134)) {
        throw new IllegalStateException("STP___INST already set");
      } else {
        filled.set(134);
      }

      stpInst.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpOogx(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("STP___OOGX already set");
      } else {
        filled.set(57);
      }

      isModexpXorStpOogxXorScn11XorExtFlag.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpValHi(final BigInteger b) {
      if (filled.get(135)) {
        throw new IllegalStateException("STP___VAL_HI already set");
      } else {
        filled.set(135);
      }

      stpValHi.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpValLo(final BigInteger b) {
      if (filled.get(136)) {
        throw new IllegalStateException("STP___VAL_LO already set");
      } else {
        filled.set(136);
      }

      stpValLo.add(b);

      return this;
    }

    public TraceBuilder pMiscellaneousStpWarm(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("STP___WARM already set");
      } else {
        filled.set(58);
      }

      isPrecompileXorStpWarmXorScn12XorHaltFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioBlake2F(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("BLAKE2f already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pScenarioCall(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("CALL already set");
      } else {
        filled.set(45);
      }

      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pScenarioCodedeposit(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("CODEDEPOSIT already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pScenarioCreate(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("CREATE already set");
      } else {
        filled.set(47);
      }

      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pScenarioEcadd(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("ECADD already set");
      } else {
        filled.set(48);
      }

      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pScenarioEcmul(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("ECMUL already set");
      } else {
        filled.set(49);
      }

      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pScenarioEcpairing(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("ECPAIRING already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pScenarioEcrecover(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("ECRECOVER already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pScenarioIdentity(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("IDENTITY already set");
      } else {
        filled.set(52);
      }

      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pScenarioModexp(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("MODEXP already set");
      } else {
        filled.set(53);
      }

      isEcmulXorOobFlagXorModexpXorDecodedFlag2.add(b);

      return this;
    }

    public TraceBuilder pScenarioRipemd160(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("RIPEMD-160 already set");
      } else {
        filled.set(54);
      }

      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn1(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("SCN_1 already set");
      } else {
        filled.set(55);
      }

      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn10(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("SCN_10 already set");
      } else {
        filled.set(56);
      }

      isIdentityXorStpFlagXorScn10XorDupFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn11(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("SCN_11 already set");
      } else {
        filled.set(57);
      }

      isModexpXorStpOogxXorScn11XorExtFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn12(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("SCN_12 already set");
      } else {
        filled.set(58);
      }

      isPrecompileXorStpWarmXorScn12XorHaltFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn2(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("SCN_2 already set");
      } else {
        filled.set(59);
      }

      isRipemDsub160XorScn2XorInvalidFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn3(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("SCN_3 already set");
      } else {
        filled.set(60);
      }

      isSha2Sub256XorScn3XorInvprex.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn4(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("SCN_4 already set");
      } else {
        filled.set(61);
      }

      rlpaddrFlagXorScn4XorJumpx.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn5(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("SCN_5 already set");
      } else {
        filled.set(62);
      }

      trmFlagXorScn5XorJumpFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn6(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("SCN_6 already set");
      } else {
        filled.set(63);
      }

      warmXorScn6XorKecFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn7(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("SCN_7 already set");
      } else {
        filled.set(64);
      }

      warmNewXorScn7XorLogFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn8(final Boolean b) {
      if (filled.get(65)) {
        throw new IllegalStateException("SCN_8 already set");
      } else {
        filled.set(65);
      }

      scn8XorMaxcsx.add(b);

      return this;
    }

    public TraceBuilder pScenarioScn9(final Boolean b) {
      if (filled.get(66)) {
        throw new IllegalStateException("SCN_9 already set");
      } else {
        filled.set(66);
      }

      scn9XorModFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioSelfdestruct(final Boolean b) {
      if (filled.get(67)) {
        throw new IllegalStateException("SELFDESTRUCT already set");
      } else {
        filled.set(67);
      }

      selfdestructXorMulFlag.add(b);

      return this;
    }

    public TraceBuilder pScenarioSha2256(final Boolean b) {
      if (filled.get(68)) {
        throw new IllegalStateException("SHA2-256 already set");
      } else {
        filled.set(68);
      }

      sha2Sub256XorMxpx.add(b);

      return this;
    }

    public TraceBuilder pStackAccFlag(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("ACC_FLAG already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pStackAddFlag(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("ADD_FLAG already set");
      } else {
        filled.set(45);
      }

      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pStackBinFlag(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("BIN_FLAG already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pStackBtcFlag(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("BTC_FLAG already set");
      } else {
        filled.set(47);
      }

      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pStackCallFlag(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("CALL_FLAG already set");
      } else {
        filled.set(48);
      }

      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pStackConFlag(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("CON_FLAG already set");
      } else {
        filled.set(49);
      }

      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pStackCopyFlag(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("COPY_FLAG already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pStackCreateFlag(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("CREATE_FLAG already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag1(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("DECODED_FLAG_1 already set");
      } else {
        filled.set(52);
      }

      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag2(final Boolean b) {
      if (filled.get(53)) {
        throw new IllegalStateException("DECODED_FLAG_2 already set");
      } else {
        filled.set(53);
      }

      isEcmulXorOobFlagXorModexpXorDecodedFlag2.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag3(final Boolean b) {
      if (filled.get(54)) {
        throw new IllegalStateException("DECODED_FLAG_3 already set");
      } else {
        filled.set(54);
      }

      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.add(b);

      return this;
    }

    public TraceBuilder pStackDecodedFlag4(final Boolean b) {
      if (filled.get(55)) {
        throw new IllegalStateException("DECODED_FLAG_4 already set");
      } else {
        filled.set(55);
      }

      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.add(b);

      return this;
    }

    public TraceBuilder pStackDupFlag(final Boolean b) {
      if (filled.get(56)) {
        throw new IllegalStateException("DUP_FLAG already set");
      } else {
        filled.set(56);
      }

      isIdentityXorStpFlagXorScn10XorDupFlag.add(b);

      return this;
    }

    public TraceBuilder pStackExtFlag(final Boolean b) {
      if (filled.get(57)) {
        throw new IllegalStateException("EXT_FLAG already set");
      } else {
        filled.set(57);
      }

      isModexpXorStpOogxXorScn11XorExtFlag.add(b);

      return this;
    }

    public TraceBuilder pStackHaltFlag(final Boolean b) {
      if (filled.get(58)) {
        throw new IllegalStateException("HALT_FLAG already set");
      } else {
        filled.set(58);
      }

      isPrecompileXorStpWarmXorScn12XorHaltFlag.add(b);

      return this;
    }

    public TraceBuilder pStackHeight(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("HEIGHT already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pStackHeightNew(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("HEIGHT_NEW already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pStackHeightOver(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("HEIGHT_OVER already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .add(b);

      return this;
    }

    public TraceBuilder pStackHeightUnder(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("HEIGHT_UNDER already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pStackInstruction(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("INSTRUCTION already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStackInvalidFlag(final Boolean b) {
      if (filled.get(59)) {
        throw new IllegalStateException("INVALID_FLAG already set");
      } else {
        filled.set(59);
      }

      isRipemDsub160XorScn2XorInvalidFlag.add(b);

      return this;
    }

    public TraceBuilder pStackInvprex(final Boolean b) {
      if (filled.get(60)) {
        throw new IllegalStateException("INVPREX already set");
      } else {
        filled.set(60);
      }

      isSha2Sub256XorScn3XorInvprex.add(b);

      return this;
    }

    public TraceBuilder pStackJumpFlag(final Boolean b) {
      if (filled.get(62)) {
        throw new IllegalStateException("JUMP_FLAG already set");
      } else {
        filled.set(62);
      }

      trmFlagXorScn5XorJumpFlag.add(b);

      return this;
    }

    public TraceBuilder pStackJumpx(final Boolean b) {
      if (filled.get(61)) {
        throw new IllegalStateException("JUMPX already set");
      } else {
        filled.set(61);
      }

      rlpaddrFlagXorScn4XorJumpx.add(b);

      return this;
    }

    public TraceBuilder pStackKecFlag(final Boolean b) {
      if (filled.get(63)) {
        throw new IllegalStateException("KEC_FLAG already set");
      } else {
        filled.set(63);
      }

      warmXorScn6XorKecFlag.add(b);

      return this;
    }

    public TraceBuilder pStackLogFlag(final Boolean b) {
      if (filled.get(64)) {
        throw new IllegalStateException("LOG_FLAG already set");
      } else {
        filled.set(64);
      }

      warmNewXorScn7XorLogFlag.add(b);

      return this;
    }

    public TraceBuilder pStackMaxcsx(final Boolean b) {
      if (filled.get(65)) {
        throw new IllegalStateException("MAXCSX already set");
      } else {
        filled.set(65);
      }

      scn8XorMaxcsx.add(b);

      return this;
    }

    public TraceBuilder pStackModFlag(final Boolean b) {
      if (filled.get(66)) {
        throw new IllegalStateException("MOD_FLAG already set");
      } else {
        filled.set(66);
      }

      scn9XorModFlag.add(b);

      return this;
    }

    public TraceBuilder pStackMulFlag(final Boolean b) {
      if (filled.get(67)) {
        throw new IllegalStateException("MUL_FLAG already set");
      } else {
        filled.set(67);
      }

      selfdestructXorMulFlag.add(b);

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

      sha2Sub256XorMxpx.add(b);

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

      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .add(b);

      return this;
    }

    public TraceBuilder pStackPushValueLo(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("PUSH_VALUE_LO already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(b);

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

      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight2(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_2 already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight3(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_3 already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .add(b);

      return this;
    }

    public TraceBuilder pStackStackItemHeight4(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("STACK_ITEM_HEIGHT_4 already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .add(b);

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

      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp2(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_2 already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp3(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_3 already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemStamp4(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("STACK_ITEM_STAMP_4 already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi1(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_1 already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi2(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_2 already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi3(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_3 already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueHi4(final BigInteger b) {
      if (filled.get(109)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_HI_4 already set");
      } else {
        filled.set(109);
      }

      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo1(final BigInteger b) {
      if (filled.get(110)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_1 already set");
      } else {
        filled.set(110);
      }

      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo2(final BigInteger b) {
      if (filled.get(111)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_2 already set");
      } else {
        filled.set(111);
      }

      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo3(final BigInteger b) {
      if (filled.get(112)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_3 already set");
      } else {
        filled.set(112);
      }

      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.add(b);

      return this;
    }

    public TraceBuilder pStackStackItemValueLo4(final BigInteger b) {
      if (filled.get(113)) {
        throw new IllegalStateException("STACK_ITEM_VALUE_LO_4 already set");
      } else {
        filled.set(113);
      }

      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.add(b);

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

      mxpSize2LoXorStaticGas.add(b);

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

      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pStorageAddressLo(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("ADDRESS_LO already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pStorageDeploymentNumber(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("DEPLOYMENT_NUMBER already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .add(b);

      return this;
    }

    public TraceBuilder pStorageStorageKeyHi(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("STORAGE_KEY_HI already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pStorageStorageKeyLo(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("STORAGE_KEY_LO already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrChanges(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("VAL_CURR_CHANGES already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrHi(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("VAL_CURR_HI already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrIsOrig(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("VAL_CURR_IS_ORIG already set");
      } else {
        filled.set(45);
      }

      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrIsZero(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("VAL_CURR_IS_ZERO already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pStorageValCurrLo(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("VAL_CURR_LO already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextHi(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("VAL_NEXT_HI already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextIsCurr(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("VAL_NEXT_IS_CURR already set");
      } else {
        filled.set(47);
      }

      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextIsOrig(final Boolean b) {
      if (filled.get(48)) {
        throw new IllegalStateException("VAL_NEXT_IS_ORIG already set");
      } else {
        filled.set(48);
      }

      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextIsZero(final Boolean b) {
      if (filled.get(49)) {
        throw new IllegalStateException("VAL_NEXT_IS_ZERO already set");
      } else {
        filled.set(49);
      }

      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.add(b);

      return this;
    }

    public TraceBuilder pStorageValNextLo(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("VAL_NEXT_LO already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .add(b);

      return this;
    }

    public TraceBuilder pStorageValOrigHi(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("VAL_ORIG_HI already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .add(b);

      return this;
    }

    public TraceBuilder pStorageValOrigIsZero(final Boolean b) {
      if (filled.get(50)) {
        throw new IllegalStateException("VAL_ORIG_IS_ZERO already set");
      } else {
        filled.set(50);
      }

      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.add(b);

      return this;
    }

    public TraceBuilder pStorageValOrigLo(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("VAL_ORIG_LO already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .add(b);

      return this;
    }

    public TraceBuilder pStorageWarm(final Boolean b) {
      if (filled.get(51)) {
        throw new IllegalStateException("WARM already set");
      } else {
        filled.set(51);
      }

      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.add(b);

      return this;
    }

    public TraceBuilder pStorageWarmNew(final Boolean b) {
      if (filled.get(52)) {
        throw new IllegalStateException("WARM_NEW already set");
      } else {
        filled.set(52);
      }

      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.add(b);

      return this;
    }

    public TraceBuilder pTransactionBasefee(final BigInteger b) {
      if (filled.get(91)) {
        throw new IllegalStateException("BASEFEE already set");
      } else {
        filled.set(91);
      }

      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(b);

      return this;
    }

    public TraceBuilder pTransactionCallDataSize(final BigInteger b) {
      if (filled.get(92)) {
        throw new IllegalStateException("CALL_DATA_SIZE already set");
      } else {
        filled.set(92);
      }

      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(b);

      return this;
    }

    public TraceBuilder pTransactionCoinbaseAddressHi(final BigInteger b) {
      if (filled.get(93)) {
        throw new IllegalStateException("COINBASE_ADDRESS_HI already set");
      } else {
        filled.set(93);
      }

      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .add(b);

      return this;
    }

    public TraceBuilder pTransactionCoinbaseAddressLo(final BigInteger b) {
      if (filled.get(94)) {
        throw new IllegalStateException("COINBASE_ADDRESS_LO already set");
      } else {
        filled.set(94);
      }

      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pTransactionFromAddressHi(final BigInteger b) {
      if (filled.get(95)) {
        throw new IllegalStateException("FROM_ADDRESS_HI already set");
      } else {
        filled.set(95);
      }

      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(b);

      return this;
    }

    public TraceBuilder pTransactionFromAddressLo(final BigInteger b) {
      if (filled.get(96)) {
        throw new IllegalStateException("FROM_ADDRESS_LO already set");
      } else {
        filled.set(96);
      }

      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .add(b);

      return this;
    }

    public TraceBuilder pTransactionGasLimit(final BigInteger b) {
      if (filled.get(97)) {
        throw new IllegalStateException("GAS_LIMIT already set");
      } else {
        filled.set(97);
      }

      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(b);

      return this;
    }

    public TraceBuilder pTransactionGasPrice(final BigInteger b) {
      if (filled.get(98)) {
        throw new IllegalStateException("GAS_PRICE already set");
      } else {
        filled.set(98);
      }

      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(b);

      return this;
    }

    public TraceBuilder pTransactionGasRefundAmount(final BigInteger b) {
      if (filled.get(99)) {
        throw new IllegalStateException("GAS_REFUND_AMOUNT already set");
      } else {
        filled.set(99);
      }

      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .add(b);

      return this;
    }

    public TraceBuilder pTransactionGasRefundCounterFinal(final BigInteger b) {
      if (filled.get(100)) {
        throw new IllegalStateException("GAS_REFUND_COUNTER_FINAL already set");
      } else {
        filled.set(100);
      }

      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .add(b);

      return this;
    }

    public TraceBuilder pTransactionInitCodeSize(final BigInteger b) {
      if (filled.get(102)) {
        throw new IllegalStateException("INIT_CODE_SIZE already set");
      } else {
        filled.set(102);
      }

      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.add(b);

      return this;
    }

    public TraceBuilder pTransactionInitGas(final BigInteger b) {
      if (filled.get(103)) {
        throw new IllegalStateException("INIT_GAS already set");
      } else {
        filled.set(103);
      }

      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.add(b);

      return this;
    }

    public TraceBuilder pTransactionInitialBalance(final BigInteger b) {
      if (filled.get(101)) {
        throw new IllegalStateException("INITIAL_BALANCE already set");
      } else {
        filled.set(101);
      }

      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .add(b);

      return this;
    }

    public TraceBuilder pTransactionIsDeployment(final Boolean b) {
      if (filled.get(44)) {
        throw new IllegalStateException("IS_DEPLOYMENT already set");
      } else {
        filled.set(44);
      }

      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .add(b);

      return this;
    }

    public TraceBuilder pTransactionIsEip1559(final Boolean b) {
      if (filled.get(45)) {
        throw new IllegalStateException("IS_EIP1559 already set");
      } else {
        filled.set(45);
      }

      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(b);

      return this;
    }

    public TraceBuilder pTransactionLeftoverGas(final BigInteger b) {
      if (filled.get(104)) {
        throw new IllegalStateException("LEFTOVER_GAS already set");
      } else {
        filled.set(104);
      }

      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.add(b);

      return this;
    }

    public TraceBuilder pTransactionNonce(final BigInteger b) {
      if (filled.get(105)) {
        throw new IllegalStateException("NONCE already set");
      } else {
        filled.set(105);
      }

      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.add(b);

      return this;
    }

    public TraceBuilder pTransactionStatusCode(final Boolean b) {
      if (filled.get(46)) {
        throw new IllegalStateException("STATUS_CODE already set");
      } else {
        filled.set(46);
      }

      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(b);

      return this;
    }

    public TraceBuilder pTransactionToAddressHi(final BigInteger b) {
      if (filled.get(106)) {
        throw new IllegalStateException("TO_ADDRESS_HI already set");
      } else {
        filled.set(106);
      }

      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.add(b);

      return this;
    }

    public TraceBuilder pTransactionToAddressLo(final BigInteger b) {
      if (filled.get(107)) {
        throw new IllegalStateException("TO_ADDRESS_LO already set");
      } else {
        filled.set(107);
      }

      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.add(
          b);

      return this;
    }

    public TraceBuilder pTransactionTxnRequiresEvmExecution(final Boolean b) {
      if (filled.get(47)) {
        throw new IllegalStateException("TXN_REQUIRES_EVM_EXECUTION already set");
      } else {
        filled.set(47);
      }

      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(b);

      return this;
    }

    public TraceBuilder pTransactionValue(final BigInteger b) {
      if (filled.get(108)) {
        throw new IllegalStateException("VALUE already set");
      } else {
        filled.set(108);
      }

      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.add(b);

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
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountAddrLoAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountBalanceAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(i, b);

      return this;
    }

    public TraceBuilder setPAccountBalanceNewAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiNewAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoNewAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(i, b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeNewAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumNewAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusAt(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusNewAt(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentNumberInftyAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(i, b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentStatusInftyAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPAccountExistsAt(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountExistsNewAt(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeAt(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeNewAt(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsBlake2FAt(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcaddAt(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcmulAt(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcpairingAt(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcrecoverAt(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsIdentityAt(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsModexpAt(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsPrecompileAt(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsRipemd160At(final Boolean b, int i) {
      isRipemDsub160XorScn2XorInvalidFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountIsSha2256At(final Boolean b, int i) {
      isSha2Sub256XorScn3XorInvprex.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceAt(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceNewAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrHiAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrLoAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrFlagAt(final Boolean b, int i) {
      rlpaddrFlagXorScn4XorJumpx.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecHiAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecLoAt(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrRecipeAt(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltHiAt(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltLoAt(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmFlagAt(final Boolean b, int i) {
      trmFlagXorScn5XorJumpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmRawAddrHiAt(final BigInteger b, int i) {
      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmAt(final Boolean b, int i) {
      warmXorScn6XorKecFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmNewAt(final Boolean b, int i) {
      warmNewXorScn7XorLogFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressHiAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressLoAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPContextAccountDeploymentNumberAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressHiAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressLoAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentNumberAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(i, b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentStatusAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          i, b);

      return this;
    }

    public TraceBuilder setPContextCallDataOffsetAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallDataSizeAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallStackDepthAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallValueAt(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressHiAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressLoAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(i, b);

      return this;
    }

    public TraceBuilder setPContextCallerContextNumberAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(i, b);

      return this;
    }

    public TraceBuilder setPContextContextNumberAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPContextIsStaticAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtOffsetAt(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtSizeAt(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataOffsetAt(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataSizeAt(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerContextNumberAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerIsPrecompileAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPContextUpdateAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousAbortFlagAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousCcrsStampAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousCcsrFlagAt(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousFcondFlagAt(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuCnSAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuCnTAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuFlagAt(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuInstAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuNum1At(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuNum2At(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuRefsAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuSizeAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuSourceOffsetHiAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuSourceOffsetLoAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuStackValHiAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuStackValLoAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuTargetOffsetHiAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuTargetOffsetLoAt(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpDeploysAt(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpFlagAt(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpGasMxpAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpInstAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpMxpxAt(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset1HiAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset1LoAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset2HiAt(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset2LoAt(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize1HiAt(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize1LoAt(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize2HiAt(final BigInteger b, int i) {
      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize2LoAt(final BigInteger b, int i) {
      mxpSize2LoXorStaticGas.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpWordsAt(final BigInteger b, int i) {
      mxpWords.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobEvent1At(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobEvent2At(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobFlagAt(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobInstAt(final BigInteger b, int i) {
      oobInst.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData1At(final BigInteger b, int i) {
      oobOutgoingData1.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData2At(final BigInteger b, int i) {
      oobOutgoingData2.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData3At(final BigInteger b, int i) {
      oobOutgoingData3.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData4At(final BigInteger b, int i) {
      oobOutgoingData4.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData5At(final BigInteger b, int i) {
      oobOutgoingData5.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData6At(final BigInteger b, int i) {
      oobOutgoingData6.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoAddrLoAt(final BigInteger b, int i) {
      precinfoAddrLo.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoCdsAt(final BigInteger b, int i) {
      precinfoCds.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoExecCostAt(final BigInteger b, int i) {
      precinfoExecCost.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoFlagAt(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoProvidesReturnDataAt(final BigInteger b, int i) {
      precinfoProvidesReturnData.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoRdsAt(final BigInteger b, int i) {
      precinfoRds.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoSuccessAt(final BigInteger b, int i) {
      precinfoSuccess.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoTouchesRamAt(final BigInteger b, int i) {
      precinfoTouchesRam.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpExistsAt(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpFlagAt(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasHiAt(final BigInteger b, int i) {
      stpGasHi.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasLoAt(final BigInteger b, int i) {
      stpGasLo.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasOopktAt(final BigInteger b, int i) {
      stpGasOopkt.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasStpdAt(final BigInteger b, int i) {
      stpGasStpd.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpInstAt(final BigInteger b, int i) {
      stpInst.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpOogxAt(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpValHiAt(final BigInteger b, int i) {
      stpValHi.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpValLoAt(final BigInteger b, int i) {
      stpValLo.set(i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpWarmAt(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioBlake2FAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioCallAt(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioCodedepositAt(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioCreateAt(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcaddAt(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcmulAt(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcpairingAt(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcrecoverAt(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioIdentityAt(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioModexpAt(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioRipemd160At(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn1At(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn10At(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn11At(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn12At(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn2At(final Boolean b, int i) {
      isRipemDsub160XorScn2XorInvalidFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn3At(final Boolean b, int i) {
      isSha2Sub256XorScn3XorInvprex.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn4At(final Boolean b, int i) {
      rlpaddrFlagXorScn4XorJumpx.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn5At(final Boolean b, int i) {
      trmFlagXorScn5XorJumpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn6At(final Boolean b, int i) {
      warmXorScn6XorKecFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn7At(final Boolean b, int i) {
      warmNewXorScn7XorLogFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn8At(final Boolean b, int i) {
      scn8XorMaxcsx.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn9At(final Boolean b, int i) {
      scn9XorModFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioSelfdestructAt(final Boolean b, int i) {
      selfdestructXorMulFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPScenarioSha2256At(final Boolean b, int i) {
      sha2Sub256XorMxpx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackAccFlagAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStackAddFlagAt(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPStackBinFlagAt(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPStackBtcFlagAt(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPStackCallFlagAt(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPStackConFlagAt(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStackCopyFlagAt(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStackCreateFlagAt(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag1At(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag2At(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag3At(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag4At(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(i, b);

      return this;
    }

    public TraceBuilder setPStackDupFlagAt(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackExtFlagAt(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHaltFlagAt(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightNewAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightOverAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStackHeightUnderAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPStackInstructionAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          i, b);

      return this;
    }

    public TraceBuilder setPStackInvalidFlagAt(final Boolean b, int i) {
      isRipemDsub160XorScn2XorInvalidFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackInvprexAt(final Boolean b, int i) {
      isSha2Sub256XorScn3XorInvprex.set(i, b);

      return this;
    }

    public TraceBuilder setPStackJumpFlagAt(final Boolean b, int i) {
      trmFlagXorScn5XorJumpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackJumpxAt(final Boolean b, int i) {
      rlpaddrFlagXorScn4XorJumpx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackKecFlagAt(final Boolean b, int i) {
      warmXorScn6XorKecFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackLogFlagAt(final Boolean b, int i) {
      warmNewXorScn7XorLogFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMaxcsxAt(final Boolean b, int i) {
      scn8XorMaxcsx.set(i, b);

      return this;
    }

    public TraceBuilder setPStackModFlagAt(final Boolean b, int i) {
      scn9XorModFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMulFlagAt(final Boolean b, int i) {
      selfdestructXorMulFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMxpFlagAt(final Boolean b, int i) {
      mxpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setPStackMxpxAt(final Boolean b, int i) {
      sha2Sub256XorMxpx.set(i, b);

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
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStackPushValueLoAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          i, b);

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
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight2At(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight3At(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight4At(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(i, b);

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
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp2At(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp3At(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp4At(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi1At(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi2At(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi3At(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi4At(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo1At(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo2At(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo3At(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo4At(final BigInteger b, int i) {
      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.set(i, b);

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
      mxpSize2LoXorStaticGas.set(i, b);

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
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageAddressLoAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageDeploymentNumberAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyHiAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyLoAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrChangesAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrHiAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsOrigAt(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsZeroAt(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrLoAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextHiAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsCurrAt(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsOrigAt(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsZeroAt(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextLoAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigHiAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigIsZeroAt(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigLoAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmAt(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmNewAt(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionBasefeeAt(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionCallDataSizeAt(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressHiAt(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressLoAt(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressHiAt(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          i, b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressLoAt(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasLimitAt(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasPriceAt(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundAmountAt(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundCounterFinalAt(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitCodeSizeAt(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitGasAt(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitialBalanceAt(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionIsDeploymentAt(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionIsEip1559At(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionLeftoverGasAt(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionNonceAt(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionStatusCodeAt(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressHiAt(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressLoAt(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          i, b);

      return this;
    }

    public TraceBuilder setPTransactionTxnRequiresEvmExecutionAt(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(i, b);

      return this;
    }

    public TraceBuilder setPTransactionValueAt(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(i, b);

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
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountAddrLoRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountBalanceRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(
              balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPAccountBalanceNewRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashHiNewRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(
              codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountCodeHashLoNewRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(
              codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPAccountCodeSizeNewRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(
              codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPAccountDepNumRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(
          depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDepNumNewRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(
          depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusRelative(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountDepStatusNewRelative(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentNumberInftyRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(
              deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPAccountDeploymentStatusInftyRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPAccountExistsRelative(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountExistsNewRelative(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(
          existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeRelative(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(
          hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountHasCodeNewRelative(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(
          hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsBlake2FRelative(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(
          isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcaddRelative(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(
          isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcmulRelative(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(
          isEcmulXorOobFlagXorModexpXorDecodedFlag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcpairingRelative(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(
          isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsEcrecoverRelative(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(
          isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsIdentityRelative(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(
          isIdentityXorStpFlagXorScn10XorDupFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsModexpRelative(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(
          isModexpXorStpOogxXorScn11XorExtFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsPrecompileRelative(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(
          isPrecompileXorStpWarmXorScn12XorHaltFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsRipemd160Relative(final Boolean b, int i) {
      isRipemDsub160XorScn2XorInvalidFlag.set(
          isRipemDsub160XorScn2XorInvalidFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountIsSha2256Relative(final Boolean b, int i) {
      isSha2Sub256XorScn3XorInvprex.set(isSha2Sub256XorScn3XorInvprex.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceRelative(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(
          nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountNonceNewRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(
          nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrHiRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(
          rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrDepAddrLoRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrFlagRelative(final Boolean b, int i) {
      rlpaddrFlagXorScn4XorJumpx.set(rlpaddrFlagXorScn4XorJumpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecHiRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(
          rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrKecLoRelative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(
          rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrRecipeRelative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(
          rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltHiRelative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(
          rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountRlpaddrSaltLoRelative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(
          rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmFlagRelative(final Boolean b, int i) {
      trmFlagXorScn5XorJumpFlag.set(trmFlagXorScn5XorJumpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountTrmRawAddrHiRelative(final BigInteger b, int i) {
      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.set(
          trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmRelative(final Boolean b, int i) {
      warmXorScn6XorKecFlag.set(warmXorScn6XorKecFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPAccountWarmNewRelative(final Boolean b, int i) {
      warmNewXorScn7XorLogFlag.set(warmNewXorScn7XorLogFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressHiRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextAccountAddressLoRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPContextAccountDeploymentNumberRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(
              balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressHiRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPContextByteCodeAddressLoRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentNumberRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(
              codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPContextByteCodeDeploymentStatusRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPContextCallDataOffsetRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(
              deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPContextCallDataSizeRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(
          depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallStackDepthRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(
          depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPContextCallValueRelative(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(
          nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressHiRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPContextCallerAddressLoRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(
              codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPContextCallerContextNumberRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(
              codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPContextContextNumberRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(
          nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextIsStaticRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(
          rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPContextReturnAtOffsetRelative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(
          rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnAtSizeRelative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(
          rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataOffsetRelative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(
          rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnDataSizeRelative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(
          rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPContextReturnerContextNumberRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPContextReturnerIsPrecompileRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(
          rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPContextUpdateRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousAbortFlagRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousCcrsStampRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousCcsrFlagRelative(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousFcondFlagRelative(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuCnSRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuCnTRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(
              balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuFlagRelative(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuInstRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuNum1Relative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuNum2Relative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(
              codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuRefsRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuSizeRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuSourceOffsetHiRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(
              codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuSourceOffsetLoRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(
              codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuStackValHiRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(
              deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuStackValLoRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(
          depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuTargetOffsetHiRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(
          depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMmuTargetOffsetLoRelative(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(
          nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpDeploysRelative(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(
          existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpFlagRelative(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(
          hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpGasMxpRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(
          nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpInstRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(
          rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpMxpxRelative(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(
          hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset1HiRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset1LoRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(
          rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset2HiRelative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(
          rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpOffset2LoRelative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(
          rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize1HiRelative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(
          rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize1LoRelative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(
          rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize2HiRelative(final BigInteger b, int i) {
      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.set(
          trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpSize2LoRelative(final BigInteger b, int i) {
      mxpSize2LoXorStaticGas.set(mxpSize2LoXorStaticGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousMxpWordsRelative(final BigInteger b, int i) {
      mxpWords.set(mxpWords.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobEvent1Relative(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(
          isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobEvent2Relative(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(
          isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobFlagRelative(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(
          isEcmulXorOobFlagXorModexpXorDecodedFlag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobInstRelative(final BigInteger b, int i) {
      oobInst.set(oobInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData1Relative(final BigInteger b, int i) {
      oobOutgoingData1.set(oobOutgoingData1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData2Relative(final BigInteger b, int i) {
      oobOutgoingData2.set(oobOutgoingData2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData3Relative(final BigInteger b, int i) {
      oobOutgoingData3.set(oobOutgoingData3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData4Relative(final BigInteger b, int i) {
      oobOutgoingData4.set(oobOutgoingData4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData5Relative(final BigInteger b, int i) {
      oobOutgoingData5.set(oobOutgoingData5.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousOobOutgoingData6Relative(final BigInteger b, int i) {
      oobOutgoingData6.set(oobOutgoingData6.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoAddrLoRelative(final BigInteger b, int i) {
      precinfoAddrLo.set(precinfoAddrLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoCdsRelative(final BigInteger b, int i) {
      precinfoCds.set(precinfoCds.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoExecCostRelative(final BigInteger b, int i) {
      precinfoExecCost.set(precinfoExecCost.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoFlagRelative(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(
          isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoProvidesReturnDataRelative(
        final BigInteger b, int i) {
      precinfoProvidesReturnData.set(precinfoProvidesReturnData.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoRdsRelative(final BigInteger b, int i) {
      precinfoRds.set(precinfoRds.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoSuccessRelative(final BigInteger b, int i) {
      precinfoSuccess.set(precinfoSuccess.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousPrecinfoTouchesRamRelative(final BigInteger b, int i) {
      precinfoTouchesRam.set(precinfoTouchesRam.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpExistsRelative(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(
          isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpFlagRelative(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(
          isIdentityXorStpFlagXorScn10XorDupFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasHiRelative(final BigInteger b, int i) {
      stpGasHi.set(stpGasHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasLoRelative(final BigInteger b, int i) {
      stpGasLo.set(stpGasLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasOopktRelative(final BigInteger b, int i) {
      stpGasOopkt.set(stpGasOopkt.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpGasStpdRelative(final BigInteger b, int i) {
      stpGasStpd.set(stpGasStpd.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpInstRelative(final BigInteger b, int i) {
      stpInst.set(stpInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpOogxRelative(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(
          isModexpXorStpOogxXorScn11XorExtFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpValHiRelative(final BigInteger b, int i) {
      stpValHi.set(stpValHi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpValLoRelative(final BigInteger b, int i) {
      stpValLo.set(stpValLo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPMiscellaneousStpWarmRelative(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(
          isPrecompileXorStpWarmXorScn12XorHaltFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioBlake2FRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPScenarioCallRelative(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioCodedepositRelative(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPScenarioCreateRelative(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPScenarioEcaddRelative(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(
          existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcmulRelative(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(
          hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcpairingRelative(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(
          hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioEcrecoverRelative(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(
          isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioIdentityRelative(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(
          isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioModexpRelative(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(
          isEcmulXorOobFlagXorModexpXorDecodedFlag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioRipemd160Relative(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(
          isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn1Relative(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(
          isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn10Relative(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(
          isIdentityXorStpFlagXorScn10XorDupFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn11Relative(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(
          isModexpXorStpOogxXorScn11XorExtFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn12Relative(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(
          isPrecompileXorStpWarmXorScn12XorHaltFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn2Relative(final Boolean b, int i) {
      isRipemDsub160XorScn2XorInvalidFlag.set(
          isRipemDsub160XorScn2XorInvalidFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn3Relative(final Boolean b, int i) {
      isSha2Sub256XorScn3XorInvprex.set(isSha2Sub256XorScn3XorInvprex.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn4Relative(final Boolean b, int i) {
      rlpaddrFlagXorScn4XorJumpx.set(rlpaddrFlagXorScn4XorJumpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn5Relative(final Boolean b, int i) {
      trmFlagXorScn5XorJumpFlag.set(trmFlagXorScn5XorJumpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn6Relative(final Boolean b, int i) {
      warmXorScn6XorKecFlag.set(warmXorScn6XorKecFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn7Relative(final Boolean b, int i) {
      warmNewXorScn7XorLogFlag.set(warmNewXorScn7XorLogFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn8Relative(final Boolean b, int i) {
      scn8XorMaxcsx.set(scn8XorMaxcsx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioScn9Relative(final Boolean b, int i) {
      scn9XorModFlag.set(scn9XorModFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioSelfdestructRelative(final Boolean b, int i) {
      selfdestructXorMulFlag.set(selfdestructXorMulFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPScenarioSha2256Relative(final Boolean b, int i) {
      sha2Sub256XorMxpx.set(sha2Sub256XorMxpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackAccFlagRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStackAddFlagRelative(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackBinFlagRelative(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackBtcFlagRelative(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackCallFlagRelative(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(
          existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackConFlagRelative(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(
          hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackCopyFlagRelative(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(
          hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackCreateFlagRelative(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(
          isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag1Relative(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(
          isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag2Relative(final Boolean b, int i) {
      isEcmulXorOobFlagXorModexpXorDecodedFlag2.set(
          isEcmulXorOobFlagXorModexpXorDecodedFlag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag3Relative(final Boolean b, int i) {
      isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.set(
          isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDecodedFlag4Relative(final Boolean b, int i) {
      isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.set(
          isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackDupFlagRelative(final Boolean b, int i) {
      isIdentityXorStpFlagXorScn10XorDupFlag.set(
          isIdentityXorStpFlagXorScn10XorDupFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackExtFlagRelative(final Boolean b, int i) {
      isModexpXorStpOogxXorScn11XorExtFlag.set(
          isModexpXorStpOogxXorScn11XorExtFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHaltFlagRelative(final Boolean b, int i) {
      isPrecompileXorStpWarmXorScn12XorHaltFlag.set(
          isPrecompileXorStpWarmXorScn12XorHaltFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHeightRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackHeightNewRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPStackHeightOverRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(
              balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStackHeightUnderRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackInstructionRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackInvalidFlagRelative(final Boolean b, int i) {
      isRipemDsub160XorScn2XorInvalidFlag.set(
          isRipemDsub160XorScn2XorInvalidFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackInvprexRelative(final Boolean b, int i) {
      isSha2Sub256XorScn3XorInvprex.set(isSha2Sub256XorScn3XorInvprex.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackJumpFlagRelative(final Boolean b, int i) {
      trmFlagXorScn5XorJumpFlag.set(trmFlagXorScn5XorJumpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackJumpxRelative(final Boolean b, int i) {
      rlpaddrFlagXorScn4XorJumpx.set(rlpaddrFlagXorScn4XorJumpx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackKecFlagRelative(final Boolean b, int i) {
      warmXorScn6XorKecFlag.set(warmXorScn6XorKecFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackLogFlagRelative(final Boolean b, int i) {
      warmNewXorScn7XorLogFlag.set(warmNewXorScn7XorLogFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMaxcsxRelative(final Boolean b, int i) {
      scn8XorMaxcsx.set(scn8XorMaxcsx.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackModFlagRelative(final Boolean b, int i) {
      scn9XorModFlag.set(scn9XorModFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMulFlagRelative(final Boolean b, int i) {
      selfdestructXorMulFlag.set(selfdestructXorMulFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMxpFlagRelative(final Boolean b, int i) {
      mxpFlag.set(mxpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackMxpxRelative(final Boolean b, int i) {
      sha2Sub256XorMxpx.set(sha2Sub256XorMxpx.size() - 1 - i, b);

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
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(
              codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStackPushValueLoRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit
                  .size()
              - 1
              - i,
          b);

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
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight2Relative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(
              codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight3Relative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(
              codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStackStackItemHeight4Relative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(
              deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
                      .size()
                  - 1
                  - i,
              b);

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
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(
          depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp2Relative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(
          depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp3Relative(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(
          nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemStamp4Relative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(
          nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi1Relative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(
          rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi2Relative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi3Relative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(
          rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueHi4Relative(final BigInteger b, int i) {
      rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.set(
          rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo1Relative(final BigInteger b, int i) {
      rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.set(
          rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo2Relative(final BigInteger b, int i) {
      rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.set(
          rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo3Relative(final BigInteger b, int i) {
      rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.set(
          rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStackStackItemValueLo4Relative(final BigInteger b, int i) {
      trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.set(
          trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.size() - 1 - i, b);

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
      mxpSize2LoXorStaticGas.set(mxpSize2LoXorStaticGas.size() - 1 - i, b);

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
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageAddressLoRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageDeploymentNumberRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(
              balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyHiRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageStorageKeyLoRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageValCurrChangesRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStorageValCurrHiRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(
              codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsOrigRelative(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValCurrIsZeroRelative(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageValCurrLoRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageValNextHiRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsCurrRelative(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsOrigRelative(final Boolean b, int i) {
      existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.set(
          existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextIsZeroRelative(final Boolean b, int i) {
      hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.set(
          hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValNextLoRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(
              codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStorageValOrigHiRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(
              codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStorageValOrigIsZeroRelative(final Boolean b, int i) {
      hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.set(
          hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageValOrigLoRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(
              deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPStorageWarmRelative(final Boolean b, int i) {
      isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.set(
          isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPStorageWarmNewRelative(final Boolean b, int i) {
      isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.set(
          isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionBasefeeRelative(final BigInteger b, int i) {
      addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.set(
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionCallDataSizeRelative(final BigInteger b, int i) {
      addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.set(
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressHiRelative(final BigInteger b, int i) {
      balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
          .set(
              balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPTransactionCoinbaseAddressLoRelative(final BigInteger b, int i) {
      balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo.set(
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressHiRelative(final BigInteger b, int i) {
      codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.set(
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionFromAddressLoRelative(final BigInteger b, int i) {
      codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
          .set(
              codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPTransactionGasLimitRelative(final BigInteger b, int i) {
      codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.set(
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionGasPriceRelative(final BigInteger b, int i) {
      codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.set(
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundAmountRelative(final BigInteger b, int i) {
      codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
          .set(
              codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPTransactionGasRefundCounterFinalRelative(final BigInteger b, int i) {
      codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
          .set(
              codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPTransactionInitCodeSizeRelative(final BigInteger b, int i) {
      depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.set(
          depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionInitGasRelative(final BigInteger b, int i) {
      depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.set(
          depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionInitialBalanceRelative(final BigInteger b, int i) {
      deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
          .set(
              deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPTransactionIsDeploymentRelative(final Boolean b, int i) {
      deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
          .set(
              deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
                      .size()
                  - 1
                  - i,
              b);

      return this;
    }

    public TraceBuilder setPTransactionIsEip1559Relative(final Boolean b, int i) {
      depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.set(
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionLeftoverGasRelative(final BigInteger b, int i) {
      nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.set(
          nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionNonceRelative(final BigInteger b, int i) {
      nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.set(
          nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPTransactionStatusCodeRelative(final Boolean b, int i) {
      depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.set(
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressHiRelative(final BigInteger b, int i) {
      rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.set(
          rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.size() - 1 - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionToAddressLoRelative(final BigInteger b, int i) {
      rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo.set(
          rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo
                  .size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionTxnRequiresEvmExecutionRelative(final Boolean b, int i) {
      existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.set(
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.size()
              - 1
              - i,
          b);

      return this;
    }

    public TraceBuilder setPTransactionValueRelative(final BigInteger b, int i) {
      rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.set(
          rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.size()
              - 1
              - i,
          b);

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
        throw new IllegalStateException(
            "ADDR_HI_xor_ACCOUNT_ADDRESS_HI_xor_CCRS_STAMP_xor_HEIGHT_xor_ADDRESS_HI_xor_BASEFEE has not been filled");
      }

      if (!filled.get(92)) {
        throw new IllegalStateException(
            "ADDR_LO_xor_ACCOUNT_ADDRESS_LO_xor_MMU___CN_S_xor_HEIGHT_NEW_xor_ADDRESS_LO_xor_CALL_DATA_SIZE has not been filled");
      }

      if (!filled.get(94)) {
        throw new IllegalStateException(
            "BALANCE_NEW_xor_BYTE_CODE_ADDRESS_HI_xor_MMU___INST_xor_HEIGHT_UNDER_xor_STORAGE_KEY_HI_xor_COINBASE_ADDRESS_LO has not been filled");
      }

      if (!filled.get(93)) {
        throw new IllegalStateException(
            "BALANCE_xor_ACCOUNT_DEPLOYMENT_NUMBER_xor_MMU___CN_T_xor_HEIGHT_OVER_xor_DEPLOYMENT_NUMBER_xor_COINBASE_ADDRESS_HI has not been filled");
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
        throw new IllegalStateException(
            "CODE_HASH_HI_NEW_xor_BYTE_CODE_DEPLOYMENT_NUMBER_xor_MMU___NUM_2_xor_PUSH_VALUE_HI_xor_VAL_CURR_HI_xor_FROM_ADDRESS_LO has not been filled");
      }

      if (!filled.get(95)) {
        throw new IllegalStateException(
            "CODE_HASH_HI_xor_BYTE_CODE_ADDRESS_LO_xor_MMU___NUM_1_xor_INSTRUCTION_xor_STORAGE_KEY_LO_xor_FROM_ADDRESS_HI has not been filled");
      }

      if (!filled.get(98)) {
        throw new IllegalStateException(
            "CODE_HASH_LO_NEW_xor_CALLER_ADDRESS_HI_xor_MMU___SIZE_xor_STACK_ITEM_HEIGHT_1_xor_VAL_NEXT_HI_xor_GAS_PRICE has not been filled");
      }

      if (!filled.get(97)) {
        throw new IllegalStateException(
            "CODE_HASH_LO_xor_BYTE_CODE_DEPLOYMENT_STATUS_xor_MMU___REFS_xor_PUSH_VALUE_LO_xor_VAL_CURR_LO_xor_GAS_LIMIT has not been filled");
      }

      if (!filled.get(100)) {
        throw new IllegalStateException(
            "CODE_SIZE_NEW_xor_CALLER_CONTEXT_NUMBER_xor_MMU___SOURCE_OFFSET_LO_xor_STACK_ITEM_HEIGHT_3_xor_VAL_ORIG_HI_xor_GAS_REFUND_COUNTER_FINAL has not been filled");
      }

      if (!filled.get(99)) {
        throw new IllegalStateException(
            "CODE_SIZE_xor_CALLER_ADDRESS_LO_xor_MMU___SOURCE_OFFSET_HI_xor_STACK_ITEM_HEIGHT_2_xor_VAL_NEXT_LO_xor_GAS_REFUND_AMOUNT has not been filled");
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
        throw new IllegalStateException(
            "DEP_NUM_NEW_xor_CALL_STACK_DEPTH_xor_MMU___TARGET_OFFSET_HI_xor_STACK_ITEM_STAMP_2_xor_INIT_GAS has not been filled");
      }

      if (!filled.get(102)) {
        throw new IllegalStateException(
            "DEP_NUM_xor_CALL_DATA_SIZE_xor_MMU___STACK_VAL_LO_xor_STACK_ITEM_STAMP_1_xor_INIT_CODE_SIZE has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException(
            "DEP_STATUS_NEW_xor_FCOND_FLAG_xor_CODEDEPOSIT_xor_BIN_FLAG_xor_VAL_CURR_IS_ZERO_xor_STATUS_CODE has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException(
            "DEP_STATUS_xor_CCSR_FLAG_xor_CALL_xor_ADD_FLAG_xor_VAL_CURR_IS_ORIG_xor_IS_EIP1559 has not been filled");
      }

      if (!filled.get(101)) {
        throw new IllegalStateException(
            "DEPLOYMENT_NUMBER_INFTY_xor_CALL_DATA_OFFSET_xor_MMU___STACK_VAL_HI_xor_STACK_ITEM_HEIGHT_4_xor_VAL_ORIG_LO_xor_INITIAL_BALANCE has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException(
            "DEPLOYMENT_STATUS_INFTY_xor_UPDATE_xor_ABORT_FLAG_xor_BLAKE2f_xor_ACC_FLAG_xor_VAL_CURR_CHANGES_xor_IS_DEPLOYMENT has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("EXCEPTION_AHOY_FLAG has not been filled");
      }

      if (!filled.get(48)) {
        throw new IllegalStateException(
            "EXISTS_NEW_xor_MXP___DEPLOYS_xor_ECADD_xor_CALL_FLAG_xor_VAL_NEXT_IS_ORIG has not been filled");
      }

      if (!filled.get(47)) {
        throw new IllegalStateException(
            "EXISTS_xor_MMU___FLAG_xor_CREATE_xor_BTC_FLAG_xor_VAL_NEXT_IS_CURR_xor_TXN_REQUIRES_EVM_EXECUTION has not been filled");
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
        throw new IllegalStateException(
            "HAS_CODE_NEW_xor_MXP___MXPX_xor_ECPAIRING_xor_COPY_FLAG_xor_VAL_ORIG_IS_ZERO has not been filled");
      }

      if (!filled.get(49)) {
        throw new IllegalStateException(
            "HAS_CODE_xor_MXP___FLAG_xor_ECMUL_xor_CON_FLAG_xor_VAL_NEXT_IS_ZERO has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("HUB_STAMP has not been filled");
      }

      if (!filled.get(51)) {
        throw new IllegalStateException(
            "IS_BLAKE2f_xor_OOB___EVENT_1_xor_ECRECOVER_xor_CREATE_FLAG_xor_WARM has not been filled");
      }

      if (!filled.get(52)) {
        throw new IllegalStateException(
            "IS_ECADD_xor_OOB___EVENT_2_xor_IDENTITY_xor_DECODED_FLAG_1_xor_WARM_NEW has not been filled");
      }

      if (!filled.get(53)) {
        throw new IllegalStateException(
            "IS_ECMUL_xor_OOB___FLAG_xor_MODEXP_xor_DECODED_FLAG_2 has not been filled");
      }

      if (!filled.get(54)) {
        throw new IllegalStateException(
            "IS_ECPAIRING_xor_PRECINFO___FLAG_xor_RIPEMDsub160_xor_DECODED_FLAG_3 has not been filled");
      }

      if (!filled.get(55)) {
        throw new IllegalStateException(
            "IS_ECRECOVER_xor_STP___EXISTS_xor_SCN_1_xor_DECODED_FLAG_4 has not been filled");
      }

      if (!filled.get(56)) {
        throw new IllegalStateException(
            "IS_IDENTITY_xor_STP___FLAG_xor_SCN_10_xor_DUP_FLAG has not been filled");
      }

      if (!filled.get(57)) {
        throw new IllegalStateException(
            "IS_MODEXP_xor_STP___OOGX_xor_SCN_11_xor_EXT_FLAG has not been filled");
      }

      if (!filled.get(58)) {
        throw new IllegalStateException(
            "IS_PRECOMPILE_xor_STP___WARM_xor_SCN_12_xor_HALT_FLAG has not been filled");
      }

      if (!filled.get(59)) {
        throw new IllegalStateException(
            "IS_RIPEMDsub160_xor_SCN_2_xor_INVALID_FLAG has not been filled");
      }

      if (!filled.get(60)) {
        throw new IllegalStateException("IS_SHA2sub256_xor_SCN_3_xor_INVPREX has not been filled");
      }

      if (!filled.get(69)) {
        throw new IllegalStateException("MXP_FLAG has not been filled");
      }

      if (!filled.get(114)) {
        throw new IllegalStateException("MXP___SIZE_2_LO_xor_STATIC_GAS has not been filled");
      }

      if (!filled.get(115)) {
        throw new IllegalStateException("MXP___WORDS has not been filled");
      }

      if (!filled.get(105)) {
        throw new IllegalStateException(
            "NONCE_NEW_xor_CONTEXT_NUMBER_xor_MXP___GAS_MXP_xor_STACK_ITEM_STAMP_4_xor_NONCE has not been filled");
      }

      if (!filled.get(104)) {
        throw new IllegalStateException(
            "NONCE_xor_CALL_VALUE_xor_MMU___TARGET_OFFSET_LO_xor_STACK_ITEM_STAMP_3_xor_LEFTOVER_GAS has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("NUMBER_OF_NON_STACK_ROWS has not been filled");
      }

      if (!filled.get(70)) {
        throw new IllegalStateException("OOB_FLAG has not been filled");
      }

      if (!filled.get(116)) {
        throw new IllegalStateException("OOB___INST has not been filled");
      }

      if (!filled.get(117)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_1 has not been filled");
      }

      if (!filled.get(118)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_2 has not been filled");
      }

      if (!filled.get(119)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_3 has not been filled");
      }

      if (!filled.get(120)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_4 has not been filled");
      }

      if (!filled.get(121)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_5 has not been filled");
      }

      if (!filled.get(122)) {
        throw new IllegalStateException("OOB___OUTGOING_DATA_6 has not been filled");
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

      if (!filled.get(123)) {
        throw new IllegalStateException("PRECINFO___ADDR_LO has not been filled");
      }

      if (!filled.get(124)) {
        throw new IllegalStateException("PRECINFO___CDS has not been filled");
      }

      if (!filled.get(125)) {
        throw new IllegalStateException("PRECINFO___EXEC_COST has not been filled");
      }

      if (!filled.get(126)) {
        throw new IllegalStateException("PRECINFO___PROVIDES_RETURN_DATA has not been filled");
      }

      if (!filled.get(127)) {
        throw new IllegalStateException("PRECINFO___RDS has not been filled");
      }

      if (!filled.get(128)) {
        throw new IllegalStateException("PRECINFO___SUCCESS has not been filled");
      }

      if (!filled.get(129)) {
        throw new IllegalStateException("PRECINFO___TOUCHES_RAM has not been filled");
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
        throw new IllegalStateException(
            "RLPADDR___DEP_ADDR_HI_xor_IS_STATIC_xor_MXP___INST_xor_STACK_ITEM_VALUE_HI_1_xor_TO_ADDRESS_HI has not been filled");
      }

      if (!filled.get(107)) {
        throw new IllegalStateException(
            "RLPADDR___DEP_ADDR_LO_xor_RETURNER_CONTEXT_NUMBER_xor_MXP___OFFSET_1_HI_xor_STACK_ITEM_VALUE_HI_2_xor_TO_ADDRESS_LO has not been filled");
      }

      if (!filled.get(61)) {
        throw new IllegalStateException("RLPADDR___FLAG_xor_SCN_4_xor_JUMPX has not been filled");
      }

      if (!filled.get(108)) {
        throw new IllegalStateException(
            "RLPADDR___KEC_HI_xor_RETURNER_IS_PRECOMPILE_xor_MXP___OFFSET_1_LO_xor_STACK_ITEM_VALUE_HI_3_xor_VALUE has not been filled");
      }

      if (!filled.get(109)) {
        throw new IllegalStateException(
            "RLPADDR___KEC_LO_xor_RETURN_AT_OFFSET_xor_MXP___OFFSET_2_HI_xor_STACK_ITEM_VALUE_HI_4 has not been filled");
      }

      if (!filled.get(110)) {
        throw new IllegalStateException(
            "RLPADDR___RECIPE_xor_RETURN_AT_SIZE_xor_MXP___OFFSET_2_LO_xor_STACK_ITEM_VALUE_LO_1 has not been filled");
      }

      if (!filled.get(111)) {
        throw new IllegalStateException(
            "RLPADDR___SALT_HI_xor_RETURN_DATA_OFFSET_xor_MXP___SIZE_1_HI_xor_STACK_ITEM_VALUE_LO_2 has not been filled");
      }

      if (!filled.get(112)) {
        throw new IllegalStateException(
            "RLPADDR___SALT_LO_xor_RETURN_DATA_SIZE_xor_MXP___SIZE_1_LO_xor_STACK_ITEM_VALUE_LO_3 has not been filled");
      }

      if (!filled.get(65)) {
        throw new IllegalStateException("SCN_8_xor_MAXCSX has not been filled");
      }

      if (!filled.get(66)) {
        throw new IllegalStateException("SCN_9_xor_MOD_FLAG has not been filled");
      }

      if (!filled.get(67)) {
        throw new IllegalStateException("SELFDESTRUCT_xor_MUL_FLAG has not been filled");
      }

      if (!filled.get(68)) {
        throw new IllegalStateException("SHA2sub256_xor_MXPX has not been filled");
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

      if (!filled.get(83)) {
        throw new IllegalStateException("STATICX has not been filled");
      }

      if (!filled.get(85)) {
        throw new IllegalStateException("STO_FLAG has not been filled");
      }

      if (!filled.get(130)) {
        throw new IllegalStateException("STP___GAS_HI has not been filled");
      }

      if (!filled.get(131)) {
        throw new IllegalStateException("STP___GAS_LO has not been filled");
      }

      if (!filled.get(132)) {
        throw new IllegalStateException("STP___GAS_OOPKT has not been filled");
      }

      if (!filled.get(133)) {
        throw new IllegalStateException("STP___GAS_STPD has not been filled");
      }

      if (!filled.get(134)) {
        throw new IllegalStateException("STP___INST has not been filled");
      }

      if (!filled.get(135)) {
        throw new IllegalStateException("STP___VAL_HI has not been filled");
      }

      if (!filled.get(136)) {
        throw new IllegalStateException("STP___VAL_LO has not been filled");
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
        throw new IllegalStateException("TRM___FLAG_xor_SCN_5_xor_JUMP_FLAG has not been filled");
      }

      if (!filled.get(113)) {
        throw new IllegalStateException(
            "TRM___RAW_ADDR_HI_xor_MXP___SIZE_2_HI_xor_STACK_ITEM_VALUE_LO_4 has not been filled");
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
        throw new IllegalStateException("WARM_NEW_xor_SCN_7_xor_LOG_FLAG has not been filled");
      }

      if (!filled.get(63)) {
        throw new IllegalStateException("WARM_xor_SCN_6_xor_KEC_FLAG has not been filled");
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
        addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee.add(BigInteger.ZERO);
        this.filled.set(91);
      }
      if (!filled.get(92)) {
        addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize.add(
            BigInteger.ZERO);
        this.filled.set(92);
      }
      if (!filled.get(94)) {
        balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo
            .add(BigInteger.ZERO);
        this.filled.set(94);
      }
      if (!filled.get(93)) {
        balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi
            .add(BigInteger.ZERO);
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
        codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo
            .add(BigInteger.ZERO);
        this.filled.set(96);
      }
      if (!filled.get(95)) {
        codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi.add(
            BigInteger.ZERO);
        this.filled.set(95);
      }
      if (!filled.get(98)) {
        codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice.add(
            BigInteger.ZERO);
        this.filled.set(98);
      }
      if (!filled.get(97)) {
        codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit.add(
            BigInteger.ZERO);
        this.filled.set(97);
      }
      if (!filled.get(100)) {
        codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal
            .add(BigInteger.ZERO);
        this.filled.set(100);
      }
      if (!filled.get(99)) {
        codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount
            .add(BigInteger.ZERO);
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
        depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas.add(
            BigInteger.ZERO);
        this.filled.set(103);
      }
      if (!filled.get(102)) {
        depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize.add(BigInteger.ZERO);
        this.filled.set(102);
      }
      if (!filled.get(46)) {
        depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode.add(false);
        this.filled.set(46);
      }
      if (!filled.get(45)) {
        depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559.add(false);
        this.filled.set(45);
      }
      if (!filled.get(101)) {
        deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance
            .add(BigInteger.ZERO);
        this.filled.set(101);
      }
      if (!filled.get(44)) {
        deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment
            .add(false);
        this.filled.set(44);
      }
      if (!filled.get(17)) {
        exceptionAhoyFlag.add(false);
        this.filled.set(17);
      }
      if (!filled.get(48)) {
        existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig.add(false);
        this.filled.set(48);
      }
      if (!filled.get(47)) {
        existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution.add(false);
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
        hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero.add(false);
        this.filled.set(50);
      }
      if (!filled.get(49)) {
        hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero.add(false);
        this.filled.set(49);
      }
      if (!filled.get(25)) {
        hubStamp.add(BigInteger.ZERO);
        this.filled.set(25);
      }
      if (!filled.get(51)) {
        isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm.add(false);
        this.filled.set(51);
      }
      if (!filled.get(52)) {
        isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew.add(false);
        this.filled.set(52);
      }
      if (!filled.get(53)) {
        isEcmulXorOobFlagXorModexpXorDecodedFlag2.add(false);
        this.filled.set(53);
      }
      if (!filled.get(54)) {
        isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3.add(false);
        this.filled.set(54);
      }
      if (!filled.get(55)) {
        isEcrecoverXorStpExistsXorScn1XorDecodedFlag4.add(false);
        this.filled.set(55);
      }
      if (!filled.get(56)) {
        isIdentityXorStpFlagXorScn10XorDupFlag.add(false);
        this.filled.set(56);
      }
      if (!filled.get(57)) {
        isModexpXorStpOogxXorScn11XorExtFlag.add(false);
        this.filled.set(57);
      }
      if (!filled.get(58)) {
        isPrecompileXorStpWarmXorScn12XorHaltFlag.add(false);
        this.filled.set(58);
      }
      if (!filled.get(59)) {
        isRipemDsub160XorScn2XorInvalidFlag.add(false);
        this.filled.set(59);
      }
      if (!filled.get(60)) {
        isSha2Sub256XorScn3XorInvprex.add(false);
        this.filled.set(60);
      }
      if (!filled.get(69)) {
        mxpFlag.add(false);
        this.filled.set(69);
      }
      if (!filled.get(114)) {
        mxpSize2LoXorStaticGas.add(BigInteger.ZERO);
        this.filled.set(114);
      }
      if (!filled.get(115)) {
        mxpWords.add(BigInteger.ZERO);
        this.filled.set(115);
      }
      if (!filled.get(105)) {
        nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce.add(BigInteger.ZERO);
        this.filled.set(105);
      }
      if (!filled.get(104)) {
        nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas.add(BigInteger.ZERO);
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
      if (!filled.get(116)) {
        oobInst.add(BigInteger.ZERO);
        this.filled.set(116);
      }
      if (!filled.get(117)) {
        oobOutgoingData1.add(BigInteger.ZERO);
        this.filled.set(117);
      }
      if (!filled.get(118)) {
        oobOutgoingData2.add(BigInteger.ZERO);
        this.filled.set(118);
      }
      if (!filled.get(119)) {
        oobOutgoingData3.add(BigInteger.ZERO);
        this.filled.set(119);
      }
      if (!filled.get(120)) {
        oobOutgoingData4.add(BigInteger.ZERO);
        this.filled.set(120);
      }
      if (!filled.get(121)) {
        oobOutgoingData5.add(BigInteger.ZERO);
        this.filled.set(121);
      }
      if (!filled.get(122)) {
        oobOutgoingData6.add(BigInteger.ZERO);
        this.filled.set(122);
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
      if (!filled.get(123)) {
        precinfoAddrLo.add(BigInteger.ZERO);
        this.filled.set(123);
      }
      if (!filled.get(124)) {
        precinfoCds.add(BigInteger.ZERO);
        this.filled.set(124);
      }
      if (!filled.get(125)) {
        precinfoExecCost.add(BigInteger.ZERO);
        this.filled.set(125);
      }
      if (!filled.get(126)) {
        precinfoProvidesReturnData.add(BigInteger.ZERO);
        this.filled.set(126);
      }
      if (!filled.get(127)) {
        precinfoRds.add(BigInteger.ZERO);
        this.filled.set(127);
      }
      if (!filled.get(128)) {
        precinfoSuccess.add(BigInteger.ZERO);
        this.filled.set(128);
      }
      if (!filled.get(129)) {
        precinfoTouchesRam.add(BigInteger.ZERO);
        this.filled.set(129);
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
        rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi.add(
            BigInteger.ZERO);
        this.filled.set(106);
      }
      if (!filled.get(107)) {
        rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo
            .add(BigInteger.ZERO);
        this.filled.set(107);
      }
      if (!filled.get(61)) {
        rlpaddrFlagXorScn4XorJumpx.add(false);
        this.filled.set(61);
      }
      if (!filled.get(108)) {
        rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue.add(
            BigInteger.ZERO);
        this.filled.set(108);
      }
      if (!filled.get(109)) {
        rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4.add(BigInteger.ZERO);
        this.filled.set(109);
      }
      if (!filled.get(110)) {
        rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1.add(BigInteger.ZERO);
        this.filled.set(110);
      }
      if (!filled.get(111)) {
        rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2.add(BigInteger.ZERO);
        this.filled.set(111);
      }
      if (!filled.get(112)) {
        rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3.add(BigInteger.ZERO);
        this.filled.set(112);
      }
      if (!filled.get(65)) {
        scn8XorMaxcsx.add(false);
        this.filled.set(65);
      }
      if (!filled.get(66)) {
        scn9XorModFlag.add(false);
        this.filled.set(66);
      }
      if (!filled.get(67)) {
        selfdestructXorMulFlag.add(false);
        this.filled.set(67);
      }
      if (!filled.get(68)) {
        sha2Sub256XorMxpx.add(false);
        this.filled.set(68);
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
      if (!filled.get(83)) {
        staticx.add(false);
        this.filled.set(83);
      }
      if (!filled.get(85)) {
        stoFlag.add(false);
        this.filled.set(85);
      }
      if (!filled.get(130)) {
        stpGasHi.add(BigInteger.ZERO);
        this.filled.set(130);
      }
      if (!filled.get(131)) {
        stpGasLo.add(BigInteger.ZERO);
        this.filled.set(131);
      }
      if (!filled.get(132)) {
        stpGasOopkt.add(BigInteger.ZERO);
        this.filled.set(132);
      }
      if (!filled.get(133)) {
        stpGasStpd.add(BigInteger.ZERO);
        this.filled.set(133);
      }
      if (!filled.get(134)) {
        stpInst.add(BigInteger.ZERO);
        this.filled.set(134);
      }
      if (!filled.get(135)) {
        stpValHi.add(BigInteger.ZERO);
        this.filled.set(135);
      }
      if (!filled.get(136)) {
        stpValLo.add(BigInteger.ZERO);
        this.filled.set(136);
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
        trmFlagXorScn5XorJumpFlag.add(false);
        this.filled.set(62);
      }
      if (!filled.get(113)) {
        trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4.add(BigInteger.ZERO);
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
        warmNewXorScn7XorLogFlag.add(false);
        this.filled.set(64);
      }
      if (!filled.get(63)) {
        warmXorScn6XorKecFlag.add(false);
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
          addrHiXorAccountAddressHiXorCcrsStampXorHeightXorAddressHiXorBasefee,
          addrLoXorAccountAddressLoXorMmuCnSXorHeightNewXorAddressLoXorCallDataSize,
          balanceNewXorByteCodeAddressHiXorMmuInstXorHeightUnderXorStorageKeyHiXorCoinbaseAddressLo,
          balanceXorAccountDeploymentNumberXorMmuCnTXorHeightOverXorDeploymentNumberXorCoinbaseAddressHi,
          batchNumber,
          callerContextNumber,
          codeAddressHi,
          codeAddressLo,
          codeDeploymentNumber,
          codeDeploymentStatus,
          codeHashHiNewXorByteCodeDeploymentNumberXorMmuNum2XorPushValueHiXorValCurrHiXorFromAddressLo,
          codeHashHiXorByteCodeAddressLoXorMmuNum1XorInstructionXorStorageKeyLoXorFromAddressHi,
          codeHashLoNewXorCallerAddressHiXorMmuSizeXorStackItemHeight1XorValNextHiXorGasPrice,
          codeHashLoXorByteCodeDeploymentStatusXorMmuRefsXorPushValueLoXorValCurrLoXorGasLimit,
          codeSizeNewXorCallerContextNumberXorMmuSourceOffsetLoXorStackItemHeight3XorValOrigHiXorGasRefundCounterFinal,
          codeSizeXorCallerAddressLoXorMmuSourceOffsetHiXorStackItemHeight2XorValNextLoXorGasRefundAmount,
          contextGetsRevrtdFlag,
          contextMayChangeFlag,
          contextNumber,
          contextNumberNew,
          contextRevertStamp,
          contextSelfRevrtsFlag,
          contextWillRevertFlag,
          counterNsr,
          counterTli,
          depNumNewXorCallStackDepthXorMmuTargetOffsetHiXorStackItemStamp2XorInitGas,
          depNumXorCallDataSizeXorMmuStackValLoXorStackItemStamp1XorInitCodeSize,
          depStatusNewXorFcondFlagXorCodedepositXorBinFlagXorValCurrIsZeroXorStatusCode,
          depStatusXorCcsrFlagXorCallXorAddFlagXorValCurrIsOrigXorIsEip1559,
          deploymentNumberInftyXorCallDataOffsetXorMmuStackValHiXorStackItemHeight4XorValOrigLoXorInitialBalance,
          deploymentStatusInftyXorUpdateXorAbortFlagXorBlake2FXorAccFlagXorValCurrChangesXorIsDeployment,
          exceptionAhoyFlag,
          existsNewXorMxpDeploysXorEcaddXorCallFlagXorValNextIsOrig,
          existsXorMmuFlagXorCreateXorBtcFlagXorValNextIsCurrXorTxnRequiresEvmExecution,
          failureConditionFlag,
          gasActual,
          gasCost,
          gasExpected,
          gasMemoryExpansion,
          gasNext,
          gasRefund,
          hasCodeNewXorMxpMxpxXorEcpairingXorCopyFlagXorValOrigIsZero,
          hasCodeXorMxpFlagXorEcmulXorConFlagXorValNextIsZero,
          hubStamp,
          isBlake2FXorOobEvent1XorEcrecoverXorCreateFlagXorWarm,
          isEcaddXorOobEvent2XorIdentityXorDecodedFlag1XorWarmNew,
          isEcmulXorOobFlagXorModexpXorDecodedFlag2,
          isEcpairingXorPrecinfoFlagXorRipemDsub160XorDecodedFlag3,
          isEcrecoverXorStpExistsXorScn1XorDecodedFlag4,
          isIdentityXorStpFlagXorScn10XorDupFlag,
          isModexpXorStpOogxXorScn11XorExtFlag,
          isPrecompileXorStpWarmXorScn12XorHaltFlag,
          isRipemDsub160XorScn2XorInvalidFlag,
          isSha2Sub256XorScn3XorInvprex,
          mxpFlag,
          mxpSize2LoXorStaticGas,
          mxpWords,
          nonceNewXorContextNumberXorMxpGasMxpXorStackItemStamp4XorNonce,
          nonceXorCallValueXorMmuTargetOffsetLoXorStackItemStamp3XorLeftoverGas,
          numberOfNonStackRows,
          oobFlag,
          oobInst,
          oobOutgoingData1,
          oobOutgoingData2,
          oobOutgoingData3,
          oobOutgoingData4,
          oobOutgoingData5,
          oobOutgoingData6,
          oogx,
          opcx,
          peekAtAccount,
          peekAtContext,
          peekAtMiscellaneous,
          peekAtScenario,
          peekAtStack,
          peekAtStorage,
          peekAtTransaction,
          precinfoAddrLo,
          precinfoCds,
          precinfoExecCost,
          precinfoProvidesReturnData,
          precinfoRds,
          precinfoSuccess,
          precinfoTouchesRam,
          programCounter,
          programCounterNew,
          pushpopFlag,
          rdcx,
          rlpaddrDepAddrHiXorIsStaticXorMxpInstXorStackItemValueHi1XorToAddressHi,
          rlpaddrDepAddrLoXorReturnerContextNumberXorMxpOffset1HiXorStackItemValueHi2XorToAddressLo,
          rlpaddrFlagXorScn4XorJumpx,
          rlpaddrKecHiXorReturnerIsPrecompileXorMxpOffset1LoXorStackItemValueHi3XorValue,
          rlpaddrKecLoXorReturnAtOffsetXorMxpOffset2HiXorStackItemValueHi4,
          rlpaddrRecipeXorReturnAtSizeXorMxpOffset2LoXorStackItemValueLo1,
          rlpaddrSaltHiXorReturnDataOffsetXorMxpSize1HiXorStackItemValueLo2,
          rlpaddrSaltLoXorReturnDataSizeXorMxpSize1LoXorStackItemValueLo3,
          scn8XorMaxcsx,
          scn9XorModFlag,
          selfdestructXorMulFlag,
          sha2Sub256XorMxpx,
          shfFlag,
          sox,
          sstorex,
          stackItemPop1,
          stackItemPop2,
          stackItemPop3,
          stackItemPop4,
          stackramFlag,
          staticFlag,
          staticx,
          stoFlag,
          stpGasHi,
          stpGasLo,
          stpGasOopkt,
          stpGasStpd,
          stpInst,
          stpValHi,
          stpValLo,
          sux,
          swapFlag,
          transactionEndStamp,
          transactionReverts,
          trmFlag,
          trmFlagXorScn5XorJumpFlag,
          trmRawAddrHiXorMxpSize2HiXorStackItemValueLo4,
          twoLineInstruction,
          txExec,
          txFinl,
          txInit,
          txSkip,
          txWarm,
          txnFlag,
          warmNewXorScn7XorLogFlag,
          warmXorScn6XorKecFlag,
          wcpFlag);
    }
  }
}
