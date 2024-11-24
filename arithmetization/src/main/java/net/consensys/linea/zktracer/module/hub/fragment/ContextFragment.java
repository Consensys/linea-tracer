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

package net.consensys.linea.zktracer.module.hub.fragment;

import static net.consensys.linea.zktracer.types.AddressUtils.highPart;
import static net.consensys.linea.zktracer.types.AddressUtils.lowPart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.Either;
import net.consensys.linea.zktracer.types.MemoryRange;
import org.hyperledger.besu.datatypes.Address;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class ContextFragment implements TraceFragment {
  private final Hub hub;
  private final Either<Integer, Integer> callFrameReference;
  private final MemoryRange returnDataRange;
  private final boolean updateReturnData;

  private static ContextFragment readContextData(final Hub hub, final Either<Integer, Integer> callFrameReference) {
    final CallFrame callFrame = callFrameReference.map(hub.callStack()::getById, hub.callStack()::getByContextNumber);
    return new ContextFragment(hub, callFrameReference, callFrame.returnDataRange().deepCopy(), false);
  }

  public static ContextFragment readContextDataById(final Hub hub, final int contextId) {
    return readContextData( hub, Either.left(contextId));
  }

  public static ContextFragment readCurrentContextData(final Hub hub) {
    return readContextData(hub, Either.left(hub.currentFrame().id()));
  }

  public static ContextFragment initializeNewExecutionContext(final Hub hub) {
    return new ContextFragment(
        hub, Either.right(hub.newChildContextNumber()), MemoryRange.EMPTY, false);
  }

  public static ContextFragment executionProvidesEmptyReturnData(final Hub hub, int contextNumber) {
    int parentId = hub.callStack().getByContextNumber(contextNumber).parentId();
    return new ContextFragment(
            hub, Either.left(parentId), new MemoryRange(contextNumber), true);
  }

  public static ContextFragment executionProvidesEmptyReturnData(final Hub hub) {
    int currentContextNumber = hub.currentFrame().contextNumber();
    return executionProvidesEmptyReturnData(hub, currentContextNumber);
  }

  public static ContextFragment executionProvidesReturnData(final Hub hub, MemoryRange returnDataRange) {
    int parentId = hub.callStack().currentCallFrame().parentId();
    return new ContextFragment(
        hub,
        Either.left(parentId),
        returnDataRange,
        true);
    // TODO: is this what we want ?
    //  also: will the latestReturnData have been updated ?
  }

  public static ContextFragment nonExecutionProvidesEmptyReturnData(final Hub hub) {
    CallStack callStack = hub.callStack();
    return new ContextFragment(
            hub,
            Either.left(callStack.currentCallFrame().id()),
            new MemoryRange(hub.newChildContextNumber()),
            true);
  }

  public static ContextFragment updateCurrentReturnData(
      final Hub hub, final MemoryRange returnDataRange) {
    return new ContextFragment(
        hub,
        Either.right(hub.currentFrame().contextNumber()),
        returnDataRange,
        true);
  }

  @Override
  public Trace trace(Trace trace) {
    final CallFrame callFrame =
        this.callFrameReference.map(hub.callStack()::getById, hub.callStack()::getByContextNumber);

    final Address address = callFrame.accountAddress();
    final Address codeAddress = callFrame.byteCodeAddress();
    final Address callerAddress = callFrame.callerAddress();

    return trace
        .peekAtContext(true)
        .pContextContextNumber(callFrame.contextNumber())
        .pContextCallStackDepth((short) callFrame.depth())
        .pContextIsRoot(callFrame.isRoot())
        .pContextIsStatic(callFrame.type().isStatic())
        .pContextAccountAddressHi(highPart(address))
        .pContextAccountAddressLo(lowPart(address))
        .pContextAccountDeploymentNumber(callFrame.accountDeploymentNumber())
        .pContextByteCodeAddressHi(highPart(codeAddress))
        .pContextByteCodeAddressLo(lowPart(codeAddress))
        .pContextByteCodeDeploymentNumber(callFrame.byteCodeDeploymentNumber())
        .pContextByteCodeDeploymentStatus(callFrame.isDeployment() ? 1 : 0)
        .pContextByteCodeCodeFragmentIndex(callFrame.getCodeFragmentIndex(hub))
        .pContextCallerAddressHi(highPart(callerAddress))
        .pContextCallerAddressLo(lowPart(callerAddress))
        .pContextCallValue(callFrame.value())
        .pContextCallDataContextNumber(callFrame.callDataRange().contextNumber())
        .pContextCallDataOffset(callFrame.callDataRange().offset())
        .pContextCallDataSize(callFrame.callDataRange().size())
        .pContextReturnAtOffset(callFrame.returnAtRange().offset())
        .pContextReturnAtCapacity(callFrame.returnAtRange().size())
        .pContextUpdate(updateReturnData)
        .pContextReturnDataContextNumber(returnDataRange.contextNumber())
        .pContextReturnDataOffset(returnDataRange.offset())
        .pContextReturnDataSize(returnDataRange.size());
  }
}
