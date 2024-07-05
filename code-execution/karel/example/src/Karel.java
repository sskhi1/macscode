import java.util.Arrays;

public class Karel {

    private static int w; // width of the grid
    private static int h; // height of the grid
    private static int[][] grid; // grid representing beepers
    private static int n; // Karel's x-coordinate
    private static int m; // Karel's y-coordinate

    public int getW() {
        return w;
    }

    public void setW(int w) {
        Karel.w = w;
    }

    public static int getH() {
        return h;
    }

    public void setH(int h) {
        Karel.h = h;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        Karel.grid = grid;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        Karel.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        Karel.m = getH() - 1 - m;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        Karel.direction = direction;
    }

    public boolean[][][] getBorders() {
        return borders;
    }

    public void setBorders(boolean[][][] borders) {
        Karel.borders = borders;
    }

    private static Direction direction;
    private static boolean[][][] borders;

    public Karel() {
        w = 10;
        h = 10;
        grid = new int[h][w];
        n = 0;
        m = h - 1;
        direction = Direction.NORTH;
        borders = new boolean[w][h][4];
    }

    public static void move() {
        if (frontIsClear()) {
            switch (direction) {
                case NORTH:
                    m--;
                    break;
                case EAST:
                    n++;
                    break;
                case SOUTH:
                    m++;
                    break;
                case WEST:
                    n--;
                    break;
            }
        } else {
            System.out.println("Move blocked by border.");
            System.exit(23);
        }
    }

    public static void turnLeft() {
        switch (direction) {
            case NORTH:
                direction = Direction.WEST;
                break;
            case EAST:
                direction = Direction.NORTH;
                break;
            case SOUTH:
                direction = Direction.EAST;
                break;
            case WEST:
                direction = Direction.SOUTH;
                break;
        }
    }

    public static void turnRight() {
        switch (direction) {
            case NORTH:
                direction = Direction.EAST;
                break;
            case EAST:
                direction = Direction.SOUTH;
                break;
            case SOUTH:
                direction = Direction.WEST;
                break;
            case WEST:
                direction = Direction.NORTH;
                break;
        }
    }

    public static void turnAround() {
        switch (direction) {
            case NORTH:
                direction = Direction.SOUTH;
                break;
            case EAST:
                direction = Direction.WEST;
                break;
            case SOUTH:
                direction = Direction.NORTH;
                break;
            case WEST:
                direction = Direction.EAST;
                break;
        }
    }

    public static void pickBeeper() {
        if (grid[m][n] > 0) {
            grid[m][n]--;
        } else {
            System.out.println("No beepers at the current location to pick up.");
            System.exit(24);
        }
    }

    public static void putBeeper() {
        grid[m][n]++;
    }

    public static boolean frontIsClear() {
        switch (direction) {
            case NORTH:
                return m > 0 && !borders[n][m - 1][2];
            case EAST:
                return n < w - 1 && !borders[n][m][1];
            case SOUTH:
                return m < h - 1 && !borders[n][m][2];
            case WEST:
                return n > 0 && !borders[n - 1][m][1];
        }
        return false;
    }

    public static boolean frontIsBlocked() {
        return !frontIsClear();
    }

    public static boolean leftIsClear() {
        turnLeft();
        boolean clear = frontIsClear();
        turnRight();
        return clear;
    }

    public static boolean leftIsBlocked() {
        return !leftIsClear();
    }

    public static boolean rightIsClear() {
        turnRight();
        boolean clear = frontIsClear();
        turnLeft();
        return clear;
    }

    public static boolean rightIsBlocked() {
        return !rightIsClear();
    }

    public static boolean beepersPresent() {
        return grid[m][n] > 0;
    }

    public static boolean noBeepersPresent() {
        return !beepersPresent();
    }

    public static boolean facingNorth() {
        return direction == Direction.NORTH;
    }

    public static boolean notFacingNorth() {
        return !facingNorth();
    }

    public static boolean facingEast() {
        return direction == Direction.EAST;
    }

    public static boolean notFacingEast() {
        return !facingEast();
    }

    public static boolean facingSouth() {
        return direction == Direction.SOUTH;
    }

    public static boolean notFacingSouth() {
        return !facingSouth();
    }

    public static boolean facingWest() {
        return direction == Direction.WEST;
    }

    public static boolean notFacingWest() {
        return !facingWest();
    }

    public static void addBorder(int x, int y, Direction side) {
        switch (side) {
            case NORTH:
                borders[x][getH() - 1 - y - 1][2] = true;
                break;
            case EAST:
                borders[x][getH() - 1 - y][1] = true;
                break;
            case SOUTH:
                borders[x][getH() - 1 - y][2] = true;
                break;
            case WEST:
                borders[x - 1][getH() - 1 - y][1] = true;
                break;
        }
    }

    public void printStatus() {
        System.out.println("Karel is at (" + n + ", " + m + ") facing " + direction);
        System.out.println("Grid state:");
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.print(grid[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void printBorders() {
        System.out.println("Borders:");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (borders[i][j][1]) System.out.println("Border at (" + i + ", " + j + ") on EAST side");
                if (borders[i][j][2]) System.out.println("Border at (" + i + ", " + j + ") on SOUTH side");
            }
        }
    }

    public String getGridRepresentation() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < w * 2 + 1; i++) {
            sb.append("_");
        }
        sb.append("\n");

        for (int y = 0; y < h; y++) {
            sb.append("|");
            for (int x = 0; x < w; x++) {
                if (x == n && y == m) {
                    sb.append("K");
                } else if (grid[y][x] > 0) {
                    sb.append(grid[y][x]);
                } else {
                    sb.append(" ");
                }

                if (borders[x][y][1]) {
                    sb.append("|");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("|\n");

            sb.append("|");
            for (int x = 0; x < w; x++) {
                if (borders[x][y][2]) {
                    sb.append("_ ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("|\n");
        }

        for (int i = 0; i < w * 2 + 1; i++) {
            sb.append("‾");
        }
        sb.append("\n");

        return sb.toString();
    }

    public void addBorderHelper(int x, int y, Direction side) {
        switch (side) {
            case NORTH:
                borders[x][getH() - 1 - y - 1][2] = true;
                break;
            case EAST:
                borders[x][getH() - 1 - y][1] = true;
                break;
            case SOUTH:
                borders[x][getH() - 1 - y][2] = true;
                break;
            case WEST:
                borders[x - 1][getH() - 1 - y][1] = true;
                break;
        }
    }
}