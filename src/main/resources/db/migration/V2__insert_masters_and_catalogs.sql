-- INSERT INITIAL BILLING DOCUMENT TYPES
INSERT INTO billing_document_type (code, description)
VALUES ('01', 'Factura A');
INSERT INTO billing_document_type (code, description)
VALUES ('02', 'Factura B');
INSERT INTO billing_document_type (code, description)
VALUES ('03', 'Ticket Factura');

-- INSERT INITIAL UNITS OF MEASURE
INSERT INTO units (id, description)
VALUES ('U', 'Unidades');
INSERT INTO units (id, description)
VALUES ('KG', 'Kilogramos');
INSERT INTO units (id, description)
VALUES ('L', 'Litros');

-- INSERT INITIAL ROLES
INSERT INTO roles (name)
VALUES ('ADMIN');
INSERT INTO roles (name)
VALUES ('USER');
INSERT INTO roles (name)
VALUES ('SELLER');
