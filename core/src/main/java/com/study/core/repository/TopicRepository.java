package com.study.core.repository;

import com.study.core.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    boolean existsByNameAndIdNot(String name, UUID id);

    Page<Topic> findByNameContainingIgnoreCase(String queryText, Pageable pageable);

    boolean existsByName(String name);
}
