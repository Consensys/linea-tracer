# Changelog

## 0.1.4-test3
Test pre-release 3 from [temp/issue-248/count-stack-only](https://github.com/Consensys/besu-sequencer-plugins/tree/temp/issue-248/count-stack-only)
* Log ZkTracer counters for every produced block [#485](https://github.com/ConsenSys/besu-sequencer-plugins/pull/485)
* fix: overflow for modexp arg [#489](https://github.com/ConsenSys/besu-sequencer-plugins/pull/489)
* bin reimplementation [#473](https://github.com/ConsenSys/besu-sequencer-plugins/pull/473)
* applyMavenExclusions=false [#477](https://github.com/ConsenSys/besu-sequencer-plugins/pull/477)

## 0.1.4-test2
Testing pre-release from branch test-release/v0.1.4-test2

* revert make loginfo counts closer to Geth
* head: disable stp & txndata

Temporary line counting release for testnet.

## 0.1.4-test
Temporary line counting release for testnet.

* count stack temporary impl: make loginfo counts closer to Geth [temp/issue-248/count-stack-only](https://github.com/Consensys/besu-sequencer-plugins/tree/temp/issue-248/count-stack-only)
--
* fix: `Bytes.toUnsignedInteger` [#484](https://github.com/ConsenSys/besu-sequencer-plugins/pull/484) 
* perf: delay computations at trace time [#483](https://github.com/ConsenSys/besu-sequencer-plugins/pull/483)

## 0.1.3
- perf: improve `StackedSet` performances  [#466](https://github.com/ConsenSys/besu-sequencer-plugins/pull/466) 
- feat: implement L1 block & Keccak limits [#445](https://github.com/ConsenSys/besu-sequencer-plugins/pull/445)
- feat: partially implement EC_DATA [#475](https://github.com/ConsenSys/besu-sequencer-plugins/pull/475)
- fix: ensure trace files are always deleted [#462](https://github.com/ConsenSys/besu-sequencer-plugins/pull/462)


## 0.1.2
Release 8 for 23.10.4-SNAPSHOT of linea-besu
- changed default file name to toml [#476](https://github.com/ConsenSys/besu-sequencer-plugins/pull/476)
- feat: implement `BIN` counting [#471](https://github.com/ConsenSys/besu-sequencer-plugins/pull/471)
- Upgrade Linea Besu to 23.10.4-SNAPSHOT [#469](https://github.com/ConsenSys/besu-sequencer-plugins/pull/469)
- fix: incorrect address comparison [#470](https://github.com/ConsenSys/besu-sequencer-plugins/pull/470)
- fix: line count discrepancy [#468](https://github.com/ConsenSys/besu-sequencer-plugins/pull/468)

## 0.1.1
Release for 23.10.3-SNAPSHOT of linea-besu

## 0.1.0
- Initial build of besu-sequencer-plugins
- uses 23.10.3-SNAPSHOT as linea-besu version
