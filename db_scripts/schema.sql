SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Requests CASCADE;
DROP TABLE IF EXISTS FinalLogs CASCADE;
SET FOREIGN_KEY_CHECKS=1;


CREATE TABLE Users (
                       uid VARCHAR(18) PRIMARY KEY,
                       bal int CHECK (bal >= 0)
);

CREATE TABLE Requests (
                          request_datetime timestamp DEFAULT NOW(),
                          uid VARCHAR(18),
                          gtype int NOT NULL CHECK (gtype > 0 AND gtype <= 3),
                          gdetail int NOT NULL,
                          amount int CHECK(amount >= 0),
                          PRIMARY KEY (request_datetime, uid),
                          FOREIGN KEY (uid) REFERENCES Users(uid)
);

CREATE TABLE FinalLogs (
                           entry_datetime timestamp DEFAULT NOW(),
                           uid VARCHAR(18),
                           amount int,
                           PRIMARY KEY (entry_datetime, uid),
                           FOREIGN KEY (uid) REFERENCES Users(uid)
);

DROP TRIGGER IF EXISTS maxBalance;

DELIMITER $$

CREATE TRIGGER maxBalance
BEFORE UPDATE ON Users
FOR EACH ROW
BEGIN
    IF NEW.bal > 1000
    THEN SET NEW.bal = 1000;
END IF;
END $$
