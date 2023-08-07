package net.consensys.linea.zktracer.opcode;

public enum Gas {
  gZero(0),
  gJumpDest(0),
  gBase(2),
  gVeryLow(3),
  gLow(5),
  gMid(8),
  gHigh(10),
  gWarmAccess(100),
  gAccessListAddress(2400),
  gAccessListStorage(1900),
  gColdAccountAccess(2600),
  gColdSLoad(2100),
  gSSet(20000),
  gSReset(2900),
  rSClear(15000),
  rSelfDestruct(24000),
  gSelfDestruct(5000),
  gCreate(32000),
  gCodeDeposit(200),
  gCallValue(9000),
  gCallStipend(2300),
  gNewAccount(25000),
  gExp(10),
  gExpByte(50),
  gMemory(3),
  gTxCreate(32000),
  gTxDataZero(4),
  gTxDataNonZero(16),
  gTransaction(21000),
  gLog0(Constants.log),
  gLog1(Constants.log + Constants.logTopic),
  gLog2(Constants.log + 2 * Constants.logTopic),
  gLog3(Constants.log + 3 * Constants.logTopic),
  gLog4(Constants.log + 4 * Constants.logTopic),
  gLogData(8),
  gLogTopic(375),
  gKeccak256(30),
  gKeccak256Word(6),
  gCopy(3),
  gBlockHash(20),
  // below are markers for gas that is computed in other modules
  // that is: hub, memory expansion, stipend, precompile info
  sMxp(0),
  sCall(0), // computing the cost of a CALL requires HUB data (warmth, account existence, ...), MXP
  // data for memory expansion, STP data for gas stipend <- made it its own type
  sHub(0),
  sStp(0),
  sPrecInfo(0);

  private final int cost;

  Gas(int cost) {
    this.cost = cost;
  }

  int cost() {
    return this.cost();
  }

  private static class Constants {
    private static final int log = 375;
    private static final int logTopic = 375;
  }
}
