DROP TABLE IF EXISTS users;
CREATE TABLE `users` (
  `PK_USER_ID` int(11) NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `TOWN` varchar(100) ,
  `ACCOUNT_TYPE` varchar(20),
  `profile_picture` mediumblob,
  `TOKEN` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`PK_USER_ID`)
);