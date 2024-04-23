from pymongo import MongoClient
import json
import os

def read_file(file_path):
    with open(file_path, 'r') as file:
        return file.read()

client = MongoClient("mongodb://localhost:9100/problems-db")

db = client['problems-db']

problems_collection = db.problems
tests_collection = db.tests

arr = {
  "methodology",
  "abstractions"
}

def get_all_dirs(main_dir):
    child_dirs = []
    for item in os.listdir(main_dir):
        full_path = os.path.join(subject, item)
        if os.path.isdir(full_path):
            child_dirs.append(full_path)
    child_dirs.sort()
    return child_dirs

for subject in arr:
    for dir in get_all_dirs(subject):
        problem_info = json.load(open(f"{dir}/info.json", "rb"))

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
            "solution_file_template": read_file(f"{dir}/src/{solution_file}"),
            "main_file": read_file(f"{dir}/src/{main_file}"),
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
                "input": read_file(f"{dir}/tests/in_{test_num}.txt"),
                "output": read_file(f"{dir}/tests/out_{test_num}.txt")
            }
            tests_collection.insert_one(new_test)

client.close()