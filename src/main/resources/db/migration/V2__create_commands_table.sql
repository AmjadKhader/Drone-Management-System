CREATE TABLE IF NOT EXISTS commands (
    id          SERIAL PRIMARY KEY,
    command     VARCHAR(255) NOT NULL,
    drone_id    INT NOT NULL
);