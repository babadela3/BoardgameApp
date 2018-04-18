package ro.bg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.bg.model.User;

@Repository
public interface UserDAO extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM USERS WHERE EMAIL = :email", nativeQuery = true)
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM USERS WHERE EMAIL = :email AND PASSWORD = :password ;", nativeQuery = true)
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}