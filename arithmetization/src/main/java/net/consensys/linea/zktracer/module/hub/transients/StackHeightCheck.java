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

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.container.ModuleOperation;

@RequiredArgsConstructor
public class StackHeightCheck extends ModuleOperation {
    final int comparison;

    @Override
    protected int computeLineCount() {
        return 0;
    }

    public StackHeightCheck(int height, int delta) {
        this.comparison = 0;
    }
    public StackHeightCheck(int height, int delta, int alpha) {
        this.comparison = 0;
    }
}
