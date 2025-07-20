package net.consensys.linea.zktracer.module.bls;

import java.math.BigInteger;

public abstract class Point<F extends Field<F>, P extends Point<F, P>> {
  F x;
  F y;
  P POINT_AT_INFINITY;
  F ZERO;
  F TWO;
  F THREE;

  abstract P createPoint(F x, F y);

  abstract boolean isOnCurve();

  abstract boolean isInSubGroup();

  P add(P other) {
    if (this.equals(POINT_AT_INFINITY)) {
      return other;
    }
    if (other.equals(POINT_AT_INFINITY)) {
      return (P) this;
    }
    if (this.x.equals(other.x) && this.y.equals(other.y.additiveInverse())) {
      return POINT_AT_INFINITY;
    }
    F slope;
    if (this.x.equals(other.x) && this.y.equals(other.y)) {
      // Point doubling
      F numerator = THREE.mul(this.x.pow2());
      F denominator = TWO.mul(this.y);
      if (denominator.equals(ZERO)) {
        return POINT_AT_INFINITY;
      }
      slope = numerator.mul(denominator.multiplicativeInverse());
    } else {
      // !this.x.equals(other.x)
      // Point multiplication
      F numerator = other.y.sub(this.y);
      F denominator = other.x.sub(this.x);
      slope = numerator.mul(denominator.multiplicativeInverse());
    }
    F xRes = slope.pow2().sub(this.x).sub(other.x);
    F yRes = slope.mul(this.x.sub(xRes)).sub(this.y);
    return createPoint(xRes, yRes);
  }

  P mul(BigInteger scalar) {
    if (scalar.equals(BigInteger.ZERO)) {
      return POINT_AT_INFINITY;
    }
    if (scalar.equals(BigInteger.ONE)) {
      return (P) this;
    }
    P result = POINT_AT_INFINITY;
    P addend = (P) this;
    // Double-and-add algorithm
    while (scalar.signum() > 0) {
      if (scalar.testBit(0)) {
        result = result.add(addend);
      }
      addend = addend.add(addend);
      scalar = scalar.shiftRight(1);
    }
    return result;
  }
}
