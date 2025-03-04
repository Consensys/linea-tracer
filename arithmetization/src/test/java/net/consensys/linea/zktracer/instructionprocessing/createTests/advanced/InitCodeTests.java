package net.consensys.linea.zktracer.instructionprocessing.createTests.advanced;

import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.SmartContractUtils;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyMultiTransaction;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.ToyTransaction.ToyTransactionBuilder;
import net.consensys.linea.testing.generated.Create2Special;
import net.consensys.linea.testing.generated.LoopContract;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;

public class InitCodeTests {

  static final Wei defaultBalance = Wei.of(4500L);

  static final String codeC =
      "0x6080604052348015600f57600080fd5b506101758061001f6000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c806366968a3114610030575b600080fd5b61004a60048036038101906100459190610112565b61004c565b005b8073ffffffffffffffffffffffffffffffffffffffff16630cd554396040518163ffffffff1660e01b8152600401600060405180830381600087803b15801561009457600080fd5b505af11580156100a8573d6000803e3d6000fd5b5050505050565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006100df826100b4565b9050919050565b6100ef816100d4565b81146100fa57600080fd5b50565b60008135905061010c816100e6565b92915050565b600060208284031215610128576101276100af565b5b6000610136848285016100fd565b9150509291505056fea264697066735822122091a3027dffb4fce4ff4c4d92282bbdfbe4a5718df6dc52dcc1afbc4ed5e771ce64736f6c634300081a0033";
  static final String codeCWithImmediateCallBack = "";

  ToyAccount loopContractAccount =
      ToyAccount.builder()
          .address(Address.fromHexString("0x789101"))
          .balance(defaultBalance)
          .nonce(0)
          .code(SmartContractUtils.getSolidityContractRuntimeByteCode(LoopContract.class))
          .build();

  ToyAccount create2SpecialAccount =
      ToyAccount.builder()
          .address(Address.fromHexString("0x789102"))
          .balance(defaultBalance)
          .nonce(1)
          .code(SmartContractUtils.getSolidityContractRuntimeByteCode(Create2Special.class))
          .build();

  static final Long gasLimit = 5000000L;

  @Test
  void deployContractCWithCreate2() {

    Bytes storeCodeC = storeCodeCPayload(codeC);
    Bytes storeSalt =
        storeSaltPayload("0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef");
    Bytes storeSalt2 =
        storeSaltPayload("0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdee");
    Bytes create2WithCodeC = create2WithCodeCPayload();

    ToyTransactionBuilder step1Builder =
        ToyTransaction.builder()
            .to(create2SpecialAccount)
            .payload(storeCodeC)
            .keyPair(keyPair)
            .gasLimit(gasLimit);

    ToyTransactionBuilder step2Builder =
        ToyTransaction.builder()
            .to(create2SpecialAccount)
            .payload(storeSalt)
            .keyPair(keyPair)
            .gasLimit(gasLimit);

    ToyTransactionBuilder step3Builder =
        ToyTransaction.builder()
            .to(create2SpecialAccount)
            .payload(create2WithCodeC)
            .keyPair(keyPair)
            .gasLimit(gasLimit);

    final ToyTransactionBuilder[] stepBuilders = {step1Builder, step2Builder, step3Builder};

    final List<Transaction> transactions =
        ToyMultiTransaction.builder().build(stepBuilders, userAccount);

    MultiBlockExecutionEnvironment.builder()
        .accounts(List.of(userAccount, loopContractAccount, create2SpecialAccount))
        .addBlock(transactions)
        // .transactionProcessingResultValidator(resultValidator)
        .build()
        .run();
  }

  Bytes storeSaltPayload(String salt) {
    Function storeSaltFunction =
        new Function(
            Create2Special.FUNC_STORESALT,
            Arrays.asList(
                new org.web3j.abi.datatypes.generated.Bytes32(
                    Bytes.fromHexStringLenient(salt).toArray())),
            Collections.emptyList());
    return Bytes.fromHexStringLenient(FunctionEncoder.encode(storeSaltFunction));
  }

  Bytes storeCodeCPayload(String codeC) {
    Function storeCodeCFunction =
        new Function(
            Create2Special.FUNC_STORECODEC,
            Arrays.asList(
                new org.web3j.abi.datatypes.DynamicBytes(Bytes.fromHexString(codeC).toArray())),
            Collections.<TypeReference<?>>emptyList());
    return Bytes.fromHexStringLenient(FunctionEncoder.encode(storeCodeCFunction));
  }

  Bytes create2WithCodeCPayload() {
    Function create2WithCodeCFunction =
        new Function(
            Create2Special.FUNC_CREATE2WITHCODEC,
            Arrays.asList(),
            Collections.<TypeReference<?>>emptyList());
    return Bytes.fromHexStringLenient(FunctionEncoder.encode(create2WithCodeCFunction));
  }
}
