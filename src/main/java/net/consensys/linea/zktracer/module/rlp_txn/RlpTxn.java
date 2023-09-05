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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.TransactionType;

/** Implementation of a {@link Module} for addition/subtraction. */
public class RlpTxn implements Module {
  final Trace.TraceBuilder trace = Trace.builder();
  int llarge = TxnrlpTrace.LLARGE.intValue();
  int llargemo = TxnrlpTrace.LLARGEMO.intValue();
  int prefix_short_int = TxnrlpTrace.int_short.intValue();
  int prefix_long_int = TxnrlpTrace.int_long.intValue();
  int prefix_short_list = TxnrlpTrace.list_short.intValue();
  int prefix_long_list = TxnrlpTrace.list_long.intValue();

  int absolute_transaction_number;

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
    for (int i =0; i<this.trace.size();i++){
      this.trace.setAbsTxNumInfinyAt(BigInteger.valueOf(this.absolute_transaction_number),i);
    }
  }

  @Override
  public void traceStartTx(Transaction tx) {
    this.absolute_transaction_number += 1;

    /** Specify transaction constant columns */
    RlpTxnColumnsValue traceValue = new RlpTxnColumnsValue();
    traceValue.ABS_TX_NUM = absolute_transaction_number;
    traceValue.ABS_TX_NUM_INFINY = absolute_transaction_number;
    if (tx.getType() == TransactionType.FRONTIER) {
      traceValue.TYPE = 0;
    } else {
      traceValue.TYPE = tx.getType().getSerializedType();
    }
    traceValue.CODE_FRAGMENT_INDEX = 0; /** TODO */

    /** RREQUIRES_EVM_EXECUTION is set to true in case of a non-empty initcode for contract creation, and non-empty
     * code for message call*/
    if (tx.getTo().isEmpty() && tx.getData().isPresent() /** && TODO non empty msg call*/){
      traceValue.REQUIRES_EVM_EXECUTION=true;
    }

    /** Phase 0 : Golbal RLP prefix */
    traceValue.DATA_LO = BigInteger.valueOf(traceValue.TYPE);
    handlePhaseGlobalRlpPrefix(traceValue);

    /** Phase 1 : ChainId */
    if (traceValue.TYPE == 1 || traceValue.TYPE == 2) {
      /** TODO check that chainID is an 8 bytes int */
      handlePhaseInteger(traceValue, 1, tx.getChainId().get(), 8, true, true);
    }

    /** Phase 2 : Nonce */
    BigInteger nonce = BigInteger.valueOf(tx.getNonce());
    traceValue.DATA_LO = nonce;
    handlePhaseInteger(traceValue, 2, nonce, 8, true, true);

    /** Phase 3 : GasPrice */
    if (traceValue.TYPE == 0 || traceValue.TYPE == 1) {
      BigInteger gasPrice = tx.getGasPrice().get().getAsBigInteger();
      /** TODO check that gasPrice is an 8 bytes int */
      traceValue.DATA_LO = gasPrice;
      handlePhaseInteger(traceValue, 3, gasPrice, 8, true, true);
    }

    /** Phase 4 : max priority fee per gas (GasTipCap) */
    if (traceValue.TYPE == 2) {
      BigInteger maxPriorityFeePerGas =
          tx.getMaxPriorityFeePerGas().get().getAsBigInteger();
      traceValue.DATA_HI = maxPriorityFeePerGas;
      /** TODO check that max priority fee per gas is an 8 bytes int */
      handlePhaseInteger(traceValue, 4, maxPriorityFeePerGas, 8, true, true);
    }

    /** Phase 5 : max fee per gas (GasFeeCap) */
    if (traceValue.TYPE == 2) {
      BigInteger maxFeePerGas = tx.getMaxFeePerGas().get().getAsBigInteger();
      /** TODO check that max fee per gas is an 8 bytes int */
      traceValue.DATA_LO = maxFeePerGas;
      handlePhaseInteger(traceValue, 6, maxFeePerGas, 8, true, true);
    }

    /** Phase 6 : GasLimit */
    BigInteger gasLimit = BigInteger.valueOf(tx.getGasLimit());
    traceValue.DATA_LO = gasLimit;
    handlePhaseInteger(traceValue, 6, gasLimit, 8, true, true);

    /** Phase 7 : To */
    if (tx.getTo().isPresent()) {
      traceValue.DATA_HI = tx.getTo().get().slice(0, 4).toBigInteger();
      traceValue.DATA_LO = tx.getTo().get().slice(4, 16).toBigInteger();
    } else {
      traceValue.DATA_HI = BigInteger.ZERO;
      traceValue.DATA_LO = BigInteger.ZERO;
    }
    handlePhaseTo(traceValue, tx);

    /** Phase 8 : Value */
    BigInteger value = tx.getValue().getAsBigInteger();
    /** TODO add check that value is a 12 bytes int */
    traceValue.DATA_LO = value;
    if (tx.getTo().isEmpty()) {
      traceValue.DATA_HI = BigInteger.ONE;
    } else {
      traceValue.DATA_HI = BigInteger.ZERO;
    }
    handlePhaseInteger(traceValue, 8, value, 12, true, true);

    /** Phase 9 : Data */
    handlePhaseData(traceValue, tx);

    /** Phase 10 : AccessList */
    handlePhaseAccessList(traceValue, tx);
    /** TODO add check on nullity of accesstuplebytesize, nbaddr, nbsto, nbstoperaddr ?*/

    /** Phase 11 : Beta / w */
    traceValue = HandlePhaseBeta(traceValue, tx);

    /** Phase 12 : y */
    traceValue = HandlePhaseY(traceValue, tx);

    /** Phase 13 : r */
    traceValue = Handle32BytesInteger(traceValue, 13, tx.getR());

    /** Phase 14 : s */
    traceValue = Handle32BytesInteger(traceValue, 14, tx.getS());
  }

  /** Define each phase's constraints */
  private void handlePhaseGlobalRlpPrefix(RlpTxnColumnsValue traceValue) {
    int phase = 0;
    /** First, trace the Type prefix of the transaction */
    traceValue.partialReset(phase, 1, true, true);
    if (traceValue.TYPE != 0) {
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = Bytes.ofUnsignedShort(traceValue.TYPE);
      traceValue.nBYTES = 1;
      traceRow(traceValue);
    } else {
      traceValue.is_padding = true;
      traceRow(traceValue);
    }

    /** RLP prefix of RLP(LT) */
    int nbstep = 8;
    boolean isbytesize = true;
    boolean islist = true;
    boolean lt = true;
    boolean lx = false;
    boolean endphase = false;
    boolean isprefix = false;
    boolean depth1 = false;
    boolean depth2 = false;
    handleInt(
            traceValue,
            phase,
            BigInteger.valueOf(traceValue.RLP_LT_BYTESIZE),
            nbstep,
            isbytesize,
            islist,
            lt,
            lx,
            endphase,
            isprefix,
            depth1,
            depth2);

    /** RLP prefix of RLP(LT) */
    lt = false;
    lx = true;
    endphase = true;
    handleInt(
            traceValue,
            phase,
            BigInteger.valueOf(traceValue.RLP_LX_BYTESIZE),
            nbstep,
            isbytesize,
            islist,
            lt,
            lx,
            endphase,
            isprefix,
            depth1,
            depth2);
  }

  private void handlePhaseInteger(
    RlpTxnColumnsValue traceValue, int phase, BigInteger input, int nbstep, boolean lt, boolean lx) {
    boolean isbytesize = false;
    boolean islist = false;
    boolean isprefix = false;
    boolean depth1 = false;
    boolean depth2 = false;
    boolean endphase = true;
    handleInt(
            traceValue,
            phase,
            input,
            nbstep,
            isbytesize,
            islist,
            lt,
            lx,
            endphase,
            isprefix,
            depth1,
            depth2);
  }

  private void handlePhaseTo(RlpTxnColumnsValue traceValue, Transaction tx) {
    int phase = 7;
    boolean lt = true;
    boolean lx = true;

    if (tx.getTo().isEmpty()) {
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int);
      traceValue.nBYTES = 1;
      traceValue.end_phase = true;
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
      /** Trivial case */
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int);
      traceValue.nBYTES = 1;
      traceValue.is_prefix = true;
      traceValue.is_bytesize = true;
      traceRow(traceValue);

      /** One row of padding */
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.is_padding = true;
      traceValue.end_phase = true;
      traceRow(traceValue);
    } else {
      /** General case */

      /** Initialise DataSize and DataGasCost */
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

      /** Trace */

      /** RLP prefix */
      if (traceValue.PHASE_BYTESIZE == 1) {
        /** Special case with 1 byte of data */
        traceValue.partialReset(phase, 8, lt, lx);
        traceValue.is_prefix = true;
        traceValue.INPUT_1 = data;
        RlpTxnBitDecOutput bitDecomposition =
            BitDecomposition(traceValue.INPUT_1.get(0), traceValue.number_step);
        if (traceValue.INPUT_1.get(0) < 128) {
          traceValue.is_padding = true;
        } else {
          traceValue.is_padding = false;
        }
        for (int ct = 0; ct < traceValue.number_step; ct++) {
          traceValue.COUNTER = ct;
          traceValue.BIT = bitDecomposition.BitDecList.get(ct);
          traceValue.BIT_ACC = bitDecomposition.BitAccList.get(ct);
          if (ct == traceValue.number_step - 1) {
            traceValue.BYTE_1 = traceValue.INPUT_1.get(0);
            traceValue.ACC_1 = traceValue.INPUT_1;
            if (traceValue.INPUT_1.get(0) >= 128) {
              traceValue.LIMB_CONSTRUCTED = true;
              traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int + 1);
              traceValue.nBYTES = 1;
            }
          }
          traceRow(traceValue);
        }
      } else {
        /** General case */
        boolean isbytesize = true;
        boolean islist = false;
        boolean endphase = false;
        boolean isprefix = true;
        boolean depth1 = false;
        boolean depth2 = false;
        handleInt(
                traceValue,
                phase,
                BigInteger.valueOf(traceValue.PHASE_BYTESIZE),
                8,
                isbytesize,
                islist,
                lt,
                lx,
                endphase,
                isprefix,
                depth1,
                depth2);
      }
      /** 16-rows ct-loop to write all the data */
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
          traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct);
          traceValue.ACC_BYTESIZE = accByteSize;
          if (ct == nbstep - 1) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_1;
            traceValue.nBYTES = accByteSize;
          }
          traceRow(traceValue);
        }
      }
      /** Two rows of padding */
      traceValue.partialReset(phase, 2, lt, lx);
      traceValue.is_padding = true;
      traceRow(traceValue);

      traceValue.COUNTER = 1;
      traceValue.end_phase = true;
      traceRow(traceValue);
    }

    /** TODO add check that phasebytesize and datagascost is zero */
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
      traceValue.LIMB=Bytes.ofUnsignedShort(prefix_short_list);
      traceValue.nBYTES=1;
      traceValue.is_bytesize=true;
      traceValue.is_list=true;
      traceValue.is_prefix=true;
      traceValue.end_phase=true;
      traceRow(traceValue);

    } else {
      /** Initialise traceValue */
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


      /** Trace */
      /** Trace RLP(Phase Byte Size) */
      int numberstep = 8;
      boolean isbytesize = true;
      boolean islist = true;
      boolean endphase = false;
      boolean isprefix = true;
      boolean depth1 = false;
      boolean depth2 = false;
      handleInt(traceValue, phase, BigInteger.valueOf(traceValue.PHASE_BYTESIZE), numberstep, isbytesize, islist, lt, lx,
        endphase, isprefix, depth1, depth2);

      /** Loop Over AccessTuple */
      for (int i=0; i<nbAddr;i++){

        /** Update columns at the begining of an AccessTuple entry */
        traceValue.nb_Addr -= 1;
        traceValue.nb_Sto_per_Addr=nbStoPerAddrList.get(i);
        /** TODO traceValue.ADDR_HI */
        /** TODO traceValue.ADDR_LO */
        traceValue.ACCESS_TUPLE_BYTESIZE = accessTupleByteSizeList.get(i);

        /** Rlp(AccessTupleByteSize) */
        numberstep = 8;
        isbytesize = true;
        islist = true;
        endphase=false;
        isprefix=true;
        depth1=true;
        depth2=false;
        handleInt(traceValue, phase, BigInteger.valueOf(traceValue.ACCESS_TUPLE_BYTESIZE), numberstep, isbytesize, islist,
          lt, lx, endphase, isprefix, depth1,depth2);

        /** RLP (address) */
        handleAddress(traceValue, phase, tx.getAccessList().get().get(i).address());

        /** Rlp prefix of the list of storage key */
        if (nbStoPerAddrList.get(i)==0){
          numberstep=1;
        } else{
          numberstep=8;
        }
        isbytesize = true;
        islist = true;
        endphase = traceValue.nb_Sto==0;
        isprefix=true;
        depth1=true;
        depth2=true;
        handleInt(traceValue, phase, BigInteger.valueOf(traceValue.ACCESS_TUPLE_BYTESIZE), numberstep, isbytesize, islist,
          lt, lx, endphase, isprefix, depth1, depth2);

        /** Loop over StorageKey */
        for (int j=0; j<nbStoPerAddrList.get(i); j++){
          traceValue.nb_Sto-=1;
          traceValue.nb_Sto_per_Addr-=1;
          endphase = traceValue.nb_Sto==0;
          handleStorageKey(traceValue, endphase, (Hash) tx.getAccessList().get().get(i).storageKeys().get(j));
        }
      }
    }
  }

  private RlpTxnColumnsValue HandlePhaseBeta(RlpTxnColumnsValue traceValue, Transaction tx){
    int phase = 11;
    BigInteger V= tx.getV();
    /** add check v is a 8 bytes int */

    /** Rlp(w) */
    boolean isbytesize =false;
    boolean islist =false;
    boolean lt = true;
    boolean lx = false;
    boolean endphase =false;
    boolean isprefix = false;
    boolean depth1 = false;
    boolean depth2 = false ;
    handleInt(traceValue, phase, V, 8, isbytesize, islist, lt, lx, endphase, isprefix, depth1, depth2);

    if (V.equals(BigInteger.valueOf(27))||V.equals(BigInteger.valueOf(28))){
      /** One row of padding */
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.is_padding=true;
      traceValue.end_phase=true;
      traceRow(traceValue);
    } else {
      /** RLP(ChainID) then one row of padding */
      lt = false;
      lx =true;
      handleInt(traceValue, phase, tx.getChainId().get(), 8, isbytesize, islist, lt, lx, endphase,
        isprefix, depth1, depth2);

      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED=true;
      traceValue.LIMB=Bytes.ofUnsignedShort(256*prefix_short_int+prefix_short_int);
      traceValue.nBYTES=2;
      traceValue.end_phase=true;
      traceRow(traceValue);
    }
    return traceValue;
  }

  private RlpTxnColumnsValue HandlePhaseY(RlpTxnColumnsValue traceValue, Transaction tx){
    int phase = 12;
    boolean lt = true;
    boolean lx = false;
    BigInteger y = tx.getV();

    if (y.equals(BigInteger.ZERO)){
      traceValue.partialReset(phase, 1, lt, lx);
      traceValue.LIMB_CONSTRUCTED=true;
      traceValue.LIMB=Bytes.ofUnsignedShort(prefix_short_int);
      traceValue.nBYTES=1;
      traceValue.end_phase=true;
      traceRow(traceValue);
    }  else {
      handleInt(traceValue, phase, y, 8, false, false, lt, lx, true, false, false, false);
    }
    return traceValue;
  }

  /** Define the constraint patterns functions */
  private void handleInt(
      RlpTxnColumnsValue traceValue,
      int phase,
      BigInteger input,
      int nb_step,
      boolean is_bytesize,
      boolean is_list,
      boolean lt,
      boolean lx,
      boolean end_phase,
      boolean is_prefix,
      boolean depth1,
      boolean depth2) {
    Bytes inputByte = Bytes.ofUnsignedShort(input.byteValue());
    int inputByteLen = inputByte.size();

    traceValue.partialReset(phase, nb_step, lt, lx);
    traceValue.INPUT_1 = inputByte;
    traceValue.is_bytesize = is_bytesize;
    traceValue.is_list = is_list;
    traceValue.is_prefix = is_prefix;
    traceValue.DEPTH_1 = depth1;
    traceValue.DEPTH_2 = depth2;

    /** Trivial case */
    if (input.equals(BigInteger.ZERO)) {
      traceValue.number_step = 1;
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.nBYTES = 1;
      traceValue.end_phase = end_phase;
      if (is_list) {
        traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_list).shiftLeft(8 * llargemo);
      } else {
        traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int).shiftLeft(8 * llargemo);
      }
      traceRow(traceValue);
    } else {
      /** General case */

      /** Compute the COMP bit column */
      traceValue.COMP = (input.compareTo(BigInteger.valueOf(56)) >= 0);

      /** Compute acc2LastRowValue, the value of the last row of ACC2 */
      BigInteger acc2LastRowValue = null;
      if (traceValue.COMP) {
        acc2LastRowValue = input.subtract(BigInteger.valueOf(56));
      } else {
        acc2LastRowValue = BigInteger.valueOf(55).subtract(input);
      }
      Bytes acc2LastRowBytes = Bytes.wrap(acc2LastRowValue.toByteArray());
      acc2LastRowBytes.shiftLeft(8*(nb_step-acc2LastRowBytes.size()));

      /** Compute the bit decomposition of the last input's byte */
      Byte lastByte = inputByte.get(inputByteLen - 1);
      RlpTxnBitDecOutput bitDecomposition = BitDecomposition(lastByte, nb_step);

      /** Compute the bytesize and Power columns */
      RlpTxnByteCountingOutput byteCounting = ByteCounting(inputByteLen, nb_step);

      /** Now, Trace */
      traceValue.number_step = nb_step;
      Bytes inputBytePadded = inputByte.shiftLeft(8 * (nb_step - inputByteLen));

      for (int ct = 0; ct < nb_step; ct++) {
        traceValue.COUNTER = ct;
        traceValue.BYTE_1 = inputBytePadded.get(ct);
        traceValue.ACC_1 = inputBytePadded.slice(0, ct);
        traceValue.ACC_BYTESIZE = byteCounting.AccByteSizeList.get(ct);
        traceValue.POWER = byteCounting.PowerList.get(ct);
        traceValue.BIT = bitDecomposition.BitDecList.get(ct);
        traceValue.BIT_ACC = bitDecomposition.BitAccList.get(ct);
        traceValue.BYTE_2 = acc2LastRowBytes.get(ct);
        traceValue.ACC_2 = acc2LastRowBytes.slice(0, ct);

        if (ct == nb_step - 2) {
          if (!is_bytesize) {
            if (input.compareTo(BigInteger.valueOf(128)) >= 0) {
              traceValue.LIMB_CONSTRUCTED = true;
              traceValue.LIMB =
                  Bytes.ofUnsignedShort(
                      prefix_short_int + byteCounting.AccByteSizeList.get(nb_step - 1));
              traceValue.nBYTES = 1;
            } else {
              if (!traceValue.COMP) {
                traceValue.LIMB_CONSTRUCTED = false;
                traceValue.LIMB = Bytes.EMPTY;
                traceValue.nBYTES = 0;
              } else {
                if (!traceValue.is_list) {
                  traceValue.LIMB_CONSTRUCTED = true;
                  traceValue.LIMB =
                      Bytes.ofUnsignedShort(
                          prefix_long_int + byteCounting.AccByteSizeList.get(nb_step - 1));
                  traceValue.nBYTES = 1;
                } else {
                  traceValue.LIMB_CONSTRUCTED = true;
                  traceValue.LIMB =
                      Bytes.ofUnsignedShort(
                          prefix_long_list + byteCounting.AccByteSizeList.get(nb_step - 1));
                  traceValue.nBYTES = 1;
                }
              }
            }
          }
        }

        if (ct == nb_step - 1) {
          traceValue.LIMB_CONSTRUCTED = true;
          traceValue.end_phase = end_phase;
          if (!traceValue.is_bytesize) {
            traceValue.LIMB = inputByte;
            traceValue.nBYTES = inputByteLen;
          } else {
            if (traceValue.COMP) {
              traceValue.LIMB = inputByte;
              traceValue.nBYTES = inputByteLen;
            } else {
              if (!traceValue.is_list) {
                traceValue.LIMB = Bytes.wrap(input.add(BigInteger.valueOf(prefix_short_int)).toByteArray());
                traceValue.nBYTES = 1;
              } else {
                traceValue.LIMB = Bytes.wrap(input.add(BigInteger.valueOf(prefix_short_list)).toByteArray());
                traceValue.nBYTES = 1;
              }
            }
          }
        }
        traceRow(traceValue);
      }
    }
  }

  private RlpTxnColumnsValue Handle32BytesInteger(RlpTxnColumnsValue traceValue, int phase, BigInteger input) {
    traceValue.partialReset(phase, llarge, true, false);
    if (input.equals(BigInteger.ZERO)) {
      /** Trivial case */
      traceValue.number_step = 1;
      traceValue.LIMB_CONSTRUCTED = true;
      traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int);
      traceValue.nBYTES = 1;
      traceValue.end_phase = true;
      traceRow(traceValue);
    } else {
      /** General case */

      Bytes32 inputByte = Bytes32.wrap(input.toByteArray());
      /** TODO make it a 32 byte */
      int inputLen = inputByte.size();
      /** TODO have the bytesize of the input before left padding */
      traceValue.INPUT_1 = inputByte.slice(0, llarge);
      traceValue.INPUT_2 = inputByte.slice(llarge, llarge);

      if (inputLen <= traceValue.number_step) {
        RlpTxnByteCountingOutput byteCounting = ByteCounting(inputLen, traceValue.number_step);
        RlpTxnBitDecOutput bitDec =
            BitDecomposition(inputByte.get(inputByte.size() - 1), traceValue.number_step);

        for (int ct = 0; ct < traceValue.number_step; ct++) {
          traceValue.COUNTER = ct;
          traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
          traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct);
          traceValue.ACC_BYTESIZE = byteCounting.AccByteSizeList.get(ct);
          traceValue.POWER = byteCounting.PowerList.get(ct);
          traceValue.BIT = bitDec.BitDecList.get(ct);
          traceValue.BIT_ACC = bitDec.BitAccList.get(ct);

          /** if input >= 128, there is a RLP prefix, nothing if 0 < input < 128 */
          if (ct == traceValue.number_step - 2 && input.compareTo(BigInteger.valueOf(128)) >= 0) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int + inputLen);
            traceValue.nBYTES = 1;
          }
          if (ct == traceValue.number_step - 1) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = traceValue.INPUT_2.slice(llarge - inputLen, inputLen);
            traceValue.nBYTES = 1;
            traceValue.end_phase = true;
          }
          traceRow(traceValue);
        }
      } else {
        inputLen -= traceValue.number_step;
        RlpTxnByteCountingOutput byteCounting = ByteCounting(inputLen, traceValue.number_step);

        for (int ct = 0; ct < traceValue.number_step; ct++) {
          traceValue.COUNTER = ct;
          traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
          traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct);
          traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
          traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct);
          traceValue.ACC_BYTESIZE = byteCounting.AccByteSizeList.get(ct);
          traceValue.POWER = byteCounting.PowerList.get(ct);

          if (ct == traceValue.number_step - 3) {
            traceValue.LIMB_CONSTRUCTED = true;
            traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int + llarge + inputLen);
            traceValue.nBYTES = 1;
          }
          if (ct == traceValue.number_step - 2) {
            traceValue.LIMB = traceValue.INPUT_1.slice(llarge - inputLen, inputLen);
            traceValue.nBYTES = inputLen;
          }
          if (ct == traceValue.number_step - 1) {
            traceValue.LIMB = traceValue.INPUT_2;
            traceValue.nBYTES = llarge;
            traceValue.end_phase = true;
          }
          traceRow(traceValue);
        }
      }
    }
    return traceValue;
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

    for (int ct = 0; ct < traceValue.number_step; ct++) {
      traceValue.COUNTER = ct;
      traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
      traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct);
      traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
      traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct);

      if (ct == traceValue.number_step - 3) {
        traceValue.LIMB_CONSTRUCTED = true;
        traceValue.LIMB = Bytes.ofUnsignedInt(prefix_short_int + 20).shiftLeft(8 * llargemo);
        traceValue.nBYTES = 1;
      }

      if (ct == traceValue.number_step - 2) {
        traceValue.LIMB = traceValue.INPUT_1;
        traceValue.nBYTES = 4;
      }
      if (ct == traceValue.number_step - 1) {
        traceValue.LIMB = traceValue.INPUT_2;
        traceValue.nBYTES = llarge;

        if (phase == 7) {
          traceValue.end_phase = true;
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

    for (int ct = 0; ct < traceValue.number_step; ct++) {
      traceValue.COUNTER = ct;
      traceValue.BYTE_1 = traceValue.INPUT_1.get(ct);
      traceValue.ACC_1 = traceValue.INPUT_1.slice(0, ct);
      traceValue.BYTE_2 = traceValue.INPUT_2.get(ct);
      traceValue.ACC_2 = traceValue.INPUT_2.slice(0, ct);

      if (ct == traceValue.number_step - 3) {
        traceValue.LIMB_CONSTRUCTED = true;
        traceValue.LIMB = Bytes.ofUnsignedShort(prefix_short_int + 32).shiftLeft(8 * llargemo);
        traceValue.nBYTES = 1;
      }

      if (ct == traceValue.number_step - 2) {
        traceValue.LIMB = traceValue.INPUT_1;
        traceValue.nBYTES = llarge;
      }

      if (ct == traceValue.number_step - 1) {
        traceValue.LIMB = traceValue.INPUT_2;
        traceValue.end_phase = end_phase;
      }

      traceRow(traceValue);
    }
  }

  /** Define side functions */
  private RlpTxnBitDecOutput BitDecomposition(Byte inputByte, int nb_step) {
    /** TODO panic if nb_step<8 */
    RlpTxnBitDecOutput output = new RlpTxnBitDecOutput();

    int input = inputByte.intValue();

    int bitAcc = 0;
    boolean bitDec = false;
    double div = 0;

    for (int i = 7; i >= 0; i--) {
      div = Math.pow(2, i);
      bitAcc *= 2;

      if (input >= div) {
        bitDec = true;
        bitAcc +=1;
        input -= div;
      } else {
        bitDec = false;
      }

      output.BitDecList.set(nb_step - i - 1, bitDec);
      output.BitAccList.set(nb_step - i - 1, bitAcc);
    }
    return output;
  }

  private RlpTxnByteCountingOutput ByteCounting(int inputByteLen, int nb_step) {
    /**
     * inputByteLen represents the number of meaningfull byte of inputByte, ie without the zero left
     * padding
     */
    RlpTxnByteCountingOutput output = new RlpTxnByteCountingOutput();

    BigInteger power = BigInteger.ZERO;
    int accByteSize = 0;
    int baseOffset = 16 - nb_step;

    if (inputByteLen == nb_step) {
      power = BigInteger.valueOf((long) Math.pow(256, baseOffset));
      accByteSize = 1;
    } else {
      baseOffset += 1;
      power = BigInteger.valueOf((long) Math.pow(256, baseOffset));
      accByteSize = 0;
    }
    output.PowerList.set(0, power);
    output.AccByteSizeList.set(0, accByteSize);

    for (int i = 1; i < nb_step; i++) {
      if (inputByteLen + i < nb_step) {
        power.multiply(BigInteger.valueOf(256));
      } else {
        accByteSize += 1;
      }
      output.PowerList.set(i, power);
      output.AccByteSizeList.set(i, accByteSize);
    }
    return output;
  }

  private int outerRlpSize(int inputSize) {
    /** Returns the size of RLP(something) where something is of size inputSize (!=1). */
    int rlpSize = inputSize;
    if (inputSize == 1) {
      /** TODO panic */
    } else {
      rlpSize += 1;
      if (inputSize >= 56) {
        rlpSize += Bytes.ofUnsignedShort(inputSize).size();
      }
    }
    return rlpSize;
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
  private void traceRow(RlpTxnColumnsValue data) {
    /** Decrements RLP_BYTESIZE */
    if (data.phase != 0) {
      if (data.LIMB_CONSTRUCTED && data.LT) {
        data.RLP_LT_BYTESIZE -= data.nBYTES;
      }
      if (data.LIMB_CONSTRUCTED && data.LX) {
        data.RLP_LX_BYTESIZE -= data.nBYTES;
      }
    }

    /** Decrement phaseByteSize and accessTupleByteSize for Phase 10 (AccessList) */
    if (data.phase == 10) {
      /** Decreases PhaseByteSize */
      if (data.DEPTH_1 && data.LIMB_CONSTRUCTED) {
        data.PHASE_BYTESIZE -= data.nBYTES;
      }
      /** Decreases AccessTupleSize */
      if (data.DEPTH_1 && !(data.is_prefix && !data.DEPTH_2) && data.LIMB_CONSTRUCTED) {
        data.ACCESS_TUPLE_BYTESIZE -= data.nBYTES;
      }
    }

    this.trace
        .absTxNum(BigInteger.valueOf(data.ABS_TX_NUM))
        .absTxNumInfiny(BigInteger.valueOf(data.ABS_TX_NUM_INFINY))
        .acc1(data.ACC_1)
        .acc2(data.ACC_2)
        .accBytesize(BigInteger.valueOf(data.ACC_BYTESIZE))
        .accessTupleBytesize(BigInteger.valueOf(data.ACCESS_TUPLE_BYTESIZE))
        .addrHi(data.ADDR_HI)
        .addrLo(data.ADDR_LO)
        .bit(data.BIT)
        .bitAcc(UnsignedByte.of(data.BIT_ACC))
        .byte1(UnsignedByte.of(data.BYTE_1))
        .byte2(UnsignedByte.of(data.BYTE_2))
        .codeFragmentIndex(BigInteger.valueOf(data.CODE_FRAGMENT_INDEX))
        .comp(data.COMP)
        .counter(BigInteger.valueOf(data.COUNTER))
        .dataHi(data.DATA_HI)
        .dataLo(data.DATA_LO)
        .datagascost(BigInteger.valueOf(data.DATAGASCOST))
        .depth1(data.DEPTH_1)
        .depth2(data.DEPTH_2);
    if (data.COUNTER == data.number_step - 1) {
      this.trace.done(Boolean.TRUE);
    } else {
      this.trace.done(Boolean.FALSE);
    }
    this.trace
        .endPhase(data.end_phase)
        .indexData(BigInteger.valueOf(data.INDEX_DATA))
        .indexLt(BigInteger.valueOf(data.INDEX_LT))
        .indexLx(BigInteger.valueOf(data.INDEX_LX))
        .input1(data.INPUT_1)
        .input2(data.INPUT_2)
        .isBytesize(data.is_bytesize)
        .isList(data.is_list)
        .isPadding(data.is_padding)
        .isPrefix(data.is_prefix)
        .limb(data.LIMB.shiftLeft(8*(llarge-data.nBYTES)))
        .limbConstructed(data.LIMB_CONSTRUCTED)
        .lt(data.LT)
        .lx(data.LX)
        .nBytes(UnsignedByte.of(data.nBYTES))
        .nbAddr(BigInteger.valueOf(data.nb_Addr))
        .nbSto(BigInteger.valueOf(data.nb_Sto))
        .nbStoPerAddr(BigInteger.valueOf(data.nb_Sto_per_Addr))
        .numberStep(UnsignedByte.of(data.number_step));
    List<Function<Boolean, Trace.TraceBuilder>> phaseColumns =
        List.of(
            this.trace::phase0,
            this.trace::phase1,
            this.trace::phase2,
            this.trace::phase3,
            this.trace::phase4,
            this.trace::phase5,
            this.trace::phase6,
            this.trace::phase7,
            this.trace::phase8,
            this.trace::phase9,
            this.trace::phase10,
            this.trace::phase11,
            this.trace::phase12,
            this.trace::phase13,
            this.trace::phase14);
for(int i = 0; i < phaseColumns.size(); i++) {
  phaseColumns.get(i).apply(i == data.phase);
}
    this.trace
        .phaseBytesize(BigInteger.valueOf(data.PHASE_BYTESIZE))
        .power(data.POWER)
        .requiresEvmExecution(data.REQUIRES_EVM_EXECUTION)
        .rlpLtBytesize(BigInteger.valueOf(data.RLP_LT_BYTESIZE))
        .rlpLxBytesize(BigInteger.valueOf(data.RLP_LX_BYTESIZE))
        .type(UnsignedByte.of(data.TYPE))
        .validateRow();

    /** Increments Index */
    if (data.LIMB_CONSTRUCTED && data.LT) {
      data.INDEX_LT += 1;
    }
    if (data.LIMB_CONSTRUCTED && data.LX) {
      data.INDEX_LX += 1;
    }

    /** Increments IndexData (Phase 9) */
    if (data.phase == 9 && !data.is_prefix && (data.LIMB_CONSTRUCTED || data.is_padding)) {
      data.INDEX_DATA += 1;
    }

    /** Decrements PhaseByteSize and DataGasCost in Data phase (phase 9) */
    if (data.phase == 9) {
      if (data.PHASE_BYTESIZE != 0 && !data.is_prefix) {
        data.PHASE_BYTESIZE -= 1;
        if (data.BYTE_1 == 0) {
          data.DATAGASCOST -= TxnrlpTrace.G_txdatazero.intValue();
        } else {
          data.DATAGASCOST -= TxnrlpTrace.G_txdatanonzero.intValue();
        }
      }
    }
    if (data.end_phase) {
      data.DataHiLoReset();
    }
    this.builder.validateRow();
  }

  @Override
  public Object commit() {
    return new RlpTxnTrace(trace.build());
  }
}
