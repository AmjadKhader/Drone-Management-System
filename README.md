# Drone Management System

The Drone Management System is a RESTful API service designed to manage drones and their movements. It allows registering drones, moving them, retrieving their positions, and fetching commands history. This system is useful for drone fleet management applications where real-time monitoring and control are essential.

## Endpoints

### 1. Register Drone

**URL:** `/drone-management-service/api/drones/register`

**Method:** POST

**Parameters:**
- `x` (integer, required): X-coordinate of the drone's initial position.
- `y` (integer, required): Y-coordinate of the drone's initial position.
- `direction` (string, required): Initial direction of the drone (NORTH, EAST, SOUTH, WEST).

**Example:**
```json
Request:
POST /drone-management-service/api/drones/register?x=0&y=0&direction=NORTH
Response:
{
  "x": 0,
  "y": 0,
  "direction": "NORTH"
}
```
### 2. Move Drone
   **URL:**  `/drone-management-service/api/drones/move/{id}`

**Method:** POST

**Parameters:**

* `{id}` (path variable, long, required): ID of the drone to move.
* `command` (request parameter, string, required): Command to move the drone (M, L, R).*

Example:
```json
Request:
POST /drone-management-service/api/drones/move/1?command=LLM
Response:
{
  "x": 0,
  "y": 1,
  "direction": "NORTH"
}
```

### 3. Get Drone Position

**URL:**  `/drone-management-service/api/drones/{id}`

**Method: GET**

**Parameters:**

* {id} (path variable, long, required): ID of the drone to retrieve position. 

Example:
```json
Request:
GET /drone-management-service/api/drones/1
Response:
{
  "x": 0,
  "y": 1,
  "direction": "NORTH"
}
```

### 4. Get All Drones
**URL:** `/drone-management-service/api/drones/`

**Method: GET**

Example:
```json
Request:
GET /drone-management-service/api/drones/
Response:
{
  "drones": [
    {
      "x": 0,
      "y": 1,
      "direction": "NORTH"
    },
    {
      "x": 1,
      "y": 0,
      "direction": "EAST"
    }
  ]
}
```

### 5. Get Drone Commands
**URL:** `/drone-management-service/api/drones/{id}/commands`

**Method: GET**

**Parameters:**

* `{id}` (path variable, long, required): ID of the drone to retrieve commands.
* `last` (request parameter, integer, optional): Number of recent commands to fetch.

Example:

```json
Request:
GET /drone-management-service/api/drones/1/commands?last=2
Response:
[
  {
    "droneId": 1,
    "command": "LLMRMM"
  },
  {
    "droneId": 1,
    "command": "L"
  }
]
```

## Running the Project
To run the Drone Management System locally:

1. Clone this repository.
2. Ensure you have Java 17, Maven installed, Docker is installed and running.
3. Configure the application properties in src/main/resources/application.properties if needed (e.g., database settings).
4. Build the project using Maven: ```mvn clean install```
5. Build docker image: ```docker build -t drone-management-system .```
6. Run docker compose: ```docker compose up -d ```
7. The application will start on http://localhost:8092. You can now use any API testing tool (e.g., Postman) to interact with the endpoints.
8. GPI.postman_collection.json contains example for all endpoints.
