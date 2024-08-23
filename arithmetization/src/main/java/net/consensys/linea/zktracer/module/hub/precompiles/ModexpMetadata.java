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

package net.consensys.linea.zktracer.module.hub.precompiles;

import static net.consensys.linea.zktracer.module.txndata.Trace.WORD_SIZE;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.Util;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.internal.Words;

@Getter
@Accessors(fluent = true)
public class ModexpMetadata {
  static final int BBS_MIN_OFFSET = 0x00;
  static final int EBS_MIN_OFFSET = 0x20;
  static final int MBS_MIN_OFFSET = 0x40;
  static final int BASE_MIN_OFFSET = 0x60;

  private final Bytes callData;
  private final EWord rawLeadingWord;
  @Setter private Bytes rawResult;

  public ModexpMetadata(final Hub hub, final Bytes callData) {
    this.callData = callData;
    int exponentOffsetInCallData = BASE_MIN_OFFSET + bbsInt();
    this.rawLeadingWord = EWord.of(hub.messageFrame().shadowReadMemory(exponentOffsetInCallData, WORD_SIZE));
  }

  public boolean extractBbs() {
    return !callData.isEmpty();
  }

  public boolean extractEbs() {
    return callData.size() > EBS_MIN_OFFSET;
  }

  public boolean extractMbs() {
    return callData.size() > MBS_MIN_OFFSET;
  }

  private int bbsShift() {
    return EBS_MIN_OFFSET - Math.min(EBS_MIN_OFFSET, callData.size());
  }

  public Bytes rawBbs() { return Util.slice(callData, BBS_MIN_OFFSET, WORD_SIZE); }
  public Bytes rawEbs() { return Util.slice(callData, EBS_MIN_OFFSET, WORD_SIZE); }
  public Bytes rawMbs() { return Util.slice(callData, MBS_MIN_OFFSET, WORD_SIZE); }

  public EWord bbs() {
    return EWord.of(rawBbs().shiftRight(bbsShift()).shiftLeft(bbsShift()));
  }

  private int ebsShift() {
    return extractEbs()
        ? EBS_MIN_OFFSET - Math.min(EBS_MIN_OFFSET, callData.size() - EBS_MIN_OFFSET)
        : 0;
  }


  public EWord ebs() {
    return EWord.of(rawEbs().shiftRight(ebsShift()).shiftLeft(ebsShift()));
  }

  private int mbsShift() {
    return extractMbs()
        ? EBS_MIN_OFFSET - Math.min(EBS_MIN_OFFSET, callData.size() - MBS_MIN_OFFSET)
        : 0;
  }

  public EWord mbs() {
    return EWord.of(rawMbs().shiftRight(mbsShift()).shiftLeft(mbsShift()));
  }

  public int bbsInt() {
    return (int) Words.clampedToLong(bbs());
  }

  public int ebsInt() {
    return (int) Words.clampedToLong(ebs());
  }

  public int mbsInt() {
    return (int) Words.clampedToLong(mbs());
  }

  public boolean loadRawLeadingWord() {
    return callData.size() > BASE_MIN_OFFSET + bbsInt() && !ebs().isZero();
  }

  public EWord rawLeadingWord() {

    // if (!loadRawLeadingWord()) {
    //   return EWord.ZERO;
    // }

    // TODO: is this precaution useless / dangerous ?
    Preconditions.checkArgument(loadRawLeadingWord());
    return this.rawLeadingWord;

    // int exponentOffsetInCallData = BASE_MIN_OFFSET + bbsInt();

    // return EWord.of(hub.messageFrame().shadowReadMemory(exponentOffsetInCallData, WORD_SIZE));

    // int numberOfBytesToGrabFromCallData =
    //     (exponentOffsetInCallData + WORD_SIZE <= callData.size())
    //         ? WORD_SIZE
    //         : callData.size() - exponentOffsetInCallData;
    // Preconditions.checkArgument(0 < numberOfBytesToGrabFromCallData);

    // return EWord.of(
    //     Bytes32.rightPad(
    //         callData.slice(BASE_MIN_OFFSET + bbsInt(), numberOfBytesToGrabFromCallData)));

  }

  public boolean extractModulus() {
    return (callData.size() > MBS_MIN_OFFSET + bbsInt() + ebsInt()) && !mbs().isZero();
  }

  public boolean extractBase() {
    return extractModulus() && !bbs().isZero();
  }

  public boolean extractExponent() {
    return extractModulus() && !ebs().isZero();
  }

  public Bytes base() {
    Bytes unpadded = Bytes.EMPTY;
    if (callData.size() >= BASE_MIN_OFFSET) {
      final int sizeToExtract = Math.min(bbsInt(), callData.size() - MBS_MIN_OFFSET);
      unpadded = callData.slice(MBS_MIN_OFFSET, sizeToExtract);
    }
    return rightPadTo(unpadded, bbsInt());
  }

  public Bytes exp() {
    Bytes unpadded = Bytes.EMPTY;
    if (callData.size() >= BASE_MIN_OFFSET + bbsInt()) {
      final int sizeToExtract = Math.min(ebsInt(), callData.size() - BASE_MIN_OFFSET - bbsInt());
      unpadded = callData.slice(BASE_MIN_OFFSET + bbsInt(), sizeToExtract);
    }
    return rightPadTo(unpadded, ebsInt());
  }

  public Bytes mod() {
    Bytes unpadded = Bytes.EMPTY;
    final int firstOffset = BASE_MIN_OFFSET + bbsInt() + ebsInt();
    if (callData.size() >= firstOffset) {
      final int sizeToExtract = Math.min(mbsInt(), callData.size() - firstOffset);
      unpadded = callData.slice(firstOffset, sizeToExtract);
    }
    return rightPadTo(unpadded, (int) Words.clampedToLong(mbs()));
  }

  public boolean mbsNonZero() {
    return !mbs().isZero();
  }
}
