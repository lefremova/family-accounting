CREATE TABLE IF NOT EXISTS `families` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `surname` varchar(255) NOT NULL

);

CREATE TABLE IF NOT EXISTS `people` (

    `person_id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `family_id` int NOT NULL,
    FOREIGN KEY (family_id) REFERENCES families(id),
    `name` varchar(255) NOT NULL,
    `gender` varchar(1) NOT NULL,
    `age` int NOT NULL

);
