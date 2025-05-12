/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.types.Conversions.*;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

@Builder
@Getter
@Accessors(fluent = true)
public class MxpExoCall {

  @Builder.Default private final boolean wcpFlag = false;
  @Builder.Default private final boolean eucFlag = false;
  @Builder.Default private final int instruction = 0;
  @Builder.Default private final Bytes arg1Hi = Bytes.EMPTY;
  @Builder.Default private final Bytes arg1Lo = Bytes.EMPTY;
  @Builder.Default private final Bytes arg2Hi = Bytes.EMPTY;
  @Builder.Default private final Bytes arg2Lo = Bytes.EMPTY;
  // results for wcp computations in trace
  @Builder.Default private final boolean resultA = false;
  // results for euc computations in trace
  @Builder.Default private final Bytes resultB = ZERO;

  public static MxpExoCall callToLT(final Wcp wcp, Bytes arg1, Bytes arg2) {

    final Bytes32 arg1B32 = Bytes32.leftPad(arg1);
    final Bytes32 arg2B32 = Bytes32.leftPad(arg2);

    return MxpExoCall.builder()
        .wcpFlag(true)
        .instruction(EVM_INST_LT)
        .arg1Hi(arg1B32.slice(0, LLARGE))
        .arg1Lo(arg1B32.slice(LLARGE, LLARGE))
        .arg2Hi(arg2B32.slice(0, LLARGE))
        .arg2Lo(arg2B32.slice(LLARGE, LLARGE))
        .resultA(wcp.callLT(arg1B32, arg2B32))
        .build();
  }

  public static MxpExoCall callToLEQ(final Wcp wcp, Bytes arg1, Bytes arg2) {

    final Bytes32 arg1B32 = Bytes32.leftPad(arg1);
    final Bytes32 arg2B32 = Bytes32.leftPad(arg2);

    return MxpExoCall.builder()
        .wcpFlag(true)
        .instruction(WCP_INST_LEQ)
        .arg1Hi(arg1B32.slice(0, LLARGE))
        .arg1Lo(arg1B32.slice(LLARGE, LLARGE))
        .arg2Hi(arg2B32.slice(0, LLARGE))
        .arg2Lo(arg2B32.slice(LLARGE, LLARGE))
        .resultA(wcp.callLT(arg1B32, arg2B32))
        .build();
  }

  public static MxpExoCall callToIsZero(final Wcp wcp, Bytes arg1) {

    final Bytes32 arg1B32 = Bytes32.leftPad(arg1);

    return MxpExoCall.builder()
        .wcpFlag(true)
        .instruction(EVM_INST_ISZERO)
        .arg1Hi(arg1B32.slice(0, LLARGE))
        .arg1Lo(arg1B32.slice(LLARGE, LLARGE))
        .resultA(wcp.callISZERO(arg1B32))
        .build();
  }

  public static MxpExoCall callToEUC(final Euc euc, Bytes arg1, Bytes arg2) {

    final Bytes32 arg1B32 = Bytes32.leftPad(arg1);
    final Bytes32 arg2B32 = Bytes32.leftPad(arg2);

    return MxpExoCall.builder()
        .eucFlag(true)
        .arg1Hi(arg1B32.slice(0, LLARGE))
        .arg1Lo(arg1B32.slice(LLARGE, LLARGE))
        .arg2Hi(arg2B32.slice(0, LLARGE))
        .arg2Lo(arg2B32.slice(LLARGE, LLARGE))
        .resultB(euc.callEUC(arg1B32, arg2B32).quotient())
        .build();
  }
}
