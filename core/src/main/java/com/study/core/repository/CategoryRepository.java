package com.study.core.repository;

import com.study.core.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);

    Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String queryNameText, String queryDescriptionText, Pageable pageable);
}
