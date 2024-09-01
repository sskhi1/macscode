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

    string tmp; inputReader >> tmp >> tmp >> tmp;
    int n; inputReader >> n;

    string call;
    inputReader >> call >> call;
    int sz; inputReader >> sz;
    ResizableArray array(sz);

    bool correct = true;
    for (int i = 0; i < n - 1; ++i) {
        inputReader >> call;
        string op; inputReader >> op;
        cout << op << endl;
        if (op == "setAt") {
            int x, y;
            inputReader >> x >> y;
            array.setAt(x, y);
        } else if (op == "getAt") {
            int x;
            inputReader >> x;
            int expected;
            outputReader >> expected;
            if (array.getAt(x) != expected) {
                cout << "hi" << endl;
                cout << array.getAt(x) << expected << endl;
                correct = false;
                break;
            }
        } else if (op == "size") {
            int expected;
            outputReader >> expected;
            if (array.size() != expected) {
                correct = false;
                break;
            }
        } else if (op == "resize") {
            int x;
            inputReader >> x;
            array.resize(x);
        }
    }

    if (correct) {
        outputWriter << "PASS\n";
    } else {
        outputWriter << "FAIL\n";
    }

    outputWriter.close();
    inputReader.close();
    outputReader.close();

    return 0;
}
