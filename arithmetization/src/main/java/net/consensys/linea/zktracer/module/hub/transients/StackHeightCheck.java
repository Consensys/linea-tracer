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
package net.consensys.linea.zktracer.module.hub.transients;

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.runtime.stack.Stack.MAX_STACK_SIZE;

import lombok.EqualsAndHashCode;
import net.consensys.linea.zktracer.container.ModuleOperation;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class StackHeightCheck extends ModuleOperation {
  static int SHIFT_FACTOR = 11;
  static int LT_SHIFT_FACTOR = SHIFT_FACTOR << 1;
  static int GT_SHIFT_FACTOR = LT_SHIFT_FACTOR + 1;
  static int SHIFTED_MAX_STACK_HEIGHT = MAX_STACK_SIZE << SHIFT_FACTOR;
  static int CHECK_USES_LT = 1 << LT_SHIFT_FACTOR;
  static int CHECK_USES_GT = 1 << GT_SHIFT_FACTOR;

  @EqualsAndHashCode.Include final int comparison;

  @Override
  protected int computeLineCount() {
    return 0;
  }

  public StackHeightCheck(int height, int delta) {
    checkArgument(0 <= height && height <= MAX_STACK_SIZE && 0 <= delta && delta <= 17);
    comparison = CHECK_USES_LT | height << SHIFT_FACTOR | delta;
  }

  public StackHeightCheck(int heightNew) {
    checkArgument(0 <= heightNew && heightNew <= MAX_STACK_SIZE);
    comparison = CHECK_USES_GT | SHIFTED_MAX_STACK_HEIGHT | heightNew;
  }
}
