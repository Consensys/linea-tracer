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

package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.xbsOobCall;

import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToIsZero;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToLT;
import static net.consensys.linea.zktracer.types.Conversions.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.LondonBlakeModexpOperation;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.modexp.ModexpXbsCase;
import net.consensys.linea.zktracer.module.hub.precompiles.modexpMetadata.LondonModexpMetadata;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.oob.OobExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class LondonModexpXbsOobCall extends ModexpXbsOobCall {

  public LondonModexpXbsOobCall(LondonModexpMetadata modexpMetaData, ModexpXbsCase modexpXbsCase) {
    super(modexpMetaData, modexpXbsCase);
  }

  protected LondonModexpMetadata getForkAppropriateModexpMetadata() {
    return (LondonModexpMetadata) this.modexpMetadata;
  }

  public int modexpComponentByteSize() {
    return LondonBlakeModexpOperation.modexpComponentByteSize();
  }

  @Override
  public void setInputData(MessageFrame frame, Hub hub) {}

  @Override
  public void callExoModulesAndSetOutputs(Add add, Mod mod, Wcp wcp) {
    // row i
    final OobExoCall xbsVsModexpComponentByteSize =
        callToLT(wcp, xbs(), Bytes.ofUnsignedInt(modexpComponentByteSize() + 1));
    exoCalls.add(xbsVsModexpComponentByteSize);

    // row i + 1
    final OobExoCall compareXbsYbsCall = callToLT(wcp, xbs().lo(), ybsLo());
    exoCalls.add(compareXbsYbsCall);
    final boolean comp = bytesToBoolean(compareXbsYbsCall.result());
    setMaxXbsYbs(computeMax() ? (comp ? ybsLo() : xbs().lo()) : Bytes.EMPTY);

    // row i + 2
    final OobExoCall xbsNonZerCall = callToIsZero(wcp, xbs().lo());
    exoCalls.add(xbsNonZerCall);
    setXbsNormalizedNonZero(computeMax() ? !bytesToBoolean(xbsNonZerCall.result()) : false);
  }

  @Override
  protected boolean xbsIsWithinBounds() {
    return false;
  }

  @Override
  protected boolean xbsIsOutOfBounds() {
    return false;
  }
}
