package com.rmeunier.servicepoller.repo;

import com.rmeunier.servicepoller.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @Query("SELECT u FROM User u WHERE u.username IN :username")
    User findByUsername(@Param("username") String username);
}
