CREATE TABLE IF NOT EXISTS `families` (

    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `family_name` varchar(255) NOT NULL UNIQUE

);

CREATE TABLE IF NOT EXISTS `families_members` (

    `person_id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `family_id` bigint NOT NULL,
    FOREIGN KEY (family_id) REFERENCES families(id),
    `name` varchar(255) NOT NULL,
    `surname` varchar(255) NOT NULL,
    `gender` varchar(1) NOT NULL,
    `birthday` date NOT NULL

);
