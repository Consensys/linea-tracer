#!/bin/bash

SOURCE_DIR="/home/amelie/Downloads"
SOURCE_NAME="jacoco-weekly-tests-exec-file"
DEST="/home/amelie/go/src/github/Consensys/linea-tracer/arithmetization/build/jacoco"
XML_REPORTS="/home/amelie/go/src/github/Consensys/linea-tracer/arithmetization/build/reports/jacoco"

./all-rm.sh
rm -rf $XML_REPORTS
unzip $SOURCE_DIR/$SOURCE_NAME -d $DEST
./gradlew jacocoWeeklyTestsReport
./gradlew sonar -Dtests=Weekly
