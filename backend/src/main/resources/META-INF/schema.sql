CREATE TABLE role(id_role SERIAL PRIMARY KEY, name VARCHAR(30));

CREATE TABLE users(id_user SERIAL PRIMARY KEY, first_name VARCHAR(20), last_name VARCHAR(20), phone VARCHAR(20), email VARCHAR(128), password VARCHAR(256), id_role INTEGER REFERENCES role (id_role), enabled BOOLEAN);

CREATE TABLE child(id_child SERIAL PRIMARY KEY, first_name VARCHAR(20), last_name VARCHAR(20), phone VARCHAR(20), id_parent INTEGER REFERENCES users (id_user));

CREATE TABLE busline(id_line SERIAL PRIMARY KEY, name VARCHAR(25) NOT NULL, id_admin INTEGER REFERENCES users (id_user));

CREATE TABLE stop(id_stop SERIAL PRIMARY KEY, name VARCHAR(25) NOT NULL, gps VARCHAR(30));

CREATE TABLE busline_stop(id_busline_stop SERIAL PRIMARY KEY, id_line INTEGER REFERENCES busline (id_line), id_stop INTEGER REFERENCES stop (id_stop), arrival_time TIME NOT NULL, direction BOOLEAN NOT NULL);

CREATE TABLE ride(id_ride SERIAL PRIMARY KEY, id_line INTEGER REFERENCES busline (id_line), date DATE NOT NULL, direction BOOLEAN NOT NULL, ride_booking_status INTEGER NOT NULL, locked BOOLEAN DEFAULT FALSE, id_latest_stop INTEGER REFERENCES stop (id_stop), latest_stop_time TIME);

CREATE TABLE reservation(id_reservation SERIAL PRIMARY KEY, id_ride INTEGER REFERENCES ride (id_ride), id_child INTEGER REFERENCES child (id_child), id_stop INTEGER REFERENCES stop (id_stop), presence BOOLEAN DEFAULT FALSE);

CREATE TABLE availability(id_availability SERIAL PRIMARY KEY, id_ride INTEGER REFERENCES ride (id_ride), id_user INTEGER REFERENCES users (id_user), id_stop INTEGER REFERENCES stop (id_stop), shift_status INTEGER DEFAULT 1);

CREATE TABLE confirmation_token(id_token SERIAL PRIMARY KEY, confirmation_token VARCHAR(255), creation_date DATE, id_user INTEGER REFERENCES users (id_user));

CREATE TABLE recover_token(id_token SERIAL PRIMARY KEY, recover_token VARCHAR(255), creation_date DATE, id_user INTEGER REFERENCES users (id_user), is_valid BOOLEAN);

/*CREATE TABLE verification_token(id_token SERIAL PRIMARY KEY, verification_token VARCHAR(255), status VARCHAR(255), expired_date DATE, issued_date DATE, confirmed_date DATE, id_user INTEGER REFERENCES users (id_user));*/