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
package net.consensys.linea.zktracer;

import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_MAINNET;
import static net.consensys.linea.zktracer.ReplayTooling.replay;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("nightly")
public class NightlyTests {
  // Leo's range split up 5104800-5104883
  ///////////////////////////////////////
  @Test
  void test_5104800_5104809() {
    replay(LINEA_MAINNET, "5104800-5104809.json.gz");
  }

  @Test
  void test_5104810_5104819() {
    replay(LINEA_MAINNET, "5104810-5104819.json.gz");
  }

  @Test
  void test_5104820_5104829() {
    replay(LINEA_MAINNET, "5104820-5104829.json.gz", false);
  }

  @Test
  void test_5104830_5104839() {
    replay(LINEA_MAINNET, "5104830-5104839.json.gz");
  }

  @Test
  void test_5104840_5104849() {
    replay(LINEA_MAINNET, "5104840-5104849.json.gz", false);
  }

  @Test
  void test_5104850_5104859() {
    replay(LINEA_MAINNET, "5104850-5104859.json.gz");
  }

  @Test
  void test_5104860_5104869() {
    replay(LINEA_MAINNET, "5104860-5104869.json.gz");
  }

  @Test
  void test_5104870_5104879() {
    replay(LINEA_MAINNET, "5104870-5104879.json.gz");
  }

  @Test
  void test_5104880_5104883() {
    replay(LINEA_MAINNET, "5104880-5104883.json.gz");
  }

  // Leo's range split up 5105646-5105728
  ///////////////////////////////////////
  @Test
  void test_5105646_5105649() {
    replay(LINEA_MAINNET, "5105646-5105649.json.gz", false);
  }

  @Test
  void test_5105650_5105659() {
    replay(LINEA_MAINNET, "5105650-5105659.json.gz", false);
  }

  @Test
  void test_5105660_5105669() {
    replay(LINEA_MAINNET, "5105660-5105669.json.gz", false);
  }

  @Test
  void test_5105670_5105679() {
    replay(LINEA_MAINNET, "5105670-5105679.json.gz", false);
  }

  @Test
  void test_5105680_5105689() {
    replay(LINEA_MAINNET, "5105680-5105689.json.gz", false);
  }

  @Test
  void test_5105690_5105699() {
    replay(LINEA_MAINNET, "5105690-5105699.json.gz", false);
  }

  @Test
  void test_5105700_5105709() {
    replay(LINEA_MAINNET, "5105700-5105709.json.gz", false);
  }

  @Test
  void test_5105710_5105719() {
    replay(LINEA_MAINNET, "5105710-5105719.json.gz", false);
  }

  @Test
  void test_5105720_5105728() {
    replay(LINEA_MAINNET, "5105720-5105728.json.gz", false);
  }

  // Leo's range split up 5106538-5106638
  @Test
  void test_5106538_5106539() {
    replay(LINEA_MAINNET, "5106538-5106539.json.gz");
  }

  @Test
  void test_5106540_5106549() {
    replay(LINEA_MAINNET, "5106540-5106549.json.gz", false);
  }

  @Test
  void test_5106550_5106559() {
    replay(LINEA_MAINNET, "5106550-5106559.json.gz", false);
  }

  @Test
  void test_5106560_5106569() {
    replay(LINEA_MAINNET, "5106560-5106569.json.gz", false);
  }

  @Test
  void test_5106570_5106579() {
    replay(LINEA_MAINNET, "5106570-5106579.json.gz");
  }

  @Test
  void test_5106580_5106589() {
    replay(LINEA_MAINNET, "5106580-5106589.json.gz");
  }

  @Test
  void test_5106590_5106599() {
    replay(LINEA_MAINNET, "5106590-5106599.json.gz");
  }

  @Test
  void test_5106600_5106609() {
    replay(LINEA_MAINNET, "5106600-5106609.json.gz");
  }

  @Test
  void test_5106610_5106619() {
    replay(LINEA_MAINNET, "5106610-5106619.json.gz", false);
  }

  @Test
  void test_5106620_5106629() {
    replay(LINEA_MAINNET, "5106620-5106629.json.gz", false);
  }

  @Test
  void test_5106630_5106638() {
    replay(LINEA_MAINNET, "5106630-5106638.json.gz", false);
  }

  // Leo's range split up 5118361-5118389
  ///////////////////////////////////////
  @Test
  void test_5118361_5118369() {
    replay(LINEA_MAINNET, "5118361-5118369.json.gz");
  }

  @Test
  void test_5118370_5118379() {
    replay(LINEA_MAINNET, "5118370-5118379.json.gz", false);
  }

  @Test
  void test_5118380_5118389() {
    replay(LINEA_MAINNET, "5118380-5118389.json.gz", false);
  }

  // Florian's ranges
  ///////////////////
  @Test
  void test_6871261_6871263() {
    replay(LINEA_MAINNET, "6871261-6871263.json.gz", false);
  }

  @Test
  void test_6930360_6930360() {
    replay(LINEA_MAINNET, "6930360-6930360.json.gz");
  }

  @Test
  void test_7040245_7040246() {
    replay(LINEA_MAINNET, "7040245-7040246.json.gz");
  }

  @Test
  void test_7037321_7037321() {
    replay(LINEA_MAINNET, "7037321-7037321.json.gz");
  }

  @Test
  void test_7037237_7037243() {
    replay(LINEA_MAINNET, "7037237-7037243.json.gz", false);
  }

  @Test
  void test_7037244_7037244() {
    replay(LINEA_MAINNET, "7037244-7037244.json.gz");
  }

  @Test
  void test_7032685_7032688() {
    replay(LINEA_MAINNET, "7032685-7032688.json.gz");
  }

  @Test
  void test_7032397_7032402() {
    replay(LINEA_MAINNET, "7032397-7032402.json.gz");
  }
}
