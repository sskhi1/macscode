from pymongo import MongoClient
import json

def read_file(file_path):
    with open(file_path, 'r') as file:
        return file.read()

client = MongoClient("mongodb://localhost:9100/problems-db")

db = client['problems-db']

problems_collection = db.problems
tests_collection = db.tests

arr = {
  "methodology": [16, 21],
  "abstractions": [1, 1]
}

for subject in arr:
    start = arr[subject][0]
    end = arr[subject][1]
    for i in range(start, end + 1):
        prefix = f"{subject}/{i}"
        problem_info = json.load(open(f"{prefix}/info.json", "rb"))

        # Check if a document with the same problem_id exists and delete it
        existing_problem = problems_collection.find_one({"problem_id": problem_info["problem_id"]})
        if existing_problem:
            problems_collection.delete_one({"_id": existing_problem["_id"]})
            tests_collection.delete_many({"problem_id": existing_problem["_id"]})

        if subject == "abstractions":
            main_file = "main.cpp"
            solution_file = "solution.h"
        else:
            main_file = "Main.java"
            solution_file = "Solution.java"

        new_problem = {
            "solution_file_template": read_file(f"{prefix}/src/{solution_file}"),
            "main_file": read_file(f"{prefix}/src/{main_file}"),
            "description": problem_info["description"],
            "name": problem_info["name"],
            "problem_id": problem_info["problem_id"],
            "topics": problem_info["topics"],
            "type": problem_info["type"],
            "difficulty": problem_info["difficulty"]
        }

        result = problems_collection.insert_one(new_problem)
        inserted_id = result.inserted_id

        for test_num in range(1, problem_info["test_count"] + 1):
            is_public = True if test_num <= problem_info["public_test_count"] else False
            new_test = {
                "problem_id": inserted_id,
                "is_public": is_public,
                "test_num": test_num,
                "input": read_file(f"{prefix}/tests/in_{test_num}.txt"),
                "output": read_file(f"{prefix}/tests/out_{test_num}.txt")
            }
            tests_collection.insert_one(new_test)

client.close()