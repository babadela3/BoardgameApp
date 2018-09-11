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

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardGameDAOTest {

    private EmbeddedDatabase db;

    @Autowired
    BoardGameDAO boardGameDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testSaveGame() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-db.sql").build();

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

    @After
    public void tearDown() {
        db.shutdown();
    }

}