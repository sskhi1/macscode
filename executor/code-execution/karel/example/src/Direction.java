public enum Direction {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Direction fromInt(int value) {
        for (Direction direction : Direction.values()) {
            if (direction.getValue() == value) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Invalid direction value: " + value);
    }
}