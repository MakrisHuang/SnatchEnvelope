DROP DATABASE IF EXISTS RequestEnvelop;
CREATE DATABASE RequestEnvelop;
USE RequestEnvelop;

CREATE TABLE User(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255),
  password VARCHAR(255),
  money BIGINT NOT NULL,
  remainTimes INT(10) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE Envelope(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  userId INT NOT NULL,
  totalPrice DECIMAL(16, 2) NOT NULL,
  unitPrice BIGINT NOT NULL,
  sendDate TIMESTAMP NOT NULL,
  totalAmount INT NOT NULL,
  remainAmount INT NOT NULL,
  version INT DEFAULT 0 NOT NULL,
  note VARCHAR(255) NULL,
  CONSTRAINT Envelope_User FOREIGN KEY (userId)
    REFERENCES User(id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE UserSnatchEnvelope(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  envelopeId INT NOT NULL,
  userId INT NOT NULL,
  priceSnatched BIGINT NOT NULL,
  grabTime TIMESTAMP NOT NULL,
  note VARCHAR(255) NULL,
  CONSTRAINT UserSnatchEnvelope_Envelope FOREIGN KEY (envelopeId)
    REFERENCES Envelope (id),
  CONSTRAINT UserSnatchEnvelope_User FOREIGN KEY (userId)
    REFERENCES User (id)
) ENGINE = InnoDB;