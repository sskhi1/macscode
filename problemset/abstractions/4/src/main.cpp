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
    vector<string> words(n);
    for (auto& v : words)
        inputReader >> v;

    Solution solution;
    string userResult = solution.mostString(words);

    string expectedResult;
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
