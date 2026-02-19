CREATE TABLE devices (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    state VARCHAR(50) NOT NULL,
    creation_time TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);
