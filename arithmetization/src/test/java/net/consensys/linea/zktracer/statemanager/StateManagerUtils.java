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
}
