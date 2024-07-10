CREATE TABLE IF NOT EXISTS drones (
    id          SERIAL PRIMARY KEY,
    direction   VARCHAR(5) NOT NULL,
    x           INT NOT NULL,
    y           INT NOT NULL
);