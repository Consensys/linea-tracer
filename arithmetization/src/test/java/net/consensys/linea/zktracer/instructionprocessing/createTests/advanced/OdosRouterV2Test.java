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
package net.consensys.linea.zktracer.instructionprocessing.createTests.advanced;

import static net.consensys.linea.zktracer.instructionprocessing.createTests.advanced.ScenarioUtils.*;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;

import java.util.List;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.SmartContractUtils;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.generated.OdosRouterV2;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class OdosRouterV2Test extends TracerTestBase {

  @Test
  void test(TestInfo testInfo) {

    // Account where smart contrat lies
    ToyAccount customAccountSmartContract =
        ToyAccount.builder()
            .address(Address.fromHexString("0x" + buildCustomCreate2Address))
            .balance(defaultBalance)
            .nonce(1)
            .code(SmartContractUtils.getSolidityContractRuntimeByteCode(OdosRouterV2.class))
            .build();

    // create payload to call swapCompact with data extracted by Jeff
    String call1 =
        "0x83bd37f90001176211869ca2b568f2a7d4ee941e073a821ee1ff0001efd81eec32b9a8222d1842ec3d99c7532c31e3480504121f29cc0a1305ee44f48c1f0000001999990001fb66e944021bf915cfb4dac2b20653930bf7d35a000000013f6177ca0b041b1a469f11261f8a8b007633ed48000000001908051b011e9c192403010001010201640106adba920a010003020101018cb0aa0a02000402010102efcb400a02000502010100d35b190a02000602010100ba15a80a020007020101013579f60a02000802010100663ed010020001090201fdb2093a090200020a0125b05d350a03000b020101346123ee0a03000c020101500ffbda0a03000d0201015f0818120a03000e0201017decc2f80a03000f0201000703000110020105a9bf9bc000000504617de60a0412120a00040a0412130a0007d60b1d2b00020608000014150200030003010101160a0196080301010011170196060a0101181901040a01011a1501ff00000000000000000000000000000000eacd56565ab642fb0dc2820b51547fe416ee8697176211869ca2b568f2a7d4ee941e073a821ee1ffb0091b7e234a67182712925ee4ef48c11bce61593cb104f044db23d6513f2a6100a1997fa5e3f587ad98eafc5c6e2e377615a5e78cb3f8738359d5237077f0cff76077d0ebb335b607db574400510557c48622190a6b91d64ee7459c62fade9abe61b48a586733678b9ac9da43dd7cb83bbb41d23677dfc3dded227d71a096c6b5d87807c1b5c456771aaa94e5d7c2a44ffddf6b295a15c148167daaaf5cf34f6e9ad0b8a41e2c148e7b0385d3ecbfdb8a216a9b6a72f4f191720c411cd1ff6a5ea8dedec3a6477135521ec62d91375ac9510d1feefe254b4b582ea05856edf9212bdcec74301ec78afc573b62d6a28392912a1dfbabd89219f79b961b34053260add7c28418e91cf8cbf7dd37b6492e23bec75d0f4d81d897a51baef69335b6248afefebd95e90399d37b0a0180912f869065c7a44617cd4c288be6bce5d1920ed01d983d65a1d624d1942ec268656987750acc8aebffb3964ec5cea0915080ddc1aca079583a4da219439258ca9da29e9cc4ce5596924745e12b935c1bf4b7563c460282617a0304e3cde133200f70b5bedd42000b71fdde22d3ee8a79bd49a568fc8fd732d4a9fe4431fa1b5aab3a0feaae47de186849e4eeb461ad1e4ef8b8ef71a33694ccd84af051c44466ad59cc374162659ba3fd410b1a5092ef48c9000000000000000000000000000000000000000000000000";
    final Bytes swapCompactPayload = OdosRouterV2Payload.swapCompact();

    Transaction transaction =
        ToyTransaction.builder()
            .to(customAccountSmartContract)
            .payload(swapCompactPayload)
            .sender(userAccount)
            .keyPair(keyPair)
            .gasLimit(gasLimit)
            .build();

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customAccountSmartContract))
            .transactions(List.of(transaction))
            .build();
    toyExecutionEnvironmentV2.run();
  }
}
