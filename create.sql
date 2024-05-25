CREATE TABLE Roles (
                       id SERIAL PRIMARY KEY ,
                       role varchar(20) NOT NULL
);

INSERT INTO Roles(role) VALUES
                            ('BANED'),
                            ('NO_AUTH'),
                            ('USER'),
                            ('ADMIN');


CREATE TABLE Users (
                       id INTEGER PRIMARY KEY,
                       username varchar(80) UNIQUE NOT NULL ,
                       password varchar(80) NOT NULL ,
                       salt varchar(10) NOT NULL,
                       role_id INTEGER NOT NULL ,
                       FOREIGN KEY (role_id) REFERENCES Roles(id)
);

CREATE TABLE OrganizationTypes (
                                   id SERIAL PRIMARY KEY,
                                   type varchar(80) NOT NULL
);

INSERT INTO OrganizationTypes(type) VALUES
                                        ('commercial'),
                                        ('public'),
                                        ('PLC'),
                                        ('OJSC');

CREATE TABLE Organizations (
                               id INTEGER unique PRIMARY KEY,
                               name text NOT NULL,
                               x INTEGER NOT NULL CHECK ( x <= 199 ),
                               y INTEGER NOT NULL,
                               creationDate DATE NOT NULL CHECK ( creationDate <= CURRENT_DATE ),
                               annualTurnover DOUBLE PRECISION NOT NULL CHECK ( annualTurnover > 0 ),
                               type_id INTEGER NOT NULL,
                               postalAddress text CHECK ( length(postalAddress) >= 9 ),
                               user_id INTEGER NOT NULL,
                               FOREIGN KEY (type_id) REFERENCES OrganizationTypes(id),
                               FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE SEQUENCE IF NOT EXISTS organization_id_seq
    AS INTEGER
    INCREMENT 1
    MINVALUE 1;

CREATE SEQUENCE IF NOT EXISTS user_id_seq
    AS INTEGER
    INCREMENT 1
    MINVALUE 0;

INSERT INTO Users(id, username, password, salt, role_id)
VALUES (nextval('user_id_seq'), 'admin', 'password', 'salt',4);