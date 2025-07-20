package net.consensys.linea.zktracer.module.bls;

import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_0;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_1;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_2;
import static net.consensys.linea.zktracer.TraceCancun.Bls.BLS_PRIME_3;
import static net.consensys.linea.zktracer.TraceCancun.Bls.POINT_EVALUATION_PRIME_HI;
import static net.consensys.linea.zktracer.TraceCancun.Bls.POINT_EVALUATION_PRIME_LO;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

public class BlsUtils {
  // Reference: https://eips.ethereum.org/EIPS/eip-2537
  static final Fp B = new Fp(BigInteger.valueOf(4));

  // Reference: https://eips.ethereum.org/assets/eip-2537/fast_subgroup_checks
  static final BigInteger SEED = new BigInteger("-15132376222941642752");
  static final Fp BETA =
      new Fp(
          "793479390729215512621379701633421447060886740281060493010456487427281649075476305620758731620350");
  static final Fp2 R =
      new Fp2(
          new Fp("0"),
          new Fp(
              "4002409555221667392624310435006688643935503118305586438271171395842971157480381377015405980053539358417135540939437"));
  static final Fp2 S =
      new Fp2(
          new Fp(
              "2973677408986561043442465346520108879172042883009249989176415018091420807192182638567116318576472649347015917690530"),
          new Fp(
              "1028732146235106349975324479215795277384839936929757896155643118032610843298655225875571310552543014690878354869257"));

  static final SmallPoint SMALL_POINT_AT_INFINITY = new SmallPoint(new Fp("0"), new Fp("0"));
  static final LargePoint LARGE_POINT_AT_INFINITY =
      new LargePoint(new Fp2(new Fp("0"), new Fp("0")), new Fp2(new Fp("0"), new Fp("0")));

  static final BigInteger BLS_PRIME =
      Bytes.concatenate(
              Bytes.ofUnsignedShort(BLS_PRIME_3),
              bigIntegerToBytes(BLS_PRIME_2),
              bigIntegerToBytes(BLS_PRIME_1),
              bigIntegerToBytes(BLS_PRIME_0))
          .toUnsignedBigInteger();

  static final EWord POINT_EVALUATION_PRIME =
      EWord.of(POINT_EVALUATION_PRIME_HI, POINT_EVALUATION_PRIME_LO);
}
