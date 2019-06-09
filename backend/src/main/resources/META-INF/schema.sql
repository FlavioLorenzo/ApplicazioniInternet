CREATE TABLE busline(id_line SERIAL PRIMARY KEY, line_name VARCHAR(25) NOT NULL, admin_email VARCHAR(56));

CREATE TABLE stop(id_stop SERIAL PRIMARY KEY, stop_name VARCHAR(25) NOT NULL, gps VARCHAR(30));

CREATE TABLE busline_stop(id_busline_stop SERIAL PRIMARY KEY, id_line INTEGER REFERENCES busline (id_line), id_stop INTEGER REFERENCES stop (id_stop), arrival_time TIME NOT NULL, direction BOOLEAN NOT NULL);

CREATE TABLE ride(id_ride SERIAL PRIMARY KEY, id_line INTEGER REFERENCES busline (id_line), date DATE NOT NULL, direction BOOLEAN NOT NULL);

CREATE TABLE role(id_role SERIAL PRIMARY KEY, name VARCHAR(30));

CREATE TABLE users(id_user SERIAL PRIMARY KEY, first_name VARCHAR(20), last_name VARCHAR(20), phone VARCHAR(20), email VARCHAR(128), password VARCHAR(256), id_role INTEGER REFERENCES role (id_role), enabled BOOLEAN);

CREATE TABLE reservation(id_reservation SERIAL PRIMARY KEY, id_ride INTEGER REFERENCES ride (id_ride), id_user INTEGER REFERENCES users (id_user), id_join_stop INTEGER REFERENCES stop (id_stop), id_leave_stop INTEGER REFERENCES stop (id_stop), presence BOOLEAN DEFAULT FALSE);

CREATE TABLE confirmation_token(id_token SERIAL PRIMARY KEY, confirmation_token VARCHAR(255), creation_date DATE, id_user INTEGER REFERENCES users (id_user));

CREATE TABLE recover_token(id_token SERIAL PRIMARY KEY, recover_token VARCHAR(255), creation_date DATE, id_user INTEGER REFERENCES users (id_user), is_valid BOOLEAN);