package net.consensys.linea.zktracer.module.hub.signals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasCostSingleton {
  private static GasCostSingleton instance;
  private long gasCost = 0;

  private GasCostSingleton() {
    // private constructor to prevent instantiation
  }

  public void incrementGasCost(long gasCost) {
    this.gasCost += gasCost;
  }

  public static GasCostSingleton getInstance() {
    if (instance == null) {
      instance = new GasCostSingleton();
    }
    return instance;
  }
}
