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

    int n; inputReader >> n;

    Solution solution;
    int userResult = solution.fib(n);

    int expectedResult;
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
