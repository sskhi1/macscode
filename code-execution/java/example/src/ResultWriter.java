import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {
    private static final String resultRelativePath = "result/result.txt";
    private static final BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter(resultRelativePath, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Pass() throws IOException {
        writer.write("Pass\n");
        writer.flush();
    }

    public static void Fail() throws IOException {
        writer.write("Fail\n");
        writer.flush();
    }
}
