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

package net.consensys.linea.zktracer.module.exp;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXP_INST_EXPLOG;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EXP_INST_MODEXPLOG;

import java.util.Comparator;

import net.consensys.linea.zktracer.module.hub.fragment.imc.exp.ExplogExpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.exp.ModexpLogExpCall;

public class ExpOperationComparator implements Comparator<ExpOperation> {
  @Override
  public int compare(ExpOperation op1, ExpOperation op2) {
    final int instructionComp =
        Integer.compare(op1.expCall().expInstruction(), op2.expCall().expInstruction());
    if (instructionComp != 0) {
      return instructionComp;
    }

    if (op1.expCall.expInstruction() == EXP_INST_EXPLOG) {
      final ExplogExpCall o1 = (ExplogExpCall) op1.expCall();
      final ExplogExpCall o2 = (ExplogExpCall) op2.expCall();

      final int dynCostComp = Long.compare(o1.dynCost(), o2.dynCost());
      if (dynCostComp != 0) {
        return dynCostComp;
      }
      return o1.exponent().compareTo(o2.exponent());
    }

    if (op1.expCall.expInstruction() == EXP_INST_MODEXPLOG) {
      final ModexpLogExpCall o1 = (ModexpLogExpCall) op1.expCall();
      final ModexpLogExpCall o2 = (ModexpLogExpCall) op2.expCall();

      final int cdsCutoffComp = Integer.compare(o1.getCdsCutoff(), o2.getCdsCutoff());
      if (cdsCutoffComp != 0) {
        return cdsCutoffComp;
      }
      final int ebsCutoffComp = Integer.compare(o1.getEbsCutoff(), o2.getEbsCutoff());
      if (ebsCutoffComp != 0) {
        return ebsCutoffComp;
      }
      final int leadLogComp = o1.getLeadLog().compareTo(o2.getLeadLog());
      if (leadLogComp != 0) {
        return leadLogComp;
      }

      return o1.getRawLeadingWord().compareTo(o2.getRawLeadingWord());
    }

    throw new IllegalStateException("Unknown instruction type");
  }
}
