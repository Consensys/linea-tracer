#!/bin/bash

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
index=0

# Compress and rename each file in the target directory
for file in "$TARGET_DIR"/*; do
  if [ -f "$file" ]; then
    index=$((index + 1))
    original_filename=$(basename "$file")
    # Extract the base filename (the range part)
    base_name=$(echo "$original_filename" | grep -o '^[0-9]\+-[0-9]\+')
    new_filename="${base_name}.conflated.v0.2.0.lt.gz"
    echo "Compressing and renaming $file to $TARGET_DIR/$new_filename... ($index/$total_files)"
    gzip "$file"
    mv "${file}.gz" "$TARGET_DIR/$new_filename"
  fi
done

echo "Compression and renaming completed."
