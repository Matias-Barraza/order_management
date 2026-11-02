-- INSERT INITIAL ORDERS
-- Order 1: Belongs to Client 1 (Juan Pérez), Type '01' (Factura A)
INSERT INTO orders (date, client_id, code_billing_document_type, serie, correlative, total)
VALUES ('2026-10-22', 1, '01', 'F001', 1001, 1685.50);

-- Order 2: Belongs to Client 2 (María Gómez), Type '02' (Factura B)
INSERT INTO orders (date, client_id, code_billing_document_type, serie, correlative, total)
VALUES ('2026-10-22', 2, '02', 'F001', 1002, 435.50);


-- INSERT ORDER DETAILS (Items inside each order)
-- Details for Order 1 (Total: 1685.50)
-- 1x Notebook Gamer (1250.00)
INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal)
VALUES (1, 1, 1, 1250.00, 1250.00);

-- 1x Monitor UltraWide (350.00)
INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal)
VALUES (1, 2, 1, 350.00, 350.00);

-- 1x Teclado Mecánico (85.50)
INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal)
VALUES (1, 3, 1, 85.50, 85.50);

-- Details for Order 2 (Total: 435.50)
-- 1x Monitor UltraWide (350.00)
INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal)
VALUES (2, 2, 1, 350.00, 350.00);

-- 1x Teclado Mecánico (85.50)
INSERT INTO order_details (order_id, product_id, quantity, unit_price, subtotal)
VALUES (2, 3, 1, 85.50, 85.50);


-- MAP MANY-TO-MANY RELATIONSHIPS (Users_Roles Junction Table)
-- Mapping Roles for User 1 (Admin):
-- Roles: 1 (ADMIN), 2 (USER)
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);

-- Mapping Roles for User 2 (Seller):
-- Roles: 3 (SELLER)
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 3);

-- Mapping Roles for User 3 (User):
-- Roles: 2 (USER)
INSERT INTO users_roles (user_id, role_id)
VALUES (3, 2);

-- Mapping Roles for User 4 (User):
-- Roles: 2 (USER)
INSERT INTO users_roles (user_id, role_id)
VALUES (4, 2);

