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

package net.consensys.linea.zktracer.module.stp;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
record StpTrace(@JsonProperty("Trace") Trace trace) {
  static final BigInteger CALL = new BigInteger("241");
  static final BigInteger CALLCODE = new BigInteger("242");
  static final BigInteger CREATE = new BigInteger("240");
  static final BigInteger CREATE2 = new BigInteger("245");
  static final BigInteger DELEGATECALL = new BigInteger("244");
  static final BigInteger GCALL_STIPEND = new BigInteger("2300");
  static final BigInteger OPCODE_DIV = new BigInteger("4");
  static final BigInteger OPCODE_ISZERO = new BigInteger("21");
  static final BigInteger OPCODE_LT = new BigInteger("16");
  static final BigInteger STATICCALL = new BigInteger("250");
}
