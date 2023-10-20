package net.consensys.linea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.consensys.linea.services.kvstore.BonsaiReferenceTestWorldState;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.MutableAccount;
import org.hyperledger.besu.evm.worldstate.WorldUpdater;

import java.util.Map;

/** Represent a worldState for testing. */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface ReferenceTestWorldState extends org.hyperledger.besu.ethereum.referencetests.ReferenceTestWorldState {

  static void insertAccount(
    final WorldUpdater updater, final Address address, final ReferenceTestWorldState.AccountMock toCopy) {
    final MutableAccount account = updater.getOrCreate(address);
    account.setNonce(toCopy.getNonce());
    account.setBalance(toCopy.getBalance());
    account.setCode(toCopy.getCode());
    for (final Map.Entry<UInt256, UInt256> entry : toCopy.getStorage().entrySet()) {
      account.setStorageValue(entry.getKey(), entry.getValue());
    }
  }

  ReferenceTestWorldState copy();

  @JsonCreator
  static ReferenceTestWorldState create(final Map<String, ReferenceTestWorldState.AccountMock> accounts) {
    // delegate to a Bonsai reference test world state:
    return BonsaiReferenceTestWorldState.create(accounts);
  }
}