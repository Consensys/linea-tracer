#!/bin/bash

# Number of CPUs to use for parallel processing
NUM_CPUS=96

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

# Get the total number of files to compress
total_files=$(find "$TARGET_DIR" -type f | wc -l)
index=1

# Compress and rename each file in the target directory
for file in "$TARGET_DIR"/*; do
  if [ -f "$file" ]; then
    original_filename=$(basename "$file")
    base_name=$(echo "$original_filename" | grep -o '^[0-9]\+-[0-9]\+')
    temp_filename="${base_name}.tmp.gz"
    new_filename="${base_name}.conflated.v0.2.0.lt.gz"
    echo "Compressing $file to $TARGET_DIR/$temp_filename... ($index/$total_files)"
    pigz -1 -p "$NUM_CPUS" -c "$file" > "$TARGET_DIR/$temp_filename"
    if [ $? -eq 0 ]; then
      mv "$TARGET_DIR/$temp_filename" "$TARGET_DIR/$new_filename"
      echo "Renamed $TARGET_DIR/$temp_filename to $TARGET_DIR/$new_filename"
    else
      echo "Compression failed for $file"
      rm -f "$TARGET_DIR/$temp_filename"
    fi
    index=$((index + 1))
  fi
done

echo "Compression and renaming completed."
