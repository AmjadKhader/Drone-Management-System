package drone.management.system.service;

import drone.management.system.exception.DroneNotFoundException;
import drone.management.system.exception.InvalidDirectionException;
import drone.management.system.model.Direction;
import drone.management.system.model.Drone;
import drone.management.system.model.DroneCommands;
import drone.management.system.repo.DroneCommandsRepository;
import drone.management.system.repo.DroneRepository;
import drone.management.system.util.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final DroneCommandsRepository droneCommandsRepository;

    public DroneService(DroneRepository droneRepository, DroneCommandsRepository droneCommandsRepository) {
        this.droneRepository = droneRepository;
        this.droneCommandsRepository = droneCommandsRepository;
    }

    public Drone registerDrone(int x, int y, String direction) {

        Validator.validateMove(x, y);
        validateDirection(direction);

        Drone drone = new Drone();
        drone.setX(x);
        drone.setY(y);
        drone.setDirection(direction.toUpperCase());
        return droneRepository.save(drone);
    }

    @Transactional
    public Drone moveDrone(Long id, String command) {
        Drone drone = droneRepository.findById(id).orElseThrow(() -> new DroneNotFoundException("Drone not found"));
        drone.move(command);

        DroneCommands droneCommand = new DroneCommands();
        droneCommand.setDroneId(id);
        droneCommand.setCommand(command);
        droneCommandsRepository.save(droneCommand);

        return droneRepository.save(drone);
    }

    public Drone getDronePosition(Long id) {
        return droneRepository.findById(id).orElseThrow(() -> new DroneNotFoundException("Drone not found"));
    }

    public List<Drone> getAllDronePosition() {
        return droneRepository.findAll();
    }

    // Public for tests
    public void validateDirection(String direction) {
        // Validate direction
        if (Direction.fromString(direction) == Direction.INVALID) {
            throw new InvalidDirectionException("Invalid direction");
        }
    }
}
