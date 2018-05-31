package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.Pub;


@Repository
public interface PubDAO extends JpaRepository<Pub,Integer> {

    @Query(value = "SELECT * FROM PUBS WHERE EMAIL = :email", nativeQuery = true)
    Pub findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM PUBS WHERE EMAIL = :email AND PASSWORD = :password ;", nativeQuery = true)
    Pub findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query(value = "SELECT * FROM PUBS WHERE ADDRESS = :address", nativeQuery = true)
    Pub findByAddress(@Param("address") String address);

}
