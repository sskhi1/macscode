#include <bits/stdc++.h>
#include "solution.h"

using namespace std;

int main(int argc, char* argv[]) {
    std::ifstream inputReader(argv[1]);
    std::ifstream outputReader(argv[2]);
    std::ofstream outputWriter(argv[3], std::ios_base::app); // Open in append mode

    if (!inputReader || !outputReader || !outputWriter) {
        std::cerr << "Error opening files." << std::endl;
        return 1;
    }

    int count, n; inputReader >> count >> n;
    vector<int> numbers(count);
    for (auto& v : numbers)
        inputReader >> v;

    Solution solution;
    bool userResult = solution.subsetSum(numbers, n);

    string expectedResult;
    outputReader >> expectedResult;

    if (userResult == (expectedResult == "true")) {
        outputWriter << "PASS\n";
    } else {
        outputWriter << "FAIL\n";
    }

    outputWriter.close();
    inputReader.close();
    outputReader.close();

    return 0;
}
