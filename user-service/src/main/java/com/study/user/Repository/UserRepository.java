package com.study.user.Repository;

import com.study.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByKeyCloakId(String keyCloakId);
    Optional<User> findByUsername(String username);
}
