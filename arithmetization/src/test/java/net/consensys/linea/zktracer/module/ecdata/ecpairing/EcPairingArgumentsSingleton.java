package net.consensys.linea.zktracer.module.ecdata.ecpairing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EcPairingArgumentsSingleton {
  private static EcPairingArgumentsSingleton instance;
  private String arguments;

  private EcPairingArgumentsSingleton() {
    // private constructor to prevent instantiation
  }

  public static EcPairingArgumentsSingleton getInstance() {
    if (instance == null) {
      instance = new EcPairingArgumentsSingleton();
    }
    return instance;
  }
}
