package drone.management.system.service;

import drone.management.system.exception.DroneNotFoundException;
import drone.management.system.exception.InvalidDirectionException;
import drone.management.system.exception.InvalidMoveException;
import drone.management.system.model.Drone;
import drone.management.system.repo.DroneCommandsRepository;
import drone.management.system.repo.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneCommandsRepository droneCommandsRepository;

    @InjectMocks
    private DroneService droneService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterDrone() {
        int x = 0;
        int y = 0;
        String direction = "NORTH";

        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> {
            Drone drone = invocation.getArgument(0);
            drone.setId(1L);
            return drone;
        });

        Drone registeredDrone = droneService.registerDrone(x, y, direction);

        assertNotNull(registeredDrone);
        assertEquals(x, registeredDrone.getX());
        assertEquals(y, registeredDrone.getY());
        assertEquals(direction.toUpperCase(), registeredDrone.getDirection());

        verify(droneRepository, times(1)).save(any(Drone.class));
    }

    @Test
    public void testRegisterDrone_InvalidDirection() {
        int x = 0;
        int y = 0;
        String direction = "INVALID";

        assertThrows(InvalidDirectionException.class, () -> droneService.registerDrone(x, y, direction));

        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    public void testRegisterDrone_InvalidPosition() {
        int x = 10;
        int y = 0;
        String direction = "NORTH";

        assertThrows(InvalidMoveException.class, () -> droneService.registerDrone(x, y, direction));
        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    public void testMoveDrone() {
        Long droneId = 1L;
        String command = "M";

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(0);
        mockDrone.setY(0);
        mockDrone.setDirection("EAST");

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(mockDrone));
        when(droneRepository.save(any(Drone.class))).thenReturn(mockDrone);

        Drone movedDrone = droneService.moveDrone(droneId, command);

        assertNotNull(movedDrone);
        assertEquals(droneId, movedDrone.getId());
        assertEquals(1, movedDrone.getX());

        verify(droneRepository, times(1)).findById(droneId);
        verify(droneRepository, times(1)).save(any(Drone.class));
        verify(droneCommandsRepository, times(1)).save(any());
    }

    @Test
    public void testMoveDrone_ChangeDirectionAndMove() {
        Long droneId = 1L;
        String command = "RM";

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(0);
        mockDrone.setY(0);
        mockDrone.setDirection("EAST");

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(mockDrone));
        when(droneRepository.save(any(Drone.class))).thenReturn(mockDrone);

        Drone movedDrone = droneService.moveDrone(droneId, command);

        assertNotNull(movedDrone);
        assertEquals(droneId, movedDrone.getId());
        assertEquals(1, movedDrone.getY());
        assertEquals("SOUTH", movedDrone.getDirection());
    }

    @Test
    public void testMoveDrone_MoveFromSouthToNorthSuccess() {
        Long droneId = 1L;
        String command = "RRM";

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(4);
        mockDrone.setY(4);
        mockDrone.setDirection("SOUTH");

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(mockDrone));
        when(droneRepository.save(any(Drone.class))).thenReturn(mockDrone);

        Drone movedDrone = droneService.moveDrone(droneId, command);

        assertNotNull(movedDrone);
        assertEquals(3, movedDrone.getY());
        assertEquals(4, movedDrone.getX());
        assertEquals("NORTH", movedDrone.getDirection());
    }

    @Test
    public void testMoveDrone_MoveFromSouthToNorthFailed() {
        Long droneId = 1L;
        String command = "M";

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(0);
        mockDrone.setY(0);
        mockDrone.setDirection("SOUTH");

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(mockDrone));
        when(droneRepository.save(any(Drone.class))).thenReturn(mockDrone);

        assertThrows(InvalidMoveException.class, () -> droneService.moveDrone(droneId, command));
    }

    @Test
    public void testMoveDrone_DroneNotFound() {
        Long droneId = 1L;
        String command = "MOVE";

        when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

        assertThrows(DroneNotFoundException.class, () -> droneService.moveDrone(droneId, command));

        verify(droneRepository, times(1)).findById(droneId);
        verify(droneCommandsRepository, never()).save(any());
    }

    @Test
    public void testGetDronePosition() {
        Long droneId = 1L;

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(0);
        mockDrone.setY(0);
        mockDrone.setDirection("NORTH");

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(mockDrone));

        Drone retrievedDrone = droneService.getDronePosition(droneId);

        assertNotNull(retrievedDrone);
        assertEquals(droneId, retrievedDrone.getId());
        assertEquals(0, retrievedDrone.getX());
        assertEquals(0, retrievedDrone.getY());
        assertEquals("NORTH", retrievedDrone.getDirection());

        verify(droneRepository, times(1)).findById(droneId);
    }

    @Test
    public void testGetDronePosition_DroneNotFound() {
        Long droneId = 1L;

        when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

        assertThrows(DroneNotFoundException.class, () -> droneService.getDronePosition(droneId));

        verify(droneRepository, times(1)).findById(droneId);
    }

    @Test
    public void testGetAllDronePositions() {
        Drone drone1 = new Drone();
        drone1.setId(1L);
        drone1.setX(0);
        drone1.setY(0);
        drone1.setDirection("NORTH");

        Drone drone2 = new Drone();
        drone2.setId(2L);
        drone2.setX(1);
        drone2.setY(1);
        drone2.setDirection("EAST");

        List<Drone> droneList = Arrays.asList(drone1, drone2);

        when(droneRepository.findAll()).thenReturn(droneList);

        List<Drone> result = droneService.getAllDronePosition();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(drone1));
        assertTrue(result.contains(drone2));

        verify(droneRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllDronePositions_EmptyResponse() {

        List<Drone> result = droneService.getAllDronePosition();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(droneRepository, times(1)).findAll();
    }

    @Test
    public void testValidateDirection_ValidDirection() {
        String validDirection = "NORTH";

        assertDoesNotThrow(() -> droneService.validateDirection(validDirection));
    }

    @Test
    public void testValidateDirection_InvalidDirection() {
        String invalidDirection = "INVALID";

        assertThrows(InvalidDirectionException.class, () -> droneService.validateDirection(invalidDirection));
    }
}
