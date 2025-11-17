package net.consensys.linea.zktracer.module.blake2fmodexpdata.osaka;

import net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeComponents;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeModexpDataOperation;
import net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata;

public class OsakaBlakeModexpDataOperation extends BlakeModexpDataOperation {

  public OsakaBlakeModexpDataOperation(ModexpMetadata modexpMetaData, int id) {
    super(modexpMetaData, id);
  }

  public OsakaBlakeModexpDataOperation(BlakeComponents blakeComponents, int id) {
    super(blakeComponents, id);
  }
}
