package net.consensys.linea.zktracer.module.blake2fmodexpdata.london;

import static net.consensys.linea.zktracer.TraceLondon.Blake2fmodexpdata.*;
import static net.consensys.linea.zktracer.TraceLondon.Blake2fmodexpdata.INDEX_MAX_MODEXP;
import static net.consensys.linea.zktracer.TraceLondon.Blake2fmodexpdata.INDEX_MAX_MODEXP_BASE;
import static net.consensys.linea.zktracer.TraceLondon.Blake2fmodexpdata.INDEX_MAX_MODEXP_EXPONENT;
import static net.consensys.linea.zktracer.TraceLondon.Blake2fmodexpdata.INDEX_MAX_MODEXP_MODULUS;
import static net.consensys.linea.zktracer.TraceLondon.Blake2fmodexpdata.INDEX_MAX_MODEXP_RESULT;

import net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeComponents;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeModexpDataOperation;
import net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata;

public class LondonBlakeModexpDataOperation extends BlakeModexpDataOperation {
  public static final short indexMaxModexp = INDEX_MAX_MODEXP;
  public static final short indexMaxModexpBase = INDEX_MAX_MODEXP_BASE;
  public static final short indexMaxModexpExponent = INDEX_MAX_MODEXP_EXPONENT;
  public static final short indexMaxModexpModulus = INDEX_MAX_MODEXP_MODULUS;
  public static final short indexMaxModexpResult = INDEX_MAX_MODEXP_RESULT;
  public static final short NB_ROWS_BLAKEMODEXP_MODEXP =
      (indexMaxModexpBase + 1)
          + (indexMaxModexpExponent + 1)
          + (indexMaxModexpModulus + 1)
          + (indexMaxModexpResult + 1);

  public LondonBlakeModexpDataOperation(ModexpMetadata modexpMetaData, int id) {
    super(modexpMetaData, id, (short) INDEX_MAX_MODEXP);
  }

  public LondonBlakeModexpDataOperation(BlakeComponents blakeComponents, int id) {
    super(blakeComponents, id, (short) INDEX_MAX_MODEXP);
  }
}
