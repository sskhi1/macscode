import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(args[0]));
        BufferedReader outputReader = new BufferedReader(new FileReader(args[1]));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(args[2], true));

        int n = Integer.parseInt(inputReader.readLine());

        int userResult = new Solution().firstNSum(n);

        int expectedResult = Integer.parseInt(outputReader.readLine());

        if (userResult == expectedResult) {
            outputWriter.write("Pass\n");
            outputWriter.flush();
        } else {
            outputWriter.write("Fail\n");
            outputWriter.flush();
        }

        inputReader.close();
    }
}