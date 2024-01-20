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
INSERT INTO roles
VALUES (1, 'USER'),
       (2, 'ADMIN');

INSERT INTO users
VALUES ((SELECT nextval('seq_users')),
        'tenant',
        'Dawid',
        'Plewik',
        '$2a$10$eC1bdpvwVdZBFpc3nu6GRO7gDs26W4Sf5mEMjXS2c4H/f3qrciKou',
        's95529@pollub.edu.pl',
        '124312124125125125125',
        '123456789');
INSERT INTO users
VALUES ((SELECT nextval('seq_users')),
        'landlord',
        'Diwad',
        'Kiwelp',
        '$2a$10$eC1bdpvwVdZBFpc3nu6GRO7gDs26W4Sf5mEMjXS2c4H/f3qrciKou',
        'diwad@kiwelp.com',
        '124312124125125125125',
        '123456789');
INSERT INTO users_roles
VALUES (1, 1);
INSERT INTO users_roles
VALUES (2, 2);

INSERT INTO voivodeships
VALUES (1, 'lubelskie'),
       (2, 'opolskie'),
       (3, 'malopolskie'),
       (4, 'slaskie'),
       (5, 'dolnoslaskie'),
       (6, 'lubuskie'),
       (7, 'mazowieckie'),
       (8, 'podkarpackie'),
       (9, 'pomorskie'),
       (10, 'podlaskie'),
       (11, 'zachodnio-pomorskie'),
       (12, 'warminsko-mazurskie'),
       (13, 'lodzkie'),
       (14, 'swietokrzyskie'),
       (15, 'kujawsko-pomorskie'),
       (16, 'lubelskie');

INSERT INTO facilities
VALUES (1, 'TV'),
       (2, 'AIR_CONDITIONER'),
       (3, 'DOUBLE_BED'),
       (4, 'SINGLE_BED'),
       (5, 'SAUNA'),
       (6, 'JACUZZI'),
       (7, 'WIFI'),
       (8, 'GRILL'),
       (9, 'GYM'),
       (10, 'SWIMMING_POOL');
INSERT INTO categories
VALUES (1, 'HOTEL'),
       (2, 'APARTMENTS'),
       (3, 'RESORTS'),
       (4, 'VILLAS'),
       (5, 'CAMPING');
INSERT INTO places(id, description, name, address_city, address_building, address_street, address_country, category,
                   address_voivodeship, user_id)
VALUES ((SELECT nextval('seq_places')), 'Hostel nieopdal centrum', 'Noclegownia', 'Lublin', '4', 'Lubelska', 'Polska',
        'HOTEL',
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hotel', 'Huzar', 'Lublin', '4', 'Cicha', 'Polska',
        (SELECT name FROM categories WHERE id = 1),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hotel', 'Hotel Maximus', 'Lublin', '14', 'Wojciechowska', 'Polska',
        (SELECT name FROM categories WHERE id = 2),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hotel', 'Hotel Rozmus', 'Lublin', '24', 'Kosmiczna', 'Polska',
        (SELECT name FROM categories WHERE id = 1),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hotel', 'Hotel u Strusia', 'Lublin', '34', 'Nadbystrzycka', 'Polska',
        (SELECT name FROM categories WHERE id = 1),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hotel', 'Rodzinna przystań', 'Opole', '54', 'Lubelska', 'Polska',
        (SELECT name FROM categories WHERE id = 4),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hotel', 'Pole namiotowe u Karola', 'Gliwice', '64', 'Zemborzycka', 'Polska',
        (SELECT name FROM categories WHERE id = 5),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Aparatment', 'Hotel Korona', 'Kielce', '14', 'Kazimierska', 'Polska',
        (SELECT name FROM categories WHERE id = 1),
        'LUBELSKIE', 2),
       ((SELECT nextval('seq_places')), 'Hostel', 'Apartamenty Jodłowa', 'Warszawa', '234', 'Jodłowa', 'Polska',
        (SELECT name FROM categories WHERE id = 2),
        'LUBELSKIE', 2);

INSERT INTO rooms(id, capacity, state, place_id, price_per_night, name, description)
VALUES ((SELECT nextval('seq_rooms')), 1, 'AVAILABLE', 1, 50.00, 'Pokoj jednoosobowy z łazienką na korytarzu', 'Opis'),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 1, 100.00, 'Pokoj dwuosobowy z łazienką na korytarzu', ''),
       ((SELECT nextval('seq_rooms')), 3, 'AVAILABLE', 1, 150.00, 'Pokoj trzyosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 2, 300.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 3, 100.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 3, 'AVAILABLE', 3, 125.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 3, 300.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 3, 100.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 3, 'AVAILABLE', 4, 125.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 5, 300.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE',5, 100.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 6, 300.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 2, 'AVAILABLE', 6, 100.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 3, 'AVAILABLE', 6, 125.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 3, 'AVAILABLE', 6, 125.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 8, 'AVAILABLE',7, 300.00, 'Pokoj dwuosobowy z łazienką', ''),
       ((SELECT nextval('seq_rooms')), 8, 'AVAILABLE',8, 300.00, 'Pokoj dwuosobowy z łazienką', '')
;

-- INSERT INTO room_details(id, name, description, image_url, room_id)
-- VALUES ((SELECT nextval('seq_room_details')), 'Pokoj jednoosobowy z łazienką na korytarzu', '', 'first', 1),
--        ((SELECT nextval('seq_room_details')), 'Pokoj dwuosobowy z łazienką na korytarzu', '', 'second', 2),
--        ((SELECT nextval('seq_room_details')), 'Pokoj trzyosobowy z łazienką', '', 3),
--        ((SELECT nextval('seq_room_details')), 'Pokoj dwuosobowy z łazienką', '', 'third', 4),
--        ((SELECT nextval('seq_room_details')), 'Pokoj dwuosobowy z łazienką', '', 'third', 5),
--        ((SELECT nextval('seq_room_details')), 'Pokoj trzyosobowy z łazienką', '', 'third', 6),
--        ((SELECT nextval('seq_room_details')), 'Apartament z łazienką', '', 'third', 7);

INSERT INTO reservation_states
VALUES (1, 'CONFIRMED'),
       (2, 'CHECK_IN'),
       (3, 'CHECK_OUT'),
       (4, 'WAITING'),
       (5, 'CANCELLED'),
       (6, 'PAID');

INSERT INTO reservations
VALUES ((SELECT nextval('seq_reservations')), 1, 2, 2, '2023-07-07T12:00', '2023-07-11T12:00', '2023-07-01T10:00:00', 1,
        400.00,
        14);

INSERT INTO reviews
VALUES ((SELECT nextval('seq_reviews')), 'Great place with amazing views. There are also very comfortable beds.', 5, 1,
        1, '2023-07-11T12:00');
INSERT INTO images
VALUES ((SELECT nextval('seq_images')), 1, 'http://localhost:9990/files/images/image1');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 2, 'http://localhost:9990/files/images/image2');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 3, 'http://localhost:9990/files/images/image3');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 4, 'http://localhost:9990/files/images/image4');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 5, 'http://localhost:9990/files/images/image5');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 6, 'http://localhost:9990/files/images/image6');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 7, 'http://localhost:9990/files/images/image7');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 8, 'http://localhost:9990/files/images/image8');
INSERT INTO images VALUES ((SELECT nextval('seq_images')), 9, 'http://localhost:9990/files/images/image9');


