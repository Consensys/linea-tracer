package net.consensys.linea.zktracer.module.gas;

import java.math.BigInteger;

public record GasParameters(BigInteger gasActl, BigInteger gasCost, boolean oogx) {}
