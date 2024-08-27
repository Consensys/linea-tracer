#!/bin/bash

# Help message
function show_help {
    echo "Usage: $0 [--help]"
    echo ""
    echo "  --help       Print this message"
    exit 1
}

# Check if --help is passed
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --help) show_help ;;
        *) echo "Unknown parameter passed: $1"; show_help ;;
    esac
    shift
done

# File containing the ranges
range_file="./range.csv"

# Generate a unique temporary directory name
uuid=$(uuidgen)
remote_dir="/tmp/replays-$uuid"

# Check if the range file exists
if [ ! -f "$range_file" ]; then
    echo "Error: range file $range_file does not exist"
    exit 1
fi

# Initialize the full command
full_cmd="mkdir -p $remote_dir; "

# Variables to track the very first and very last ranges
very_first=""
very_last=""
number_of_ranges=0
skipped_ranges=0

# Read the range file and process each range
while IFS= read -r line || [[ -n "$line" ]]; do
    # Debug: Show the current line being processed

    # Skip empty lines
    if [ -z "$line" ]; then
        echo "Debug: Skipping empty line"
        continue
    fi

    # Extract start and end from the line
    start=$(echo "$line" | cut -d'-' -f1)
    end=$(echo "$line" | cut -d'-' -f2)

    # Debug: Show extracted start and end values

    # Check if start and end ranges are valid
    if [ -z "$start" ] || [ -z "$end" ] || [ "$start" -gt "$end" ]; then
        echo "[WARNING]: Skipping invalid range: '$line'"
        skipped_ranges=$((skipped_ranges + 1))
        continue
    fi

    # Track the very first and very last ranges
    if [ -z "$very_first" ] || [ "$start" -lt "$very_first" ]; then
        very_first="$start"
    fi
    if [ -z "$very_last" ] || [ "$end" -gt "$very_last" ]; then
        very_last="$end"
    fi

    # Increment the number of ranges
    number_of_ranges=$((number_of_ranges + 1))

    # Set remote filename
    remote_filename="$remote_dir/$start-$end.json.gz"

    echo "Capturing conflation $start - $end"
    echo "Writing replay to \`$remote_filename\`"

    # Form payload
    payload=$(cat <<EOF
{
   "jsonrpc":"2.0",
   "method":"linea_captureConflation",
   "params":["$start", "$end"], "id":"1"
}
EOF
    )

    # Append the command to full_cmd
    full_cmd+="curl -X POST 'http://localhost:8545' --data '$payload' | jq '.result.capture' -r | jq . | gzip > $remote_filename; echo 'Captured $start-$end'; sleep 3; "
done < "$range_file"

# Set the compressed file name with the number of ranges context
compressed_file="/tmp/replays_${number_of_ranges}_conflations_between_${very_first}_and_${very_last}.tar.gz"
local_compressed_file="replays_${number_of_ranges}_conflations_between_${very_first}_and_${very_last}.tar.gz"
full_cmd+="cd /tmp; tar -czf $compressed_file replays-$uuid; echo 'Compressed capture_files'; "

# Debug: Show the full command
echo "Debug: Full command: $full_cmd"

# Execute the full command over SSH
ssh ec2-user@ec2-107-21-85-50.compute-1.amazonaws.com -C "$full_cmd"

# Download the compressed file
scp ec2-user@ec2-107-21-85-50.compute-1.amazonaws.com:$compressed_file .

# Clean up the remote directories and compressed file
ssh ec2-user@ec2-107-21-85-50.compute-1.amazonaws.com -C "rm -rf /tmp/replays-$uuid && rm $compressed_file"

# Extract and move the files
tar -xvf $local_compressed_file
mkdir -p replays
mv ./replays-$uuid/* ./replays

# Remove the extracted folder
rm -rf ./replays-$uuid

# Remove the local compressed file. Comment out this line to keep the compressed file locally.
rm $local_compressed_file

echo "Replay files for ${number_of_ranges} conflations between ${very_first} and ${very_last} have been processed and downloaded."
echo "Skipped ranges due to invalid configurations: $skipped_ranges."
