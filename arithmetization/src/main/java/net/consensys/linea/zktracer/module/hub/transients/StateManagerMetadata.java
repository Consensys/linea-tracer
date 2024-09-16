package net.consensys.linea.zktracer.module.hub.transients;

import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;

import java.util.HashMap;
import java.util.Map;

public class StateManagerMetadata
{
    public static class AddrBlockPair {
        @Getter
        private Address address;
        @Getter
        private int blockNumber;

        public AddrBlockPair(Address addr, int blockNumber) {
            this.address = addr;
            this.blockNumber = blockNumber;
        }
    }
    @Getter
    Map<AddrBlockPair, TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>> txnAccountFirstLastBlockMap = new HashMap<>();

    @Getter
    Map<Address, TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>> txnAccountFirstLastConflationMap = new HashMap<>();
}
