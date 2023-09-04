package net.consensys.linea.zktracer.module.hub.chunks;

import java.math.BigInteger;

import net.consensys.linea.zktracer.EWord;
import net.consensys.linea.zktracer.module.hub.Exceptions;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.TxState;
import net.consensys.linea.zktracer.opcode.InstructionFamily;

public record CommonChunk(
    int txNumber,
    int batchNumber,
    TxState txState,
    int stamp,
    int txEndStamp,
    boolean txReverts,
    InstructionFamily instructionFamily,
    Exceptions exceptions,
    boolean abortFlag,
    boolean failureConditionFlag,
    int contextNumber,
    int newContextNumber,
    int revertStamp,
    boolean willRevert,
    boolean getsReverted,
    boolean selfReverts,
    int pc,
    int newPc,
    EWord codeAddress,
    int codeDeploymentNumber,
    boolean codeDeploymentStatus,
    int callerContextNumber,
    long gasExpected,
    long gasActual,
    long gasCost,
    long gasNext,
    long gasRefund,
    boolean twoLinesInstruction,
    boolean twoLinesInstructionCounter,
    int numberOfNonStackRows,
    int nonStackRowsCounter)
    implements TraceChunk {
  @Override
  public Trace.TraceBuilder trace(Trace.TraceBuilder trace) {
    return trace
        .absoluteTransactionNumber(BigInteger.valueOf(this.txNumber))
        .batchNumber(BigInteger.valueOf(this.batchNumber))
        .txSkip(this.txState == TxState.TX_SKIP)
        .txWarm(this.txState == TxState.TX_WARM)
        .txInit(this.txState == TxState.TX_INIT)
        .txExec(this.txState == TxState.TX_STATE)
        .txFinl(this.txState == TxState.TX_FINAL)
        .hubStamp(BigInteger.valueOf(this.stamp))
        .transactionEndStamp(BigInteger.valueOf(txEndStamp))
        .transactionReverts(BigInteger.valueOf(txReverts ? 1 : 0))
        .contextMayChangeFlag(
            (instructionFamily == InstructionFamily.CALL
                    || instructionFamily == InstructionFamily.CREATE
                    || instructionFamily == InstructionFamily.HALT
                    || instructionFamily == InstructionFamily.INVALID)
                || exceptions.any())
        .exceptionAhoyFlag(exceptions.any())
        .abortFlag(abortFlag)
        .failureConditionFlag(failureConditionFlag)

        // Context data
        .contextNumber(BigInteger.valueOf(contextNumber))
        .contextNumberNew(BigInteger.valueOf(newContextNumber))
        .contextRevertStamp(BigInteger.valueOf(revertStamp))
        .contextWillRevertFlag(willRevert)
        .contextGetsRevrtdFlag(getsReverted)
        .contextSelfRevrtsFlag(selfReverts)
        .programCounter(BigInteger.valueOf(pc))
        .programCounterNew(BigInteger.valueOf(newPc))

        // Bytecode metadata
        .codeAddressHi(codeAddress.hiBigInt())
        .codeAddressLo(codeAddress.loBigInt())
        .codeDeploymentNumber(BigInteger.valueOf(codeDeploymentNumber))
        .codeDeploymentStatus(codeDeploymentStatus)
        .callerContextNumber(BigInteger.valueOf(callerContextNumber))
        .gasExpected(BigInteger.valueOf(gasExpected))
        .gasActual(BigInteger.valueOf(gasActual))
        .gasCost(BigInteger.valueOf(gasCost))
        .gasNext(BigInteger.valueOf(gasNext))
        .gasRefund(BigInteger.valueOf(gasRefund))
        .twoLineInstruction(twoLinesInstruction)
        .counterTli(twoLinesInstructionCounter)
        .numberOfNonStackRows(BigInteger.valueOf(numberOfNonStackRows))
        .counterNsr(BigInteger.valueOf(nonStackRowsCounter));
  }
}
