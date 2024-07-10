package drone.management.system.service;

import drone.management.system.model.Drone;
import drone.management.system.model.DroneCommands;
import drone.management.system.repo.DroneCommandsRepository;
import drone.management.system.repo.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DroneCommandServiceTest {

    @Mock
    private DroneCommandsRepository droneCommandsRepository;

    @InjectMocks
    private DroneCommandService droneCommandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCommandsForDrone() {
        Long droneId = 1L;
        Integer last = null;

        List<DroneCommands> mockCommands = createMockCommands(droneId, 5);

        when(droneCommandsRepository.findByDroneId(droneId)).thenReturn(mockCommands);

        List<DroneCommands> result = droneCommandService.getCommandsForDrone(droneId, last);

        assertEquals(mockCommands.size(), result.size());
        assertEquals(mockCommands, result);

        verify(droneCommandsRepository, times(1)).findByDroneId(droneId);
    }

    @Test
    public void testGetCommandsForDroneWithLast() {
        Long droneId = 1L;
        Integer last = 3;

        List<DroneCommands> mockCommands = createMockCommands(droneId, 5);

        when(droneCommandsRepository.findByDroneId(droneId)).thenReturn(mockCommands);

        List<DroneCommands> result = droneCommandService.getCommandsForDrone(droneId, last);

        assertEquals(last.intValue(), result.size());
        List<DroneCommands> expected = mockCommands.subList(2, 5);
        assertEquals(expected, result);

        verify(droneCommandsRepository, times(1)).findByDroneId(droneId);
    }

    @Test
    public void testGetCommandsForDroneWithInvalidLast() {
        Long droneId = 1L;
        Integer last = 10;

        List<DroneCommands> mockCommands = createMockCommands(droneId, 5);

        when(droneCommandsRepository.findByDroneId(droneId)).thenReturn(mockCommands);

        List<DroneCommands> result = droneCommandService.getCommandsForDrone(droneId, last);

        assertEquals(mockCommands.size(), result.size());

        verify(droneCommandsRepository, times(1)).findByDroneId(droneId);
    }

    @Test
    public void testGetCommandsForDroneWithNoCommands() {
        Long droneId = 1L;
        Integer last = null;

        when(droneCommandsRepository.findByDroneId(droneId)).thenReturn(new ArrayList<>());

        List<DroneCommands> result = droneCommandService.getCommandsForDrone(droneId, last);

        assertEquals(0, result.size());

        verify(droneCommandsRepository, times(1)).findByDroneId(droneId);
    }

    private List<DroneCommands> createMockCommands(Long droneId, int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    DroneCommands command = new DroneCommands();
                    command.setId((long) i);
                    command.setDroneId(droneId);
                    command.setCommand("COMMAND_" + i);
                    return command;
                })
                .collect(Collectors.toList());
    }
}