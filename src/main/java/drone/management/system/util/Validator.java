package drone.management.system.util;

import drone.management.system.exception.InvalidMoveException;

public class Validator {

    public static void validateMove(int xAxis, int yAxis) {
        // Validate boundaries
        if (xAxis < 0 || xAxis > 9 || yAxis < 0 || yAxis > 9) {
            throw new InvalidMoveException("Position is outside the field boundaries");
        }
    }
}
