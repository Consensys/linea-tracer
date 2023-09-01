package net.consensys.linea.zktracer.module.hub;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.evm.Code;

public class Bytecode {
  public static Bytecode EMPTY = new Bytecode(Bytes.EMPTY);

  private final Bytes bytecode;
  private Hash hash;

  public Bytecode(Bytes bytes) {
    this.bytecode = bytes;
  }

  public Bytecode(Bytes bytes, Hash hash) {
    this.bytecode = bytes;
    this.hash = hash;
  }

  public Bytecode(Code code) {
    this.bytecode = code.getBytes();
    this.hash = code.getCodeHash();
  }

  public int getSize() {
    return this.bytecode.size();
  }

  public Bytes getBytes() {
    return this.bytecode;
  }

  public boolean isEmpty() {
    return this.bytecode.isEmpty();
  }

  public Hash getCodeHash() {
    if (this.hash == null) {
      this.hash = Hash.hash(this.bytecode);
    }
    return this.hash;
  }
}
