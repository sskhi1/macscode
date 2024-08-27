#include <iostream>
#include <fstream>
#include <string>
#include "solution.h"

int main(int argc, char* argv[]) {
    std::ifstream inputReader(argv[1]);
    std::ifstream outputReader(argv[2]);
    std::ofstream outputWriter(argv[3], std::ios_base::app); // Open in append mode

    if (!inputReader || !outputReader || !outputWriter) {
        std::cerr << "Error opening files." << std::endl;
        return 1;
    }

    int a, b, expectedResult, userResult;
    inputReader >> a >> b;

    Solution solution;
    userResult = solution.add(a, b);

    outputReader >> expectedResult;

    if (userResult == expectedResult) {
        outputWriter << "PASS\n";
    } else {
        outputWriter << "FAIL\n";
    }

    outputWriter.close();
    inputReader.close();
    outputReader.close();

    return 0;
}
