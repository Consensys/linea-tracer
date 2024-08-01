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

import static net.consensys.linea.zktracer.types.Utils.rightPadTo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.internal.Words;

@Getter
@Accessors(fluent = true)
public class ModExpMetadata {
  static final int EBS_MIN_OFFSET = 32;
  static final int MBS_MIN_OFFSET = 64;
  static final int BASE_MIN_OFFSET = 96;

  private final Bytes callData;
  private final MemorySpan callDataSource;
  @Setter private Bytes rawResult;

  public ModExpMetadata(final Bytes callData, final MemorySpan callDataSource) {
    this.callData = callData;
    this.callDataSource = callDataSource;
  }

  public boolean extractBbs() {
    return !callDataSource.lengthNull();
  }

  public boolean extractEbs() {
    return callDataSource.length() > EBS_MIN_OFFSET;
  }

  public boolean extractMbs() {
    return callDataSource.length() > MBS_MIN_OFFSET;
  }

  private int bbsShift() {
    return EBS_MIN_OFFSET - (int) Math.min(EBS_MIN_OFFSET, callDataSource.length());
  }

  public Bytes rawBbs() {
    return extractBbs()
        ? callData.slice((int) callDataSource.offset(), EBS_MIN_OFFSET)
        : Bytes.EMPTY;
  }

  public EWord bbs() {
    return EWord.of(rawBbs().shiftRight(bbsShift()).shiftLeft(bbsShift()));
  }

  private int ebsShift() {
    return extractEbs()
        ? EBS_MIN_OFFSET - (int) Math.min(EBS_MIN_OFFSET, callDataSource.length() - EBS_MIN_OFFSET)
        : 0;
  }

  public Bytes rawEbs() {
    return extractEbs()
        ? callData.slice((int) (callDataSource.offset() + EBS_MIN_OFFSET), EBS_MIN_OFFSET)
        : Bytes.EMPTY;
  }

  public EWord ebs() {
    return EWord.of(rawEbs().shiftRight(ebsShift()).shiftLeft(ebsShift()));
  }

  private int mbsShift() {
    return extractMbs()
        ? EBS_MIN_OFFSET - (int) Math.min(EBS_MIN_OFFSET, callDataSource.length() - MBS_MIN_OFFSET)
        : 0;
  }

  public Bytes rawMbs() {
    return extractMbs()
        ? callData.slice((int) (callDataSource.offset() + MBS_MIN_OFFSET), EBS_MIN_OFFSET)
        : Bytes.EMPTY;
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
    return callDataSource.length() > BASE_MIN_OFFSET + bbsInt() && !ebs().isZero();
  }

  public EWord rawLeadingWord() {

    return loadRawLeadingWord()
        ? EWord.of(
            callData.slice(
                (int) (callDataSource.offset() + BASE_MIN_OFFSET + bbsInt()), EBS_MIN_OFFSET))
        : EWord.ZERO;
  }

  public boolean extractModulus() {
    return (callDataSource.length() > MBS_MIN_OFFSET + bbsInt() + ebsInt()) && !mbs().isZero();
  }

  public boolean extractBase() {
    return extractModulus() && !bbs().isZero();
  }

  public boolean extractExponent() {
    return extractModulus() && !ebs().isZero();
  }

  public Bytes base() {
    Bytes unpadded = Bytes.EMPTY;
    if (callDataSource.length() >= BASE_MIN_OFFSET) {
      final int sizeToExtract =
          (int) Math.min(bbsInt(), callDataSource().length() - MBS_MIN_OFFSET);
      unpadded = callData.slice(MBS_MIN_OFFSET, sizeToExtract);
    }
    return rightPadTo(unpadded, bbsInt());
  }

  public Bytes exp() {
    Bytes unpadded = Bytes.EMPTY;
    if (callDataSource.length() >= BASE_MIN_OFFSET + bbsInt()) {
      final int sizeToExtract =
          (int) Math.min(ebsInt(), callDataSource().length() - BASE_MIN_OFFSET - bbsInt());
      unpadded = callData.slice(BASE_MIN_OFFSET + bbsInt(), sizeToExtract);
    }
    return rightPadTo(unpadded, ebsInt());
  }

  public Bytes mod() {
    Bytes unpadded = Bytes.EMPTY;
    final int firstOffset = BASE_MIN_OFFSET + bbsInt() + ebsInt();
    if (callDataSource.length() >= firstOffset) {
      final int sizeToExtract = (int) Math.min(mbsInt(), callDataSource().length() - firstOffset);
      unpadded = callData.slice(firstOffset, sizeToExtract);
    }
    return rightPadTo(unpadded, (int) Words.clampedToLong(mbs()));
  }

  public boolean mbsNonZero() {
    return !mbs().isZero();
  }
}
