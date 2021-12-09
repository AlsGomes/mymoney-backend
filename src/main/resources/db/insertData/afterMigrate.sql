set foreign_key_checks=0;

TRUNCATE TABLE category;
TRUNCATE TABLE person;
TRUNCATE TABLE registers;
TRUNCATE TABLE user;
TRUNCATE TABLE permission;
TRUNCATE TABLE user_permission;

set foreign_key_checks=1;

INSERT INTO category(code, name) VALUES ("342fd092-ec78-4cc8-8fca-d04c34be56e2",'Lazer');
INSERT INTO category(code, name) VALUES ("2d78b3e2-2b61-4c45-9a75-eb79f7b6db81",'Alimentação');
INSERT INTO category(code, name) VALUES ("e45031d6-a3b3-4ef0-bc1f-0c3cb7e6fd22",'Supermercado');
INSERT INTO category(code, name) VALUES ("96cc0bbd-067e-4767-a128-1bac0ef1e074",'Farmácia');
INSERT INTO category(code, name) VALUES ("ee1f7943-8412-42c8-9c2c-711333464fab",'Outros');

INSERT INTO person(code, name, active, street, num, complement, district, postal_Code, city, state) VALUES 
("aa2310d7-e159-42f9-933c-c15048588d2b",'Alisson', true, 'Rua da casa da mãe Joana', '52', 'Bloco J Apto. 12', 'Pracinha', '01234567', 'São Paulo', 'SP');
INSERT INTO person(code, name, active) VALUES ("d757fb84-fcd2-43d8-b2d8-9be5f756938b",'Cristina', true);

INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('9ebcbdb2-b229-4e7f-aa92-5e9b81ea5f6f', 'Salário mensal', '2017-06-10', null, 6500.00, 'Distribuição de lucros', 'INCOME', 1, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('5b3728c6-4e37-439c-8094-06274269c077', 'Bahamas', '2017-02-10', '2017-02-10', 100.32, null, 'EXPENSE', 2, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('ed8134b3-72dd-4356-8d33-7dd5a0d5afa6', 'Top Club', '2017-06-10', null, 120, null, 'INCOME', 3, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('a59dc9d4-58dd-4472-a19a-d3365de65839', 'CEMIG', '2017-02-10', '2017-02-10', 110.44, 'Geração', 'INCOME', 3, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('12c67a61-8bfb-49a7-80b1-6b5b0ea49099', 'DMAE', '2017-06-10', null, 200.30, null, 'EXPENSE', 3, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('0ac88bf6-0fa0-4b99-a1df-d6d436cc77c0', 'Extra', '2017-03-10', '2017-03-10', 1010.32, null, 'INCOME', 4, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('a29cc34f-900b-45ea-b708-f5ea870f6375', 'Bahamas', '2017-06-10', null, 500, null, 'INCOME', 1, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('97c1e4fe-e85a-4cfa-8e7a-b9ec65711e0b', 'Top Club', '2017-03-10', '2017-03-10', 400.32, null, 'EXPENSE', 4, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('53889fe9-fea7-4cbc-b915-f51732769af1', 'Despachante', '2017-06-10', null, 123.64, 'Multas', 'EXPENSE', 3, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('4cf2cab7-3890-4342-a07d-ab4a72e309d4', 'Pneus', '2017-04-10', '2017-04-10', 665.33, null, 'INCOME', 5, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('d06a6aa7-3459-42cc-a533-936357e63550', 'Café', '2017-06-10', null, 8.32, null, 'EXPENSE', 1, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('5b76158a-e18e-4078-a87a-065e485e61bf', 'Eletrônicos', '2017-04-10', '2017-04-10', 2100.32, null, 'EXPENSE', 5, 1);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('393662e1-633f-4463-95a2-8052d2e0edd8', 'Instrumentos', '2017-06-10', null, 1040.32, null, 'EXPENSE', 4, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('2566ad4f-8e3e-4729-8f5c-16bc79312f19', 'Café', '2017-04-10', '2017-04-10', 4.32, null, 'EXPENSE', 4, 2);
INSERT INTO registers (code, description, due_date, payment_date, value, obs, type, id_category, id_person) values ('d96523b6-22fc-4e1c-a98a-1df1f2009ca5', 'Lanche', '2017-06-10', null, 10.20, null, 'EXPENSE', 4, 1);

INSERT INTO user (code, name, email, password) values ("093cc8dc-587e-11ec-84ad-c79b81813928", 'Administrador', 'admin@algamoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO user (code, name, email, password) values ("1b6a3aee-587e-11ec-ae5e-fbbf4023da0a", 'Bolete Gomes', 'bolete@algamoney.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO permission (description) values ('ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permission (description) values ('ROLE_PESQUISAR_CATEGORIA');

INSERT INTO permission (description) values ('ROLE_CADASTRAR_PESSOA');
INSERT INTO permission (description) values ('ROLE_REMOVER_PESSOA');
INSERT INTO permission (description) values ('ROLE_PESQUISAR_PESSOA');

INSERT INTO permission (description) values ('ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO permission (description) values ('ROLE_REMOVER_LANCAMENTO');
INSERT INTO permission (description) values ('ROLE_PESQUISAR_LANCAMENTO');

-- admin
INSERT INTO user_permission (id_user, id_permission) values (1, 1);
INSERT INTO user_permission (id_user, id_permission) values (1, 2);
INSERT INTO user_permission (id_user, id_permission) values (1, 3);
INSERT INTO user_permission (id_user, id_permission) values (1, 4);
INSERT INTO user_permission (id_user, id_permission) values (1, 5);
INSERT INTO user_permission (id_user, id_permission) values (1, 6);
INSERT INTO user_permission (id_user, id_permission) values (1, 7);
INSERT INTO user_permission (id_user, id_permission) values (1, 8);

-- maria
INSERT INTO user_permission (id_user, id_permission) values (2, 2);
INSERT INTO user_permission (id_user, id_permission) values (2, 5);
INSERT INTO user_permission (id_user, id_permission) values (2, 8);
