package drone.management.system.service;

import drone.management.system.model.DroneCommands;
import drone.management.system.repo.DroneCommandsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneCommandService {
    private final DroneCommandsRepository droneCommandsRepository;

    public DroneCommandService(DroneCommandsRepository droneCommandsRepository) {
        this.droneCommandsRepository = droneCommandsRepository;
    }

    public List<DroneCommands> getCommandsForDrone(Long droneId, Integer last) {
        List<DroneCommands> commands = droneCommandsRepository.findByDroneId(droneId);
        if (last != null && last > 0 && last < commands.size()) {
            return commands.subList(commands.size() - last, commands.size());
        }
        return commands;
    }
}
