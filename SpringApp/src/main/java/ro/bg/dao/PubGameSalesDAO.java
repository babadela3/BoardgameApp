package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.PubGameSales;

import java.util.List;

@Repository
public interface PubGameSalesDAO extends JpaRepository<PubGameSales,Integer>{

    @Query(value = "select * from pub_game_sales\n" +
            "where pk_pub_id = :pubId;",nativeQuery = true)
    List<PubGameSales> getAllGames(@Param("pubId") int pubId);

    @Query(value = "select * from pub_game_sales\n" +
            "where pk_pub_id = :pubId " +
            "and pk_board_game_id = :boardGameId ;",nativeQuery = true)
    PubGameSales getPubGameSale(@Param("pubId") int pubId, @Param("boardGameId") int boardGameId);
}
