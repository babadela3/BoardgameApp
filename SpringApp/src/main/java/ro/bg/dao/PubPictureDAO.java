package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Pub;
import ro.bg.model.PubPicture;

import java.util.List;

@Repository
public interface PubPictureDAO extends JpaRepository<PubPicture, Integer>{

    @Query(value = "SELECT * FROM PUB_PICTURES WHERE FK_PUB_ID = :id", nativeQuery = true)
    List<PubPicture> findByPubId(@Param("id") int id);
}
