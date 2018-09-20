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
import ro.bg.model.User;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDAOTest {

    private EmbeddedDatabase db;

    @Autowired
    UserDAO userDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testFindByEmail() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-user-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO USERS (PK_USER_ID, NAME, EMAIL, PASSWORD, PROFILE_PICTURE) VALUES (?, ?, ?, ?, ?)",
                1, "Teo", "email", "parola", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        User user = userDAO.findByEmail("email");
        Assert.assertEquals(1,user.getId());
        Assert.assertEquals("Teo",user.getName());
        Assert.assertEquals("parola",user.getPassword());
    }

    @Test
    public void testFindByEmailAndPassword() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-user-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO USERS (PK_USER_ID, NAME, EMAIL, PASSWORD, PROFILE_PICTURE) VALUES (?, ?, ?, ?, ?)",
                1, "Teo", "email", "parola", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        User user = userDAO.findByEmailAndPassword("email","parola");
        Assert.assertEquals(1,user.getId());
        Assert.assertEquals("Teo",user.getName());
    }

    @Test
    public void testFindByNameContaining() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-user-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO USERS (PK_USER_ID, NAME, EMAIL, PASSWORD, PROFILE_PICTURE) VALUES (?, ?, ?, ?, ?)",
                1, "Teodor", "email", "parola", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d")
        );

        List<User> users = userDAO.findByNameContaining("Teo");
        Assert.assertEquals(1,users.size());
        Assert.assertEquals(1,users.get(0).getId());
        Assert.assertEquals("Teodor",users.get(0).getName());
        Assert.assertEquals("parola",users.get(0).getPassword());
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
