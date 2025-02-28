INSERT INTO families(family_name) VALUES ('Петровы');
INSERT INTO families(family_name) VALUES ('Новиковы-Петровы');

INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (1, 'Владимир', 'Петров', 'М', '1977-07-15');
INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (1, 'Оксана', 'Петрова', 'Ж', '1981-04-16');
INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (1, 'Олеся', 'Петрова','Ж', '2007-02-20');

INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (2, 'Дмитрий', 'Новиков', 'М', '1990-01-15');
INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (2, 'Анастасия', 'Петрова', 'Ж', '1990-07-23');
INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (2, 'Никита', 'Новиков', 'М', '2010-02-13');
INSERT INTO families_members(family_id, name, surname, gender, birthday) VALUES (2, 'Дарья', 'Новикова', 'Ж', '2013-03-07');
