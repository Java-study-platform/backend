package com.study.user.Repository;

import com.study.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByKeyCloakId(String keyCloakId);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users ORDER BY experience DESC LIMIT 100", nativeQuery = true)
    List<User> findTop100Scorers();
}
