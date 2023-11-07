package net.consensys.linea.zktracer.parquet;

public interface ModuleRecord<T extends ModuleRecord<T>> {
     void accept(LocalWriteSupport<T> tLocalWriteSupport);
}
