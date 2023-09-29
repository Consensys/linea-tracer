package net.consensys.linea.zktracer.module.rlpAddr;

import java.util.List;
import java.util.Random;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.ToyAccount;
import net.consensys.linea.zktracer.testing.ToyExecutionEnvironment;
import net.consensys.linea.zktracer.testing.ToyTransaction;
import net.consensys.linea.zktracer.testing.ToyWorld;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class rlpAddrTest {
  private final Random rnd = new Random(666);

  @Test
  void test() {
    for (int i = 0; i < 100; i++) {

      KeyPair keyPair = new SECP256K1().generateKeyPair();
      Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
      ToyAccount senderAccount = randSenderAccount(senderAddress);

      ToyAccount receiverAccount = randReceiverAccount();

      Transaction tx =
          ToyTransaction.builder()
              .sender(senderAccount)
              .to(receiverAccount)
              .keyPair(keyPair)
              .build();

      ToyWorld toyWorld =
          ToyWorld.builder().accounts(List.of(senderAccount, receiverAccount)).build();

      ToyExecutionEnvironment.builder().toyWorld(toyWorld).transaction(tx).build().run();
    }
  }

  final ToyAccount randReceiverAccount() {
    int create = rnd.nextInt(1, 3);
    return switch (create) {
      case 1 -> ToyAccount.builder()
          .balance(Wei.MAX_WEI)
          .nonce(randLong())
          .address(Address.wrap(Bytes.random(20)))
          .code(BytecodeCompiler.newProgram().op(OpCode.CREATE).compile())
          .build();
      case 2 -> ToyAccount.builder()
          .balance(Wei.MAX_WEI)
          .nonce(randLong())
          .address(Address.wrap(Bytes.random(20)))
          .code(BytecodeCompiler.newProgram().op(OpCode.CREATE2).compile())
          .build();
      default -> throw new IllegalStateException("Unexpected value: " + create);
    };
  }

  final ToyAccount randSenderAccount(Address senderAddress) {
    return ToyAccount.builder()
        .balance(Wei.MAX_WEI)
        .nonce(randLong())
        .address(senderAddress)
        .build();
  }

  final Long randLong() {
    int selector = rnd.nextInt(0, 4);
    return switch (selector) {
      case 0 -> 0L;
      case 1 -> rnd.nextLong(1, 128);
      case 2 -> rnd.nextLong(128, 256);
      case 3 -> rnd.nextLong(256, 0xfffffffffffffffL);
      default -> throw new IllegalStateException("Unexpected value: " + selector);
    };
  }
}
