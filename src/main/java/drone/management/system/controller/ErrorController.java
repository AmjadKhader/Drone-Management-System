package drone.management.system.controller;

import drone.management.system.exception.DroneNotFoundException;
import drone.management.system.exception.InvalidCommandException;
import drone.management.system.exception.InvalidDirectionException;
import drone.management.system.exception.InvalidMoveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMoveException.class)
    public String handleInvalidMoveException(InvalidMoveException e) {
        logger.error("Invalid Move ...", e);
        return e.getMessage();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DroneNotFoundException.class)
    public String handleDroneNotFoundException(DroneNotFoundException e) {
        logger.error("Drone not found ...", e);
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDirectionException.class)
    public String handleInvalidDirectionException(InvalidDirectionException e) {
        logger.error("Invalid Direction ...", e);
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCommandException.class)
    public String handleInvalidCommandException(InvalidCommandException e) {
        logger.error("Invalid Move Command ...", e);
        return e.getMessage();
    }
}
