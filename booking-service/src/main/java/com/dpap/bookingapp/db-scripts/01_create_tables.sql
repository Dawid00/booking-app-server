CREATE SEQUENCE seq_places START 1;
CREATE SEQUENCE seq_rooms START 1;
CREATE SEQUENCE seq_room_details START 1;
CREATE SEQUENCE seq_reservations START 1;
CREATE SEQUENCE seq_accommodations START 1;
CREATE SEQUENCE seq_users START 1;
CREATE SEQUENCE seq_images START 1;
CREATE SEQUENCE seq_accommodations_rooms START 1;
CREATE SEQUENCE seq_usages START 1 INCREMENT BY 1;;
CREATE SEQUENCE seq_reviews START 1 INCREMENT BY 1;;
CREATE TABLE roles
(
    id   INT PRIMARY KEY NOT NULL,
    name TEXT
);
CREATE TABLE users
(
    id             INT PRIMARY KEY NOT NULL,
    username       TEXT,
    first_name     TEXT,
    last_name      TEXT,
    password       TEXT,
    email          TEXT,
    account_number TEXT,
    phone_number   TEXT            NULL
);
CREATE TABLE users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
ALTER TABLE users_roles
    ADD CONSTRAINT "ID_PKEY" PRIMARY KEY (user_id, role_id);
CREATE TABLE voivodeships
(
    id   INT PRIMARY KEY NOT NULL,
    name TEXT
);
CREATE TABLE categories
(
    id   INT PRIMARY KEY NOT NULL,
    name TEXT
);

CREATE TABLE places
(
    id                  INT PRIMARY KEY NOT NULL,
    description         TEXT,
    name                TEXT,
    address_city        TEXT,
    address_country     TEXT,
    address_street      TEXT,
    address_building    TEXT,
    category            TEXT,
    address_voivodeship TEXT,
    user_id             INT             NOT NULL,
    facilities          jsonb,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE rooms
(
    id              INT PRIMARY KEY NOT NULL,
    capacity        INT             NOT NULL,
    state           TEXT,
    place_id        INT,
    price_per_night FLOAT,
    facilities      jsonb,
    name            TEXT,
    description     TEXT,
    FOREIGN KEY (place_id) REFERENCES places (id)
);

CREATE TABLE facilities
(
    id   INT PRIMARY KEY NOT NULL,
    name TEXT
);
CREATE TABLE usages
(
    id        INT PRIMARY KEY NOT NULL,
    object_id INT,
    start     TIMESTAMP       NOT NULL,
    finish    TIMESTAMP       NOT NULL,
    at        TIMESTAMP       NOT NULL
);
CREATE TABLE reviews
(
    id       INT PRIMARY KEY NOT NULL,
    content  TEXT            NOT NULL,
    rating   INT             NOT NULL,
    place_id INT             NOT NULL,
    user_id  INT             NOT NULL,
    at       timestamp       NOT NULL,
    FOREIGN KEY (place_id) REFERENCES places (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE reservation_states
(
    id   INT PRIMARY KEY NOT NULL,
    name TEXT            NOT NULL
);
CREATE TABLE reservations
(
    id                     INT PRIMARY KEY NOT NULL,
    room_id                INT             NOT NULL,
    place_id               INT             NOT NULL,
    user_id                INT,
    start                  TIMESTAMP       NOT NULL,
    finish                 TIMESTAMP       NOT NULL,
    at                     TIMESTAMP       NOT NULL,
    state_id               INT,
    value                  FLOAT,
    free_cancellation_days INT,
    FOREIGN KEY (state_id) REFERENCES reservation_states (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id),
    FOREIGN KEY (place_id) REFERENCES places (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE images
(
    id       INT PRIMARY KEY NOT NULL,
    place_id INT             NOT NULL,
    url      text
);
-- CREATE TABLE accommodations
-- (
--     id             INT PRIMARY KEY NOT NULL,
--     user_id        INT,
--     landlord_id    INT,
--     reservation_id INT,
--     place_id       INT,
--     start_date     TIMESTAMP,
--     end_date       TIMESTAMP,
--     value          FLOAT,
--     duration       INT
-- );
--
-- CREATE TABLE accommodations_rooms
-- (
--     id               INT NOT NULL,
--     room_id          INT NOT NULL,
--     accommodation_id INT NOT NULL,
--     FOREIGN KEY (accommodation_id) REFERENCES accommodations (id),
--     FOREIGN KEY (room_id) REFERENCES rooms (id)
-- );
