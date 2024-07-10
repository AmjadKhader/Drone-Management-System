package drone.management.system.controller;

import drone.management.system.model.Drone;
import drone.management.system.model.DroneCommands;
import drone.management.system.response.AllDronesResponse;
import drone.management.system.response.DronePositionResponse;
import drone.management.system.service.DroneCommandService;
import drone.management.system.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drone-management-service/api/drones")
class DroneController {

    private final DroneService droneService;
    private final DroneCommandService droneCommandService;

    public DroneController(DroneService droneService, DroneCommandService droneCommandService) {
        this.droneService = droneService;
        this.droneCommandService = droneCommandService;
    }

    @PostMapping("/register")
    public ResponseEntity<Drone> registerDrone(
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam String direction) {
        Drone drone = droneService.registerDrone(x, y, direction);
        return ResponseEntity.ok(drone);
    }

    @PostMapping("/move/{id}")
    public ResponseEntity<DronePositionResponse> moveDrone(
            @PathVariable Long id,
            @RequestParam String command) {
        Drone drone = droneService.moveDrone(id, command);
        return ResponseEntity.ok(new DronePositionResponse(drone));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DronePositionResponse> getDronePosition(@PathVariable Long id) {
        Drone drone = droneService.getDronePosition(id);
        return ResponseEntity.ok(new DronePositionResponse(drone));
    }

    @GetMapping("/")
    public ResponseEntity<AllDronesResponse> getAllDrones() {
        return ResponseEntity.ok(new AllDronesResponse(droneService.getAllDronePosition()));
    }

    @GetMapping("/{id}/commands")
    public ResponseEntity<List<DroneCommands>> getDroneCommands(
            @PathVariable Long id,
            @RequestParam(required = false) Integer last) {
        List<DroneCommands> drones = droneCommandService.getCommandsForDrone(id, last);
        return ResponseEntity.ok(drones);
    }
}
