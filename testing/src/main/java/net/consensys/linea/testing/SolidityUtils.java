package net.consensys.linea.testing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tuweni.bytes.Bytes;
import org.web3j.tx.Contract;

import org.web3j.protocol.core.methods.response.Log;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class SolidityUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final ClassLoader classLoader = SolidityUtils.class.getClassLoader();

  public static Bytes getContractByteCode(Class<? extends Contract> contractClass) {
    String contractResourcePath = String.format("solidity/%s.json", contractClass.getSimpleName());
    URL contractResourceURL = classLoader.getResource(contractResourcePath);
    try {
      JsonNode jsonRoot = objectMapper.readTree(contractResourceURL);
      Iterator<Map.Entry<String, JsonNode>> contracts = jsonRoot.get("contracts").fields();
      while (contracts.hasNext()) {
        Map.Entry<String, JsonNode> contract = contracts.next();
        if(contract.getKey().contains(contractClass.getSimpleName())) {
          return Bytes.fromHexStringLenient(contract.getValue().get("bin-runtime").asText());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException("Could not find contract bytecode");
  }

  public static Log fromBesuLog(org.hyperledger.besu.evm.log.Log log) {
    return new Log();
  }
}
