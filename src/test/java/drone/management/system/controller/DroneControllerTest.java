package drone.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import drone.management.system.exception.DroneNotFoundException;
import drone.management.system.exception.InvalidDirectionException;
import drone.management.system.model.Drone;
import drone.management.system.model.DroneCommands;
import drone.management.system.service.DroneCommandService;
import drone.management.system.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DroneController.class)
@AutoConfigureMockMvc
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @MockBean
    private DroneCommandService droneCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterDrone_Success() throws Exception {
        int x = 0;
        int y = 0;
        String direction = "NORTH";

        Drone mockDrone = new Drone();
        mockDrone.setX(x);
        mockDrone.setY(y);
        mockDrone.setDirection(direction.toUpperCase());

        when(droneService.registerDrone(x, y, direction)).thenReturn(mockDrone);

        mockMvc.perform(post("/drone-management-service/api/drones/register")
                        .param("x", String.valueOf(x))
                        .param("y", String.valueOf(y))
                        .param("direction", direction)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", is(x)))
                .andExpect(jsonPath("$.y", is(y)))
                .andExpect(jsonPath("$.direction", is(direction.toUpperCase())));

        verify(droneService, times(1)).registerDrone(x, y, direction);
    }

    @Test
    void testRegisterDrone_InvalidDirection() throws Exception {
        int x = 0;
        int y = 0;
        String direction = "INVALID";

        when(droneService.registerDrone(x, y, direction)).thenThrow(new InvalidDirectionException("Invalid direction"));

        mockMvc.perform(post("/drone-management-service/api/drones/register")
                        .param("x", String.valueOf(x))
                        .param("y", String.valueOf(y))
                        .param("direction", direction)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid direction")));

        verify(droneService, times(1)).registerDrone(x, y, direction);
    }

    @Test
    void testMoveDrone_Success() throws Exception {
        Long droneId = 1L;
        String command = "MOVE";

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(0);
        mockDrone.setY(0);
        mockDrone.setDirection("NORTH");

        when(droneService.moveDrone(droneId, command)).thenReturn(mockDrone);

        mockMvc.perform(post("/drone-management-service/api/drones/move/{id}", droneId)
                        .param("command", command)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", is(0)))
                .andExpect(jsonPath("$.y", is(0)))
                .andExpect(jsonPath("$.direction", is("NORTH")));

        verify(droneService, times(1)).moveDrone(droneId, command);
    }

    @Test
    void testMoveDrone_DroneNotFound() throws Exception {
        Long droneId = 1L;
        String command = "MOVE";

        when(droneService.moveDrone(droneId, command)).thenThrow(new DroneNotFoundException("Drone not found"));

        mockMvc.perform(post("/drone-management-service/api/drones/move/{id}", droneId)
                        .param("command", command)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Drone not found")));

        verify(droneService, times(1)).moveDrone(droneId, command);
    }

    @Test
    void testGetDronePosition_Success() throws Exception {
        Long droneId = 1L;

        Drone mockDrone = new Drone();
        mockDrone.setId(droneId);
        mockDrone.setX(0);
        mockDrone.setY(0);
        mockDrone.setDirection("NORTH");

        when(droneService.getDronePosition(droneId)).thenReturn(mockDrone);

        mockMvc.perform(get("/drone-management-service/api/drones/{id}", droneId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", is(0)))
                .andExpect(jsonPath("$.y", is(0)))
                .andExpect(jsonPath("$.direction", is("NORTH")));

        verify(droneService, times(1)).getDronePosition(droneId);
    }

    @Test
    void testGetDronePosition_DroneNotFound() throws Exception {
        Long droneId = 1L;

        when(droneService.getDronePosition(droneId)).thenThrow(new DroneNotFoundException("Drone not found"));

        mockMvc.perform(get("/drone-management-service/api/drones/{id}", droneId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Drone not found")));

        verify(droneService, times(1)).getDronePosition(droneId);
    }

    @Test
    void testGetAllDrones_Success() throws Exception {
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

        when(droneService.getAllDronePosition()).thenReturn(droneList);

        mockMvc.perform(get("/drone-management-service/api/drones/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.drones", hasSize(2)))
                .andExpect(jsonPath("$.drones[0].x", is(drone1.getX())))
                .andExpect(jsonPath("$.drones[0].y", is(drone1.getY())))
                .andExpect(jsonPath("$.drones[0].direction", is(drone1.getDirection())))
                .andExpect(jsonPath("$.drones[1].x", is(drone2.getX())))
                .andExpect(jsonPath("$.drones[1].y", is(drone2.getY())))
                .andExpect(jsonPath("$.drones[1].direction", is(drone2.getDirection())));

        verify(droneService, times(1)).getAllDronePosition();
    }

    @Test
    void testGetDroneCommands_Success() throws Exception {
        Long droneId = 1L;
        Integer last = 2;

        DroneCommands command1 = new DroneCommands();
        command1.setDroneId(droneId);
        command1.setCommand("MOVE");

        DroneCommands command2 = new DroneCommands();
        command2.setDroneId(droneId);
        command2.setCommand("TURN_LEFT");

        List<DroneCommands> commands = Arrays.asList(command1, command2);

        when(droneCommandService.getCommandsForDrone(droneId, last)).thenReturn(commands);

        mockMvc.perform(get("/drone-management-service/api/drones/{id}/commands", droneId)
                        .param("last", String.valueOf(last))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].droneId", is(droneId.intValue())))
                .andExpect(jsonPath("$[0].command", is("MOVE")))
                .andExpect(jsonPath("$[1].droneId", is(droneId.intValue())))
                .andExpect(jsonPath("$[1].command", is("TURN_LEFT")));

        verify(droneCommandService, times(1)).getCommandsForDrone(droneId, last);
    }
}
