package net.consensys.linea.replaytests;

import static net.consensys.linea.replaytests.ReplayTestTools.replay;
import static net.consensys.linea.zktracer.ChainConfig.MAINNET_TESTCONFIG;
import static net.consensys.linea.zktracer.Fork.PRAGUE;

import net.consensys.linea.reporting.TracerTestBase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Tag("replay")
public class Issue1335Tests extends TracerTestBase {

  @Test
  void issue1335(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "25022126.json", testInfo);
  }
}
