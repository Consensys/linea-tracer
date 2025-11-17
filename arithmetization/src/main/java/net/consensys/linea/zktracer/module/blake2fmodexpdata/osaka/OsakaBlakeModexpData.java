package net.consensys.linea.zktracer.module.blake2fmodexpdata.osaka;

import net.consensys.linea.zktracer.container.module.IncrementAndDetectModule;
import net.consensys.linea.zktracer.container.module.IncrementingModule;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeModexpData;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeModexpDataOperation;
import net.consensys.linea.zktracer.module.limits.precompiles.BlakeRounds;
import net.consensys.linea.zktracer.module.wcp.Wcp;

public class OsakaBlakeModexpData extends BlakeModexpData {

  public OsakaBlakeModexpData(
      Wcp wcp,
      IncrementAndDetectModule modexpEffectiveCall,
      IncrementingModule modexpLargeCall,
      IncrementingModule blakeEffectiveCall,
      BlakeRounds blakeRounds) {
    super(wcp, modexpEffectiveCall, modexpLargeCall, blakeEffectiveCall, blakeRounds);
  }

  public void addOperation(BlakeModexpDataOperation operation) {
    // Implementation for adding a LondonBlakeModexpDataOperation
  }
}
