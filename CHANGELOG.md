# Changelog

## 0.6.0-rc7
* feat: added test (which, again, works out of the box ...) ([#1290](https://github.com/Consensys/linea-tracer/pull/1290))
* fix: fix daily blockchain workflow ([#1152](https://github.com/Consensys/linea-tracer/pull/1152))
* fix: the base was indeed incorrectly extracted for MODEXP ([#1299](https://github.com/Consensys/linea-tracer/pull/1299))
* clean: some cleaning in signals and module triggering ([#1293](https://github.com/Consensys/linea-tracer/pull/1293))
* fix: BlockCapturer for Created Accounts ([#1291](https://github.com/Consensys/linea-tracer/pull/1291))

## 0.6.0-rc6
* docs: add TracerReadinessPlugin docs and re-introduce dev setup docs ([#1287](https://github.com/Consensys/linea-tracer/pull/1287))
* feat(req-limit): implement request limiting for trace generation and line counting ([#1241](https://github.com/Consensys/linea-tracer/pull/1241))
* chore: reset logging ([#1284](https://github.com/Consensys/linea-tracer/pull/1284))
* chore: enable working directory for corset command ([#1282](https://github.com/Consensys/linea-tracer/pull/1282))
* feat: new test (that works out of the box) ([#1275](https://github.com/Consensys/linea-tracer/pull/1275))
* fix: failing ranges for `SHF` with `0.6.0-rc1` ([#1268](https://github.com/Consensys/linea-tracer/pull/1268))
* fix: failing ranges for `MOD` with `0.6.0-rc1` ([#1266](https://github.com/Consensys/linea-tracer/pull/1266))
* fix(mmio): use `TBO` for `ramToLimb` instructions ([#1256](https://github.com/Consensys/linea-tracer/pull/1256))
* fix(tests): notation ([#1248](https://github.com/Consensys/linea-tracer/pull/1248))
* fix: add exception if we try to get chainId from a tx wo chainId (v = 27 or 28) ([#1210](https://github.com/Consensys/linea-tracer/pull/1210))
* perf: optimize stack related `WCP` operations ([#1242](https://github.com/Consensys/linea-tracer/pull/1242))

## 0.6.0-rc5
* Test CI using smaller runner (#1251)
* perf(WCP): split StackedSet per opcode (#1249)
* Remove duplicate `ReplayTests.java` file (#1253)
* fix(abortingCondiftions): handle EIP2681 MAX_NONCE (#1188)
* added `GAS` module to `getModulesToCount()` (#1247)
* use CliqueProtocolSchedule instead of MainnetProtocolSchedule to extr… (#1222)


## 0.6.0-rc4
*  Fix file permission ([#1243](https://github.com/Consensys/linea-tracer/pull/1168))

## 0.6.0-rc3
* chore: preliminary selection of nightly tests ([#1176](https://github.com/Consensys/linea-tracer/pull/1176))
* Add a trace log on existing operations ([#1237](https://github.com/Consensys/linea-tracer/pull/1237))
* count L2L1Logs ([#1234](https://github.com/Consensys/linea-tracer/pull/1234))
* Fix for legacy transactions with chainID ([#1231](https://github.com/Consensys/linea-tracer/pull/1231))
* Show only failing tests in CI output ([#1217](https://github.com/Consensys/linea-tracer/pull/1217))
* alphabetical ordering ([#1228](https://github.com/Consensys/linea-tracer/pull/1228))
* Some optimizations for CI and Reference tests ([#1168](https://github.com/Consensys/linea-tracer/pull/1168))



## 0.6.0-rc2
* fix(oob): update constraints ([#1224](https://github.com/Consensys/linea-tracer/pull/1224))
* added ranges from issue ([#1225](https://github.com/Consensys/linea-tracer/pull/1225))
* Fix for NPE in `SelfdestructSection` ([#1221](https://github.com/Consensys/linea-tracer/pull/1221))
* Update README.md ([#1206](https://github.com/Consensys/linea-tracer/pull/1206))
* reintegrate ref table in module to count ([#1215](https://github.com/Consensys/linea-tracer/pull/1215))
* typo in RlpTxn for small signature ([#1208](https://github.com/Consensys/linea-tracer/pull/1208))
* update constraints ([#1219](https://github.com/Consensys/linea-tracer/pull/1219))
* fix(exp): remove not necessary computation ([#1204](https://github.com/Consensys/linea-tracer/pull/1204))
* [README] fix broken link ([#1207](https://github.com/Consensys/linea-tracer/pull/1207))
* [996] Add features to write multi block tests ([#1178](https://github.com/Consensys/linea-tracer/pull/1178))
* 1126 oob issue ([#1134](https://github.com/Consensys/linea-tracer/pull/1134))
* Fix #1200 ([#1201](https://github.com/Consensys/linea-tracer/pull/1201))
* update constraints ([#1199](https://github.com/Consensys/linea-tracer/pull/1199))
* Set daily workflows to only execute on weekdays ([#1179](https://github.com/Consensys/linea-tracer/pull/1179))
* build: build a separate zkevm.bin for reference tests ([#1177](https://github.com/Consensys/linea-tracer/pull/1177))
* Rectification of `AccountSnapshot`'s for some of the `CallSection` after care methods ([#1174](https://github.com/Consensys/linea-tracer/pull/1174))
* add PRC_DATA module to moduleToCount ([#1186](https://github.com/Consensys/linea-tracer/pull/1186))
* `MOD` constraint failure on mainnet ([#1181](https://github.com/Consensys/linea-tracer/pull/1181))
* fix: add validations to block number params of trace generation and line counting JSON-RPC endpoints ([#1191](https://github.com/Consensys/linea-tracer/pull/1191))
* update constraints ([#1184](https://github.com/Consensys/linea-tracer/pull/1184))
* feat: use .tmp file extension while writing a conflated trace in order to indicate that the trace is incomplete if it stays with the .tmp extension ([#1170](https://github.com/Consensys/linea-tracer/pull/1170))
* feat: exceptions vs tracedexceptions ([#1133](https://github.com/Consensys/linea-tracer/pull/1133))
* Fixed `RETURN` pricing ([#1166](https://github.com/Consensys/linea-tracer/pull/1166))
* fix(ecAddTest): remove comment ([#1165](https://github.com/Consensys/linea-tracer/pull/1165))

## 0.6.0-rc1
* Refactor ReplayExecutionEnviroment and ToyExecutionEnvironment ([#1159](https://github.com/Consensys/linea-tracer/pull/1159))
* chore: make spotless and checkSPDXHeader a mandatory step after compilation in order to avoid not passing through the linting process ([#1161](https://github.com/Consensys/linea-tracer/pull/1161))
* Enable reporting in `go-corset` ([#1150](https://github.com/Consensys/linea-tracer/pull/1150)) to get more information about failing constraints.
* Set besuVersion=24.9-delivery32 ([#1157](https://github.com/Consensys/linea-tracer/pull/1157))
* regenerate Trace.java for the HUB
* delete deleted columns
* fix: block-1339346-context-revert-twice ([#1151](https://github.com/Consensys/linea-tracer/pull/1151))
* fix: delete duplicate of setting revertStamp + set the revertStamp at the right moment
* fix(copy instruction) take destOffset to compute memoryExpension, not source offset
* Fixed failing precondition for ECRECOVER call ([#1154](https://github.com/Consensys/linea-tracer/pull/1154))
* fix: Modexp fix
* Fix(oob): fixed size parameter in OOB_INST_DEPLOYMENT
* feat: tidy up `ToyExecutionEnvironment` and remove `ToyWorld` ([#1143](https://github.com/Consensys/linea-tracer/pull/1143))This tidies up the `ToyExecutionEnvironment` in several ways.  Firstly, it updates the manner in which the `MainnetTransactionProcessor` is created by reusing as much from BESU as possible; secondly, it removes`ToyWorld` altogether (as this is no longer needed); finally, it removes`ToyExecutionEnvironment.execute()` since this is no longer being used.
* Support Mainnet vs Sepolia Tests This adds support for distinguishing between Mainnet and Sepolia tests. This is helpful for the odds cases where we want a replay which was inteded for sepolia.
* Remove `getMainnetTransactionProcessorOrig()`. This removes the now redundant (original) method for getting the mainnet transaction processor.
* Fix broken replay test.`modexpTriggeringNonAlignedFirstLimbSingleSourceMmuModexp` was missing the chain identifier.  I'm assuming LINEA_MAINNET for now.
* chore: switch to ubuntu-22.04-16core github runner on the tests stage ([#973](https://github.com/Consensys/linea-tracer/pull/973))
* Update Linea-Besu to 24.9-delivery30 ([#1131](https://github.com/Consensys/linea-tracer/pull/1131))
* fix failing mmu constraints ([#1123](https://github.com/Consensys/linea-tracer/pull/1123))
* Add nightly tests tag and improve ci test run time
* fix env
* Tag issue tests as nightly
* separate unit tests and replay tests
* enable concurrent replay tests
* 1049 filter and run reference tests + GHA ([#1132](https://github.com/Consensys/linea-tracer/pull/1132))
* Added failedTestsFilePath, failedModule and failedConstraint to filter reference blockchain tests.
* Added new GHA workflow for automatically running blockchain tests each evening and storing the results arfifact
* Added header
* Increase threads for `corset` on Github CI ([#1144](https://github.com/Consensys/linea-tracer/pull/1144))
  This changes the default number of threads to be used when running `RustCorsetValidator` from `2` to the number of available cores. The ability to override this using `CORSET_THREADS` remains.
* [997] Migrate Stp and OobCall test to v2 ([#1115](https://github.com/Consensys/linea-tracer/pull/1115))
* Migrate TxSkip test to V2 ([#1080](https://github.com/Consensys/linea-tracer/pull/1080))
* fix(hub:skip): coinbase snapshot if not yet in the world at start tx
* fix after merger Added single range ... that isn't problematic ([#1128](https://github.com/Consensys/linea-tracer/pull/1128))
* Debugging 6690-6699 ([#1110](https://github.com/Consensys/linea-tracer/pull/1110))
* Range 10-20 ([#1129](https://github.com/Consensys/linea-tracer/pull/1129))
* test: new (unexceptional) range
* fix CountOnlyModule ([#1120](https://github.com/Consensys/linea-tracer/pull/1120))
* 1049 - Reference test tool ([#1108](https://github.com/Consensys/linea-tracer/pull/1108))
* Created TestWatcher to intercept failing reference tests and record the failing module and constraint
* Updated template to include ReferenceTestWatcher
* Updated linea-constraints submodule up to kebab-case refactor 2a49dae
* Implemented getModuleFromFailedConstraint() and removed module from constraint name
* Separated mapping and read/write logic from watcher
* Added tests for MapFailedReferenceTestsTool and made fixes to the tool
* Remove comments
* Added header to new files
* Improved readability and names of methods

## 0.5.3-beta
* fix(return): OOGX for return for deployment + call SHAKIRA for return + cleaning ([#1105](https://github.com/Consensys/linea-tracer/pull/1105))
* Failing tests file ([#1098](https://github.com/Consensys/linea-tracer/pull/1098))
* fix(oob): inst modexp pircing f of max ([#1107](https://github.com/Consensys/linea-tracer/pull/1107))
* chore: add validation and usage for bulk capture ([#1096](https://github.com/Consensys/linea-tracer/pull/1096))
* TX_SKIP: canonical snapshots + preconditions ([#1094](https://github.com/Consensys/linea-tracer/pull/1094))
* feat: update replay tests with tx outcomes ([#1091](https://github.com/Consensys/linea-tracer/pull/1091))
* Tweak Memory Settings for Go Corset
* fix(selfdestruct): invoke selfDestructAtTransactionEnd ([#1022](https://github.com/Consensys/linea-tracer/pull/1022))
* Renaming zkevm-constraints to linea-constraints + .gitmodules update ([#1083](https://github.com/Consensys/linea-tracer/pull/1083))

## 0.5.2-beta
* fix(callDataCopy): source ID ([#1081](https://github.com/Consensys/linea-tracer/pull/1081))
* add a binary search script ([#1073](https://github.com/Consensys/linea-tracer/pull/1073))
* chore: add more description for bulkReplay ([#1058](https://github.com/Consensys/linea-tracer/pull/1058))
* Leo's new ranges ([#1051](https://github.com/Consensys/linea-tracer/pull/1051))
* fix: precompiles are considered COLD when using BESU frame method ([#1079](https://github.com/Consensys/linea-tracer/pull/1079))
* perf: more efficient data storage structure ([#1034](https://github.com/Consensys/linea-tracer/pull/1034))
* [997] Migrate most of the tests to ToyExecutionEnviromentV2 ([#1066](https://github.com/Consensys/linea-tracer/pull/1066))
* Feat/issue 975/ec data add relevant limits for ecpairing ([#993](https://github.com/Consensys/linea-tracer/pull/993))
* fix(mmu call): need CFI for failed CREATE2 that doesn't trigger the ROM ([#1070](https://github.com/Consensys/linea-tracer/pull/1070))
* fix(selfdestruct): wrap ([#1074](https://github.com/Consensys/linea-tracer/pull/1074))
* Feat: bulk capture and bulk replay ([#1001](https://github.com/Consensys/linea-tracer/pull/1001))
* Fix initial contract nonce ([#1056](https://github.com/Consensys/linea-tracer/pull/1056))
* fix(precompile): defers with wrong id ([#1055](https://github.com/Consensys/linea-tracer/pull/1055))
* Use `ReferenceTestWorldState` instead of `ToyWorld` ([#1045](https://github.com/Consensys/linea-tracer/pull/1045))
* fix: many things ... ([#1027](https://github.com/Consensys/linea-tracer/pull/1027))
* [934] Use ToyExecutionEnvironmentV2 for blockhash test ([#1040](https://github.com/Consensys/linea-tracer/pull/1040))
* fix: update `Trace.java` file for Hub ([#1037](https://github.com/Consensys/linea-tracer/pull/1037))
* Tests/ethereum ([#1012](https://github.com/Consensys/linea-tracer/pull/1012))
* Expose true output from tx result failure ([#1030](https://github.com/Consensys/linea-tracer/pull/1030))
* format(txndata): ras ([#1032](https://github.com/Consensys/linea-tracer/pull/1032))
* feat: update gradle test config to show better output ([#1021](https://github.com/Consensys/linea-tracer/pull/1021))
* fix(mmu): anyToRamWithPadding mixed subcase ([#1016](https://github.com/Consensys/linea-tracer/pull/1016))
* Fix for `BLOCKHASH` conversion error ([#1025](https://github.com/Consensys/linea-tracer/pull/1025))
* perf(MMU): discard CallStackReader ([#988](https://github.com/Consensys/linea-tracer/pull/988))
* feat: capture block hashes in `BlockCapturer` for replay ([#1019](https://github.com/Consensys/linea-tracer/pull/1019))
* add tests for exceptions. ([#1007](https://github.com/Consensys/linea-tracer/pull/1007))
* Fix #998 ([#1015](https://github.com/Consensys/linea-tracer/pull/1015))
* Update Linea-Besu dependency to 24.8-develop-915fcb01 (delivery-29) ([#1011](https://github.com/Consensys/linea-tracer/pull/1011))
* feat: update blockcapturer to capture outcomes ([#982](https://github.com/Consensys/linea-tracer/pull/982))
* Fix stp fix ([#1003](https://github.com/Consensys/linea-tracer/pull/1003))
* Make `ReturnSection`'s `resolveAtContextReEntry` method use the createe frame ([#992](https://github.com/Consensys/linea-tracer/pull/992))
* feat(corset): enable `go-corset` in `gradle` action ([#938](https://github.com/Consensys/linea-tracer/pull/938))
* fix(STP): fixed exists
* fix(StpCall): made STP fillers private
* fix(CallSection): removed redundant stpCallForCalls call
* fix(CreateSection): removed redundant stpCallForCreates call

## 0.5.1-beta
* fix: removed old documentation files ([#995](https://github.com/Consensys/linea-tracer/pull/995))
* feat(txndata): implement txn data update handling the max nonce ([#987](https://github.com/Consensys/linea-tracer/pull/987))
* feat(hub): fill pMiscOobData9 ([#985](https://github.com/Consensys/linea-tracer/pull/985))
* feat(ecdata): use constants for call data sizes ([#983](https://github.com/Consensys/linea-tracer/pull/983))
* fix(hub): static exceptions that should not be thrown. ([#986](https://github.com/Consensys/linea-tracer/pull/986))
* feat(ecdata): switch from stacked set to stacked list ([#981](https://github.com/Consensys/linea-tracer/pull/981))
* fix(Makefile): updated file path to AccountFragment.java

## 0.5.0-beta
* Merged the HUB branch ([#748](https://github.com/Consensys/linea-tracer/pull/748))

## 0.4.0-rc2
* fix: change implementation of gasAvailableForChildCall due to having side effects in TangerineWhistleGasCalculator ([#950](https://github.com/Consensys/linea-tracer/pull/950))
* fix: `ToyWorld.commit()` ([#966](https://github.com/Consensys/linea-tracer/pull/966))
* feat(ecadd): add test ([#956](https://github.com/Consensys/linea-tracer/pull/956))
* fix: Use correct CHAINID in EVM ([#947](https://github.com/Consensys/linea-tracer/pull/947))
* fix: swap ordering of r/s fields in tx snapshot ([#946](https://github.com/Consensys/linea-tracer/pull/946))

## 0.4.0-rc1
* feat(toy-exec-env-v2): add new ToyExecutionEnvironment that builds the General State Test Case spec to run tests ([#842](https://github.com/Consensys/linea-tracer/pull/842))
* feat: initial Integration of Go Corset ([#907](https://github.com/Consensys/linea-tracer/pull/907))
* feat(exp): update ([#937](https://github.com/Consensys/linea-tracer/pull/937))
* fix(blockCapturer): missing handling of selfdestruct ([#936](https://github.com/Consensys/linea-tracer/pull/936))
* docs: retires zk-EVM ([#903](https://github.com/Consensys/linea-tracer/pull/903))
* fix: add replay test for incident 777 on zkGeth mainnet ([#927](https://github.com/Consensys/linea-tracer/pull/927))
* test(ecpairing): implement extensive test for ecpairing ([#822](https://github.com/Consensys/linea-tracer/pull/822))([#909](https://github.com/Consensys/linea-tracer/pull/909))

## 0.3.0-rc2
* feat: Update Linea-Besu to 24.7-develop-c0029e6 ([#905](https://github.com/Consensys/linea-tracer/pull/905))

## 0.3.0-rc1
* feat: upgrade besu version to 24.7-develop-f812936 ([#880](https://github.com/Consensys/linea-arithmetization/pull/880))

## 0.2.0-rc5
* fix(ecData): ugly hack to discard unsuccessful EcRecover call ([#891](https://github.com/Consensys/linea-arithmetization/pull/891))

## 0.2.0-rc4
* fix: init config object only once ([#873](https://github.com/Consensys/linea-arithmetization/pull/873))
* feat: improve design of shared and private CLI options ([#864](https://github.com/Consensys/linea-arithmetization/pull/864))

## 0.2.0-rc3
* fix: make --plugin-linea-conflated-trace-generation-traces-output-path option required to avoid faulty registration of the trace generation RPC endpoint ([#858](https://github.com/Consensys/linea-arithmetization/pull/858))
* feat: separate shared and private CLI options ([#856](https://github.com/Consensys/linea-arithmetization/pull/856))

## 0.2.0-rc2
* feat: improve ZkTracer initialization time by doing only once Opcodes and spillings loading from disk resources ([#720](https://github.com/Consensys/linea-arithmetization/pull/720))
* perf: parallelize refundedGas for big transactions ([#793](https://github.com/Consensys/linea-arithmetization/pull/793))

## 0.2.0-rc1
* feat: add PRECOMPILE_ECPAIRING_G2_MEMBERSHIP_CALLS in spillings.toml and did some renaming ([#819](https://github.com/Consensys/linea-arithmetization/pull/819))
* feat: optimise trace generation (except hub) ([#838](https://github.com/Consensys/linea-arithmetization/pull/838))

## 0.1.5-rc6
* Migrating of `TRACES_DIR` env var to
`plugin-linea-conflated-trace-generation-traces-output-path` CLI option that can be included in the toml config files.
The path specified in `plugin-linea-conflated-trace-generation-traces-output-path` will be created automatically if it does not exist.
This time this has nothing to do with the `ContinuousTracingPlugin` [#830](https://github.com/Consensys/linea-arithmetization/pull/830).

## 0.1.5-rc5
* Migrating of `TRACES_DIR` env var to `plugin-linea-continuous-tracing-traces-dir` CLI option that can be included in the
toml config files. The path specified in `plugin-linea-continuous-tracing-traces-dir` will be created automatically
  if it does not exist [#825](https://github.com/Consensys/linea-arithmetization/pull/825).

## 0.1.4-test21
Test pre-release 21 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix: capture SSTORE-touched storage slots for correct gas computations [#606](https://github.com/Consensys/linea-arithmetization/pull/606)
* build: make the build script portable, explicit dependency on Go & GCC, test libcompress build [#621](https://github.com/Consensys/linea-arithmetization/pull/621)
* Update after the refactor of transaction selection service [#626](https://github.com/Consensys/linea-arithmetization/pull/626)
* Use the right classloader to load the native library [#628](https://github.com/Consensys/linea-arithmetization/pull/628)

## 0.1.4-test20
Test pre-release 20 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Get L2L1 settings from CLI options [#591](https://github.com/Consensys/linea-arithmetization/pull/591)
* feat: add a replay capture script [#600](https://github.com/Consensys/linea-arithmetization/pull/600)
* move compress native into plugin repo [#604](https://github.com/Consensys/linea-arithmetization/pull/604)
* Add compression [#605](https://github.com/Consensys/linea-arithmetization/pull/605)
* Update for the new bad block manager [#607](https://github.com/Consensys/linea-arithmetization/pull/607)

## 0.1.4-test19
Test pre-release 19 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Avoid returning an estimated priority fee that is less than the min gas price [#598](https://github.com/Consensys/linea-arithmetization/pull/598)

## 0.1.4-test18
Test pre-release 18 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix: check that spilling and limits file contain all counted modules [#592](https://github.com/Consensys/linea-arithmetization/pull/592)

## 0.1.4-test18-RC3
Test pre-release 18-RC3 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
*  Use compressed tx size also when selecting txs from block creation [#590](https://github.com/Consensys/linea-arithmetization/pull/590)

## 0.1.4-test18-RC2
Test pre-release 18-RC2 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
*  Fix linea_estimateGas reports Internal error when value or gas price is missing [#587](https://github.com/Consensys/linea-arithmetization/pull/587)

## 0.1.4-test18-RC1
Test pre-release 18-RC1 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Linea estimate gas endpoint [#585](https://github.com/Consensys/linea-arithmetization/pull/585)

## 0.1.4-test17
Test pre-release 17 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* tests: drop huge random tests [#563](https://github.com/Consensys/linea-arithmetization/pull/563)
* feat(modexp-data): implement MODEXP_DATA module [#547](https://github.com/Consensys/linea-arithmetization/pull/547)
* feat: mechanics to capture conflations & replay them as test cases [#561](https://github.com/Consensys/linea-arithmetization/pull/561)
* perf(EUC): one less column [#570](https://github.com/Consensys/linea-arithmetization/pull/570)
* docs: Add basic plugins doc [#509](https://github.com/Consensys/linea-arithmetization/pull/509)
* Check upfront profitability + Unprofitable txs cache and retry limit [#565](https://github.com/Consensys/linea-arithmetization/pull/565)
* Avoid reprocessing txs that go over line count limit [#571](https://github.com/Consensys/linea-arithmetization/pull/571)

## 0.1.4-test16
Test pre-release 16 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix: bug-compatibility with Geth
* fix: PubHash 16 factor

Full changeset https://github.com/Consensys/linea-arithmetization/compare/v0.1.4-test15...v0.1.4-test16

## 0.1.4-test15
release rebase off of main
* add option to adjust the tx size used to calculate the profitability of a tx during block creation(#562)[https://github.com/Consensys/linea-arithmetization/pull/562]

## 0.1.4-test14
release rebase off of main
Test pre-release 14 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Fix log of line counts in case of block limit reached + minor changes [#555](https://github.com/ConsenSys/linea-arithmetization/pull/555)
* build: update Corset to 9.3.0 [#554](https://github.com/ConsenSys/linea-arithmetization/pull/554)

## 0.1.4-test13
Test pre-release 13 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix stackedSet [c3f226775f24508b93a758e4226a51ae386d76a5](https://github.com/Consensys/linea-arithmetization/commit/c3f226775f24508b93a758e4226a51ae386d76a5)

## 0.1.4-test12
Test pre-release 12 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix: stacked set multiple insertions in a single transaction (#548)

## 0.1.4-test11
Test pre-release 11 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* same as 0.1.4-test10

## 0.1.4-test10
Test pre-release 10 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix: semantics of LinkedList (#544)
* refactor: add @EqualsAndHashCode annotations and remove corresponding methods (#541)

## 0.1.4-test9
Test pre-release 9 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Bump Linea Besu to 24.1.1-SNAPSHOT

## 0.1.4-test8
Test pre-release 8 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Add profitable transaction selector [#530](https://github.com/Consensys/linea-arithmetization/pull/530)
* temp: geth-compatibily hacks [820918a](https://github.com/Consensys/linea-arithmetization/commit/820918a39e8d394e73b8de85a46391ffe7d314b1)

## 0.1.4-test7
Test pre-release 7 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* fix: invalid SStore gas computation [#532](https://github.com/Consensys/linea-arithmetization/pull/532)

## 0.1.4-test6
Test pre-release 6, fix: [make precompile counters work](https://github.com/Consensys/linea-arithmetization/commit/10f03ead5207746f253703a328f13988ed9b9305)
* feat: implement fake hashdata/info [Franklin Delehelle]
* temp: geth-compatibily hacks [Franklin Delehelle]
* refactor: group RLPs modules, use retro-compatible module keys [#508](https://github.com/ConsenSys/linea-arithmetization/pull/508)
* [MINOR] Add javadoc [#507](https://github.com/ConsenSys/linea-arithmetization/pull/507)
* style: update name of prec limits to avoid confusion with old geth name [#506](https://github.com/ConsenSys/linea-arithmetization/pull/506)
* perf: cache tx-specific line counter [#497](https://github.com/ConsenSys/linea-arithmetization/pull/497)
* fix: continuous tracing plugin start check [#500](https://github.com/ConsenSys/linea-arithmetization/pull/500)
* fix: lookup txndata <-> wcp [#488](https://github.com/ConsenSys/linea-arithmetization/pull/488)
* fix(romLex): wrong stack arg for extcodecopy address [#498](https://github.com/ConsenSys/linea-arithmetization/pull/498)

## 0.1.4-test3
Test pre-release 3 from [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
* Log ZkTracer counters for every produced block [#485](https://github.com/ConsenSys/linea-arithmetization/pull/485)
* fix: overflow for modexp arg [#489](https://github.com/ConsenSys/linea-arithmetization/pull/489)
* bin reimplementation [#473](https://github.com/ConsenSys/linea-arithmetization/pull/473)
* applyMavenExclusions=false [#477](https://github.com/ConsenSys/linea-arithmetization/pull/477)

## 0.1.4-test2
Testing pre-release from branch test-release/v0.1.4-test2

* revert make loginfo counts closer to Geth
* head: disable stp & txndata

## 0.1.4-test
Temporary line counting release for testnet.

* count stack temporary impl: make loginfo counts closer to Geth [temp/issue-248/count-stack-only](https://github.com/Consensys/linea-arithmetization/tree/temp/issue-248/count-stack-only)
  --
* fix: `Bytes.toUnsignedInteger` [#484](https://github.com/ConsenSys/linea-arithmetization/pull/484)
* perf: delay computations at trace time [#483](https://github.com/ConsenSys/linea-arithmetization/pull/483)

## 0.1.3
- perf: improve `StackedSet` performances  [#466](https://github.com/ConsenSys/linea-arithmetization/pull/466)
- feat: implement L1 block & Keccak limits [#445](https://github.com/ConsenSys/linea-arithmetization/pull/445)
- feat: partially implement EC_DATA [#475](https://github.com/ConsenSys/linea-arithmetization/pull/475)
- fix: ensure trace files are always deleted [#462](https://github.com/ConsenSys/linea-arithmetization/pull/462)


## 0.1.2
Release 8 for 23.10.4-SNAPSHOT of linea-besu
- changed default file name to toml [#476](https://github.com/ConsenSys/linea-arithmetization/pull/476)
- feat: implement `BIN` counting [#471](https://github.com/ConsenSys/linea-arithmetization/pull/471)
- Upgrade Linea Besu to 23.10.4-SNAPSHOT [#469](https://github.com/ConsenSys/linea-arithmetization/pull/469)
- fix: incorrect address comparison [#470](https://github.com/ConsenSys/linea-arithmetization/pull/470)
- fix: line count discrepancy [#468](https://github.com/ConsenSys/linea-arithmetization/pull/468)

## 0.1.1
Release for 23.10.3-SNAPSHOT of linea-besu

## 0.1.0
- Initial build of linea-arithmetization
- uses 23.10.3-SNAPSHOT as linea-besu version
