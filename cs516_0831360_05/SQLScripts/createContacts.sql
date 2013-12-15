USE D0831360;

DROP TABLE IF EXISTS contacts;
CREATE TABLE contacts (
  id int(11) NOT NULL auto_increment,
  emailAddress varchar(50) NOT NULL default '',
  firstName varchar(20) NOT NULL default '',
  lastName varchar(40) NOT NULL default '',
  PRIMARY KEY  (id)
) ENGINE=InnoDB;

INSERT INTO contacts (emailAddress, firstName, lastName) 
VALUES ("anthonyvirgil.bermejo@gmail.com", "Anthony-Virgil", "Bermejo"),
("just.pat@hotmail.com", "Patrick", "Nicoll");