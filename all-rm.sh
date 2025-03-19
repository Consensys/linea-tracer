#!/bin/bash

DEST_ARI="/home/amelie/go/src/github/Consensys/linea-tracer/arithmetization/build/jacoco"
DEST_REF="/home/amelie/go/src/github/Consensys/linea-tracer/reference-tests/build/jacoco"

rm -rf $DEST_ARI/fastReplayTests.exec
rm -rf $DEST_ARI/nightlyTests.exec
rm -rf $DEST_ARI/test.exec
rm -rf $DEST_ARI/weeklyTests.exec

rm -rf $DEST_REF/referenceBlockchainTests.exec
rm -rf $DEST_REF/referenceGeneralStateTests.exec
