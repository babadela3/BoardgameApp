package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;

import java.util.List;


@Repository
public interface PubDAO extends JpaRepository<Pub,Integer> {

    @Query(value = "SELECT * FROM PUBS WHERE EMAIL = :email", nativeQuery = true)
    Pub findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM PUBS WHERE EMAIL = :email AND PASSWORD = :password ;", nativeQuery = true)
    Pub findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query(value = "SELECT * FROM PUBS WHERE ADDRESS = :address", nativeQuery = true)
    Pub findByAddress(@Param("address") String address);

    List<Pub> findByNameContaining(String name);

    @Query(value = "SELECT BG.pk_board_game_id, BG.name, BG.description, BG.profile_picture FROM BOARD_GAMES BG JOIN PUB_GAME_PLAY PGP ON BG.PK_BOARD_GAME_ID = PGP.PK_BOARD_GAME_ID " +
            "WHERE PGP.PK_PUB_ID = :pubId", nativeQuery = true)
    List<BoardGame> getPubGames(@Param("pubId") int pubId);
}
