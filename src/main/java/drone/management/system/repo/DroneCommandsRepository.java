package drone.management.system.repo;

import drone.management.system.model.DroneCommands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneCommandsRepository extends JpaRepository<DroneCommands, Long> {
    List<DroneCommands> findByDroneId(Long droneId);
}
