package net.consensys.linea.zktracer.module.hub;

enum TxState {
  // A state marking the first trace of the current tx, required to set up some things
  TxPreInit,
  TxExec,
  TxFinal,
  TxInit,
  TxSkip,
  TxWarm,
}
