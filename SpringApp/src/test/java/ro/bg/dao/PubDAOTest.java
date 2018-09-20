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

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PubDAOTest {
    private EmbeddedDatabase db;

    @Autowired
    PubDAO pubDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testFindByEmail() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-user-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO PUBS (PK_PUB_ID, NAME, EMAIL, DESCRIPTION, PROFILE_PICTURE, ADDRESS, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?)",
                3, "NAME", "EMAIL", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d"), "address" , "password"
        );

        Pub pub = pubDAO.findByEmail("EMAIL");
        Assert.assertEquals(3,pub.getId());
        Assert.assertEquals("NAME",pub.getName());
        Assert.assertEquals("DESCRIPTION",pub.getDescription());
        Assert.assertEquals("address",pub.getAddress());
        Assert.assertEquals("password",pub.getPassword());

    }

    @Test
    public void testFindByEmailAndPassword() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-user-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO PUBS (PK_PUB_ID, NAME, EMAIL, DESCRIPTION, PROFILE_PICTURE, ADDRESS, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?)",
                3, "NAME", "EMAIL", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d"), "address" , "password"
        );

        Pub pub = pubDAO.findByEmailAndPassword("EMAIL","password");
        Assert.assertEquals(3,pub.getId());
        Assert.assertEquals("NAME",pub.getName());
        Assert.assertEquals("DESCRIPTION",pub.getDescription());
        Assert.assertEquals("address",pub.getAddress());
    }

    @Test
    public void testFindByNameContaining() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/create-user-db.sql").build();

        jdbcTemplate.update(
                "INSERT INTO PUBS (PK_PUB_ID, NAME, EMAIL, DESCRIPTION, PROFILE_PICTURE, ADDRESS, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?)",
                3, "NAME", "EMAIL", "DESCRIPTION", hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d"), "address" , "password"
        );

        List<Pub> pub = pubDAO.findByNameContaining("ME");
        Assert.assertEquals(1,pub.size());
        Assert.assertEquals(3,pub.get(0).getId());
        Assert.assertEquals("NAME",pub.get(0).getName());
        Assert.assertEquals("DESCRIPTION",pub.get(0).getDescription());
        Assert.assertEquals("address",pub.get(0).getAddress());
        Assert.assertEquals("password",pub.get(0).getPassword());
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
