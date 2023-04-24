package net.consensys.linea.zktracer.module.trm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.module.AbstractModuleTracerTest;
import net.consensys.linea.zktracer.module.ModuleTracer;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrmTracerTest extends AbstractModuleTracerTest {
  @Override
  protected Stream<Arguments> provideNonRandomArguments() {
    List<Arguments> arguments = new ArrayList<>();
    for (OpCode opCode : getModuleTracer().supportedOpCodes()) {
      for (int k = 0; k <= 3; k++) {
        for (int i = 1; i <= 4; i++) {
          arguments.add(Arguments.of(opCode, UInt256.valueOf(i), UInt256.valueOf(k)));
        }
      }
    }
    return arguments.stream();
  }

  @Override
  protected ModuleTracer getModuleTracer() {
    return new TrmTracer();
  }
}
