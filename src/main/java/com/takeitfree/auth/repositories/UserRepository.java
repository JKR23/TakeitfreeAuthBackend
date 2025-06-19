package com.takeitfree.auth.repositories;

import com.takeitfree.auth.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.profile p " +
            "WHERE LOWER(p.username) LIKE LOWER(concat('%', :username, '%'))")
    List<User> findByUsernameContainingIgnoreCase(@Param("username") String username);
}
