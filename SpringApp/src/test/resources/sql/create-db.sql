DROP TABLE IF EXISTS board_games;
  CREATE TABLE board_games
  ( PK_BOARD_GAME_ID int(10) NOT NULL,
    NAME varchar(100) NOT NULL,
    PROFILE_PICTURE varchar(500) NOT NULL,
      DESCRIPTION varchar(5000) NOT NULL,

    CONSTRAINT customers_pk PRIMARY KEY (PK_BOARD_GAME_ID)
  );