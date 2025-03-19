#!/bin/bash

SOURCE_DIR="/home/amelie/Downloads"
DEST_ARI="/home/amelie/go/src/github/Consensys/linea-tracer/arithmetization/build/jacoco"
DEST_REF="/home/amelie/go/src/github/Consensys/linea-tracer/reference-tests/build/jacoco"
SOURCE_NAME="jacoco-fast-replay-tests-exec-file"
XML_REPORTS_ARI="/home/amelie/go/src/github/Consensys/linea-tracer/arithmetization/reports/jacoco"
XML_REPORTS_REF="/home/amelie/go/src/github/Consensys/linea-tracer/reference-tests/build/reports/jacoco"

./all-rm.sh
rm -rf $XML_REPORTS_ARI
rm -rf $XML_REPORTS_REF
unzip $SOURCE_DIR/jacoco-unit-tests-exec-file -d $DEST_ARI
unzip $SOURCE_DIR/jacoco-fast-replay-tests-exec-file -d $DEST_ARI
unzip $SOURCE_DIR/jacoco-weekly-tests-exec-file -d $DEST_ARI
unzip $SOURCE_DIR/jacoco-nightly-tests-exec-file  -d $DEST_ARI
unzip $SOURCE_DIR/jacoco-blockchain-tests-exec-file -d $DEST_REF
unzip $SOURCE_DIR/jacoco-state-tests-exec-file -d $DEST_REF
./gradlew jacocoUnitFastReplayWeeklyNightlyReferenceBlockchainAndStateTestsReport
./gradlew sonar -Dtests=All
