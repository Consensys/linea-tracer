package net.consensys.linea;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ReferenceTestWatcher.class)
public class ReferenceTestWatcherTest {

  @Test
  void onFailedTest() {
    assertThat(false).isTrue();
  }
}
