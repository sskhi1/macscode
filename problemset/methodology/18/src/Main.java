import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(args[0]));
        BufferedReader outputReader = new BufferedReader(new FileReader(args[1]));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(args[2], true));

        int n = Integer.parseInt(inputReader.readLine());

        int userResult = new Solution().numberOfDivisors(n);

        int expectedResult = Integer.parseInt(outputReader.readLine());

        if (userResult == expectedResult) {
            outputWriter.write("PASS\n");
            outputWriter.flush();
        } else {
            outputWriter.write("FAIL\n");
            outputWriter.flush();
        }

        inputReader.close();
    }
}