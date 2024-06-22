#!/bin/bash

# Number of CPUs to use for parallel processing
NUM_CPUS=48

# Check if the target directory is provided as an argument
if [ -z "$1" ]; then
  echo "Usage: $0 <target_directory>"
  exit 1
fi

# Directory containing the files to compress
TARGET_DIR="$1"

# Check if the target directory exists
if [ ! -d "$TARGET_DIR" ]; then
  echo "The directory $TARGET_DIR does not exist."
  exit 1
fi

# Function to compress and rename a single file
compress_and_rename() {
  file="$1"
  TARGET_DIR="$2"
  original_filename=$(basename "$file")
  base_name=$(echo "$original_filename" | grep -o '^[0-9]\+-[0-9]\+')
  new_filename="${base_name}.conflated.v0.2.0.lt.gz"
  echo "Compressing and renaming $file to $TARGET_DIR/$new_filename..."
  gzip "$file"
  mv "${file}.gz" "$TARGET_DIR/$new_filename"
}

export -f compress_and_rename

# Get the total number of files to compress
total_files=$(find "$TARGET_DIR" -type f | wc -l)
index=0

# Find all files and compress them in parallel
find "$TARGET_DIR" -type f | xargs -I {} -P "$NUM_CPUS" bash -c 'compress_and_rename "{}" "$0"' "$TARGET_DIR"

echo "Compression and renaming completed."
