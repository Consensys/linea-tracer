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
import java.util.stream.Stream;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.Preconditions;

public class EcPairingrTest {
  @Test
  void testEcPairingWithTrivialPairing() {
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
  void testEcPairingWithMinimalNonTrivialValidPairing() {
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
  @MethodSource("ecPairingSource")
  void testEcPairingForScenario(
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

  private static Stream<Arguments> ecPairingSource() {
    // TODO: add other cases based on https://github.com/Consensys/linea-arithmetization/issues/822

    // SMALL POINTS
    List<Arguments> smallPointsOutOfRange =
        List.of(
            Arguments.of(
                "0xb3a1cca4c86cdc017597bb5e39705666199eccabc367f8c6aa5e713921c0886e",
                "0x2018c3b9c5993f6e66a53edf1524775bd337cd82931b44760bdb4e0f557fcf55"),
            Arguments.of(
                "0x29c5b47fbe82856ac08cfc5e72ee76f9ca909a4360ceafdbb058792b4dea2380",
                "0xced846e53d3564f9d059532c94207b64cba68393988bb0bd26711c828f68cd64"));

    List<Arguments> smallPointsNotOnC1 =
        List.of(
            Arguments.of(
                "0x1dac6eed25388228c5542dfc2c0b30bd5afbb4fb091791052ad8ca6a8e1c94fb",
                "0x0b35150d9fe2b8cff4e81fd7445b6d529f68ca798d16618adf0f1d319e772987"));

    List<Arguments> smallPointsOnC1 =
        List.of(
            Arguments.of(
                "0x26d7d8759964ac70b4d5cdf698ad5f70da246752481ea37da637551a60a2a57f",
                "0x13991eda70bd3bd91c43e7c93f8f89c949bd271ba8f9f5a38bce7248f1f6056b"),
            Arguments.of(
                "0x1760ca14c35b8978c10ba226b4654e4c925218417ec23731c29da60f481c2c0a",
                "0x16206149ae732094afbc9921444e04cae6e093a0e73f3212a5e9f93a04f7f07a"));

    // LARGE POINTS
    List<Arguments> largePointsOutOfRange =
        List.of(
            Arguments.of(
                "0xfacbb907b18555b9fa34efaa266ab946e1c7eadfe9d8f0cd821e7448e8a8053a",
                "0x268294ac35af168840b758c749a19a7b1ffcb3df81e0047f2a4a3a68549fe8b9",
                "0x1b38210130d62850704dfeeee59e982308d9c976e583ad20a4289145cfcf3b1d",
                "0x2fe08b1ff6a7fbe4cc3f0bfc309ae33bca35e9b29de0b2b3a73a829f3b9393cf"),
            Arguments.of(
                "0x28747a6a24cce55f56d4847a08e45f2b19a575097915bfb39048be362f605ff8",
                "0x4dbf7d5e29252a3f871fe6ec7bbb736a82c6a92e1f383988fbf1993fd254535c",
                "0x302ca42cef622575199921b4f82e95009d09aba8e18c2240b9d084ac8bfd27de",
                "0x031b86886f00782d83503db96caf1bb0ed6f68a907464fbb5b0de864b80c1ffd"));

    List<Arguments> largePointsNotOnC2 =
        List.of(
            Arguments.of(
                "0x119606e6d3ea97cea4eff54433f5c7dbc026b8d0670ddfbe6441e31225028d31",
                "0x1d3df5be6084324da6333a6ad1367091ca9fbceb70179ec484543a58b8cb5d63",
                "0x1b9a36ea373fe2c5b713557042ce6deb2907d34e12be595f9bbe84c144de86ef",
                "0x49fe60975e8c78b7b31a6ed16a338ac8b28cf6a065cfd2ca47e9402882518ba0"));

    List<Arguments> largePointsNotOnG2 =
        List.of(
            Arguments.of(
                "0x07192b9fd0e2a32e3e1caa8e59462b757326d48f641924e6a1d00d66478913eb",
                "0x15ce93f1b1c4946dd6cfbb3d287d9c9a1cdedb264bda7aada0844416d8a47a63",
                "0x0fa65a9b48ba018361ed081e3b9e958451de5d9e8ae0bd251833ebb4b2fafc96",
                "0x06e1f5e20f68f6dfa8a91a3bea048df66d9eaf56cc7f11215401f7e05027e0c6"));

    List<Arguments> largePointsOnG2 =
        List.of(
            Arguments.of(
                "0x18335a998f3db61d3d0b63cd1371789a9a8a5ed4fb1a4adaa20ab9573251a9d0",
                "0x13eb8555f322c3885846df8a505693a0012493a30c34196a529f964a684c0cb2",
                "0x257a64fbc5a9cf9c3f7be349d09efa099305fe61f364c31c082ef6a81c815c1d",
                "0x20494259608bfb4cd04716ba62e1350ce90d00d8af00a5170f46f59ae72d060c"),
            Arguments.of(
                "0x14675488459758a6e2f0bebcf5cd4c70c0c6d9733575c999a09418ae773ae6b5",
                "0x2f1e9fe1d767c3ee1f801d43e4238a28cb4f94d3e644b2c43e236a503eade386",
                "0x237392d458d5e3a479ebb4feac87c4371150ea86ee61f3268f5b65bca02daa4a",
                "0x0ae82edaac30d3ff28ea0c4d8e43e39622b47a19beb334a4216994e3a98a796a"));

    // TODO: continue from here
    List<Arguments> arguments = new ArrayList<>();

    arguments.add(
        Arguments.of(
            "26d7d8759964ac70b4d5cdf698ad5f70da246752481ea37da637551a60a2a57f",
            "13991eda70bd3bd91c43e7c93f8f89c949bd271ba8f9f5a38bce7248f1f6056b",
            "13eb8555f322c3885846df8a505693a0012493a30c34196a529f964a684c0cb2",
            "18335a998f3db61d3d0b63cd1371789a9a8a5ed4fb1a4adaa20ab9573251a9d0",
            "20494259608bfb4cd04716ba62e1350ce90d00d8af00a5170f46f59ae72d060c",
            "257a64fbc5a9cf9c3f7be349d09efa099305fe61f364c31c082ef6a81c815c1d"));
    return arguments.stream();
  }

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
