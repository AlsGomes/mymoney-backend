set foreign_key_checks=0;

TRUNCATE TABLE category;
TRUNCATE TABLE person;

set foreign_key_checks=1;

INSERT INTO category(code, name) VALUES ("342fd092-ec78-4cc8-8fca-d04c34be56e2",'Lazer');
INSERT INTO category(code, name) VALUES ("2d78b3e2-2b61-4c45-9a75-eb79f7b6db81",'Alimentação');
INSERT INTO category(code, name) VALUES ("e45031d6-a3b3-4ef0-bc1f-0c3cb7e6fd22",'Supermercado');
INSERT INTO category(code, name) VALUES ("96cc0bbd-067e-4767-a128-1bac0ef1e074",'Farmácia');
INSERT INTO category(code, name) VALUES ("ee1f7943-8412-42c8-9c2c-711333464fab",'Outros');

INSERT INTO person(code, name, active, street, num, complement, district, postal_Code, city, state) VALUES 
("aa2310d7-e159-42f9-933c-c15048588d2b",'Alisson', true, 'Rua da casa da mãe Joana', '52', 'Bloco J Apto. 12', 'Pracinha', '01234567', 'São Paulo', 'SP');
INSERT INTO person(code, name, active) VALUES ("d757fb84-fcd2-43d8-b2d8-9be5f756938b",'Cristina', true);