package ro.bg.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardGameDAOTest {

    private EmbeddedDatabase db;

    @Autowired
    BoardGameDAO boardGameDAO;

    @Autowired
    PubDAO pubDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testSaveGame() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-games-db.sql").build();

        BoardGame boardGame = new BoardGame(16,"Catan","Description","Picture");
        boardGameDAO.saveAndFlush(boardGame);

        List<Map<String, Object>> boardGameMap = jdbcTemplate.
                queryForList("SELECT * FROM BOARD_GAMES WHERE PK_BOARD_GAME_ID = 16 ");
        Assert.assertNotNull(boardGameMap);
        Assert.assertEquals(1,boardGameMap.size());
        for(Map<String, Object> bg : boardGameMap){
            Assert.assertEquals(boardGame.getId(),bg.get("PK_BOARD_GAME_ID"));
            Assert.assertEquals(boardGame.getDescription(),bg.get("DESCRIPTION"));
            Assert.assertEquals(boardGame.getName(),bg.get("NAME"));
            Assert.assertEquals(boardGame.getPicture(),bg.get("PROFILE_PICTURE"));
        }
    }

    @Test
    public void testGetAllById() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-games-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO PUBS (PK_PUB_ID, NAME, EMAIL, DESCRIPTION, PROFILE_PICTURE, ADDRESS, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?)",
                3, "NAME", "EMAIL", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d"), "address" , "password"
        );

        jdbcTemplate.update(
                "INSERT INTO BOARD_GAMES (PK_BOARD_GAME_ID, NAME, DESCRIPTION, PROFILE_PICTURE) VALUES (?, ?, ?, ?)",
                3, "Catan", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        jdbcTemplate.update(
                "INSERT INTO BOARD_GAMES (PK_BOARD_GAME_ID, NAME, DESCRIPTION, PROFILE_PICTURE) VALUES (?, ?, ?, ?)",
                8, "Monopoly", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        jdbcTemplate.update(
                "INSERT INTO PUB_GAME_PLAY (PK_BOARD_GAME_ID, PK_PUB_ID) VALUES (?, ?)",
                3, 3
        );

        jdbcTemplate.update(
                "INSERT INTO PUB_GAME_PLAY (PK_BOARD_GAME_ID, PK_PUB_ID) VALUES (?, ?)",
                8, 3
        );

        List<BoardGame> boardGames = boardGameDAO.getAllById(3);
        Assert.assertEquals(2,boardGames.size());
        Assert.assertEquals(3,boardGames.get(0).getId());
        Assert.assertEquals("Catan",boardGames.get(0).getName());
        Assert.assertEquals("DESCRIPTION",boardGames.get(0).getDescription());
        Assert.assertEquals(8,boardGames.get(1).getId());
        Assert.assertEquals("Monopoly",boardGames.get(1).getName());
        Assert.assertEquals("DESCRIPTION",boardGames.get(1).getDescription());
    }

    @Test
    public void testGetUserBoardGameById() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-games-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO BOARD_GAMES (PK_BOARD_GAME_ID, NAME, DESCRIPTION, PROFILE_PICTURE) VALUES (?, ?, ?, ?)",
                3, "Catan", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        jdbcTemplate.update(
                "INSERT INTO BOARD_GAMES (PK_BOARD_GAME_ID, NAME, DESCRIPTION, PROFILE_PICTURE) VALUES (?, ?, ?, ?)",
                8, "Monopoly", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        jdbcTemplate.update(
                "INSERT INTO USERS (PK_USER_ID, NAME, EMAIL, PASSWORD, PROFILE_PICTURE) VALUES (?, ?, ?, ?, ?)",
                1, "Teo", "email", "parola", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        jdbcTemplate.update(
                "INSERT INTO USER_GAMES (PK_BOARD_GAME_ID, PK_USER_ID) VALUES (?, ?)",
                3, 1
        );

        jdbcTemplate.update(
                "INSERT INTO USER_GAMES (PK_BOARD_GAME_ID, PK_USER_ID) VALUES (?, ?)",
                8, 1
        );

        List<BoardGame> boardGames = boardGameDAO.getUserBoardGameById(1);
        Assert.assertEquals(2,boardGames.size());
        Assert.assertEquals(3,boardGames.get(0).getId());
        Assert.assertEquals("Catan",boardGames.get(0).getName());
        Assert.assertEquals("DESCRIPTION",boardGames.get(0).getDescription());
        Assert.assertEquals(8,boardGames.get(1).getId());
        Assert.assertEquals("Monopoly",boardGames.get(1).getName());
        Assert.assertEquals("DESCRIPTION",boardGames.get(1).getDescription());
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

}