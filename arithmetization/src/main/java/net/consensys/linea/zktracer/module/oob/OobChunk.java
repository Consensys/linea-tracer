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

package net.consensys.linea.zktracer.module.oob;

import static com.google.common.math.BigIntegerMath.log2;
import static java.lang.Byte.toUnsignedInt;
import static java.lang.Math.min;
import static net.consensys.linea.zktracer.types.AddressUtils.getDeploymentAddress;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBoolean;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBigInteger;

import java.math.BigInteger;
import java.math.RoundingMode;

import lombok.Getter;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@Getter
public class OobChunk extends ModuleOperation {
  private boolean oobEvent1;
  private boolean oobEvent2;

  private BigInteger incomingInst;

  private boolean isJump;
  private boolean isJumpi;
  private boolean isRdc;
  private boolean isCdl;
  private boolean isXCall;
  private boolean isCall;
  private boolean isCreate;
  private boolean isSstore;
  private boolean isReturn;

  private boolean prcEcRecover;
  private boolean prcSha2;
  private boolean prcRipemd;
  private boolean prcIdentity;
  private boolean prcEcadd;
  private boolean prcEcmul;
  private boolean prcEcpairing;
  private boolean prcBlake2FA;
  private boolean prcBlake2FB;
  private boolean prcModexpCds;
  private boolean prcModexpBase;
  private boolean prcModexpExponent;
  private boolean prcModexpModulus;
  private boolean prcModexpPricing;

  private final int ctMaxJump = 0;
  private final int ctMaxJumpi = 1;
  private final int ctMaxRdc = 2;
  private final int ctMaxCdl = 0;
  private final int ctMaxXCall = 0;
  private final int ctMaxCall = 2;
  private final int ctMaxCreate = 2;
  private final int ctMaxSstore = 0;
  private final int ctMaxReturn = 0;

  private final int ctMaxPrcEcrecover = 2;
  private final int ctMaxPrcSha2 = 3;
  private final int ctMaxPrcRipemd = 3;
  private final int ctMaxPrcIdentity = 3;
  private final int ctMaxPrcEcadd = 2;
  private final int ctMaxPrcEcmul = 2;
  private final int ctMaxPrcEcpairing = 4;
  private final int ctMaxPrcBlake2FA = 0;
  private final int ctMaxPrcBlake2FB = 2;

  private final int ctMaxPrcModexpCds = 3;
  private final int ctMaxPrcModexpBase = 3;
  private final int ctMaxPrcModexpExponent = 2;
  private final int ctMaxPrcModexpModulus = 2;
  private final int ctMaxPrcModexpPricing = 5;

  private final boolean[] addFlag;
  private final boolean[] modFlag;
  private final boolean[] wcpFlag;

  private final UnsignedByte[] outgoingInst;

  private final BigInteger[] outgoingData1;
  private final BigInteger[] outgoingData2;
  private final BigInteger[] outgoingData3;
  private final BigInteger[] outgoingData4;

  private final BigInteger[] outgoingResLo;

  private OobParameters oobParameters;

  private BigInteger wghtSum;

  private final BigInteger G_CALLSTIPEND =
      BigInteger.valueOf(2300); // TODO: GasConstants.G_CALL_STIPEND.cost()?
  private final BigInteger G_QUADDIVISOR =
      BigInteger.valueOf(3); // TODO: should this go in GasConstants?

  private BigInteger precompileCost;

  // Modules for lookups
  private final Add add;
  private final Mod mod;
  private final Wcp wcp;

  private final Hub hub;

  private int blake2FCallNumber;
  private int modexpCallNumber;

  public OobChunk(
      final MessageFrame frame,
      final Add add,
      final Mod mod,
      final Wcp wcp,
      final Hub hub,
      boolean isPrecompile,
      int blake2FCallNumber,
      int modexpCallNumber) {
    this.add = add;
    this.mod = mod;
    this.wcp = wcp;
    this.hub = hub;

    if (isPrecompile) {
      setPrecomileFlagsAndWghtSumAndIncomingInst(frame);
    } else {
      setOpCodeFlagsAndWghtSumAndIncomingInst(frame);
    }

    this.blake2FCallNumber = blake2FCallNumber;
    this.modexpCallNumber = modexpCallNumber;

    // Init arrays
    int nRows = nRows();
    addFlag = new boolean[nRows];
    modFlag = new boolean[nRows];
    wcpFlag = new boolean[nRows];

    outgoingInst = new UnsignedByte[nRows];
    outgoingData1 = new BigInteger[nRows];
    outgoingData2 = new BigInteger[nRows];
    outgoingData3 = new BigInteger[nRows];
    outgoingData4 = new BigInteger[nRows];
    outgoingResLo = new BigInteger[nRows];

    // TODO: ensure that the nonce update for CREATE is not already done
    populateLookupColumns(frame);
  }

  private void setOpCodeFlagsAndWghtSumAndIncomingInst(MessageFrame frame) {
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    // In the case of CALLs and CREATEs this value will be replaced
    wghtSum = UnsignedByte.of(opCode.byteValue()).toBigInteger();

    switch (opCode) {
      case JUMP:
        isJump = true;
        break;
      case JUMPI:
        isJumpi = true;
        break;
      case RETURNDATACOPY:
        isRdc = true;
        break;
      case CALLDATALOAD:
        isCdl = true;
        break;
      case CALL, CALLCODE, DELEGATECALL, STATICCALL:
        if (opCode == OpCode.CALL
            && !hub.pch().exceptions().stackUnderflow()
            && hub.pch().exceptions().any()) {
          isXCall = true;
          wghtSum = BigInteger.valueOf(0xCC);
        } else {
          isCall = true;
          wghtSum = BigInteger.valueOf(0xCA);
        }
        break;
      case CREATE, CREATE2:
        isCreate = true;
        wghtSum = BigInteger.valueOf(0xCE);
        break;
      case SSTORE:
        isSstore = true;
        break;
      case RETURN:
        isReturn = true;
        break;
      default:
        throw new IllegalArgumentException("OpCode not relevant for Oob");
    }

    incomingInst = wghtSum;
  }

  private void setPrecomileFlagsAndWghtSumAndIncomingInst(MessageFrame frame) {
    Address target = Words.toAddress(frame.getStackItem(1));

    if (target.equals(Address.ECREC)) {
      prcEcRecover = true;
      wghtSum = Bytes.fromHexString("FF01").toUnsignedBigInteger();
    } else if (target.equals(Address.SHA256)) {
      prcSha2 = true;
      wghtSum = Bytes.fromHexString("FF02").toUnsignedBigInteger();
    } else if (target.equals(Address.RIPEMD160)) {
      prcRipemd = true;
      wghtSum = Bytes.fromHexString("FF03").toUnsignedBigInteger();
    } else if (target.equals(Address.ID)) {
      prcIdentity = true;
      wghtSum = Bytes.fromHexString("FF04").toUnsignedBigInteger();
    } else if (target.equals(Address.ALTBN128_ADD)) {
      prcEcadd = true;
      wghtSum = Bytes.fromHexString("FF06").toUnsignedBigInteger();
    } else if (target.equals(Address.ALTBN128_MUL)) {
      prcEcmul = true;
      wghtSum = Bytes.fromHexString("FF07").toUnsignedBigInteger();
    } else if (target.equals(Address.ALTBN128_PAIRING)) {
      prcEcpairing = true;
      wghtSum = Bytes.fromHexString("FF08").toUnsignedBigInteger();
    } else if (target.equals(Address.BLAKE2B_F_COMPRESSION)) {
      if (blake2FCallNumber == 1) {
        prcBlake2FA = true;
        wghtSum = Bytes.fromHexString("FA09").toUnsignedBigInteger();
      } else if (blake2FCallNumber == 2) {
        prcBlake2FB = true;
        wghtSum = Bytes.fromHexString("FB09").toUnsignedBigInteger();
      }
    } else if (target.equals(Address.MODEXP)) {
      switch (modexpCallNumber) {
        case 1:
          prcModexpCds = true;
          wghtSum = Bytes.fromHexString("FA05").toUnsignedBigInteger();
        case 2:
          prcModexpBase = true;
          wghtSum = Bytes.fromHexString("FB05").toUnsignedBigInteger();
        case 3:
          prcModexpExponent = true;
          wghtSum = Bytes.fromHexString("FC05").toUnsignedBigInteger();
        case 4:
          prcModexpModulus = true;
          wghtSum = Bytes.fromHexString("FD05").toUnsignedBigInteger();
        case 5:
          prcModexpPricing = true;
          wghtSum = Bytes.fromHexString("FE05").toUnsignedBigInteger();
      }
    } else {
      throw new IllegalArgumentException("Precompile not relevant for Oob");
    }

    incomingInst = wghtSum;
  }

  public boolean isInst() {
    return isJump || isJumpi || isRdc || isCdl || isCall || isXCall || isCreate || isSstore
        || isReturn;
  }

  public boolean isPrcCommon() {
    return prcEcRecover
        || prcSha2
        || prcRipemd
        || prcIdentity
        || prcEcadd
        || prcEcmul
        || prcEcpairing;
  }

  public boolean isPrcBlake() {
    return prcBlake2FA || prcBlake2FB;
  }

  public boolean isPrcModexp() {
    return prcModexpCds
        || prcModexpBase
        || prcModexpExponent
        || prcModexpModulus
        || prcModexpPricing;
  }

  public boolean isPrc() {
    return isPrcCommon() || isPrcBlake() || isPrcModexp();
  }

  public int maxCt() {
    return ctMaxJump * (isJump ? 1 : 0)
        + ctMaxJumpi * (isJumpi ? 1 : 0)
        + ctMaxRdc * (isRdc ? 1 : 0)
        + ctMaxCdl * (isCdl ? 1 : 0)
        + ctMaxXCall * (isXCall ? 1 : 0)
        + ctMaxCall * (isCall ? 1 : 0)
        + ctMaxCreate * (isCreate ? 1 : 0)
        + ctMaxSstore * (isSstore ? 1 : 0)
        + ctMaxReturn * (isReturn ? 1 : 0)
        + ctMaxPrcEcrecover * (prcEcRecover ? 1 : 0)
        + ctMaxPrcSha2 * (prcSha2 ? 1 : 0)
        + ctMaxPrcRipemd * (prcRipemd ? 1 : 0)
        + ctMaxPrcIdentity * (prcIdentity ? 1 : 0)
        + ctMaxPrcEcadd * (prcEcadd ? 1 : 0)
        + ctMaxPrcEcmul * (prcEcmul ? 1 : 0)
        + ctMaxPrcEcpairing * (prcEcpairing ? 1 : 0)
        + ctMaxPrcBlake2FA * (prcBlake2FA ? 1 : 0)
        + ctMaxPrcBlake2FB * (prcBlake2FB ? 1 : 0)
        + ctMaxPrcModexpCds * (prcModexpCds ? 1 : 0)
        + ctMaxPrcModexpBase * (prcModexpBase ? 1 : 0)
        + ctMaxPrcModexpExponent * (prcModexpExponent ? 1 : 0)
        + ctMaxPrcModexpModulus * (prcModexpModulus ? 1 : 0)
        + ctMaxPrcModexpPricing * (prcModexpPricing ? 1 : 0);
  }

  public int nRows() {
    return maxCt() + 1;
  }

  private void populateLookupColumns(final MessageFrame frame) {
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());

    if (isInst()) {
      if (isJump) {
        JumpOobParameters jumpOobParameters =
            new JumpOobParameters(
                EWord.of(frame.getStackItem(0)), BigInteger.valueOf(frame.getCode().getSize()));
        oobParameters = jumpOobParameters;
        setJump(jumpOobParameters);
      } else if (isJumpi) {
        JumpiOobParameters jumpiOobParameters =
            new JumpiOobParameters(
                EWord.of(frame.getStackItem(0)),
                EWord.of(frame.getStackItem(1)),
                BigInteger.valueOf(frame.getCode().getSize()));
        oobParameters = jumpiOobParameters;
        setJumpi(jumpiOobParameters);
      } else if (isRdc) {
        RdcOobParameters rdcOobParameters =
            new RdcOobParameters(
                EWord.of(frame.getStackItem(1)),
                EWord.of(frame.getStackItem(2)),
                BigInteger.valueOf(frame.getReturnData().size()));
        oobParameters = rdcOobParameters;
        setRdc(rdcOobParameters);
      } else if (isCdl) {
        CdlOobParameters cdlOobParameters =
            new CdlOobParameters(
                EWord.of(frame.getStackItem(0)), BigInteger.valueOf(frame.getInputData().size()));
        oobParameters = cdlOobParameters;
        setCdl(cdlOobParameters);
      } else if (isXCall) {
        // CallOobParameters is used since this is a subcase of CALL
        CallOobParameters callOobParameters =
            new CallOobParameters(
                EWord.of(frame.getStackItem(2)),
                BigInteger.ZERO,
                !frame.getStackItem(2).isZero(),
                BigInteger.ZERO);
        oobParameters = callOobParameters;
        setXCall(callOobParameters);
      } else if (isCall) {
        Account callerAccount = frame.getWorldUpdater().get(frame.getRecipientAddress());
        // DELEGATECALL, STATICCALL cases
        EWord val = EWord.of(0);
        boolean nonZeroValue = false;
        // CALL, CALLCODE cases
        if (opCode == OpCode.CALL || opCode == OpCode.CALLCODE) {
          val = EWord.of(frame.getStackItem(2));
          nonZeroValue = !frame.getStackItem(2).isZero();
        }
        CallOobParameters callOobParameters =
            new CallOobParameters(
                val,
                callerAccount.getBalance().toUnsignedBigInteger(), // balance (caller address)
                nonZeroValue,
                BigInteger.valueOf(frame.getDepth()));
        oobParameters = callOobParameters;
        setCall(callOobParameters);
      } else if (isCreate) {
        Account creatorAccount = frame.getWorldUpdater().get(frame.getRecipientAddress());
        Address deploymentAddress = getDeploymentAddress(frame);
        Account deployedAccount = frame.getWorldUpdater().get(deploymentAddress);
        long nonce = 0;
        boolean hasCode = false;
        if (deployedAccount != null) {
          nonce = deployedAccount.getNonce();
          hasCode = deployedAccount.hasCode();
        }
        CreateOobParameters createOobParameters =
            new CreateOobParameters(
                EWord.of(frame.getStackItem(0)),
                creatorAccount.getBalance().toUnsignedBigInteger(), // balance (creator address)
                BigInteger.valueOf(nonce), // nonce (deployment address)
                hasCode, // has_code (deployment address)
                BigInteger.valueOf(frame.getDepth()));
        oobParameters = createOobParameters;
        setCreate(createOobParameters);
      } else if (isSstore) {
        SstoreOobParameters sstoreOobParameters =
            new SstoreOobParameters(BigInteger.valueOf(frame.getRemainingGas()));
        oobParameters = sstoreOobParameters;
        setSstore(sstoreOobParameters);
      } else if (isReturn) {
        ReturnOobParameters returnOobParameters =
            new ReturnOobParameters(EWord.of(frame.getStackItem(0)));
        oobParameters = returnOobParameters;
        setReturn(returnOobParameters);
      }
    } else if (isPrc()) {
      // DELEGATECALL, STATICCALL cases
      int argsOffset = 2;
      // this corresponds to argsSize on evm.codes
      int cdsIndex = 3;
      // this corresponds to retSize on evm.codes
      int returnAtCapacityIndex = 5;
      // value is not part of the arguments for DELEGATECALL and STATICCALL
      boolean transfersValue = false;
      // CALL, CALLCODE cases
      if (opCode == OpCode.CALL || opCode == OpCode.CALLCODE) {
        argsOffset = 3;
        cdsIndex = 4;
        returnAtCapacityIndex = 6;
        transfersValue = !frame.getStackItem(2).isZero();
      }

      BigInteger callGas =
          BigInteger.valueOf(
              ZkTracer.gasCalculator.gasAvailableForChildCall(
                  frame, Words.clampedToLong(frame.getStackItem(0)), transfersValue));

      BigInteger cds = EWord.of(frame.getStackItem(cdsIndex)).toUnsignedBigInteger();
      // Note that this check will disappear since it will be the MXP module taking care of it
      if (cds.compareTo(EWord.of(frame.getStackItem(cdsIndex)).loBigInt()) > 0) {
        throw new IllegalArgumentException("cds hi part is non-zero");
      }

      BigInteger returnAtCapacity =
          EWord.of(frame.getStackItem(returnAtCapacityIndex)).toUnsignedBigInteger();

      if (isPrcCommon()) {
        PrcCommonOobParameters prcCommonOobParameters =
            new PrcCommonOobParameters(
                callGas,
                cds,
                cds.longValue() == 0,
                returnAtCapacity,
                returnAtCapacity.signum() == 0);
        oobParameters = prcCommonOobParameters;
        setPrc(prcCommonOobParameters);
        if (prcEcRecover || prcEcadd || prcEcmul) {
          setPrcEcRecoverPrcEcaddPrcEcmul(prcCommonOobParameters);
        } else if (prcSha2 || prcRipemd || prcIdentity) {
          setPrcSha2PrcRipemdPrcIdentity(prcCommonOobParameters);
        } else if (prcEcpairing) {
          setPrcEcpairing(prcCommonOobParameters);
        }
      } else if (isPrcBlake()) {
        if (prcBlake2FA) {
          PrcBlake2FAParameters prcBlake2FAParameters = new PrcBlake2FAParameters(cds);
          oobParameters = prcBlake2FAParameters;
          setPrcBlake2FA(prcBlake2FAParameters);
        } else if (prcBlake2FB) {
          BigInteger blakeR =
              frame
                  .shadowReadMemory(argsOffset, cds.longValue())
                  .slice(0, 4)
                  .toUnsignedBigInteger();

          BigInteger blakeF =
              BigInteger.valueOf(
                  toUnsignedInt(frame.shadowReadMemory(argsOffset, cds.longValue()).get(212)));

          PrcBlake2FBParameters prcBlake2FBParameters =
              new PrcBlake2FBParameters(
                  callGas, blakeR, blakeF, returnAtCapacity, returnAtCapacity.signum() == 0);
          oobParameters = prcBlake2FBParameters;
          setPrcBlake2FB(prcBlake2FBParameters);
        }
      } else if (isPrcModexp()) {
        Bytes unpaddedCallData = frame.shadowReadMemory(argsOffset, cds.longValue());
        // pad unpaddedCallData to 96
        Bytes paddedCallData =
            cds.intValue() < 96
                ? Bytes.concatenate(unpaddedCallData, Bytes.repeat((byte) 0, 96 - cds.intValue()))
                : unpaddedCallData;

        // cds and the data below can be int when compared (after size check)
        BigInteger bbs = paddedCallData.slice(0, 32).toUnsignedBigInteger();
        BigInteger ebs = paddedCallData.slice(32, 32).toUnsignedBigInteger();
        BigInteger mbs = paddedCallData.slice(64, 32).toUnsignedBigInteger();

        // Check if bbs, ebs and mbs are <= 512
        if (bbs.compareTo(BigInteger.valueOf(512)) > 0
            || ebs.compareTo(BigInteger.valueOf(512)) > 0
            || mbs.compareTo(BigInteger.valueOf(512)) > 0) {
          throw new IllegalArgumentException("byte sizes are too big");
        }

        // pad paddedCallData to 96 + bbs + ebs
        Bytes doublePaddedCallData =
            cds.intValue() < 96 + bbs.intValue() + ebs.intValue()
                ? Bytes.concatenate(
                    paddedCallData,
                    Bytes.repeat((byte) 0, 96 + bbs.intValue() + ebs.intValue() - cds.intValue()))
                : paddedCallData;

        BigInteger leadingBytesOfExponent =
            doublePaddedCallData
                .slice(96 + bbs.intValue(), min(ebs.intValue(), 32))
                .toUnsignedBigInteger();
        BigInteger exponentLog;
        if (ebs.intValue() <= 32 && leadingBytesOfExponent.signum() == 0) {
          exponentLog = BigInteger.ZERO;
        } else if (ebs.intValue() <= 32 && leadingBytesOfExponent.signum() != 0) {
          exponentLog = BigInteger.valueOf(log2(leadingBytesOfExponent, RoundingMode.FLOOR));
        } else if (ebs.intValue() > 32 && leadingBytesOfExponent.signum() != 0) {
          exponentLog =
              BigInteger.valueOf(8)
                  .multiply(ebs.subtract(BigInteger.valueOf(32)))
                  .add(BigInteger.valueOf(log2(leadingBytesOfExponent, RoundingMode.FLOOR)));
        } else {
          exponentLog = BigInteger.valueOf(8).multiply(ebs.subtract(BigInteger.valueOf(32)));
        }
        if (prcModexpCds) {
          PrcModexpCdsParameters prcModexpCdsParameters =
              new PrcModexpCdsParameters(
                  cds,
                  cds.signum() != 0,
                  cds.intValue() > 32,
                  cds.intValue() > 64,
                  cds.intValue() < 32,
                  cds.intValue() < 64,
                  cds.intValue() < 96);
          oobParameters = prcModexpCdsParameters;
          setPrcModexpCds(prcModexpCdsParameters);
        } else if (prcModexpBase) {
          boolean callDataExtendsBeyondBase = 96 + bbs.intValue() < cds.intValue();
          boolean byLessThanAnEVMWord = cds.intValue() - (96 + bbs.intValue()) < 32;
          BigInteger NCallDataBytes;
          if (!callDataExtendsBeyondBase) {
            NCallDataBytes = BigInteger.ZERO;
          } else {
            if (!byLessThanAnEVMWord) {
              NCallDataBytes = BigInteger.valueOf(32);
            } else {
              NCallDataBytes = cds.subtract(BigInteger.valueOf(96).add(bbs));
            }
          }
          PrcModexpBaseParameters prcModexpBaseParameters =
              new PrcModexpBaseParameters(
                  cds,
                  bbs,
                  bbs.signum() == 0,
                  callDataExtendsBeyondBase,
                  byLessThanAnEVMWord,
                  NCallDataBytes);
          oobParameters = prcModexpBaseParameters;
          setPrcModexpBase(prcModexpBaseParameters);
        } else if (prcModexpExponent) {
          PrcModexpExponentParameters prcModexpExponentParameters =
              new PrcModexpExponentParameters(
                  ebs,
                  ebs.signum() == 0,
                  ebs.longValue() < 32,
                  ebs.min(BigInteger.valueOf(32)),
                  ebs.subtract(BigInteger.valueOf(32)));
          oobParameters = prcModexpExponentParameters;
          setPrcModexpExponent(prcModexpExponentParameters);
        } else if (prcModexpModulus) {
          PrcModexpModulusParameters prcModexpModulusParameters =
              new PrcModexpModulusParameters(bbs, mbs, mbs.signum() == 0, mbs.max(bbs));
          oobParameters = prcModexpModulusParameters;
          setPrcModexpModulus(prcModexpModulusParameters);
        } else if (prcModexpPricing) {
          PrcModexpPricingParameters prcModexpPricingParameters =
              new PrcModexpPricingParameters(
                  callGas,
                  exponentLog,
                  mbs.max(bbs),
                  returnAtCapacity,
                  returnAtCapacity.signum() == 0);
          oobParameters = prcModexpPricingParameters;
          setPrcModexpPricing(prcModexpPricingParameters);
        }
      }
    } else {
      throw new RuntimeException("no opcode or precompile flag was set to true");
    }
  }

  // Constraint systems for populating lookups
  private void callToADD(
      int k, BigInteger arg1Hi, BigInteger arg1Lo, BigInteger arg2Hi, BigInteger arg2Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    EWord arg2 = EWord.of(arg2Hi, arg2Lo);
    addFlag[k] = true;
    modFlag[k] = false;
    wcpFlag[k] = false;
    outgoingInst[k] = UnsignedByte.of(OpCode.ADD.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = arg2Hi;
    outgoingData4[k] = arg2Lo;
    outgoingResLo[k] =
        BigInteger.ZERO; // This value is never used and BigInteger.ZERO is a dummy one

    // lookup
    add.callADD(arg1, arg2);
  }

  private void callToDIV(
      int k, BigInteger arg1Hi, BigInteger arg1Lo, BigInteger arg2Hi, BigInteger arg2Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    EWord arg2 = EWord.of(arg2Hi, arg2Lo);
    addFlag[k] = false;
    modFlag[k] = true;
    wcpFlag[k] = false;
    outgoingInst[k] = UnsignedByte.of(OpCode.DIV.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = arg2Hi;
    outgoingData4[k] = arg2Lo;
    outgoingResLo[k] = arg1.toUnsignedBigInteger().divide(arg2.toUnsignedBigInteger());

    // lookup
    mod.callDIV(arg1, arg2);
  }

  private void callToMOD(
      int k, BigInteger arg1Hi, BigInteger arg1Lo, BigInteger arg2Hi, BigInteger arg2Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    EWord arg2 = EWord.of(arg2Hi, arg2Lo);
    addFlag[k] = false;
    modFlag[k] = true;
    wcpFlag[k] = false;
    outgoingInst[k] = UnsignedByte.of(OpCode.MOD.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = arg2Hi;
    outgoingData4[k] = arg2Lo;
    outgoingResLo[k] = arg1.toUnsignedBigInteger().mod(arg2.toUnsignedBigInteger());

    // lookup
    mod.callMOD(arg1, arg2);
  }

  private void callToLT(
      int k, BigInteger arg1Hi, BigInteger arg1Lo, BigInteger arg2Hi, BigInteger arg2Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    EWord arg2 = EWord.of(arg2Hi, arg2Lo);
    addFlag[k] = false;
    modFlag[k] = false;
    wcpFlag[k] = true;
    outgoingInst[k] = UnsignedByte.of(OpCode.LT.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = arg2Hi;
    outgoingData4[k] = arg2Lo;
    outgoingResLo[k] = booleanToBigInteger(arg1.lessThan(arg2));

    // lookup
    wcp.callLT(arg1, arg2);
  }

  private void callToGT(
      int k, BigInteger arg1Hi, BigInteger arg1Lo, BigInteger arg2Hi, BigInteger arg2Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    EWord arg2 = EWord.of(arg2Hi, arg2Lo);
    addFlag[k] = false;
    modFlag[k] = false;
    wcpFlag[k] = true;
    outgoingInst[k] = UnsignedByte.of(OpCode.GT.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = arg2Hi;
    outgoingData4[k] = arg2Lo;
    outgoingResLo[k] = booleanToBigInteger(arg1.greaterThan(arg2));

    // lookup
    wcp.callGT(arg1, arg2);
  }

  private void callToISZERO(int k, BigInteger arg1Hi, BigInteger arg1Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    addFlag[k] = false;
    modFlag[k] = false;
    wcpFlag[k] = true;
    outgoingInst[k] = UnsignedByte.of(OpCode.ISZERO.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = BigInteger.ZERO;
    outgoingData4[k] = BigInteger.ZERO;
    outgoingResLo[k] = booleanToBigInteger(arg1.isZero());

    // lookup
    wcp.callISZERO(arg1);
  }

  private void callToEQ(
      int k, BigInteger arg1Hi, BigInteger arg1Lo, BigInteger arg2Hi, BigInteger arg2Lo) {
    EWord arg1 = EWord.of(arg1Hi, arg1Lo);
    EWord arg2 = EWord.of(arg2Hi, arg2Lo);
    addFlag[k] = false;
    modFlag[k] = false;
    wcpFlag[k] = true;
    outgoingInst[k] = UnsignedByte.of(OpCode.EQ.byteValue());
    outgoingData1[k] = arg1Hi;
    outgoingData2[k] = arg1Lo;
    outgoingData3[k] = arg2Hi;
    outgoingData4[k] = arg2Lo;
    outgoingResLo[k] = booleanToBigInteger(arg1.equals(arg2));

    // lookup
    wcp.callEQ(arg1, arg2);
  }

  private void noCall(int k) {
    addFlag[k] = false;
    modFlag[k] = false;
    wcpFlag[k] = false;
    outgoingInst[k] = UnsignedByte.of(0);
    outgoingData1[k] = BigInteger.ZERO;
    outgoingData2[k] = BigInteger.ZERO;
    outgoingData3[k] = BigInteger.ZERO;
    outgoingData4[k] = BigInteger.ZERO;
    outgoingResLo[k] = BigInteger.ZERO;
  }

  // Methods to populate columns
  private void setJump(JumpOobParameters jumpOobParameters) {
    // row i
    callToLT(
        0,
        jumpOobParameters.pcNewHi(),
        jumpOobParameters.pcNewLo(),
        BigInteger.ZERO,
        jumpOobParameters.codesize());
    int invalidPcNew = 1 - (bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0);

    // OOB_EVENT_k
    oobEvent1 = invalidPcNew == 1;
    oobEvent2 = false;
  }

  private void setJumpi(JumpiOobParameters jumpiOobParameters) {
    // row i
    callToLT(
        0,
        jumpiOobParameters.pcNewHi(),
        jumpiOobParameters.pcNewLo(),
        BigInteger.ZERO,
        jumpiOobParameters.codesize());
    int invalidPcNew = 1 - (bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0);

    // row i + 1
    callToISZERO(1, jumpiOobParameters.jumpConditionHi(), jumpiOobParameters.jumpConditionLo());
    int attemptJump = 1 - (bigIntegerToBoolean(outgoingResLo[1]) ? 1 : 0);

    // OOB_EVENT_k
    oobEvent1 = attemptJump * invalidPcNew == 1;
    oobEvent2 = attemptJump == 1;
  }

  private void setRdc(RdcOobParameters rdcOobParameters) {
    // row i
    callToISZERO(0, rdcOobParameters.offsetHi(), rdcOobParameters.sizeHi());
    int rdcRoob = 1 - (bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0);

    // row i + 1
    if (rdcRoob == 0) {
      callToADD(
          1,
          BigInteger.ZERO,
          rdcOobParameters.offsetLo(),
          BigInteger.ZERO,
          rdcOobParameters.sizeLo());
    } else {
      noCall(1);
    }
    BigInteger sum =
        addFlag[1] ? rdcOobParameters.offsetLo().add(rdcOobParameters.sizeLo()) : BigInteger.ZERO;

    // row i + 2
    if (rdcRoob == 0) {
      callToGT(
          2,
          EWord.of(sum).hiBigInt(),
          EWord.of(sum).loBigInt(),
          BigInteger.ZERO,
          rdcOobParameters.rds());
    } else {
      noCall(2);
    }
    int rdcSoob = bigIntegerToBoolean(outgoingResLo[2]) ? 1 : 0;

    // OOB_EVENT_k
    oobEvent1 = rdcRoob + (1 - rdcRoob) * rdcSoob == 1;
    oobEvent2 = false;
  }

  private void setCdl(CdlOobParameters cdlOobParameters) {
    // row i
    callToLT(
        0,
        cdlOobParameters.offsetHi(),
        cdlOobParameters.offsetLo(),
        BigInteger.ZERO,
        cdlOobParameters.cds());
    int touchesRam = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;

    // OOB_EVENT_k
    oobEvent1 = 1 - touchesRam == 1;
    oobEvent2 = false;
  }

  private void setXCall(CallOobParameters callOobParameters) {
    // row i
    callToISZERO(0, callOobParameters.valHi(), callOobParameters.valLo());
  }

  private void setCall(CallOobParameters callOobParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        callOobParameters.bal(),
        callOobParameters.valHi(),
        callOobParameters.valLo());
    int insufficientBalanceAbort = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;

    // row i + 1
    callToLT(
        1, BigInteger.ZERO, callOobParameters.csd(), BigInteger.ZERO, BigInteger.valueOf(1024));
    int callStackDepthAbort = 1 - (bigIntegerToBoolean(outgoingResLo[1]) ? 1 : 0);

    // row i + 2
    callToISZERO(2, callOobParameters.valHi(), callOobParameters.valLo());

    // OOB_EVENT_k
    oobEvent1 =
        insufficientBalanceAbort + (1 - insufficientBalanceAbort) * callStackDepthAbort == 1;
    oobEvent2 = false;
  }

  private void setCreate(CreateOobParameters createOobParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        createOobParameters.bal(),
        createOobParameters.valHi(),
        createOobParameters.valLo());
    int insufficientBalanceAbort = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;

    // row i + 1
    callToLT(
        1, BigInteger.ZERO, createOobParameters.csd(), BigInteger.ZERO, BigInteger.valueOf(1024));
    int callStackDepthAbort = 1 - (bigIntegerToBoolean(outgoingResLo[1]) ? 1 : 0);

    // row i + 2
    callToISZERO(2, BigInteger.ZERO, createOobParameters.nonce());
    int nonZeroNonce = 1 - (bigIntegerToBoolean(outgoingResLo[2]) ? 1 : 0);

    // OOB_EVENT_k
    oobEvent1 =
        insufficientBalanceAbort + (1 - insufficientBalanceAbort) * callStackDepthAbort == 1;
    oobEvent2 =
        (1 - (oobEvent1 ? 1 : 0))
                * ((createOobParameters.hasCode() ? 1 : 0)
                    + (1 - (createOobParameters.hasCode() ? 1 : 0)) * nonZeroNonce)
            == 1;
  }

  private void setSstore(SstoreOobParameters sstoreOobParameters) {
    // row i
    callToLT(0, BigInteger.ZERO, G_CALLSTIPEND, BigInteger.ZERO, sstoreOobParameters.gas());
    int sufficientGas = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;

    // OOB_EVENT_k
    oobEvent1 = 1 - sufficientGas == 1;
    oobEvent2 = false;
  }

  private void setReturn(ReturnOobParameters returnOobParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        BigInteger.valueOf(24576),
        returnOobParameters.sizeHi(),
        returnOobParameters.sizeLo());

    // OOB_EVENT_k
    int exceedsMaxCodeSize = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;
    oobEvent1 = exceedsMaxCodeSize == 1;
    oobEvent2 = false;
  }

  private void setPrc(PrcCommonOobParameters prcOobParameters) {
    // row i
    callToISZERO(0, BigInteger.ZERO, prcOobParameters.cds);

    // row i + 1
    callToISZERO(1, BigInteger.ZERO, prcOobParameters.returnAtCapacity);
  }

  private void setPrcEcRecoverPrcEcaddPrcEcmul(PrcCommonOobParameters prcCommonOobParameters) {
    precompileCost =
        BigInteger.valueOf(
            3000 * (prcEcRecover ? 1 : 0) + 150 * (prcEcadd ? 1 : 0) + 6000 * (prcEcmul ? 1 : 0));

    // row i + 2
    callToLT(2, BigInteger.ZERO, prcCommonOobParameters.callGas, BigInteger.ZERO, precompileCost);

    // OOB_EVENT_k
    oobEvent1 = (bigIntegerToBoolean(outgoingResLo[2]) ? 1 : 0) == 1;
    oobEvent2 = false;

    // Set remaining gas
    BigInteger remainingGas =
        (prcCommonOobParameters.callGas.subtract(precompileCost)).max(BigInteger.ZERO);
    prcCommonOobParameters.setRemainingGas(remainingGas);
  }

  private void setPrcSha2PrcRipemdPrcIdentity(PrcCommonOobParameters prcCommonOobParameters) {
    // row i + 2
    callToDIV(
        2,
        BigInteger.ZERO,
        prcCommonOobParameters.cds.add(BigInteger.valueOf(31)),
        BigInteger.ZERO,
        BigInteger.valueOf(32));
    BigInteger ceil = outgoingResLo[2];

    precompileCost =
        (BigInteger.valueOf(5).add(ceil))
            .multiply(
                BigInteger.valueOf(
                    12 * (prcSha2 ? 1 : 0)
                        + 120 * (prcRipemd ? 1 : 0)
                        + 3 * (prcIdentity ? 1 : 0)));

    // row i + 3
    callToLT(3, BigInteger.ZERO, prcCommonOobParameters.callGas, BigInteger.ZERO, precompileCost);

    // OOB_EVENT_k
    oobEvent1 = (bigIntegerToBoolean(outgoingResLo[3]) ? 1 : 0) == 1;
    oobEvent2 = false;

    // Set remaining gas
    BigInteger remainingGas =
        (prcCommonOobParameters.callGas.subtract(precompileCost)).max(BigInteger.ZERO);
    prcCommonOobParameters.setRemainingGas(remainingGas);
  }

  private void setPrcEcpairing(PrcCommonOobParameters prcCommonOobParameters) {
    // row i + 2
    callToMOD(
        2, BigInteger.ZERO, prcCommonOobParameters.cds, BigInteger.ZERO, BigInteger.valueOf(192));
    BigInteger remainder = outgoingResLo[2];

    // row i + 3
    callToISZERO(3, BigInteger.ZERO, remainder);
    boolean isMultipleOf192 = outgoingResLo[3].equals(BigInteger.ONE);

    precompileCost = BigInteger.ZERO;
    if (isMultipleOf192) {
      precompileCost =
          BigInteger.valueOf(45000)
              .add(
                  BigInteger.valueOf(34000)
                      .multiply(prcCommonOobParameters.cds.divide(BigInteger.valueOf(192))));
    }

    // row i + 4
    if (isMultipleOf192) {
      callToLT(4, BigInteger.ZERO, prcCommonOobParameters.callGas, BigInteger.ZERO, precompileCost);
    } else {
      noCall(4);
    }
    boolean insufficientGas = outgoingResLo[4].equals(BigInteger.ONE);

    // OOB_EVENT_k
    oobEvent1 =
        (1 - (isMultipleOf192 ? 1 : 0)) + (isMultipleOf192 ? 1 : 0) * (insufficientGas ? 1 : 0)
            == 1;
    oobEvent2 = false;

    // Set remaining gas
    BigInteger remainingGas = BigInteger.ZERO;

    if (!oobEvent1) {
      remainingGas = (prcCommonOobParameters.callGas.subtract(precompileCost)).max(BigInteger.ZERO);
    }
    prcCommonOobParameters.setRemainingGas(remainingGas);
  }

  private void setPrcBlake2FA(PrcBlake2FAParameters prcBlake2FAParameters) {
    // row i
    callToEQ(
        0, BigInteger.ZERO, prcBlake2FAParameters.cds(), BigInteger.ZERO, BigInteger.valueOf(213));
    int validCds = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;

    // OOB_EVENT_k
    oobEvent1 = 1 - validCds == 1;
    oobEvent2 = false;
  }

  private void setPrcBlake2FB(PrcBlake2FBParameters prcBlake2FBParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        prcBlake2FBParameters.callGas,
        BigInteger.ZERO,
        prcBlake2FBParameters.blakeR);
    int insufficientGas = bigIntegerToBoolean(outgoingResLo[0]) ? 1 : 0;

    // row i + 1
    callToEQ(
        1,
        BigInteger.ZERO,
        prcBlake2FBParameters.blakeF,
        BigInteger.ZERO,
        prcBlake2FBParameters.blakeF.multiply(prcBlake2FBParameters.blakeF));
    int fIsNotABit = 1 - (bigIntegerToBoolean(outgoingResLo[1]) ? 1 : 0);

    // row i + 2
    callToISZERO(2, BigInteger.ZERO, prcBlake2FBParameters.returnAtCapacity);

    // OOB_EVENT_k
    oobEvent1 = insufficientGas + (1 - insufficientGas) * fIsNotABit == 1;
    oobEvent2 = false;

    // Set remaining gas
    BigInteger remainingGas =
        (prcBlake2FBParameters.callGas.subtract(prcBlake2FBParameters.blakeR)).max(BigInteger.ZERO);
    prcBlake2FBParameters.setRemainingGas(remainingGas);
  }

  private void setPrcModexpCds(PrcModexpCdsParameters prcModexpCdsParameters) {
    // row i
    callToGT(0, BigInteger.ZERO, prcModexpCdsParameters.cds(), BigInteger.ZERO, BigInteger.ZERO);

    // row i + 1
    callToGT(
        1, BigInteger.ZERO, prcModexpCdsParameters.cds(), BigInteger.ZERO, BigInteger.valueOf(32));

    // row i + 2
    callToGT(
        2, BigInteger.ZERO, prcModexpCdsParameters.cds(), BigInteger.ZERO, BigInteger.valueOf(64));

    // row i + 3
    callToLT(
        3, BigInteger.ZERO, prcModexpCdsParameters.cds(), BigInteger.ZERO, BigInteger.valueOf(32));

    // row i + 4
    callToLT(
        4, BigInteger.ZERO, prcModexpCdsParameters.cds(), BigInteger.ZERO, BigInteger.valueOf(64));

    // row i + 5
    callToLT(
        5, BigInteger.ZERO, prcModexpCdsParameters.cds(), BigInteger.ZERO, BigInteger.valueOf(96));

    // OOB_EVENT_k
    oobEvent1 = prcModexpCdsParameters.cdsLT96();
    oobEvent2 = false;
  }

  private void setPrcModexpBase(PrcModexpBaseParameters prcModexpBaseParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        prcModexpBaseParameters.bbs(),
        BigInteger.ZERO,
        BigInteger.valueOf(512));

    // row i + 1
    callToISZERO(1, BigInteger.ZERO, prcModexpBaseParameters.bbs());

    // row i + 2
    callToLT(
        2,
        BigInteger.ZERO,
        BigInteger.valueOf(96).add(prcModexpBaseParameters.bbs()),
        BigInteger.ZERO,
        prcModexpBaseParameters.cds());

    // row i + 3
    if (!prcModexpBaseParameters.callDataExtendesBeyondBase()) {
      noCall(3);
    } else {
      callToLT(
          3,
          BigInteger.ZERO,
          prcModexpBaseParameters
              .cds()
              .subtract(BigInteger.valueOf(96).add(prcModexpBaseParameters.bbs())),
          BigInteger.ZERO,
          BigInteger.valueOf(32));
    }

    // OOB_EVENT_k
    oobEvent1 = false;
    oobEvent2 = false;
  }

  private void setPrcModexpExponent(PrcModexpExponentParameters prcModexpExponentParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        prcModexpExponentParameters.ebs(),
        BigInteger.ZERO,
        BigInteger.valueOf(513));

    // row i + 1
    callToISZERO(1, BigInteger.ZERO, prcModexpExponentParameters.ebs());

    // row i + 2
    callToLT(
        2,
        BigInteger.ZERO,
        prcModexpExponentParameters.ebs(),
        BigInteger.ZERO,
        BigInteger.valueOf(32));

    // OOB_EVENT_k
    oobEvent1 = false;
    oobEvent2 = false;
  }

  private void setPrcModexpModulus(PrcModexpModulusParameters prcModexpModulusParameters) {
    // row i
    callToLT(
        0,
        BigInteger.ZERO,
        prcModexpModulusParameters.mbs(),
        BigInteger.ZERO,
        BigInteger.valueOf(513));

    // row i + 1
    callToISZERO(1, BigInteger.ZERO, prcModexpModulusParameters.mbs());

    // row i + 2
    callToLT(
        2,
        BigInteger.ZERO,
        prcModexpModulusParameters.mbs(),
        BigInteger.ZERO,
        prcModexpModulusParameters.bbs());

    // OOB_EVENT_k
    oobEvent1 = false;
    oobEvent2 = false;
  }

  private void setPrcModexpPricing(PrcModexpPricingParameters prcModexpPricingParameters) {
    // row i
    callToISZERO(0, BigInteger.ZERO, prcModexpPricingParameters.returnAtCapacity);

    // row i + 1
    callToISZERO(1, BigInteger.ZERO, prcModexpPricingParameters.exponentLog);
    boolean exponentLogISZERO = bigIntegerToBoolean(outgoingResLo[1]);

    // row i + 2
    callToDIV(
        2,
        BigInteger.ZERO,
        (prcModexpPricingParameters.maxMbsBbs.multiply(prcModexpPricingParameters.maxMbsBbs))
            .add(BigInteger.valueOf(7)),
        BigInteger.ZERO,
        BigInteger.valueOf(8));
    BigInteger fOfMax = outgoingResLo[2];

    // row i + 3
    BigInteger bigNumerator;
    if (!exponentLogISZERO) {
      bigNumerator = fOfMax.multiply(prcModexpPricingParameters.exponentLog);
    } else {
      bigNumerator = fOfMax;
    }
    callToDIV(3, BigInteger.ZERO, bigNumerator, BigInteger.ZERO, G_QUADDIVISOR);
    BigInteger bigQuotient = outgoingResLo[3];

    // row i + 4
    callToLT(4, BigInteger.ZERO, bigQuotient, BigInteger.ZERO, BigInteger.valueOf(200));
    boolean bigQuotientLT200 = bigIntegerToBoolean(outgoingResLo[4]);

    // row i + 5
    precompileCost =
        BigInteger.valueOf(200)
            .multiply(booleanToBigInteger(bigQuotientLT200))
            .add(bigQuotient)
            .multiply(BigInteger.valueOf(1).subtract(booleanToBigInteger(bigQuotientLT200)));
    callToLT(
        5, BigInteger.ZERO, prcModexpPricingParameters.callGas, BigInteger.ZERO, precompileCost);
    boolean insufficientGas = bigIntegerToBoolean(outgoingResLo[5]);

    // OOB_EVENT_k
    oobEvent1 = insufficientGas;
    oobEvent2 = false;

    // Set remaining gas
    BigInteger remainingGas =
        (prcModexpPricingParameters.callGas.subtract(precompileCost)).max(BigInteger.ZERO);
    prcModexpPricingParameters.setRemainingGas(remainingGas);
  }

  @Override
  protected int computeLineCount() {
    return this.nRows();
  }
}
