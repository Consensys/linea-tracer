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

    @Getter
    Map<AddrBlockPair, Integer> minDeplNoBlock = new HashMap<>();
    @Getter
    Map<AddrBlockPair, Integer> maxDeplNoBlock = new HashMap<>();
    public void updateDeplNoBlockMaps(Address address, int blockNumber, int currentDeplNo) {
        AddrBlockPair addrBlockPair = new AddrBlockPair(address, blockNumber);
        if (minDeplNoBlock.containsKey(addrBlockPair)) {
            // the maps already contain deployment info for this address, and this is not the first one in the block
            // since it is not the first, we do not update the minDeplNoBlock
            // but we update the maxDeplNoBlock
            maxDeplNoBlock.put(addrBlockPair, currentDeplNo);
        } else {
            // this is the first time we have a deployment at this address in the block
            minDeplNoBlock.put(addrBlockPair, currentDeplNo);
            maxDeplNoBlock.put(addrBlockPair, currentDeplNo);
        }
    }
}
