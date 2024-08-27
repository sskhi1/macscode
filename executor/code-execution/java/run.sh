#!/bin/bash
# Entrypoint for docker container

cd execution

mkdir result
echo "${test_count}" > result/test_count.txt

compile_error_tmp_file=$(mktemp)
javac src/*.java 2> "$compile_error_tmp_file"

# Check and write compile errors to every test result
if [ $? -ne 0 ]; then
  for test_num in $(seq 1 "${test_count}")
  do
    output_file=result/result_"${test_num}".txt
    echo "COMPILE_ERROR" > "$output_file"
    cat "$compile_error_tmp_file" >> "$output_file"
  done
  exit
fi

for test_num in $(seq 1 "${test_count}")
do
  java -classpath src/ Main tests/in_"${test_num}".txt tests/out_"${test_num}".txt result/result_"${test_num}".txt
done