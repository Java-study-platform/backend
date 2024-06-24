package com.study.core.repository;

import com.study.core.models.Chat;
import com.study.core.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByChatAndParentMessageIsNull(Chat chat);
}