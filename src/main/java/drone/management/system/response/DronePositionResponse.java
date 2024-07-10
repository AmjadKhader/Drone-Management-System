package drone.management.system.response;

import drone.management.system.model.Drone;
import lombok.Data;

@Data
public class DronePositionResponse {
    private int x;
    private int y;

    private String direction;
    String[][] world = new String[10][10];

    public DronePositionResponse(Drone drone) {
        this.x = drone.getX();
        this.y = drone.getY();
        this.direction = drone.getDirection();

        for (int i = 9; i >= 0; i--) {
            for (int j = 9; j >= 0; j--) {
                world[i][j] = "-";
            }
        }

        if (drone.getDirection().equalsIgnoreCase("EAST")) {
            world[drone.getY()][drone.getX()] = ">";
        }
        if (drone.getDirection().equalsIgnoreCase("WEST")) {
            world[drone.getY()][drone.getX()] = "<";
        }
        if (drone.getDirection().equalsIgnoreCase("NORTH")) {
            world[drone.getY()][drone.getX()] = "^";
        }
        if (drone.getDirection().equalsIgnoreCase("SOUTH")) {
            world[drone.getY()][drone.getX()] = "⌄";
        }
    }

    public String[] getWorld() {
        String[] rows = new String[world.length];
        for (int i = 0; i < world.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (String cell : world[i]) {
                sb.append(cell).append(" ");
            }
            rows[i] = sb.toString().trim();
        }
        return rows;
    }

}
