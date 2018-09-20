DROP TABLE IF EXISTS board_games;
  CREATE TABLE board_games
  ( PK_BOARD_GAME_ID int(10) NOT NULL,
    NAME varchar(100) NOT NULL,
    PROFILE_PICTURE varchar(500) NOT NULL,
      DESCRIPTION varchar(5000) NOT NULL,

    CONSTRAINT customers_pk PRIMARY KEY (PK_BOARD_GAME_ID)
  );

DROP TABLE IF EXISTS pubs;
CREATE TABLE `pubs` (
  `PK_PUB_ID` int(11) NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `ADDRESS` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `profile_picture` mediumblob,
  `token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`PK_PUB_ID`)
);

DROP TABLE IF EXISTS pub_game_play;
CREATE TABLE pub_game_play(
  PK_PUB_ID int(11) NOT NULL,
  PK_BOARD_GAME_ID int(11) NOT NULL,
  PRIMARY KEY (`PK_PUB_ID`,`PK_BOARD_GAME_ID`),
  FOREIGN KEY (`PK_PUB_ID`) REFERENCES `pubs` (`PK_PUB_ID`),
  FOREIGN KEY (`PK_BOARD_GAME_ID`) REFERENCES `board_games` (`PK_BOARD_GAME_ID`)
);

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

DROP TABLE IF EXISTS user_games;
CREATE TABLE `user_games` (
  `PK_BOARD_GAME_ID` int(11) NOT NULL,
  `PK_USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`PK_BOARD_GAME_ID`,`PK_USER_ID`),
  FOREIGN KEY (`PK_BOARD_GAME_ID`) REFERENCES `board_games` (`PK_BOARD_GAME_ID`),
  FOREIGN KEY (`PK_USER_ID`) REFERENCES `users` (`PK_USER_ID`)
);


