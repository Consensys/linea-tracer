# Besu zkBesu-tracer Plugin

A zk-evm tracing implementation for [Hyperledger Besu](https://github.com/hyperledger/besu) based on
an [existing implementation in Go](https://github.com/ConsenSys/zk-evm/).

## Prerequisites

- Java 17

```
brew install openjdk@17
```

- Install Go

```
brew install go
```

- Install Rust

```
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh

# Use local git executable to fetch from repos (needed for private repos)
echo "net.git-fetch-with-cli=true" >> .cargo/config.toml
```

- Install Corset

```
cargo install --git ssh://git@github.com/ConsenSys/corset
```

- Clone zk-geth & compile zkevm.bin

```
git clone git@github.com:ConsenSys/zk-geth.git --recursive

cd zk-geth/zk-evm
make zkevm.bin
```

- Set environment with path to zkevm.bin

```
export ZK_EVM_BIN=PATH_TO_ZK_GETH/zk-evm/zkevm.bin
```

## Development Setup

**Step 0.** Install [pre-commit](https://pre-commit.com/):

```shell
pip install pre-commit

# For macOS users.
brew install pre-commit
```

Then run `pre-commit install` to setup git hook scripts.
Used hooks can be found [here](.pre-commit-config.yaml).

______________________________________________________________________

NOTE

> `pre-commit` aids in running checks (end of file fixing,
> markdown linting, go linting, runs go tests, json validation, etc.)
> before you perform your git commits.

______________________________________________________________________

**Step 1.** Run tests

```
# Run all tests
./gradlew clean test

# Run only unit tests
./gradlew clean unitTests

# Run only acceptance tests
./gradlew clean corsetTests
```

## Debugging traces

JSON files can be debugged with the following command:

```
corset check -T JSON_FILE -v $ZK_EVM_BIN
```
