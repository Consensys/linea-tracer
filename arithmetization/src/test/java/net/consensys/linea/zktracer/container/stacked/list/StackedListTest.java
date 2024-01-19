package net.consensys.linea.zktracer.container.stacked.list;

import com.google.common.collect.ImmutableList;
import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.add.AddOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class StackedListTest {

  private final static AddOperation ONE_PLUS_ONE =
          new AddOperation(
                  OpCode.ADD,
                  Bytes.wrap(BigInteger.ONE.toByteArray()),
                  Bytes.wrap(BigInteger.ONE.toByteArray()));

  private final static AddOperation ONE_PLUS_TWO =
          new AddOperation(
                  OpCode.ADD,
                  Bytes.wrap(BigInteger.ONE.toByteArray()),
                  Bytes.wrap(BigInteger.TWO.toByteArray()));
  @Test
  public void push() {
    StackedList<AddOperation> chunks = new StackedList<>();
    chunks.enter();

    chunks.add(ONE_PLUS_ONE);
    chunks.add(ONE_PLUS_ONE);
    chunks.add(ONE_PLUS_ONE);
    Assertions.assertEquals(3, chunks.size());
    chunks.pop();
    Assertions.assertEquals(0, chunks.size());
  }

  @Test
  public void multiplePushPop() {
    StackedList<AddOperation> chunks = new StackedList<>();
    chunks.enter();
    chunks.add(ONE_PLUS_ONE);
    chunks.add(ONE_PLUS_ONE);
    Assertions.assertEquals(2, chunks.size());

    chunks.enter();
    chunks.add(ONE_PLUS_ONE);
    Assertions.assertEquals(3, chunks.size());
    chunks.add(ONE_PLUS_TWO);
    Assertions.assertEquals(4, chunks.size());
    chunks.pop();
    Assertions.assertEquals(2, ImmutableList.copyOf(chunks.iterator()).size());
  }
}