from pymongo import MongoClient
import json

def read_file(file_path):
    with open(file_path, 'r') as file:
        return file.read()

client = MongoClient("mongodb://localhost:9100/problems-db")

db = client['problems-db']

problems_collection = db.problems
tests_collection = db.tests

for i in range(16, 22):
    problem_info = json.load(open(f"methodology/{i}/info.json", "rb"))

    # Check if a document with the same problem_id exists and delete it
    existing_problem = problems_collection.find_one({"problem_id": problem_info["problem_id"]})
    if existing_problem:
        problems_collection.delete_one({"_id": existing_problem["_id"]})
        tests_collection.delete_many({"problem_id": existing_problem["_id"]})

    new_problem = {
        "solution_file_template": read_file(f"methodology/{i}/src/Solution.java"),
        "main_file": read_file(f"methodology/{i}/src/Main.java"),
        "description": problem_info["description"],
        "name": problem_info["name"],
        "problem_id": problem_info["problem_id"],
        "topics": problem_info["topics"],
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
            "input": read_file(f"methodology/{i}/tests/in_{test_num}.txt"),
            "output": read_file(f"methodology/{i}/tests/out_{test_num}.txt")
        }
        tests_collection.insert_one(new_test)

client.close()