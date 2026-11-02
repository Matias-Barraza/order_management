-- INSERT INITIAL USERS
-- Note: Passwords below are sample BCrypt hashes. In production, these
-- must be generated securely via Spring Security's PasswordEncoder.
INSERT INTO users (email, password, active)
VALUES ('admin@email.com', '{bcrypt}$2a$10$/v/gQ45UrRk0obweR72Fi.gDe2knX7l5ZYY0alBfW3KTKDCI4qjci', TRUE);

INSERT INTO users (email, password, active)
VALUES ('seller@email.com', '{bcrypt}$2a$10$/v/gQ45UrRk0obweR72Fi.gDe2knX7l5ZYY0alBfW3KTKDCI4qjci', TRUE);

INSERT INTO users (email, password, active)
VALUES ('user@email.com', '{bcrypt}$2a$10$/v/gQ45UrRk0obweR72Fi.gDe2knX7l5ZYY0alBfW3KTKDCI4qjci', true);

INSERT INTO users (email, password, active)
VALUES ('inactive_user@email.com', '{bcrypt}$2a$10$/v/gQ45UrRk0obweR72Fi.gDe2knX7l5ZYY0alBfW3KTKDCI4qjci', FALSE);


-- INSERT INITIAL CLIENTS
INSERT INTO clients (first_name, last_name, document_type, document_number, phone_number, email, street, number, city, province, postal_code, country)
VALUES ('Juan', 'Pérez', 'DNI', '35123456', '+541144445555', 'juan.perez@email.com', 'Av. Santa Fe', '1234', 'CABA', 'Buenos Aires', '1425', 'Argentina');

INSERT INTO clients (first_name, last_name, document_type, document_number, phone_number, email, street, number, city, province, postal_code, country)
VALUES ('María Lauren', 'Gómez', 'CUIT', '20301234567', '+543415556666', 'maria.gomez@email.com', 'Calle Córdoba', '456', 'Rosario', 'Santa Fe', '2000', 'Argentina');

INSERT INTO clients (first_name, last_name, document_type, document_number, phone_number, email, street, number, city, province, postal_code, country)
VALUES ('Matias', 'Barraza', 'CUIT', '20891011567', '+543417778888', 'matias.barraza@email.com', 'Calle siempre viva', '789', 'Moreno', 'Buenos Aires', '1744', 'Argentina');


-- INSERT INITIAL PRODUCTS
INSERT INTO products (code, name, unit_price, unit_id)
VALUES ('PROD-001', 'Notebook Gamer Core i7', 1250.00, 'U');

INSERT INTO products (code, name, unit_price, unit_id)
VALUES ('PROD-002', 'Monitor UltraWide 29"', 350.00, 'U');

INSERT INTO products (code, name, unit_price, unit_id)
VALUES ('PROD-003', 'Teclado Mecánico RGB', 85.50, 'U');

