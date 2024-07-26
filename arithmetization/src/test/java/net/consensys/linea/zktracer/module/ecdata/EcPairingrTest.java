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

package net.consensys.linea.zktracer.module.ecdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.Preconditions;

public class EcPairingrTest {
  // https://github.com/Consensys/linea-arithmetization/issues/822

  // SMALL POINTS
  static final List<Arguments> smallPointsOutOfRange =
      List.of(
          Arguments.of(
              "b3a1cca4c86cdc017597bb5e39705666199eccabc367f8c6aa5e713921c0886e",
              "2018c3b9c5993f6e66a53edf1524775bd337cd82931b44760bdb4e0f557fcf55"),
          Arguments.of(
              "29c5b47fbe82856ac08cfc5e72ee76f9ca909a4360ceafdbb058792b4dea2380",
              "ced846e53d3564f9d059532c94207b64cba68393988bb0bd26711c828f68cd64"),
          Arguments.of(
              "70bf1a09651c7990437f031943fefc89fcf4db43f48c36932e5dce0aad9f747b",
              "5c9d0608b3bd76f3464fef9402ea1cafdf388d58ee6a9bfea0c996ae6357fe7f"),
          Arguments.of(
              "5eaa17cb8e1208585eba5d2b4e836421919b4b79ca16becff6d2e50db4cd0527",
              "1fc12c00d4cbbe5194c82a03fc221a7f79b9fb9184075b109b2d202705920b22"),
          Arguments.of(
              "248ac61b824e9f04836facfaa73b279dca4ba04342af547dcf4d6a3eb45a01b9",
              "54ef148e63803f2e3bbff2b128bc7ffb61cd0ad4ab211f0b0b6df6558cd6ff00"),
          Arguments.of(
              "42603eedeb961906414d6570008db85f5b895a83ab2a26f676b943bfcd8bec4e",
              "3c90ed3461516edfed77ff88f12b03b732dabb51a9a121e9c72d6291f3066252"));

  static final List<Arguments> smallPointsNotOnC1 =
      List.of(
          Arguments.of(
              "1dac6eed25388228c5542dfc2c0b30bd5afbb4fb091791052ad8ca6a8e1c94fb",
              "0b35150d9fe2b8cff4e81fd7445b6d529f68ca798d16618adf0f1d319e772987"));

  static final List<Arguments> smallPointsOnC1 =
      List.of(
          Arguments.of(
              "26d7d8759964ac70b4d5cdf698ad5f70da246752481ea37da637551a60a2a57f",
              "13991eda70bd3bd91c43e7c93f8f89c949bd271ba8f9f5a38bce7248f1f6056b"),
          Arguments.of(
              "1760ca14c35b8978c10ba226b4654e4c925218417ec23731c29da60f481c2c0a",
              "16206149ae732094afbc9921444e04cae6e093a0e73f3212a5e9f93a04f7f07a"),
          Arguments.of(
              "214eb4ed76fa9ea509001fa6d4a1ddc86ac42da639fba6e4956b4045dd74fa26",
              "0a847e7fee1a9b1c6166724ec7eea284fd71506e31371674164f860ac641b0e4"),
          Arguments.of(
              "1cbcc5ae2ad1e062ffbfe7858b0f962d24656075da7f168779afb5167da8e946",
              "1504db20d014e62edf9b2105d8fc6c62919351144cfdd8c6e12c3290d9903a79"),
          Arguments.of(
              "117e59d40acc2608b961101994f76516f4cd4204af85de7d499c2b9043e0d771",
              "2c76b5677261cc2851a41b75f03497ef99522c1c29a4f4d2aba921997cec664a"));

  static final Arguments smallPointInfinity = Arguments.of("0", "0");

  // LARGE POINTS
  static final List<Arguments> largePointsOutOfRange =
      List.of(
          Arguments.of(
              "268294ac35af168840b758c749a19a7b1ffcb3df81e0047f2a4a3a68549fe8b9",
                  "facbb907b18555b9fa34efaa266ab946e1c7eadfe9d8f0cd821e7448e8a8053a",
              "2fe08b1ff6a7fbe4cc3f0bfc309ae33bca35e9b29de0b2b3a73a829f3b9393cf",
                  "1b38210130d62850704dfeeee59e982308d9c976e583ad20a4289145cfcf3b1d"),
          Arguments.of(
              "4dbf7d5e29252a3f871fe6ec7bbb736a82c6a92e1f383988fbf1993fd254535c",
                  "28747a6a24cce55f56d4847a08e45f2b19a575097915bfb39048be362f605ff8",
              "031b86886f00782d83503db96caf1bb0ed6f68a907464fbb5b0de864b80c1ffd",
                  "302ca42cef622575199921b4f82e95009d09aba8e18c2240b9d084ac8bfd27de"),
          Arguments.of(
              "0fefff1654529948ddff9422011555b212236211766ce7674f9de0867248bec0",
              "101dce57329a75b870c3ed82911561dddc38337d9de49c1a201e42097074d358",
              "0b9a97e4531fb7c625ba34bf2939bc22a8ad8b2e03d0bd4cc9def5fdf96f30dc",
              "5786a896704be805b5301789420715b9e6c4608d6bbf593b8c2bdc27b504eb91"),
          Arguments.of(
              "2ab1c49ae7628ad0ef94a9e181a47f7ebbf697173af1587ee152684b98b3b0d5",
              "0efd3c694ab7d704b3f241402397b340a509ba863775d7b8ee1e8c99eddec530",
              "5b2a592225a2ce715a205171f503ae70024f4842b4888407d67e0c580d3459d3",
              "06433c581782ec0072e2185740924ac1fa2cf74905b40a901d3a7dfc7edb10e0"),
          Arguments.of(
              "4ff3dc314cf1a04200904cd987949e253a594fc8f63cd345805fd090b0a9109a",
              "5707bebe515ba78d92d4c9b46b87c861f8886954bc429b68b0f82e89b1519923",
              "4323d04a74d4ebbb7a7223201e31faa21e3bb172d54e177c436a2aaaa2b06fd2",
              "4f47d2affd33161882639176e700137e44be4b847c1202c4a6db1f9837ac285a"));

  static final List<Arguments> largePointsNotOnC2 =
      List.of(
          Arguments.of(
              "1d3df5be6084324da6333a6ad1367091ca9fbceb70179ec484543a58b8cb5d63",
                  "119606e6d3ea97cea4eff54433f5c7dbc026b8d0670ddfbe6441e31225028d31",
              "49fe60975e8c78b7b31a6ed16a338ac8b28cf6a065cfd2ca47e9402882518ba0",
                  "1b9a36ea373fe2c5b713557042ce6deb2907d34e12be595f9bbe84c144de86ef"));

  static final List<Arguments> largePointsNotOnG2 =
      List.of(
          Arguments.of(
              "15ce93f1b1c4946dd6cfbb3d287d9c9a1cdedb264bda7aada0844416d8a47a63",
                  "07192b9fd0e2a32e3e1caa8e59462b757326d48f641924e6a1d00d66478913eb",
              "06e1f5e20f68f6dfa8a91a3bea048df66d9eaf56cc7f11215401f7e05027e0c6",
                  "0fa65a9b48ba018361ed081e3b9e958451de5d9e8ae0bd251833ebb4b2fafc96"));

  static final List<Arguments> largePointsOnG2 =
      List.of(
          Arguments.of(
              "13eb8555f322c3885846df8a505693a0012493a30c34196a529f964a684c0cb2",
                  "18335a998f3db61d3d0b63cd1371789a9a8a5ed4fb1a4adaa20ab9573251a9d0",
              "20494259608bfb4cd04716ba62e1350ce90d00d8af00a5170f46f59ae72d060c",
                  "257a64fbc5a9cf9c3f7be349d09efa099305fe61f364c31c082ef6a81c815c1d"),
          Arguments.of(
              "2f1e9fe1d767c3ee1f801d43e4238a28cb4f94d3e644b2c43e236a503eade386",
                  "14675488459758a6e2f0bebcf5cd4c70c0c6d9733575c999a09418ae773ae6b5",
              "0ae82edaac30d3ff28ea0c4d8e43e39622b47a19beb334a4216994e3a98a796a",
                  "237392d458d5e3a479ebb4feac87c4371150ea86ee61f3268f5b65bca02daa4a"),
          Arguments.of(
              "210c2d4972a76beee46732b60ed998e2e60769847350553f274523a7a9b23d17",
              "0dd0cf966d5a0ef6d560e7dc949b48839e14e26bb8e22d08029b17e1f916de8b",
              "22a8f09fd8c34454c77a7aeb7d7d6e00e160cdf03700c1e503b7e0cabf9aba3c",
              "2090ce9e959dcf086d024dd626111301fd559aab1b4a475bb4c59cd7f598af83"),
          Arguments.of(
              "15746a21d15b2ffb960a9cb93426fc05f20921467c23f89494966d13e1507e87",
              "29670f195cd081b64b28dae6420ad919a7ca8a5c3a0ae5c313420cd4aca339af",
              "056e6142247149ea3505c5adbd6f3d1af9f51d50fdd2cdcd637221ee9ef80a3c",
              "025f9828f38707d5cb94e8905be933d4ae03e7f084e250a2c65f0c856035a2a4"),
          Arguments.of(
              "22007e1404f2d2f0a9b676095daef5b4d49be05df224ef98a2cd00da756900d9",
              "1d8281748ef6cd4dd40149ce6f2afbcc26f3d2499cb484b0a4c21872f9816171",
              "191fc3a7eb19e1f750c5c3dc7a008c8d35dc08feb1de5dd24f293bc2fd84a739",
              "16dca8731c250e792423469ab23a2f7d4891d55d43da021f6ce572f268e2cab9"));

  static final Arguments largePointInfinity = Arguments.of("0", "0", "0", "0");

  static List<Arguments> smallPoints;
  static List<Arguments> largePoints;

  @BeforeAll
  public static void initConcatenetedLists() {
    smallPoints =
        Stream.of(
                smallPointsOutOfRange,
                smallPointsNotOnC1,
                smallPointsOnC1,
                List.of(smallPointInfinity))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    largePoints =
        Stream.of(
                largePointsOutOfRange,
                largePointsNotOnC2,
                largePointsNotOnG2,
                largePointsOnG2,
                List.of(largePointInfinity))
            .flatMap(List::stream)
            .collect(Collectors.toList());
  }

  @Test
  void testEcPairingWithSingleTrivialPairing() {
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @Test
  void testEcPairingWithSingleNonTrivialValidPairing() {
    // small point: (Ax,Ay)
    // large point: (BxRe + i*BxIm, ByRe + i*ByIm)
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            // random point in C1
            .push("26d7d8759964ac70b4d5cdf698ad5f70da246752481ea37da637551a60a2a57f") // Ax
            .push(0)
            .op(OpCode.MSTORE)
            .push("13991eda70bd3bd91c43e7c93f8f89c949bd271ba8f9f5a38bce7248f1f6056b") // Ay
            .push(0x20)
            .op(OpCode.MSTORE)
            // random point in G2
            .push("13eb8555f322c3885846df8a505693a0012493a30c34196a529f964a684c0cb2") // BxIm
            .push(0x40)
            .op(OpCode.MSTORE)
            .push("18335a998f3db61d3d0b63cd1371789a9a8a5ed4fb1a4adaa20ab9573251a9d0") // BxRe
            .push(0x60)
            .op(OpCode.MSTORE)
            .push("20494259608bfb4cd04716ba62e1350ce90d00d8af00a5170f46f59ae72d060c") // ByIm
            .push(0x80)
            .op(OpCode.MSTORE)
            .push("257a64fbc5a9cf9c3f7be349d09efa099305fe61f364c31c082ef6a81c815c1d") // ByRe
            .push(0xA0)
            .op(OpCode.MSTORE)
            // Do the call
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @ParameterizedTest
  @MethodSource("ecPairingSingleSource")
  void testEcPairingSingleForScenario(
      String Ax, String Ay, String BxIm, String BxRe, String ByIm, String ByRe) {
    // small point: (Ax,Ay)
    // large point: (BxRe + i*BxIm, ByRe + i*ByIm)
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            // point supposed to be in C1
            .push(Ax) // Ax
            .push(0)
            .op(OpCode.MSTORE)
            .push(Ay) // Ay
            .push(0x20)
            .op(OpCode.MSTORE)
            // point supposed to be in G2
            .push(BxIm) // BxIm
            .push(0x40)
            .op(OpCode.MSTORE)
            .push(BxRe) // BxRe
            .push(0x60)
            .op(OpCode.MSTORE)
            .push(ByIm) // ByIm
            .push(0x80)
            .op(OpCode.MSTORE)
            .push(ByRe) // ByRe
            .push(0xA0)
            .op(OpCode.MSTORE)
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  private static Stream<Arguments> ecPairingSingleSource() {
    // TODO: some cases can be filtered since redundant
    List<Arguments> arguments = new ArrayList<>();
    for (Arguments smallPoint : smallPoints) {
      for (Arguments largePoint : largePoints) {
        arguments.add(pair(smallPoint, largePoint));
      }
    }
    return arguments.stream();
  }

  // Support methods
  @Test
  public void testPair() {
    Arguments smallPoint = Arguments.of("Ax", "Ay");
    Arguments largePoint = Arguments.of("BxIm", "BxRe", "ByIm", "ByRe");
    Arguments pair = pair(smallPoint, largePoint);
    assertEquals(6, pair.get().length);
    assertEquals("Ax", pair.get()[0]);
    assertEquals("Ay", pair.get()[1]);
    assertEquals("BxIm", pair.get()[2]);
    assertEquals("BxRe", pair.get()[3]);
    assertEquals("ByIm", pair.get()[4]);
    assertEquals("ByRe", pair.get()[5]);
  }

  public static Arguments pair(Arguments smallPointArgs, Arguments largePointArgs) {
    Preconditions.notNull(smallPointArgs, "first argument must not be null");
    Preconditions.notNull(largePointArgs, "second argument must not be null");

    Object[] smallPointArray = smallPointArgs.get();
    Object[] largePointArray = largePointArgs.get();
    Object[] pairArray = new Object[smallPointArray.length + largePointArray.length];

    System.arraycopy(smallPointArray, 0, pairArray, 0, smallPointArray.length);
    System.arraycopy(largePointArray, 0, pairArray, smallPointArray.length, largePointArray.length);

    return Arguments.of(pairArray);
  }
}
