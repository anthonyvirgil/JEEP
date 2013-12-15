USE D0831360;

DROP TABLE IF EXISTS mail;

CREATE TABLE mail (
  id int(11) NOT NULL auto_increment,
  sender text NOT NULL,
  recipient text NOT NULL,
  bcc text NOT NULL,
  cc text NOT NULL,
  subject varchar(50) NOT NULL default '',
  message text,
  mailDate datetime,
  folder varchar(20) NOT NULL default '',
  PRIMARY KEY  (id)
) ENGINE=InnoDB;

INSERT INTO mail VALUES (null, "anthony@abc.com", "ven@abc.com", "patrick@abc.com", '' , "Test1", "blah blah blah blah", CURDATE(), "Inbox");
INSERT INTO mail VALUES (null, "anthony@abc.com", "ven@abc.com", "patrick@abc.com", '', "Test2", "I love you", CURDATE(), "Inbox");
INSERT INTO mail VALUES (null, "anthony@abc.com", "alexis@abc.com", "deleted@test.com", "ven@brother.com" , "Deleted1", "I don't want this in my inbox", CURDATE(), "Deleted");
INSERT INTO mail VALUES (null, "anthony@abc.com", "alexis@abc.com", "deleted@test.com", "ven@brother.com", "Deleted2", "I'm breaking up with you", CURDATE(), "Deleted");
INSERT INTO mail VALUES (null, "anthony@abc.com", "alexis@abc.com", "send@test.com", '' , "Sending1", "Please send this ASAP", CURDATE(), "Ready to Send");
INSERT INTO mail VALUES (null, "anthony@abc.com", "alexis@abc.com", "send@test.com", '', "Sending2", "Send this whenever", CURDATE(), "Ready to Send");