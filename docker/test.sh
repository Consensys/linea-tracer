#!/bin/bash

export TEST_PATH=../tests
export GOSS_PATH=$TEST_PATH/goss-linux-${architecture}
export GOSS_OPTS="$GOSS_OPTS --format junit"
export GOSS_FILES_STRATEGY=cp
DOCKER_IMAGE=$1
DOCKER_FILE="${2:-$PWD/Dockerfile}"

i=0

## Checks on the Dockerfile
GOSS_FILES_PATH=$TEST_PATH/00 \
bash $TEST_PATH/dgoss dockerfile $DOCKER_IMAGE $DOCKER_FILE \
> ../reports/00.xml || i=`expr $i + 1`
# fail fast if we dont pass static checks
if [[ $i != 0 ]]; then exit $i; fi

# Test for normal startup with ports opened
# we test that things listen on the right interface/port, not what interface they advertise
GOSS_FILES_PATH=$TEST_PATH/01 \
bash $TEST_PATH/dgoss run $DOCKER_IMAGE --rpc-http-host="0.0.0.0" --data-path="/opt/sequencer/database"\
> ../reports/01.xml || i=`expr $i + 1`

exit $i
