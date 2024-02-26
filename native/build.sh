#!/bin/sh

# Initialize external vars - need this to get around unbound variable errors
SKIP_GRADLE="$SKIP_GRADLE"

# Exit script if you try to use an uninitialized variable.
set -o nounset

# Exit script if a statement returns a non-true return value.
set -o errexit

# Use the error status of the first failure, rather than that of the last item in a pipeline.
set -o pipefail


SCRIPTDIR=$( dirname -- "$( readlink -f -- "$0"; )"; )

# delete old build dir, if exists
rm -rf "$SCRIPTDIR/compress/build/native" || true
mkdir -p "$SCRIPTDIR/compress/build/native"

if [[ "$OSTYPE" == "msys" ]]; then
  LIBRARY_EXTENSION=dll
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
  LIBRARY_EXTENSION=so
elif [[ "$OSTYPE" == "darwin"* ]]; then
  LIBRARY_EXTENSION=dylib
else
  echo "*** Unknown OS: $OSTYPE"
  exit 1
fi


if ! command -v go &> /dev/null; then
    echo "*** The Go compiler could not be found"
    exit 1
fi

cd "$SCRIPTDIR/compress/compress-jni"
echo "Building Go module libcompress_jni.$LIBRARY_EXTENSION for $OSTYPE"
CGO_ENABLED=1 go build -buildmode=c-shared -o libcompress_jni.$LIBRARY_EXTENSION compress-jni.go
mv libcompress_jni.* "$SCRIPTDIR/compress/build/native"
