import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(args[0]));
        BufferedReader outputReader = new BufferedReader(new FileReader(args[1]));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(args[2], true));

        Solution solution = new Solution();

        int w = Integer.parseInt(inputReader.readLine());
        int h = Integer.parseInt(inputReader.readLine());
        int n = Integer.parseInt(inputReader.readLine());
        int m = Integer.parseInt(inputReader.readLine());
        int direction = Integer.parseInt(inputReader.readLine());
        int[][] grid = new int[h][w];
        for (int i = 0; i < h; i++) {
            String[] line = inputReader.readLine().split(" ");
            for (int j = 0; j < w; j++) {
                grid[i][j] = Integer.parseInt(line[j]);
            }
        }

        solution.setW(w);
        solution.setH(h);
        solution.setM(m);
        solution.setN(n);
        solution.setDirection(Direction.fromInt(direction));
        solution.setGrid(grid);


        ArrayList<String[]> lines = new ArrayList<>();
        while (true) {
            String line = inputReader.readLine();
            if (line == null) break;
            String[] lineB = line.split(" ");
            lines.add(lineB);
        }
        boolean[][][] borders = new boolean[w][h][4];
        solution.setBorders(borders);

        for (String[] l : lines) {
            int x = Integer.parseInt(l[0]);
            int y = Integer.parseInt(l[1]);
            int dir = Integer.parseInt(l[2]);
            solution.addBorderHelper(x, y, Direction.fromInt(dir));
        }

        System.out.println(solution.getGridRepresentation());


        int expectedN = Integer.parseInt(outputReader.readLine());
        int expectedM = Integer.parseInt(outputReader.readLine());
        int expectedDir = Integer.parseInt(outputReader.readLine());
        int[][] expectedGrid = new int[h][w];
        for (int i = 0; i < h; i++) {
            String[] line = outputReader.readLine().split(" ");
            for (int j = 0; j < w; j++) {
                expectedGrid[i][j] = Integer.parseInt(line[j]);
            }
        }

        solution.run();

        System.out.println(solution.getGridRepresentation());

        boolean checkM = expectedM == -1 || h - 1 - solution.getM() == expectedM;
        boolean checkN = expectedN == -1 || solution.getN() == expectedN;
        boolean checkDir = expectedDir == -1 || solution.getDirection().getValue() == expectedDir;
        if (checkM && checkN && checkDir && Arrays.deepEquals(solution.getGrid(), expectedGrid)) {
            outputWriter.write("PASS\n");
            outputWriter.flush();
        } else {
            outputWriter.write("FAIL\n");
            outputWriter.flush();
        }

        inputReader.close();
    }
}