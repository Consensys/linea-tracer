package net.consensys.linea.zktracer.statemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;

public class StateManagerUtils {

  public static List<Map<Address, FragmentFirstAndLast<AccountFragment>>>
      computeAccountFirstAndLastMapList(Hub hub) {
    List<Map<Address, FragmentFirstAndLast<AccountFragment>>> accountFirstAndLastMapList =
        new ArrayList<>();

    // We count the number of transactions in the hub
    int txCount = hub.state().txCount();
    // We iterate over the transactions
    for (int txNb = 0; txNb < txCount; txNb++) {
      // We create an accountFirstAndLastMap for each transaction
      accountFirstAndLastMapList.add(new HashMap<>());
      // We retrieve the trace section list
      List<TraceSection> traceSectionList =
          hub.state().getState().operationsInTransactionBundle().get(txNb).traceSections().trace();
      // For each trace section
      for (TraceSection traceSection : traceSectionList) {
        // We iterate over the fragments
        for (TraceFragment traceFragment : traceSection.fragments()) {
          // We cast them to AccountFragment
          // If an exception occurs, it means the Fragment is not an AccountFragment so we
          // disregard it and continue
          try {
            AccountFragment accountFragment = (AccountFragment) traceFragment;
            // We update the AccountFirstAndLastMap
            accountFirstAndLastMapList.set(
                txNb,
                updateAccountFirstAndLast(accountFragment, accountFirstAndLastMapList.get(txNb)));
          } catch (Exception e) {
            // ignore
          }
        }
      }
    }
    return accountFirstAndLastMapList;
  }

  public static List<Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>>>
      computeStorageFirstAndLastMapList(Hub hub) {
    List<Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>>>
        storageFirstAndLastMapList = new ArrayList<>();
    // We count the number of transactions in the hub
    int txCount = hub.state().txCount();
    // We iterate over the transactions
    for (int txNb = 0; txNb < txCount; txNb++) {
      // We create an storageFirstAndLastMap for each transaction
      storageFirstAndLastMapList.add(new HashMap<>());
      // We retrieve the trace section list
      List<TraceSection> traceSectionList =
          hub.state().getState().operationsInTransactionBundle().get(txNb).traceSections().trace();
      // For each trace section
      for (TraceSection traceSection : traceSectionList) {
        // We iterate over the fragments
        for (TraceFragment traceFragment : traceSection.fragments()) {
          // We cast them to StorageFragment
          // If an exception occurs, it means the Fragment is not a StorageFragment so we
          // disregard it and continue
          try {
            StorageFragment storageFragment = (StorageFragment) traceFragment;
            Address address = storageFragment.getStorageSlotIdentifier().getAddress();
            EWord key = storageFragment.getStorageSlotIdentifier().getStorageKey();
            // We update the storageFirstAndLastMapList
            storageFirstAndLastMapList.set(
                txNb,
                updateStorageFirstAndLast(
                    storageFragment, storageFirstAndLastMapList.get(txNb), Map.of(address, key)));
          } catch (Exception e) {
            // ignore
          }
        }
      }
    }
    return storageFirstAndLastMapList;
  }

  public static Map<Address, FragmentFirstAndLast<AccountFragment>> updateAccountFirstAndLast(
      AccountFragment fragment,
      Map<Address, FragmentFirstAndLast<AccountFragment>> accountFirstAndLastMap) {
    // Setting the post transaction first and last value
    int dom = fragment.domSubStampsSubFragment().domStamp();
    int sub = fragment.domSubStampsSubFragment().subStamp();

    Address key = fragment.oldState().address();

    if (!accountFirstAndLastMap.containsKey(key)) {
      FragmentFirstAndLast<AccountFragment> txnFirstAndLast =
          new FragmentFirstAndLast<AccountFragment>(fragment, fragment, dom, sub, dom, sub);
      accountFirstAndLastMap.put(key, txnFirstAndLast);
    } else {
      FragmentFirstAndLast<AccountFragment> txnFirstAndLast = accountFirstAndLastMap.get(key);
      // Replace condition
      if (FragmentFirstAndLast.strictlySmallerStamps(
          txnFirstAndLast.getLastDom(), txnFirstAndLast.getLastSub(), dom, sub)) {
        txnFirstAndLast.setLast(fragment);
        txnFirstAndLast.setLastDom(dom);
        txnFirstAndLast.setLastSub(sub);
        accountFirstAndLastMap.put(key, txnFirstAndLast);
      }
    }
    return accountFirstAndLastMap;
  }

  public static Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>>
      updateStorageFirstAndLast(
          StorageFragment fragment,
          Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>> storageFirstAndLastMap,
          Map<Address, EWord> key) {
    // Setting the post transaction first and last value
    int dom = fragment.getDomSubStampsSubFragment().domStamp();
    int sub = fragment.getDomSubStampsSubFragment().subStamp();

    if (!storageFirstAndLastMap.containsKey(key)) {
      FragmentFirstAndLast<StorageFragment> txnFirstAndLast =
          new FragmentFirstAndLast<StorageFragment>(fragment, fragment, dom, sub, dom, sub);
      storageFirstAndLastMap.put(key, txnFirstAndLast);
    } else {
      // the storage key has already been accessed for this account
      FragmentFirstAndLast<StorageFragment> txnFirstAndLast = storageFirstAndLastMap.get(key);
      // Replace condition
      if (FragmentFirstAndLast.strictlySmallerStamps(
          txnFirstAndLast.getLastDom(), txnFirstAndLast.getLastSub(), dom, sub)) {
        txnFirstAndLast.setLast(fragment);
        txnFirstAndLast.setLastDom(dom);
        txnFirstAndLast.setLastSub(sub);
        storageFirstAndLastMap.put(key, txnFirstAndLast);
      }
    }
    return storageFirstAndLastMap;
  }

  public static Map<Address, Map<Integer, FragmentFirstAndLast<AccountFragment>>>
      computeBlockMapAccount(
          Hub hub,
          List<Map<Address, FragmentFirstAndLast<AccountFragment>>> accountFirstAndLastMapList) {
    Map<Address, Map<Integer, FragmentFirstAndLast<AccountFragment>>> blockMapAccount =
        new HashMap<>();
    int blockCount = hub.blockdata().getOperations().size() / 7;
    int txCount = hub.state().txCount();
    for (int i = 0; i < blockCount; i++) {
      int relBlokNoFromBlock = (int) hub.blockdata().getOperations().get(i * 7).relBlock();

      for (int txNb = 0; txNb < txCount; txNb++) {
        int relBlokNoFromTx =
            hub.txStack().getByAbsoluteTransactionNumber(txNb + 1).getRelativeBlockNumber();

        if (relBlokNoFromTx == relBlokNoFromBlock) {
          Map<Address, FragmentFirstAndLast<AccountFragment>> accountFirstAndLastMap =
              accountFirstAndLastMapList.get(txNb);
          for (var entry : accountFirstAndLastMap.entrySet()) {
            Address addr = entry.getKey();
            FragmentFirstAndLast<AccountFragment> localValueAccount = entry.getValue();

            if (!blockMapAccount.containsKey(addr)) {
              // the pair is not present in the map
              blockMapAccount.put(addr, new HashMap<>());
              blockMapAccount.get(addr).put(relBlokNoFromBlock, localValueAccount);
            } else if (!blockMapAccount.get(addr).containsKey(relBlokNoFromBlock)) {
              // the pair is present in the map, but the block is not present
              blockMapAccount.get(addr).put(relBlokNoFromBlock, localValueAccount);
            } else {
              FragmentFirstAndLast<AccountFragment> fetchedValue =
                  blockMapAccount.get(addr).get(relBlokNoFromBlock);
              // we make a copy that will be modified to not change the values already present in
              // the
              // transaction maps
              FragmentFirstAndLast<AccountFragment> blockValue = fetchedValue.copy();
              // update the first part of the blockValue
              // Todo: Refactor and remove code duplication
              if (FragmentFirstAndLast.strictlySmallerStamps(
                  localValueAccount.getFirstDom(),
                  localValueAccount.getFirstSub(),
                  blockValue.getFirstDom(),
                  blockValue.getFirstSub())) {
                // chronologically checks that localValue.First is before blockValue.First
                // localValue comes chronologically before, and should be the first value of the
                // map.
                blockValue.setFirst(localValueAccount.getFirst());
                blockValue.setFirstDom(localValueAccount.getFirstDom());
                blockValue.setFirstSub(localValueAccount.getFirstSub());
              }

              // update the last part of the blockValue
              if (FragmentFirstAndLast.strictlySmallerStamps(
                  blockValue.getLastDom(),
                  blockValue.getLastSub(),
                  localValueAccount.getLastDom(),
                  localValueAccount.getLastSub())) {
                // chronologically checks that blockValue.Last is before localValue.Last
                // localValue comes chronologically after, and should be the final value of the map.
                blockValue.setLast(localValueAccount.getLast());
                blockValue.setLastDom(localValueAccount.getLastDom());
                blockValue.setLastSub(localValueAccount.getLastSub());
              }
              blockMapAccount.get(addr).put(relBlokNoFromBlock, blockValue);
            }
          }
        }
      }
    }
    return blockMapAccount;
  }
}
