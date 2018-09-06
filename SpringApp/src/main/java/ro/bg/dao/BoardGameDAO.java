package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.BoardGame;

import java.util.List;

@Repository
public interface BoardGameDAO extends JpaRepository<BoardGame,Integer> {

    @Query(value = "select bg.pk_board_game_id, bg.name, bg.description, bg.profile_picture from board_games bg\n" +
            "join pub_game_play pgb on bg.pk_board_game_id = pgb.pk_board_game_id\n" +
            "where pk_pub_id = :boardGameId ;",nativeQuery = true)
    List<BoardGame> getAllById(@Param("boardGameId") int boardGameId);

    @Query(value = "select bg.pk_board_game_id, bg.name, bg.description, bg.profile_picture from board_games bg\n" +
            "join user_games ug on bg.pk_board_game_id = ug.pk_board_game_id\n" +
            "where pk_user_id = :userId ;",nativeQuery = true)
    List<BoardGame> getUserBoardGameById(@Param("userId") int userId);

}
