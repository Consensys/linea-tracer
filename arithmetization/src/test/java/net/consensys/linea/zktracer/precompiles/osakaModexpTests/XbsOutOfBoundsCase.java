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
package net.consensys.linea.zktracer.precompiles.osakaModexpTests;

enum XbsOutOfBoundsCase {
  BBS_OUT_OF_BOUNDS,
  EBS_OUT_OF_BOUNDS,
  MBS_OUT_OF_BOUNDS,

  BBS_AND_EBS_OUT_OF_BOUNDS,
  EBS_AND_MBS_OUT_OF_BOUNDS,
  BBS_AND_MBS_OUT_OF_BOUNDS,

  ALL_XBS_OUT_OF_BOUNDS;
}
