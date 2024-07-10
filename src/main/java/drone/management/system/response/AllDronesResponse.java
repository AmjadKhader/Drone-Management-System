package drone.management.system.response;

import drone.management.system.model.Drone;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllDronesResponse {
    List<DronePositionResponse> drones = new ArrayList<>();

    public AllDronesResponse(List<Drone> drones) {
        for (Drone drone : drones) {
            this.drones.add(new DronePositionResponse(drone));
        }
    }
}
