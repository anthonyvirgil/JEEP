USE D0831360;

DROP TABLE IF EXISTS folders;
CREATE TABLE folders (
  id int(11) NOT NULL auto_increment,
  name varchar(20) NOT NULL,
  PRIMARY KEY  (id)
) ENGINE=InnoDB;

INSERT INTO folders (name) VALUES ("Inbox");
INSERT INTO folders (name) VALUES ("Ready to Send"); 
INSERT INTO folders (name) VALUES ("Outbox");
INSERT INTO folders (name) VALUES ("Deleted");