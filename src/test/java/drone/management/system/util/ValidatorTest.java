package drone.management.system.util;

import drone.management.system.exception.InvalidMoveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    @Test
    void testValidateMove() {
        assertDoesNotThrow(() -> Validator.validateMove(4, 4));
    }

    @Test
    void testValidateMove_FailedXLessThan0() {
        assertThrows(InvalidMoveException.class, () -> Validator.validateMove(-1, 0));
    }

    @Test
    void testValidateMove_FailedYLessThan0() {
        assertThrows(InvalidMoveException.class, () -> Validator.validateMove(0, -1));
    }

    @Test
    void testValidateMove_FailedXMoreThan9() {
        assertThrows(InvalidMoveException.class, () -> Validator.validateMove(10, 0));
    }

    @Test
    void testValidateMove_FailedYMoreThan9() {
        assertThrows(InvalidMoveException.class, () -> Validator.validateMove(0, 10));
    }
}