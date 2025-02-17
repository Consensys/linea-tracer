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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecrecover;

/**
 * Most values of {@link MemoryContentsParameter} values are self-explanatory. Memory will be
 * made to contain inputs of the form
 *
 * <p><b>[ h | v | r | s ]</b></p>
 *
 * <p> where <b>h</b>, <b>v</b>, <b>r</b> and <b>s</b> are <b>32</b>-byte integers. With the enum
 * we will test in particular:
 *
 * <p>- {@link #MALFORMED_AT_7f_BUT_RECOVERABLE}
 * <p>- {@link #INVALID_V}
 * <p>- {@link #BOUNDARY_R} and {@link #BOUNDARY_S}: cases where <b>r</b> or <b>s</b> are
 * equal to 0 or the secp256k1n prime
 */
public enum MemoryContentsParameter {
    ZEROS,
    WELL_FORMED,
    MALFORMED_AT_7f_BUT_RECOVERABLE,
    INVALID_V,
    BOUNDARY_R,
    BOUNDARY_S,
    RANDOM,
    MAX;
}
