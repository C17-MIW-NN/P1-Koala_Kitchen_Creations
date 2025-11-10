DROP SCHEMA IF EXISTS koala_kitchen_creations;
CREATE SCHEMA koala_kitchen_creations;
USE koala_kitchen_creations;
DROP USER IF EXISTS 'userKoala'@'localhost';
CREATE USER 'userKoala'@'localhost' IDENTIFIED BY 'userKoalaPW';
GRANT ALL PRIVILEGES ON koala_kitchen_creations.* TO 'userKoala'@'localhost';