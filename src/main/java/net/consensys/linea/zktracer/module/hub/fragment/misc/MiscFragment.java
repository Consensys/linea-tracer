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

package net.consensys.linea.zktracer.module.hub.fragment.misc;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.EWord;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.opcode.InstructionFamily;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class MiscFragment implements TraceFragment {
  private final boolean mmuFlag;
  private final boolean mxpFlag;
  private final boolean oobFlag;
  private final boolean precInfoFlag;
  private final boolean stpFlag;
  private final boolean expFlag;

  private final List<TraceSubFragment> subFragments = new ArrayList<>();

  private static boolean canMmu(Hub hub, OpCodeData opCodeData) {
    return opCodeData.ramSettings().enabled() && hub.exceptions().none();
  }

  private static boolean canMxp(Hub hub, OpCodeData opCodeData) {
    boolean mxpFlag = false;
    if (opCodeData.isMxp()) {
      mxpFlag =
          switch (opCodeData.instructionFamily()) {
            case CALL, CREATE, LOG -> !hub.exceptions().stackOverflow()
                || !hub.exceptions().staticViolation();
            default -> false;
          };
    }
    return mxpFlag;
  }

  private static boolean canOob(Hub hub, OpCodeData opCodeData) {
    boolean oobFlag = false;

    if (opCodeData.stackSettings().oobFlag()) {
      if (opCodeData.mnemonic() == OpCode.CALLDATALOAD) {
        oobFlag = !hub.exceptions().stackUnderflow();
      } else if (opCodeData.instructionFamily() == InstructionFamily.JUMP) {
        oobFlag = !hub.exceptions().stackUnderflow();
      } else if (opCodeData.mnemonic() == OpCode.RETURNDATACOPY) {
        oobFlag = !hub.exceptions().stackUnderflow(); // TODO: update it
      } else if (opCodeData.instructionFamily() == InstructionFamily.CALL) {
        oobFlag = !hub.exceptions().any();
      } else if (opCodeData.instructionFamily() == InstructionFamily.CREATE) {
        oobFlag = !hub.exceptions().any();
      } else if (opCodeData.mnemonic() == OpCode.SSTORE) {
        oobFlag = !hub.exceptions().stackUnderflow() && !hub.exceptions().staticViolation();
      } else if (opCodeData.mnemonic() == OpCode.RETURN) {
        oobFlag =
            !hub.exceptions().stackUnderflow()
                && hub.currentFrame().codeDeploymentStatus(); // TODO: see for the rest
      }
    }

    return oobFlag;
  }

  private static boolean canPrecinfo(Hub hub, OpCodeData opCodeData) {
    boolean precInfoFlag = false;

    if (opCodeData.instructionFamily() == InstructionFamily.CALL) {
      precInfoFlag =
          !hub.exceptions()
              .any(); // TODO:  && no abort(assez de balance && CSD < 1024) && to precompile
    }

    return precInfoFlag;
  }

  private static boolean canStp(Hub hub, OpCodeData opCodeData) {
    boolean stpFlag = false;

    if (opCodeData.instructionFamily() == InstructionFamily.CALL) {
      stpFlag =
          !hub.exceptions().stackUnderflow()
              && !hub.exceptions().staticViolation()
              && !hub.exceptions().outOfMemoryExpansion();
    } else if (opCodeData.instructionFamily() == InstructionFamily.CREATE) {
      stpFlag = false; // TODO:
    }

    return stpFlag;
  }

  private static boolean canExp(Hub hub, OpCodeData opCodeData) {
    return opCodeData.mnemonic() == OpCode.EXP && !hub.exceptions().stackUnderflow();
  }

  public MiscFragment(
      Hub hub,
      MessageFrame frame,
      boolean wantMmu,
      boolean wantMxp,
      boolean wantOob,
      boolean wantPrecinfo,
      boolean wantStp,
      boolean wantExp) {
    final OpCodeData opCodeData = hub.currentFrame().opCode().getData();

    this.mmuFlag = wantMmu && canMmu(hub, opCodeData);
    this.mxpFlag = wantMxp && canMxp(hub, opCodeData);
    this.oobFlag = wantOob && canOob(hub, opCodeData);
    this.precInfoFlag = wantPrecinfo && canPrecinfo(hub, opCodeData);
    this.stpFlag = wantStp && canStp(hub, opCodeData);
    this.expFlag = wantExp && canExp(hub, opCodeData);

    // TODO: the rest
    if (this.expFlag) {
      this.subFragments.add(new MiscExpSubFragment(EWord.of(frame.getStackItem(1))));
    }
  }

  @Override
  public Trace.TraceBuilder trace(Trace.TraceBuilder trace) {
    trace
        .peekAtMiscellaneous(true)
        .pMiscellaneousMmuFlag(mmuFlag)
        .pMiscellaneousMxpFlag(mxpFlag)
        .pMiscellaneousOobFlag(oobFlag)
        .pMiscellaneousPrecinfoFlag(precInfoFlag)
        .pMiscellaneousStpFlag(stpFlag)
        .pMiscellaneousExpFlag(expFlag);

    for (TraceSubFragment subFragment : this.subFragments) {
      subFragment.trace(trace);
    }

    return trace;
  }
}
