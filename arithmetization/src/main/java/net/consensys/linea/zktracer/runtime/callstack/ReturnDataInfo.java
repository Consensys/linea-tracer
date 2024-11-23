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
package net.consensys.linea.zktracer.runtime.callstack;

import lombok.Getter;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

@Getter
public class ReturnDataInfo {

    private final Bytes data;
    private final MemorySpan memorySpan;
    private final long returnDataContextNumber;

    private static final ReturnDataInfo EMPTY = new ReturnDataInfo(Bytes.EMPTY, 0, 0, 0);

    /** This method could be problematic: we may want to remember the {@link #returnDataContextNumber} */
    public static ReturnDataInfo empty() {
        return EMPTY;
    }

    public static ReturnDataInfo empty(final long returnDataContextNumber) {
        return new ReturnDataInfo(Bytes.EMPTY, 0, 0, returnDataContextNumber);
    }

    public static ReturnDataInfo standard(final Bytes data, final long returnDataContextNumber) {
        return new ReturnDataInfo(data, 0, data.size(), returnDataContextNumber);
    }

    /** Recall the acronym mbs ≡ modulus byte size */
    public static ReturnDataInfo forModExp(final Bytes unpaddedOutputData, final int mbs, final long returnDataContextNumber) {
        checkArgument(0 < mbs && mbs <= 512);
        checkArgument(unpaddedOutputData.size() <= mbs);

        Bytes leftPaddedReturnData = leftPadTo(unpaddedOutputData, 512);
        return new ReturnDataInfo(leftPaddedReturnData, 512 - mbs, mbs, returnDataContextNumber);
    }

    public ReturnDataInfo(
        final Bytes data,
        final long returnDataOffset,
        final long returnDataSize,
        final long returnDataContextNumber) {
        this.data = data;
        this.memorySpan = new MemorySpan(returnDataOffset, returnDataSize);
        this.returnDataContextNumber = returnDataContextNumber;
    }

    /** RDO is short for Return Data Offset */
    public long rdo() {
        return memorySpan.offset();
    }

    /** RDS is short for Return Data Size */
    public long rds() {
        return memorySpan.length();
    }
}
