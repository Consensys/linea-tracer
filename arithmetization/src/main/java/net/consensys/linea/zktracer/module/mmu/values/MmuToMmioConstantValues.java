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

package net.consensys.linea.zktracer.module.mmu.values;

import lombok.Builder;

@Builder
public record MmuToMmioConstantValues(
    long sourceContextNumber,
    long targetContextNumber,
    boolean successBit,
    int exoSum,
    boolean exoIsRom,
    boolean exoIsBlake2fModexp,
    boolean exoIsEcData,
    boolean exoIsRipSha,
    boolean exoIsKeccak,
    boolean exoIsLog,
    boolean exoIsTxcd,
    int phase,
    int exoId,
    int kecId,
    long totalSize) {}
