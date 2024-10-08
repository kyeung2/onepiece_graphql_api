CREATE TABLE CREW
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE CHARACTERS
(
    id      UUID PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    role    VARCHAR(255) NOT NULL,
    faction VARCHAR(255),
    crew_id UUID,
    devil_fruit_code VARCHAR(6),
    FOREIGN KEY (crew_id) REFERENCES CREW (id) ON DELETE SET NULL
);