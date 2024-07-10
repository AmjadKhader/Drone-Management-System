package drone.management.system.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum Direction {
    EAST,
    WEST,
    SOUTH,
    NORTH,
    INVALID;

    public static Direction fromString(String direction) {
        try {
            return Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return INVALID;
        }
    }
}
