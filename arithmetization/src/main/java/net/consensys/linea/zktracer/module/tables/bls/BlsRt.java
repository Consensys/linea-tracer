/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.tables.bls;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.Module;
import org.hyperledger.besu.datatypes.Address;

public class BlsRt implements Module {
  @Override
  public String moduleKey() {
    return "BLS_REFERENCE_TABLE";
  }

  @Override
  public void popTransactionBundle() {}

  @Override
  public void commitTransactionBundle() {}

  @Override
  public int lineCount() {
    return 128 * 2;
  }

  @Override
  public int spillage(Trace trace) {
    return trace.blsreftable().spillage();
  }

  @Override
  public List<Trace.ColumnHeader> columnHeaders(Trace trace) {
    return trace.blsreftable().headers(lineCount());
  }

  // TODO: double check
  static final List<Map.Entry<Integer, Integer>> G1_MSM_DISCOUNTS =
      Arrays.<Map.Entry<Integer, Integer>>asList(
          new AbstractMap.SimpleEntry<>(1, 1000),
          new AbstractMap.SimpleEntry<>(2, 949),
          new AbstractMap.SimpleEntry<>(3, 848),
          new AbstractMap.SimpleEntry<>(4, 797),
          new AbstractMap.SimpleEntry<>(5, 764),
          new AbstractMap.SimpleEntry<>(6, 750),
          new AbstractMap.SimpleEntry<>(7, 738),
          new AbstractMap.SimpleEntry<>(8, 728),
          new AbstractMap.SimpleEntry<>(9, 719),
          new AbstractMap.SimpleEntry<>(10, 712),
          new AbstractMap.SimpleEntry<>(11, 705),
          new AbstractMap.SimpleEntry<>(12, 698),
          new AbstractMap.SimpleEntry<>(13, 692),
          new AbstractMap.SimpleEntry<>(14, 687),
          new AbstractMap.SimpleEntry<>(15, 682),
          new AbstractMap.SimpleEntry<>(16, 677),
          new AbstractMap.SimpleEntry<>(17, 673),
          new AbstractMap.SimpleEntry<>(18, 669),
          new AbstractMap.SimpleEntry<>(19, 665),
          new AbstractMap.SimpleEntry<>(20, 661),
          new AbstractMap.SimpleEntry<>(21, 658),
          new AbstractMap.SimpleEntry<>(22, 654),
          new AbstractMap.SimpleEntry<>(23, 651),
          new AbstractMap.SimpleEntry<>(24, 648),
          new AbstractMap.SimpleEntry<>(25, 645),
          new AbstractMap.SimpleEntry<>(26, 642),
          new AbstractMap.SimpleEntry<>(27, 640),
          new AbstractMap.SimpleEntry<>(28, 637),
          new AbstractMap.SimpleEntry<>(29, 635),
          new AbstractMap.SimpleEntry<>(30, 632),
          new AbstractMap.SimpleEntry<>(31, 630),
          new AbstractMap.SimpleEntry<>(32, 627),
          new AbstractMap.SimpleEntry<>(33, 625),
          new AbstractMap.SimpleEntry<>(34, 623),
          new AbstractMap.SimpleEntry<>(35, 621),
          new AbstractMap.SimpleEntry<>(36, 619),
          new AbstractMap.SimpleEntry<>(37, 617),
          new AbstractMap.SimpleEntry<>(38, 615),
          new AbstractMap.SimpleEntry<>(39, 613),
          new AbstractMap.SimpleEntry<>(40, 611),
          new AbstractMap.SimpleEntry<>(41, 609),
          new AbstractMap.SimpleEntry<>(42, 608),
          new AbstractMap.SimpleEntry<>(43, 606),
          new AbstractMap.SimpleEntry<>(44, 604),
          new AbstractMap.SimpleEntry<>(45, 603),
          new AbstractMap.SimpleEntry<>(46, 601),
          new AbstractMap.SimpleEntry<>(47, 599),
          new AbstractMap.SimpleEntry<>(48, 598),
          new AbstractMap.SimpleEntry<>(49, 596),
          new AbstractMap.SimpleEntry<>(50, 595),
          new AbstractMap.SimpleEntry<>(51, 593),
          new AbstractMap.SimpleEntry<>(52, 592),
          new AbstractMap.SimpleEntry<>(53, 591),
          new AbstractMap.SimpleEntry<>(54, 589),
          new AbstractMap.SimpleEntry<>(55, 588),
          new AbstractMap.SimpleEntry<>(56, 586),
          new AbstractMap.SimpleEntry<>(57, 585),
          new AbstractMap.SimpleEntry<>(58, 584),
          new AbstractMap.SimpleEntry<>(59, 582),
          new AbstractMap.SimpleEntry<>(60, 581),
          new AbstractMap.SimpleEntry<>(61, 580),
          new AbstractMap.SimpleEntry<>(62, 579),
          new AbstractMap.SimpleEntry<>(63, 577),
          new AbstractMap.SimpleEntry<>(64, 576),
          new AbstractMap.SimpleEntry<>(65, 575),
          new AbstractMap.SimpleEntry<>(66, 574),
          new AbstractMap.SimpleEntry<>(67, 573),
          new AbstractMap.SimpleEntry<>(68, 572),
          new AbstractMap.SimpleEntry<>(69, 570),
          new AbstractMap.SimpleEntry<>(70, 569),
          new AbstractMap.SimpleEntry<>(71, 568),
          new AbstractMap.SimpleEntry<>(72, 567),
          new AbstractMap.SimpleEntry<>(73, 566),
          new AbstractMap.SimpleEntry<>(74, 565),
          new AbstractMap.SimpleEntry<>(75, 564),
          new AbstractMap.SimpleEntry<>(76, 563),
          new AbstractMap.SimpleEntry<>(77, 562),
          new AbstractMap.SimpleEntry<>(78, 561),
          new AbstractMap.SimpleEntry<>(79, 560),
          new AbstractMap.SimpleEntry<>(80, 559),
          new AbstractMap.SimpleEntry<>(81, 558),
          new AbstractMap.SimpleEntry<>(82, 557),
          new AbstractMap.SimpleEntry<>(83, 556),
          new AbstractMap.SimpleEntry<>(84, 555),
          new AbstractMap.SimpleEntry<>(85, 554),
          new AbstractMap.SimpleEntry<>(86, 553),
          new AbstractMap.SimpleEntry<>(87, 552),
          new AbstractMap.SimpleEntry<>(88, 551),
          new AbstractMap.SimpleEntry<>(89, 550),
          new AbstractMap.SimpleEntry<>(90, 549),
          new AbstractMap.SimpleEntry<>(91, 548),
          new AbstractMap.SimpleEntry<>(92, 547),
          new AbstractMap.SimpleEntry<>(93, 547),
          new AbstractMap.SimpleEntry<>(94, 546),
          new AbstractMap.SimpleEntry<>(95, 545),
          new AbstractMap.SimpleEntry<>(96, 544),
          new AbstractMap.SimpleEntry<>(97, 543),
          new AbstractMap.SimpleEntry<>(98, 542),
          new AbstractMap.SimpleEntry<>(99, 541),
          new AbstractMap.SimpleEntry<>(100, 540),
          new AbstractMap.SimpleEntry<>(101, 540),
          new AbstractMap.SimpleEntry<>(102, 539),
          new AbstractMap.SimpleEntry<>(103, 538),
          new AbstractMap.SimpleEntry<>(104, 537),
          new AbstractMap.SimpleEntry<>(105, 536),
          new AbstractMap.SimpleEntry<>(106, 536),
          new AbstractMap.SimpleEntry<>(107, 535),
          new AbstractMap.SimpleEntry<>(108, 534),
          new AbstractMap.SimpleEntry<>(109, 533),
          new AbstractMap.SimpleEntry<>(110, 532),
          new AbstractMap.SimpleEntry<>(111, 532),
          new AbstractMap.SimpleEntry<>(112, 531),
          new AbstractMap.SimpleEntry<>(113, 530),
          new AbstractMap.SimpleEntry<>(114, 529),
          new AbstractMap.SimpleEntry<>(115, 528),
          new AbstractMap.SimpleEntry<>(116, 528),
          new AbstractMap.SimpleEntry<>(117, 527),
          new AbstractMap.SimpleEntry<>(118, 526),
          new AbstractMap.SimpleEntry<>(119, 525),
          new AbstractMap.SimpleEntry<>(120, 525),
          new AbstractMap.SimpleEntry<>(121, 524),
          new AbstractMap.SimpleEntry<>(122, 523),
          new AbstractMap.SimpleEntry<>(123, 522),
          new AbstractMap.SimpleEntry<>(124, 522),
          new AbstractMap.SimpleEntry<>(125, 521),
          new AbstractMap.SimpleEntry<>(126, 520),
          new AbstractMap.SimpleEntry<>(127, 520),
          new AbstractMap.SimpleEntry<>(128, 519));

  static final List<Map.Entry<Integer, Integer>> G2_MSM_DISCOUNTS =
      Arrays.<Map.Entry<Integer, Integer>>asList(
          new AbstractMap.SimpleEntry<>(1, 1000),
          new AbstractMap.SimpleEntry<>(2, 1000),
          new AbstractMap.SimpleEntry<>(3, 923),
          new AbstractMap.SimpleEntry<>(4, 884),
          new AbstractMap.SimpleEntry<>(5, 855),
          new AbstractMap.SimpleEntry<>(6, 832),
          new AbstractMap.SimpleEntry<>(7, 812),
          new AbstractMap.SimpleEntry<>(8, 796),
          new AbstractMap.SimpleEntry<>(9, 782),
          new AbstractMap.SimpleEntry<>(10, 770),
          new AbstractMap.SimpleEntry<>(11, 759),
          new AbstractMap.SimpleEntry<>(12, 749),
          new AbstractMap.SimpleEntry<>(13, 740),
          new AbstractMap.SimpleEntry<>(14, 732),
          new AbstractMap.SimpleEntry<>(15, 724),
          new AbstractMap.SimpleEntry<>(16, 717),
          new AbstractMap.SimpleEntry<>(17, 711),
          new AbstractMap.SimpleEntry<>(18, 704),
          new AbstractMap.SimpleEntry<>(19, 699),
          new AbstractMap.SimpleEntry<>(20, 693),
          new AbstractMap.SimpleEntry<>(21, 688),
          new AbstractMap.SimpleEntry<>(22, 683),
          new AbstractMap.SimpleEntry<>(23, 679),
          new AbstractMap.SimpleEntry<>(24, 674),
          new AbstractMap.SimpleEntry<>(25, 670),
          new AbstractMap.SimpleEntry<>(26, 666),
          new AbstractMap.SimpleEntry<>(27, 663),
          new AbstractMap.SimpleEntry<>(28, 659),
          new AbstractMap.SimpleEntry<>(29, 655),
          new AbstractMap.SimpleEntry<>(30, 652),
          new AbstractMap.SimpleEntry<>(31, 649),
          new AbstractMap.SimpleEntry<>(32, 646),
          new AbstractMap.SimpleEntry<>(33, 643),
          new AbstractMap.SimpleEntry<>(34, 640),
          new AbstractMap.SimpleEntry<>(35, 637),
          new AbstractMap.SimpleEntry<>(36, 634),
          new AbstractMap.SimpleEntry<>(37, 632),
          new AbstractMap.SimpleEntry<>(38, 629),
          new AbstractMap.SimpleEntry<>(39, 627),
          new AbstractMap.SimpleEntry<>(40, 624),
          new AbstractMap.SimpleEntry<>(41, 622),
          new AbstractMap.SimpleEntry<>(42, 620),
          new AbstractMap.SimpleEntry<>(43, 618),
          new AbstractMap.SimpleEntry<>(44, 615),
          new AbstractMap.SimpleEntry<>(45, 613),
          new AbstractMap.SimpleEntry<>(46, 611),
          new AbstractMap.SimpleEntry<>(47, 609),
          new AbstractMap.SimpleEntry<>(48, 607),
          new AbstractMap.SimpleEntry<>(49, 606),
          new AbstractMap.SimpleEntry<>(50, 604),
          new AbstractMap.SimpleEntry<>(51, 602),
          new AbstractMap.SimpleEntry<>(52, 600),
          new AbstractMap.SimpleEntry<>(53, 598),
          new AbstractMap.SimpleEntry<>(54, 597),
          new AbstractMap.SimpleEntry<>(55, 595),
          new AbstractMap.SimpleEntry<>(56, 593),
          new AbstractMap.SimpleEntry<>(57, 592),
          new AbstractMap.SimpleEntry<>(58, 590),
          new AbstractMap.SimpleEntry<>(59, 589),
          new AbstractMap.SimpleEntry<>(60, 587),
          new AbstractMap.SimpleEntry<>(61, 586),
          new AbstractMap.SimpleEntry<>(62, 584),
          new AbstractMap.SimpleEntry<>(63, 583),
          new AbstractMap.SimpleEntry<>(64, 582),
          new AbstractMap.SimpleEntry<>(65, 580),
          new AbstractMap.SimpleEntry<>(66, 579),
          new AbstractMap.SimpleEntry<>(67, 578),
          new AbstractMap.SimpleEntry<>(68, 576),
          new AbstractMap.SimpleEntry<>(69, 575),
          new AbstractMap.SimpleEntry<>(70, 574),
          new AbstractMap.SimpleEntry<>(71, 573),
          new AbstractMap.SimpleEntry<>(72, 571),
          new AbstractMap.SimpleEntry<>(73, 570),
          new AbstractMap.SimpleEntry<>(74, 569),
          new AbstractMap.SimpleEntry<>(75, 568),
          new AbstractMap.SimpleEntry<>(76, 567),
          new AbstractMap.SimpleEntry<>(77, 566),
          new AbstractMap.SimpleEntry<>(78, 565),
          new AbstractMap.SimpleEntry<>(79, 563),
          new AbstractMap.SimpleEntry<>(80, 562),
          new AbstractMap.SimpleEntry<>(81, 561),
          new AbstractMap.SimpleEntry<>(82, 560),
          new AbstractMap.SimpleEntry<>(83, 559),
          new AbstractMap.SimpleEntry<>(84, 558),
          new AbstractMap.SimpleEntry<>(85, 557),
          new AbstractMap.SimpleEntry<>(86, 556),
          new AbstractMap.SimpleEntry<>(87, 555),
          new AbstractMap.SimpleEntry<>(88, 554),
          new AbstractMap.SimpleEntry<>(89, 553),
          new AbstractMap.SimpleEntry<>(90, 552),
          new AbstractMap.SimpleEntry<>(91, 552),
          new AbstractMap.SimpleEntry<>(92, 551),
          new AbstractMap.SimpleEntry<>(93, 550),
          new AbstractMap.SimpleEntry<>(94, 549),
          new AbstractMap.SimpleEntry<>(95, 548),
          new AbstractMap.SimpleEntry<>(96, 547),
          new AbstractMap.SimpleEntry<>(97, 546),
          new AbstractMap.SimpleEntry<>(98, 545),
          new AbstractMap.SimpleEntry<>(99, 545),
          new AbstractMap.SimpleEntry<>(100, 544),
          new AbstractMap.SimpleEntry<>(101, 543),
          new AbstractMap.SimpleEntry<>(102, 542),
          new AbstractMap.SimpleEntry<>(103, 541),
          new AbstractMap.SimpleEntry<>(104, 541),
          new AbstractMap.SimpleEntry<>(105, 540),
          new AbstractMap.SimpleEntry<>(106, 539),
          new AbstractMap.SimpleEntry<>(107, 538),
          new AbstractMap.SimpleEntry<>(108, 537),
          new AbstractMap.SimpleEntry<>(109, 537),
          new AbstractMap.SimpleEntry<>(110, 536),
          new AbstractMap.SimpleEntry<>(111, 535),
          new AbstractMap.SimpleEntry<>(112, 535),
          new AbstractMap.SimpleEntry<>(113, 534),
          new AbstractMap.SimpleEntry<>(114, 533),
          new AbstractMap.SimpleEntry<>(115, 532),
          new AbstractMap.SimpleEntry<>(116, 532),
          new AbstractMap.SimpleEntry<>(117, 531),
          new AbstractMap.SimpleEntry<>(118, 530),
          new AbstractMap.SimpleEntry<>(119, 530),
          new AbstractMap.SimpleEntry<>(120, 529),
          new AbstractMap.SimpleEntry<>(121, 528),
          new AbstractMap.SimpleEntry<>(122, 528),
          new AbstractMap.SimpleEntry<>(123, 527),
          new AbstractMap.SimpleEntry<>(124, 526),
          new AbstractMap.SimpleEntry<>(125, 526),
          new AbstractMap.SimpleEntry<>(126, 525),
          new AbstractMap.SimpleEntry<>(127, 524),
          new AbstractMap.SimpleEntry<>(128, 524));

  public void commit(Trace trace) {
    for (Map.Entry<Integer, Integer> entry : G1_MSM_DISCOUNTS) {
      int numInputs = entry.getKey();
      int discount = entry.getValue();
      trace
          .blsreftable()
          .prcName(Address.BLS12_G1MULTIEXP.toInt())
          .numInputs(numInputs)
          .discount(discount)
          .validateRow();
    }
    for (Map.Entry<Integer, Integer> entry : G2_MSM_DISCOUNTS) {
      int numInputs = entry.getKey();
      int discount = entry.getValue();
      trace
          .blsreftable()
          .prcName(Address.BLS12_G2MULTIEXP.toInt())
          .numInputs(numInputs)
          .discount(discount)
          .validateRow();
    }
  }
}
