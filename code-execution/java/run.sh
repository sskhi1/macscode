#!/bin/bash
# Entrypoint for docker container

cd execution

mkdir result && touch result/result.txt

javac src/*.java

for test_num in $(seq 1 "${test_count}")
do
  java -classpath src/ Main tests/in_"${test_num}".txt tests/out_"${test_num}".txt result/result.txt
done