import csv
import argparse

def process_range(int1, int2):
    return [f"{start}-{min((start // 10 + 1) * 10 - 1, int2)}" for start in range(int1, int2 + 1, 10)]

def read_csv(input_file):
    with open(input_file, mode='r', newline='') as file:
        return list(csv.reader(file))

def write_csv(output_file, data):
    with open(output_file, mode='w', newline='') as file:
        csv.writer(file).writerows(data)

def main(input_file, output_file):
    data = read_csv(input_file)
    processed_data = []

    for row in data:
        if not row:  # Empty row
            processed_data.append(row)
        elif row[0].startswith('#'):  # Comment row
            processed_data.append(row)
        else:
            try:
                int1, int2 = map(int, row[0].split('-'))
                if int1 < int2:
                    processed_data.extend([[r] for r in process_range(int1, int2)])
                else:
                    print(f"Skipping invalid range: {row}")
            except ValueError:
                print(f"Skipping invalid row: {row}")

    write_csv(output_file, processed_data)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Process a CSV file of ranges.')
    parser.add_argument('input_file', help='The input CSV file containing ranges.')
    parser.add_argument('output_file', help='The output CSV file to write the processed ranges.')

    args = parser.parse_args()
    main(args.input_file, args.output_file)
