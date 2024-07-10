package drone.management.system.model;

import drone.management.system.exception.InvalidCommandException;
import drone.management.system.exception.InvalidMoveException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static drone.management.system.model.Direction.*;
import static drone.management.system.util.Validator.validateMove;

@Data
@Entity
@Table(name = "drones")
@AllArgsConstructor
@NoArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;

    private String direction;

    public void move(String command) {
        int x = this.getX();
        int y = this.getY();
        Direction newDirection = Direction.fromString(this.getDirection());
        Direction tempDirection = newDirection;

        command = command.toUpperCase();
        for (Character character : command.toCharArray()) {
            if (!character.equals('R') && !character.equals('L') && !character.equals('M')) {
                throw new InvalidCommandException("Invalid move command");
            }

            if (character.equals('R')) {
                newDirection = switch (newDirection) {
                    case NORTH -> EAST;
                    case WEST -> NORTH;
                    case EAST -> SOUTH;
                    default -> WEST;
                };
            }

            if (character.equals('L')) {
                newDirection = switch (newDirection) {
                    case NORTH -> WEST;
                    case WEST -> SOUTH;
                    case EAST -> NORTH;
                    default -> EAST;
                };
            }

            if ((tempDirection == SOUTH && newDirection == SOUTH) ||
                    (tempDirection == NORTH && newDirection == NORTH)) {
                throw new InvalidMoveException("Invalid direct movement from " + tempDirection + " to " + newDirection);
            }

            if (character.equals('M')) {
                if (newDirection == NORTH) {
                    y--;
                }
                if (newDirection == WEST) {
                    x--;
                }
                if (newDirection == EAST) {
                    x++;
                }
                if (newDirection == SOUTH) {
                    y++;
                }

                validateMove(x, y);
                tempDirection = newDirection;
            }
        }

        this.setX(x);
        this.setY(y);
        this.setDirection(newDirection.toString());
    }
}