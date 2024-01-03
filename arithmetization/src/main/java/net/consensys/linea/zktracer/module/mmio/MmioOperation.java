package net.consensys.linea.zktracer.module.mmio;

import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;

record MmioOperation(
    MicroData microData,
    MmioDataProcessor mmioDataProcessor,
    State.TxState.Stamps moduleStamps,
    int microStamp,
    boolean isInitCode) {}
