package net.consensys.linea.services.kvstore;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.consensys.linea.ReferenceTestWorldState;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.ethereum.bonsai.cache.CachedMerkleTrieLoader;
import org.hyperledger.besu.ethereum.bonsai.storage.BonsaiPreImageProxy;
import org.hyperledger.besu.ethereum.bonsai.storage.BonsaiWorldStateKeyValueStorage;
import org.hyperledger.besu.ethereum.bonsai.trielog.TrieLogAddedEvent;
import org.hyperledger.besu.ethereum.bonsai.trielog.TrieLogFactoryImpl;
import org.hyperledger.besu.ethereum.bonsai.trielog.TrieLogManager;
import org.hyperledger.besu.ethereum.bonsai.worldview.BonsaiWorldState;
import org.hyperledger.besu.ethereum.bonsai.worldview.BonsaiWorldStateUpdateAccumulator;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.referencetests.BonsaiReferenceTestUpdateAccumulator;
import org.hyperledger.besu.ethereum.referencetests.BonsaiReferenceTestWorldStateStorage;
import org.hyperledger.besu.evm.worldstate.WorldUpdater;
import org.hyperledger.besu.metrics.ObservableMetricsSystem;
import org.hyperledger.besu.metrics.noop.NoOpMetricsSystem;
import org.hyperledger.besu.plugin.services.trielogs.TrieLog;
import org.hyperledger.besu.plugin.services.trielogs.TrieLogEvent;
import org.hyperledger.besu.plugin.services.trielogs.TrieLogFactory;
import org.hyperledger.besu.util.Subscribers;

public class BonsaiReferenceTestWorldState extends BonsaiWorldState
  implements ReferenceTestWorldState {

  private final BonsaiReferenceTestWorldStateStorage refTestStorage;
  private final BonsaiPreImageProxy preImageProxy;

  private boolean disableRootHashVerification;

  protected BonsaiReferenceTestWorldState(
    final BonsaiReferenceTestWorldStateStorage worldStateStorage,
    final CachedMerkleTrieLoader cachedMerkleTrieLoader,
    final TrieLogManager trieLogManager,
    final BonsaiPreImageProxy preImageProxy) {
    super(worldStateStorage, cachedMerkleTrieLoader, trieLogManager);
    this.refTestStorage = worldStateStorage;
    this.preImageProxy = preImageProxy;
    setAccumulator(
      new BonsaiReferenceTestUpdateAccumulator(
        this,
        (addr, value) ->
          cachedMerkleTrieLoader.preLoadAccount(
            getWorldStateStorage(), worldStateRootHash, addr),
        (addr, value) ->
          cachedMerkleTrieLoader.preLoadStorageSlot(getWorldStateStorage(), addr, value),
        preImageProxy));
  }

  @Override
  public ReferenceTestWorldState copy() {
    var layerCopy = new BonsaiReferenceTestWorldStateStorage(worldStateStorage, preImageProxy);
    return new BonsaiReferenceTestWorldState(
      layerCopy, cachedMerkleTrieLoader, trieLogManager, preImageProxy);
  }

  /**
   * If the supplied header has a non-zero state root, verify. Else we assume that stateroot is an
   * output instead of an input for this reference test and we bypass the state root check.
   *
   * <p>Besu reference-test style test cases should supply a stateroot to verify to prevent bonsai
   * regressions.
   *
   * @param calculatedStateRoot state root calculated during bonsai persist step.
   * @param header supplied reference test block header.
   */
  @Override
  protected void verifyWorldStateRoot(final Hash calculatedStateRoot, final BlockHeader header) {
    if (!disableRootHashVerification) {
      super.verifyWorldStateRoot(calculatedStateRoot, header);
    }
  }

  @JsonCreator
  public static BonsaiReferenceTestWorldState create(
    final Map<String, AccountMock> accounts) {
    final ObservableMetricsSystem metricsSystem = new NoOpMetricsSystem();
    final CachedMerkleTrieLoader cachedMerkleTrieLoader = new CachedMerkleTrieLoader(metricsSystem);
    final TrieLogManager trieLogManager = new NoOpTrieLogManager();
    final BonsaiPreImageProxy preImageProxy =
      new BonsaiPreImageProxy.BonsaiReferenceTestPreImageProxy();

    final BonsaiReferenceTestWorldStateStorage worldStateStorage =
      new BonsaiReferenceTestWorldStateStorage(
        new BonsaiWorldStateKeyValueStorage(
          new InMemoryKeyValueStorageProvider(), metricsSystem),
        preImageProxy);

    final BonsaiReferenceTestWorldState worldState =
      new BonsaiReferenceTestWorldState(
        worldStateStorage, cachedMerkleTrieLoader, trieLogManager, preImageProxy);

    final WorldUpdater updater = worldState.updater();
    for (final Map.Entry<String, ReferenceTestWorldState.AccountMock> entry : accounts.entrySet()) {
      ReferenceTestWorldState.insertAccount(
        updater, Address.fromHexString(entry.getKey()), entry.getValue());
    }
    updater.commit();
    return worldState;
  }

  @Override
  public Stream<StreamableAccount> streamAccounts(final Bytes32 startKeyHash, final int limit) {
    return this.refTestStorage.streamAccounts(this, startKeyHash, limit);
  }

  public void disableRootHashVerification() {
    disableRootHashVerification = true;
  }

  static class NoOpTrieLogManager implements TrieLogManager {
    private final Subscribers<TrieLogEvent.TrieLogObserver> trieLogObservers = Subscribers.create();
    private final TrieLogFactory trieLogFactory = new TrieLogFactoryImpl();

    @Override
    public void saveTrieLog(
      final BonsaiWorldStateUpdateAccumulator localUpdater,
      final Hash forWorldStateRootHash,
      final BlockHeader forBlockHeader,
      final BonsaiWorldState forWorldState) {
      // notify trie log added observers, synchronously
      TrieLog trieLog = trieLogFactory.create(localUpdater, forBlockHeader);
      trieLogObservers.forEach(o -> o.onTrieLogAdded(new TrieLogAddedEvent(trieLog)));
    }

    @Override
    public void addCachedLayer(
      final BlockHeader blockHeader,
      final Hash worldStateRootHash,
      final BonsaiWorldState forWorldState) {}

    @Override
    public boolean containWorldStateStorage(final Hash blockHash) {
      return false;
    }

    @Override
    public Optional<BonsaiWorldState> getWorldState(final Hash blockHash) {
      return Optional.empty();
    }

    @Override
    public Optional<BonsaiWorldState> getNearestWorldState(final BlockHeader blockHeader) {
      return Optional.empty();
    }

    @Override
    public Optional<BonsaiWorldState> getHeadWorldState(
      final Function<Hash, Optional<BlockHeader>> hashBlockHeaderFunction) {
      return Optional.empty();
    }

    @Override
    public long getMaxLayersToLoad() {
      return 0;
    }

    @Override
    public void reset() {}

    @Override
    public Optional<? extends TrieLog> getTrieLogLayer(final Hash blockHash) {
      return Optional.empty();
    }

    @Override
    public synchronized long subscribe(final TrieLogEvent.TrieLogObserver sub) {
      return trieLogObservers.subscribe(sub);
    }

    @Override
    public synchronized void unsubscribe(final long id) {
      trieLogObservers.unsubscribe(id);
    }
  }

  @Override
  protected Hash hashAndSavePreImage(final Bytes value) {
    // by default do not save has preImages
    return preImageProxy.hashAndSavePreImage(value);
  }
}
