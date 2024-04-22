#!/bin/bash
set -euo pipefail

LINEA_ARITHMETIZATION_VERSION=${1:?Must specify Linea Arithmetization version}
#TAR_DIST=${2:?Must specify path to tar distribution}
#ZIP_DIST=${3:?Must specify path to zip distribution}
JAR_PATH=${2:?Must specify path to built jar file}
POM_FILE_PATH=${3:?Must specify path to pom file of built project}

ENV_DIR=./build/tmp/cloudsmith-env
if [[ -d ${ENV_DIR} ]] ; then
    source ${ENV_DIR}/bin/activate
else
    python3 -m venv ${ENV_DIR}
    source ${ENV_DIR}/bin/activate
fi

python3 -m pip install --upgrade cloudsmith-cli

#cloudsmith push raw consensys/linea-arithmetization $TAR_DIST --republish --name 'linea-arithmetization.tar.gz' --version "${LINEA_ARITHMETIZATION_VERSION}" --summary "Linea Arithmetization ${LINEA_ARITHMETIZATION_VERSION} binary distribution" --description "Binary distribution of Linea Arithmetization ${LINEA_ARITHMETIZATION_VERSION}." --content-type 'application/tar+gzip'
#cloudsmith push raw consensys/linea-arithmetization $ZIP_DIST --republish --name 'linea-arithmetization.zip' --version "${LINEA_ARITHMETIZATION_VERSION}" --summary "Linea Arithmetization ${LINEA_ARITHMETIZATION_VERSION} binary distribution" --description "Binary distribution of Linea Arithmetization ${LINEA_ARITHMETIZATION_VERSION}." --content-type 'application/zip'
cloudsmith push maven consensys/linea-arithmetization "$JAR_PATH" \
  --version "$LINEA_ARITHMETIZATION_VERSION" \
  --pom-file "$POM_FILE_PATH"
