package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.bg.model.PubPicture;

@Repository
public interface PubPictureDAO extends JpaRepository<PubPicture, Integer>{
}
