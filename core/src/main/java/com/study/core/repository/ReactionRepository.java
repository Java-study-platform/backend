package com.study.core.repository;

import com.study.core.models.Message;
import com.study.core.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> findByAuthorLoginAndMessage(String authorLogin, Message message);
}