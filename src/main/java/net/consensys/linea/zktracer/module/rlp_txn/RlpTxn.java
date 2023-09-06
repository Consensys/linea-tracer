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

package net.consensys.linea.zktracer.module.rlp_txn;

import static net.consensys.linea.zktracer.module.rlptxrcpt.RlpTxrcpt.bitDecomposition;
import static net.consensys.linea.zktracer.module.rlptxrcpt.RlpTxrcpt.byteCounting;
import static net.consensys.linea.zktracer.module.rlptxrcpt.RlpTxrcpt.outerRlpSize;
import static net.consensys.linea.zktracer.module.rlptxrcpt.RlpTxrcpt.toGivenSize;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.rlppatterns.RlpBitDecOutput;
import net.consensys.linea.zktracer.module.rlppatterns.RlpByteCountAndPowerOutput;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.TransactionType;

/** Implementation of a {@link Module} for addition/subtraction. */
public class RlpTxn implements Module {
  final Trace.TraceBuilder builder = Trace.builder();
  int llarge = TxnrlpTrace.LLARGE.intValue();
  int llargemo = TxnrlpTrace.LLARGEMO.intValue();
  int prefix_short_int = TxnrlpTrace.int_short.intValue();
  int prefix_long_int = TxnrlpTrace.int_long.intValue();
  int prefix_short_list = TxnrlpTrace.list_short.intValue();
  int prefix_long_list = TxnrlpTrace.list_long.intValue();

  int absolute_transaction_number;

  // Used to check the reconstruction of RLPs
  Bytes reconstructedRlpLt;
  Bytes reconstructedRlpLx;

  @Override
  public String jsonKey() {
    return "txRlp";
  }

  @Override
  public final List<OpCode> supportedOpCodes() {
    return List.of();
  }

  @Override
  public final void traceEndConflation() {
    /** Rewrite the ABS_TX_NUM_INFINY column with the last absolute_transaction_number*/
    for (int i =0; i<this.builder.size();i++){
      this.builder.setAbsTxNumInfinyAt(BigInteger.valueOf(this.absolute_transaction_number),i);
    }
  }

  @Override
  public void traceStartTx(Transaction tx) {
    this.absolute_transaction_number += 1;
    reconstructedRlpLt = Bytes.EMPTY;
    reconstructedRlpLx = Bytes.EMPTY;

    /** Specify transaction constant columns */
    RlpTxnColumnsValue traceValue = new RlpTxnColumnsValue();
    traceValue.ABS_TX_NUM = absolute_transaction_number;
    if (tx.getType() == TransactionType.FRONTIER) {
      traceValue.TYPE = 0;
    } else {
      traceValue.TYPE = tx.getType().getSerializedType();
    }
    traceValue.CODE_FRAGMENT_INDEX = 0; /** TODO */

    /** RREQUIRES_EVM_EXECUTION is set to true in case of a non-empty initcode for contract creation, and non-empty
     * code for message call*/
    if ((tx.getTo().isEmpty() && tx.getData().isPresent() )|| (tx.getTo().isPresent()) /** && TODO non empty msg call*/){
      traceValue.REQUIRES_EVM_EXECUTION=true;
    }

    // Phase 0 : Golbal RLP prefix
    traceValue.DATA_LO = BigInteger.valueOf(traceValue.TYPE);
    handlePhaseGlobalRlpPrefix(traceValue);

    // Phase 1 : ChainId
    if (traceValue.TYPE == 1 || traceValue.TYPE == 2) {
      /** TODO check that chainID is an 8 bytes int */
      handlePhaseInteger(traceValue, 1, tx.getChainId().get(), 8);
    }

    // Phase 2 : Nonce
    BigInteger nonce = BigInteger.valueOf(tx.getNonce());
    traceValue.DATA_LO = nonce;
    handlePhaseInteger(traceValue, 2, nonce, 8);

    // Phase 3 : GasPrice
    if (traceValue.TYPE == 0 || traceValue.TYPE == 1) {
      BigInteger gasPrice = tx.getGasPrice().get().getAsBigInteger();
      /** TODO check that gasPrice is an 8 bytes int */
      traceValue.DATA_LO = gasPrice;
      handlePhaseInteger(traceValue, 3, gasPrice, 8);
    }

    // Phase 4 : max priority fee per gas (GasTipCap)
    if (traceValue.TYPE == 2) {
      BigInteger maxPriorityFeePerGas =
          tx.getMaxPriorityFeePerGas().get().getAsBigInteger();
      traceValue.DATA_HI = maxPriorityFeePerGas;
      /** TODO check that max priority fee per gas is an 8 bytes int */
      handlePhaseInteger(traceValue, 4, maxPriorityFeePerGas, 8);
    }

    // Phase 5 : max fee per gas (GasFeeCap)
    if (traceValue.TYPE == 2) {
      BigInteger maxFeePerGas = tx.getMaxFeePerGas().get().getAsBigInteger();
      /** TODO check that max fee per gas is an 8 bytes int */
      traceValue.DATA_LO = maxFeePerGas;
      handlePhaseInteger(traceValue, 6, maxFeePerGas, 8);
    }

    // Phase 6 : GasLimit
    BigInteger gasLimit = BigInteger.valueOf(tx.getGasLimit());
    traceValue.DATA_LO = gasLimit;
    handlePhaseInteger(traceValue, 6, gasLimit, 8);

    // Phase 7 : To
    if (tx.getTo().isPresent()) {
      traceValue.DATA_HI = tx.getTo().get().slice(0, 4).toBigInteger();
      traceValue.DATA_LO = tx.getTo().get().slice(4, 16).toBigInteger();
    } else {
      traceValue.DATA_HI = BigInteger.ZERO;
      traceValue.DATA_LO = BigInteger.ZERO;
    }
    handlePhaseTo(traceValue, tx);

    // Phase 8 : Value
    BigInteger value = tx.getValue().getAsBigInteger();
    traceValue.DATA_LO = value;
    if (tx.getTo().isEmpty()) {
      traceValue.DATA_HI = BigInteger.ONE;
    } else {
      traceValue.DATA_HI = BigInteger.ZERO;
    }
    handlePhaseInteger(traceValue, 8, value, llarge);

    // Phase 9 : Data
    handlePhaseData(traceValue, tx);

    // Phase 10 : AccessList
    handlePhaseAccessList(traceValue, tx);

    // Phase 11 : Beta / w
    handlePhaseBeta(traceValue, tx);

    // Phase 12 : y
    handlePhaseY(traceValue, tx);

    // Phase 13 : r
    handle32BytesInteger(traceValue, 13, tx.getR());

    // Phase 14 : s
    handle32BytesInteger(traceValue, 14, tx.getS());
  }

  /** Define each phase's constraints */
  private void handlePhaseGlobalRlpPrefix(RlpTxnColumnsValue traceValue) {
    int phase = 0;
    /** First, trace the Type prefix of the transaction */
    traceValue.partialReset(phase, 1, true, true);
    if (traceValue.TYPE != 0) {
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = BigInteger.valueOf(traceValue.TYPE);
      traceValue.nBYTES = 1;
      traceRow(traceValue);
    } else {
      traceValue.LC_CORRECTION = true;
      traceRow(traceValue);
    }

    /** RLP prefix of RLP(LT) */
    rlpByteString(0, traceValue.RLP_LT_BYTESIZE, true, true, false, false, false, false, false, traceValue);

    /** RLP prefix of RLP(LT) */
    rlpByteString(0,traceValue.RLP_LX_BYTESIZE,true,false,true,false,false,false,true,traceValue);
  }

  private void handlePhaseInteger(
    RlpTxnColumnsValue traceValue, int phase, BigInteger input, int nbstep) {
    rlpInt(phase,input,nbstep,true,true,false,true,false,traceValue);
  }

  private void handlePhaseTo(RlpTxnColumnsValue traceValue, Transaction tx) {
    int phase = 7;
    boolean lt = true;
    boolean lx = true;

    if (tx.getTo().isEmpty()) {
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = BigInteger.valueOf(prefix_short_int);
      traceValue.nBYTES = 1;
      traceValue.PHASE_END = true;
      traceRow(traceValue);
    } else {
      handleAddress(traceValue, phase, tx.getTo().get());
    }
  }

  private void handlePhaseData(RlpTxnColumnsValue traceValue, Transaction tx) {
    int phase = 9;
    boolean lt = true;
    boolean lx = true;

    if (tx.getData().isEmpty()) {
      // Trivial case
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = BigInteger.valueOf(prefix_short_int);
      traceValue.nBYTES = 1;
      traceValue.IS_PREFIX = true;
      traceRow(traceValue);

      // One row of padding
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LC_CORRECTION = true;
      traceValue.PHASE_END = true;
      traceRow(traceValue);
    } else {
      // General case

      // Initialise DataSize and DataGasCost
      Bytes data = tx.getData().get();
      traceValue.partialReset(phase, 8, lt, lx);
      traceValue.PHASE_BYTESIZE = data.size();
      for (int i = 0; i < traceValue.PHASE_BYTESIZE; i++) {
        if (data.get(i) == 0) {
          traceValue.DATAGASCOST += TxnrlpTrace.G_txdatazero.intValue();
        } else {
          traceValue.DATAGASCOST += TxnrlpTrace.G_txdatanonzero.intValue();
        }
      }
      traceValue.DATA_HI = BigInteger.valueOf(traceValue.DATAGASCOST);
      traceValue.DATA_LO = BigInteger.valueOf(traceValue.PHASE_BYTESIZE);

      // Trace
      // RLP prefix
      if (traceValue.PHASE_BYTESIZE == 1) {
        rlpInt(9,BigInteger.valueOf(tx.getData().get().get(0)),8,lt,lx,true,false,true,traceValue);
      } else {
        // General case
        rlpByteString(9,traceValue.PHASE_BYTESIZE,false,lt,lx,true,false,false,false,traceValue);
      }
      // 16-rows ct-loop to write all the data
      int nbstep = 16;
      int nbloop = (traceValue.PHASE_BYTESIZE - 1) / 16 + 1;
      data.shiftLeft(8 * (16 * nbloop - traceValue.PHASE_BYTESIZE));
      for (int i = 0; i < nbloop; i++) {
        traceValue.partialReset(phase, nbstep, lt, lx);
        traceValue.INPUT_1 = data.slice(llarge * i, llarge);
        int accByteSize = 0;
        for (int ct = 0; ct < llarge; ct++) {
          traceValue.COUNTER = ct;
          if (traceValue.PHASE_BYTESIZE != 0) {
            accByteSize += 1;
          }
          traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
          traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct+1);
          traceValue.ACC_BYTESIZE = accByteSize;
          if (ct == nbstep - 1) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
            traceValue.nBYTES = accByteSize;
          }
          traceRow(traceValue);
        }
      }
      /** Two rows of padding */
      traceValue.partialReset(phase, 2, lt, lx);
      traceValue.LC_CORRECTION = true;
      traceRow(traceValue);

      traceValue.COUNTER = 1;
      traceValue.PHASE_END = true;
      traceRow(traceValue);
    }

    // Put INDEX_DATA to 0 at the end of the phase
    traceValue.INDEX_DATA = 0;
  }

  private void handlePhaseAccessList(RlpTxnColumnsValue traceValue, Transaction tx){
    int phase = 10;
    boolean lt = true;
    boolean lx = true;

    /** Trivial case */
    if (tx.getAccessList().isEmpty()){
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED=true;
      traceValue.LIMB=BigInteger.valueOf(prefix_short_list);
      traceValue.nBYTES=1;
      traceValue.IS_PREFIX =true;
      traceValue.PHASE_END =true;
      traceRow(traceValue);

    } else {
      // Initialise traceValue
      int nbAddr = 0;
      int nbSto = 0;
      List<Integer> nbStoPerAddrList = new ArrayList<>();
      List<Integer> accessTupleByteSizeList = new ArrayList<>();
      int phaseByteSize =0;
      for (int i = 0; i < tx.getAccessList().get().size(); i++) {
        nbAddr +=1;
        nbSto +=tx.getAccessList().get().get(i).storageKeys().size();
        nbStoPerAddrList.set(i,tx.getAccessList().get().get(i).storageKeys().size());
        accessTupleByteSizeList.set(i, outerRlpSize(33*tx.getAccessList().get().get(i).storageKeys().size()));
        phaseByteSize += outerRlpSize(33*tx.getAccessList().get().get(i).storageKeys().size());
      }
      phaseByteSize = outerRlpSize(phaseByteSize);

      traceValue.partialReset(phase, 0, lt, lx);
      traceValue.nb_Addr=nbAddr;
      traceValue.DATA_LO=BigInteger.valueOf(nbAddr);
      traceValue.nb_Sto=nbSto;
      traceValue.DATA_HI=BigInteger.valueOf(nbSto);
      traceValue.PHASE_BYTESIZE=phaseByteSize;


      // Trace RLP(Phase Byte Size)
      rlpByteString(phase,traceValue.PHASE_BYTESIZE,true,lt,lx,true,false,false,false,traceValue);

      // Loop Over AccessTuple
      for (int i=0; i<nbAddr;i++){

        // Update columns at the beginning of an AccessTuple entry
        traceValue.nb_Addr -= 1;
        traceValue.nb_Sto_per_Addr=nbStoPerAddrList.get(i);
        traceValue.ADDR_HI=tx.getAccessList().get().get(i).address().slice(0,4);
        traceValue.ADDR_LO=tx.getAccessList().get().get(i).address().slice(4,llarge);
        traceValue.ACCESS_TUPLE_BYTESIZE = accessTupleByteSizeList.get(i);

        // Rlp(AccessTupleByteSize)
        rlpByteString(phase, traceValue.ACCESS_TUPLE_BYTESIZE, true,lt,lx,true,true,false,false,traceValue);

        // RLP (address)
        handleAddress(traceValue, phase, tx.getAccessList().get().get(i).address());

        // Rlp prefix of the list of storage key
        if (nbStoPerAddrList.get(i)==0){
          traceValue.partialReset(10,1,lt,lx);
          traceValue.IS_PREFIX=true;
          traceValue.DEPTH_1=true;
          traceValue.DEPTH_2=true;
          traceValue.LIMB_CONSTRUCTED=true;
          traceValue.LIMB=BigInteger.valueOf(prefix_short_list);
          traceValue.nBYTES=1;
          traceValue.PHASE_END=(traceValue.nb_Sto==0);
          traceRow(traceValue);
        } else{
        rlpByteString(phase, 33L *traceValue.nb_Sto_per_Addr,true,lt,lx,true,true,true,false,traceValue);
        }

        // Loop over StorageKey
        for (int j=0; j<nbStoPerAddrList.get(i); j++){
          traceValue.nb_Sto-=1;
          traceValue.nb_Sto_per_Addr-=1;
          handleStorageKey(traceValue, traceValue.nb_Sto==0, (Hash) tx.getAccessList().get().get(i).storageKeys().get(j));
        }
      }
    }
  }

  private void handlePhaseBeta(RlpTxnColumnsValue traceValue, Transaction tx){
    int phase = 11;
    BigInteger V= tx.getV();
    // TODO add check v is a 8 bytes int

    // Rlp(w)
    boolean lt = true;
    boolean lx = false;
    rlpInt(phase,V,8,lt,lx,false,false,false,traceValue);

    if (V.equals(BigInteger.valueOf(27))||V.equals(BigInteger.valueOf(28))){
      // One row of padding
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.IS_PREFIX=true;
      traceValue.LC_CORRECTION =true;
      traceValue.PHASE_END =true;
      traceRow(traceValue);
    } else {
      // RLP(ChainID) then one row with RLP().RLP()
      lt = false;
      lx =true;
      rlpInt(phase,tx.getChainId().get(),8,lt,lx,true,false,false,traceValue);

      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED=true;
      traceValue.LIMB=BigInteger.valueOf(256L *prefix_short_int+prefix_short_int);
      traceValue.nBYTES=2;
      traceValue.PHASE_END =true;
      traceRow(traceValue);
    }
  }

  private void handlePhaseY(RlpTxnColumnsValue traceValue, Transaction tx){
    traceValue.partialReset(12, 1, true, false);
    traceValue.LIMB_CONSTRUCTED=true;
    if (tx.getV().equals(BigInteger.ZERO)){
      traceValue.LIMB=BigInteger.valueOf(prefix_short_int);
    }  else {
      traceValue.LIMB= BigInteger.ONE;
    }
    traceValue.nBYTES=1;
    traceValue.PHASE_END =true;
    traceRow(traceValue);
  }

  private void rlpByteString(
    int phase,
    long length,
    boolean isList,
    boolean lt,
    boolean lx,
    boolean isPrefix,
    boolean depth1,
    boolean depth2,
    boolean endPhase,
    RlpTxnColumnsValue traceValue) {
    int lengthSize =
      Bytes.ofUnsignedLong(length).size()
        - Bytes.ofUnsignedLong(length).numberOfLeadingZeroBytes();

    RlpByteCountAndPowerOutput byteCountingOutput = byteCounting(lengthSize, 8);

    traceValue.partialReset(phase, 8,lt,lx);
    traceValue.INPUT_1 = Bytes.ofUnsignedInt(length);
    traceValue.IS_PREFIX=isPrefix;
    traceValue.DEPTH_1 = depth1;
    traceValue.DEPTH_2=depth2;

    Bytes input1RightShift = toGivenSize(traceValue.INPUT_1, 8);
    long acc2LastRow = 0;

    if (length >= 56) {
      acc2LastRow = length - 56;
    } else {
      acc2LastRow = 55 - length;
    }

    Bytes acc2LastRowShift = toGivenSize(Bytes.ofUnsignedInt(acc2LastRow), 8);
    for (int ct = 0; ct < 8; ct++) {
      traceValue.COUNTER = ct;
      traceValue.ACC_BYTESIZE = byteCountingOutput.getAccByteSizeList().get(ct);
      traceValue.POWER = byteCountingOutput.getPowerList().get(ct);
      traceValue.BYTE_1 = input1RightShift.get(ct);
      traceValue.ACC_1 = input1RightShift.slice(0, ct + 1);
      traceValue.BYTE_2 = acc2LastRowShift.get(ct);
      traceValue.ACC_2 = acc2LastRowShift.slice(0, ct + 1);

      if (length >= 56) {
        if (ct == 6) {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.nBYTES = 1;
          if (isList) {
            traceValue.LIMB = BigInteger.valueOf(prefix_long_list + lengthSize);
          } else {
            traceValue.LIMB = BigInteger.valueOf(prefix_long_int + lengthSize);
          }
        }

        if (ct == 7) {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.LIMB = BigInteger.valueOf(length);
          traceValue.nBYTES = lengthSize;
          traceValue.BIT = true;
          traceValue.BIT_ACC = 1;
          traceValue.PHASE_END = endPhase;
        }
      } else {
        if (ct == 7) {
          traceValue.LIMB_CONSTRUCTED = true;
          if (isList) {
            traceValue.LIMB = BigInteger.valueOf(prefix_short_list + length);
          } else {
            traceValue.LIMB = BigInteger.valueOf(prefix_short_int + length);
          }
          traceValue.nBYTES = 1;
          traceValue.PHASE_END = endPhase;
        }
      }
      traceRow(traceValue);
    }
  }

  private void rlpInt(
    int phase,
    BigInteger input,
    int nStep,
    boolean lt,
    boolean lx,
    boolean isPrefix,
    boolean endPhase,
    boolean onlyPrefix,
    RlpTxnColumnsValue traceValue) {

    traceValue.partialReset(phase, nStep, lt, lx);
    traceValue.IS_PREFIX = isPrefix;

    Bytes inputByte = Bytes.wrap(input.toByteArray());
    int inputSize = inputByte.size();
    RlpByteCountAndPowerOutput byteCountingOutput = byteCounting(inputSize, nStep);

    Bytes inputBytePadded = toGivenSize(inputByte, nStep);
    RlpBitDecOutput bitDecOutput =
      bitDecomposition(0xff & inputBytePadded.get(inputBytePadded.size() - 1), nStep);

    traceValue.INPUT_1 = inputByte;

    for (int ct = 0; ct < nStep; ct++) {
      traceValue.COUNTER = ct;
      traceValue.BYTE_1 = inputBytePadded.get(ct);
      traceValue.ACC_1 = inputBytePadded.slice(0, ct + 1);
      traceValue.POWER = byteCountingOutput.getPowerList().get(ct);
      traceValue.ACC_BYTESIZE = byteCountingOutput.getAccByteSizeList().get(ct);
      traceValue.BIT = bitDecOutput.getBitDecList().get(ct);
      traceValue.BIT_ACC = bitDecOutput.getBitAccList().get(ct);

      if (input.compareTo(BigInteger.valueOf(128))>=0 && ct == nStep-2) {
        traceValue.LIMB_CONSTRUCTED = true;
        traceValue.LIMB = BigInteger.valueOf(prefix_short_int + inputSize);
        traceValue.nBYTES = 1;
      }

      if (ct == nStep-1) {
        if (onlyPrefix) {
          traceValue.LC_CORRECTION = true;
          traceValue.LIMB_CONSTRUCTED = false;
          traceValue.LIMB = BigInteger.ZERO;
          traceValue.nBYTES = 0;
        } else {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.LIMB = input;
          traceValue.nBYTES = inputSize;
          traceValue.PHASE_END = endPhase;
        }
      }
      traceRow(traceValue);
    }
  }

  private void handle32BytesInteger(RlpTxnColumnsValue traceValue, int phase, BigInteger input) {
    traceValue.partialReset(phase, llarge, true, false);
    if (input.equals(BigInteger.ZERO)) {
      // Trivial case
      traceValue.nSTEP = 1;
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = BigInteger.valueOf(prefix_short_int);
      traceValue.nBYTES = 1;
      traceValue.PHASE_END = true;
      traceRow(traceValue);
    } else {
      // General case
      Bytes inputByte = Bytes.wrap(input.toByteArray());
      int inputLen = inputByte.size();
      Bytes inputByte32 = toGivenSize(inputByte, 32);
      traceValue.INPUT_1 = inputByte32.slice(0, llarge);
      traceValue.INPUT_2 = inputByte32.slice(llarge, llarge);

      RlpByteCountAndPowerOutput byteCounting;
      if (inputLen <= traceValue.nSTEP) {
        RlpByteCountAndPowerOutput byteCountingOutput = byteCounting(inputLen, traceValue.nSTEP);
        RlpBitDecOutput bitDecOutput =
          bitDecomposition(inputByte.get(inputByte.size() - 1), traceValue.nSTEP);

        for (int ct = 0; ct < traceValue.nSTEP; ct++) {
          traceValue.COUNTER = ct;
          traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
          traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct+1);
          traceValue.ACC_BYTESIZE = byteCountingOutput.getAccByteSizeList().get(ct);
          traceValue.POWER = byteCountingOutput.getPowerList().get(ct);
          traceValue.BIT = bitDecOutput.getBitDecList().get(ct);
          traceValue.BIT_ACC = bitDecOutput.getBitAccList().get(ct);

          // if input >= 128, there is a RLP prefix, nothing if 0 < input < 128
          if (ct == traceValue.nSTEP - 2 && input.compareTo(BigInteger.valueOf(128)) >= 0) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = BigInteger.valueOf(prefix_short_int + inputLen);
            traceValue.nBYTES = 1;
          }
          if (ct == traceValue.nSTEP - 1) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_2.slice(llarge - inputLen, inputLen).toUnsignedBigInteger();
            traceValue.nBYTES = inputLen;
            traceValue.PHASE_END = true;
          }
          traceRow(traceValue);
        }
      } else {
        inputLen -= traceValue.nSTEP;
        byteCounting = byteCounting(inputLen, traceValue.nSTEP);

        for (int ct = 0; ct < traceValue.nSTEP; ct++) {
          traceValue.COUNTER = ct;
          traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
          traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct+1);
          traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
          traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct+1);
          traceValue.ACC_BYTESIZE = byteCounting.getAccByteSizeList().get(ct);
          traceValue.POWER = byteCounting.getPowerList().get(ct);

          if (ct == traceValue.nSTEP - 3) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = BigInteger.valueOf(prefix_short_int + llarge + inputLen);
            traceValue.nBYTES = 1;
          }
          if (ct == traceValue.nSTEP - 2) {
            traceValue.LIMB = traceValue.INPUT_1.slice(llarge - inputLen, inputLen).toUnsignedBigInteger();
            traceValue.nBYTES = inputLen;
          }
          if (ct == traceValue.nSTEP - 1) {
            traceValue.LIMB = traceValue.INPUT_2.toUnsignedBigInteger();
            traceValue.nBYTES = llarge;
            traceValue.PHASE_END = true;
          }
          traceRow(traceValue);
        }
      }
    }
  }

  private void handleAddress(RlpTxnColumnsValue traceValue, int phase, Address address) {
    boolean lt = true;
    boolean lx = true;
    traceValue.partialReset(phase, llarge, lt, lx);
    traceValue.INPUT_1 = address.slice(0, 4).shiftRight(8 * 11);
    traceValue.INPUT_2 = address.slice(4, llarge);

    if (phase == 10) {
      traceValue.DEPTH_1 = true;
    }

    for (int ct = 0; ct < traceValue.nSTEP; ct++) {
      traceValue.COUNTER = ct;
      traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
      traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct+1);
      traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
      traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct+1);

      if (ct == traceValue.nSTEP - 3) {
        traceValue.LIMB_CONSTRUCTED = true;
        traceValue.LIMB = BigInteger.valueOf(prefix_short_int + 20).shiftLeft(8 * llargemo);
        traceValue.nBYTES = 1;
      }

      if (ct == traceValue.nSTEP - 2) {
        traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
        traceValue.nBYTES = 4;
      }
      if (ct == traceValue.nSTEP - 1) {
        traceValue.LIMB = traceValue.INPUT_2.toUnsignedBigInteger();
        traceValue.nBYTES = llarge;

        if (phase == 7) {
          traceValue.PHASE_END = true;
        }
      }
      traceRow(traceValue);
    }
  }

  private void handleStorageKey(RlpTxnColumnsValue traceValue, boolean end_phase, Hash storage_key) {
    traceValue.partialReset(10, llarge, true, true);
    traceValue.DEPTH_1 = true;
    traceValue.DEPTH_2 = true;
    traceValue.INPUT_1 = storage_key.slice(0, llarge);
    traceValue.INPUT_2 = storage_key.slice(llarge, llarge);

    for (int ct = 0; ct < traceValue.nSTEP; ct++) {
      traceValue.COUNTER = ct;
      traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
      traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct+1);
      traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
      traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct+1);

      if (ct == traceValue.nSTEP - 3) {
        traceValue.LIMB_CONSTRUCTED = true;
        traceValue.LIMB = BigInteger.valueOf(prefix_short_int + 32).shiftLeft(8 * llargemo);
        traceValue.nBYTES = 1;
      }

      if (ct == traceValue.nSTEP - 2) {
        traceValue.LIMB = traceValue.INPUT_1.toUnsignedBigInteger();
        traceValue.nBYTES = llarge;
      }

      if (ct == traceValue.nSTEP - 1) {
        traceValue.LIMB = traceValue.INPUT_2.toUnsignedBigInteger();
        traceValue.PHASE_END = end_phase;
      }

      traceRow(traceValue);
    }
  }

  private int InnerRlpSize(int rlpSize) {
    /** If rlpSize >1, return size(something) where rlpSize = size(RLP(something)). */
    if (rlpSize == 0 || rlpSize == 1) {
      /** TODO panic */
    }
    int output = rlpSize;
    if (rlpSize < 57) {
      output -= 1;
    } else if (rlpSize == 57) {
      /** TODO panic */
    } else if (rlpSize < 258) {
      output -= 2;
    } else {
      for (int i = 0; i < 8; i++) {
        if ((rlpSize - 2 - i >= Math.pow(2, i)) && (rlpSize - i - 1 <= Math.pow(2, i + 1))) {
          output -= (2 + i);
        } else if (rlpSize == Math.pow(2, i) + 1 + i) {
          /** TODO panic */
        }
      }
    }
    return output;
  }

  /** Define the Tracer */
  private void traceRow(RlpTxnColumnsValue traceValue) {
    /** Decrements RLP_BYTESIZE */
    if (traceValue.phase != 0) {
      if (traceValue.LIMB_CONSTRUCTED && traceValue.LT) {
        traceValue.RLP_LT_BYTESIZE -= traceValue.nBYTES;
      }
      if (traceValue.LIMB_CONSTRUCTED && traceValue.LX) {
        traceValue.RLP_LX_BYTESIZE -= traceValue.nBYTES;
      }
    }

    /** Decrement phaseByteSize and accessTupleByteSize for Phase 10 (AccessList) */
    if (traceValue.phase == 10) {
      /** Decreases PhaseByteSize */
      if (traceValue.DEPTH_1 && traceValue.LIMB_CONSTRUCTED) {
        traceValue.PHASE_BYTESIZE -= traceValue.nBYTES;
      }
      /** Decreases AccessTupleSize */
      if (traceValue.DEPTH_1 && !(traceValue.IS_PREFIX && !traceValue.DEPTH_2) && traceValue.LIMB_CONSTRUCTED) {
        traceValue.ACCESS_TUPLE_BYTESIZE -= traceValue.nBYTES;
      }
    }

    this.builder
        .absTxNum(BigInteger.valueOf(traceValue.ABS_TX_NUM))
        .absTxNumInfiny(BigInteger.valueOf(traceValue.ABS_TX_NUM_INFINY))
        .acc1(traceValue.ACC_1)
        .acc2(traceValue.ACC_2)
        .accBytesize(BigInteger.valueOf(traceValue.ACC_BYTESIZE))
        .accessTupleBytesize(BigInteger.valueOf(traceValue.ACCESS_TUPLE_BYTESIZE))
        .addrHi(traceValue.ADDR_HI)
        .addrLo(traceValue.ADDR_LO)
        .bit(traceValue.BIT)
        .bitAcc(UnsignedByte.of(traceValue.BIT_ACC))
        .byte1(UnsignedByte.of(traceValue.BYTE_1))
        .byte2(UnsignedByte.of(traceValue.BYTE_2))
        .codeFragmentIndex(BigInteger.valueOf(traceValue.CODE_FRAGMENT_INDEX))
        .counter(BigInteger.valueOf(traceValue.COUNTER))
        .dataHi(traceValue.DATA_HI)
        .dataLo(traceValue.DATA_LO)
        .datagascost(BigInteger.valueOf(traceValue.DATAGASCOST))
        .depth1(traceValue.DEPTH_1)
        .depth2(traceValue.DEPTH_2);
    if (traceValue.COUNTER == traceValue.nSTEP - 1) {
      this.builder.done(Boolean.TRUE);
    } else {
      this.builder.done(Boolean.FALSE);
    }
    this.builder
        .endPhase(traceValue.PHASE_END)
        .indexData(BigInteger.valueOf(traceValue.INDEX_DATA))
        .indexLt(BigInteger.valueOf(traceValue.INDEX_LT))
        .indexLx(BigInteger.valueOf(traceValue.INDEX_LX))
        .input1(traceValue.INPUT_1)
        .input2(traceValue.INPUT_2)
        .isPadding(traceValue.LC_CORRECTION)
        .isPrefix(traceValue.IS_PREFIX)
        .limb(traceValue.LIMB.shiftLeft(8 * (llargemo - traceValue.nBYTES)))
        .limbConstructed(traceValue.LIMB_CONSTRUCTED)
        .lt(traceValue.LT)
        .lx(traceValue.LX)
        .nBytes(UnsignedByte.of(traceValue.nBYTES))
        .nbAddr(BigInteger.valueOf(traceValue.nb_Addr))
        .nbSto(BigInteger.valueOf(traceValue.nb_Sto))
        .nbStoPerAddr(BigInteger.valueOf(traceValue.nb_Sto_per_Addr))
        .numberStep(UnsignedByte.of(traceValue.nSTEP));
    List<Function<Boolean, Trace.TraceBuilder>> phaseColumns =
        List.of(
            this.builder::phase0,
            this.builder::phase1,
            this.builder::phase2,
            this.builder::phase3,
            this.builder::phase4,
            this.builder::phase5,
            this.builder::phase6,
            this.builder::phase7,
            this.builder::phase8,
            this.builder::phase9,
            this.builder::phase10,
            this.builder::phase11,
            this.builder::phase12,
            this.builder::phase13,
            this.builder::phase14);
for(int i = 0; i < phaseColumns.size(); i++) {
  phaseColumns.get(i).apply(i == traceValue.phase);
}
    this.builder
        .phaseBytesize(BigInteger.valueOf(traceValue.PHASE_BYTESIZE))
        .power(traceValue.POWER)
        .requiresEvmExecution(traceValue.REQUIRES_EVM_EXECUTION)
        .rlpLtBytesize(BigInteger.valueOf(traceValue.RLP_LT_BYTESIZE))
        .rlpLxBytesize(BigInteger.valueOf(traceValue.RLP_LX_BYTESIZE))
        .type(UnsignedByte.of(traceValue.TYPE))
        .validateRow();

    // Increments Index
    if (traceValue.LIMB_CONSTRUCTED && traceValue.LT) {
      traceValue.INDEX_LT += 1;
    }
    if (traceValue.LIMB_CONSTRUCTED && traceValue.LX) {
      traceValue.INDEX_LX += 1;
    }

    // Increments IndexData (Phase 9)
    if (traceValue.phase == 9 && !traceValue.IS_PREFIX && (traceValue.LIMB_CONSTRUCTED || traceValue.LC_CORRECTION)) {
      traceValue.INDEX_DATA += 1;
    }

    // Decrements PhaseByteSize and DataGasCost in Data phase (phase 9)
    if (traceValue.phase == 9) {
      if (traceValue.PHASE_BYTESIZE != 0 && !traceValue.IS_PREFIX) {
        traceValue.PHASE_BYTESIZE -= 1;
        if (traceValue.BYTE_1 == 0) {
          traceValue.DATAGASCOST -= TxnrlpTrace.G_txdatazero.intValue();
        } else {
          traceValue.DATAGASCOST -= TxnrlpTrace.G_txdatanonzero.intValue();
        }
      }
    }
    if (traceValue.PHASE_END) {
      traceValue.DataHiLoReset();
    }
    this.builder.validateRow();

    //reconstruct RLPs
    if (traceValue.LT){
      this.reconstructedRlpLt = Bytes.concatenate(this.reconstructedRlpLt, Bytes.of(traceValue.LIMB.toByteArray()).slice(0,traceValue.nBYTES));
    }
    if (traceValue.LX){
      this.reconstructedRlpLx = Bytes.concatenate(this.reconstructedRlpLx,Bytes.of(traceValue.LIMB.toByteArray()).slice(0,traceValue.nBYTES));
    }
  }

  @Override
  public Object commit() {
    return new RlpTxnTrace(builder.build());
  }
}
