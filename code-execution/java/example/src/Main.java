import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(args[0]));
        BufferedReader outputReader = new BufferedReader(new FileReader(args[1]));

        int a = Integer.parseInt(inputReader.readLine());
        int b = Integer.parseInt(inputReader.readLine());

        int userResult = new Problem().add(a, b);

        int expectedResult = Integer.parseInt(outputReader.readLine());

        // For testing purposes let's fail it
        if (userResult == 300) {
            userResult++;
        }
        if (userResult == expectedResult) {
            ResultWriter.Pass();
        } else {
            ResultWriter.Fail();
        }

        inputReader.close();
    }
}