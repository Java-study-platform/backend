package com.study.core.service.impl;

import com.study.core.dto.Topic.CreateTopicModel;
import com.study.core.dto.Topic.EditTopicModel;
import com.study.core.exceptions.Category.CategoryNotFoundException;
import com.study.core.exceptions.Topic.TopicAlreadyExistsException;
import com.study.core.exceptions.Topic.TopicNotFoundException;
import com.study.core.models.Category;
import com.study.core.models.Chat;
import com.study.core.models.Topic;
import com.study.core.repository.CategoryRepository;
import com.study.core.repository.TopicRepository;
import com.study.core.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;


    @Override
    @Transactional
    public Topic createTopic(Jwt user, UUID categoryId, CreateTopicModel createTopicModel) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        if (topicRepository.existsByName(createTopicModel.getName())) {
            throw new TopicAlreadyExistsException(createTopicModel.getName());
        }

        Topic topic = new Topic();
        topic.setName(createTopicModel.getName());
        topic.setMaterial(createTopicModel.getMaterial());
        topic.setCategory(category);
        topic.setAuthorLogin(user.getClaim("preferred_username"));

        Chat chat = new Chat();
        chat.setTopic(topic);

        return topicRepository.save(topic);
    }

    @Override
    @Transactional
    public Topic editTopic(EditTopicModel editTopicModel, UUID id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(id));

        if (topicRepository.existsByNameAndIdNot(editTopicModel.getName(), id)) {
            throw new TopicAlreadyExistsException(editTopicModel.getName());
        }

        topic.setName(editTopicModel.getName());
        topic.setMaterial(editTopicModel.getMaterial());

        return topicRepository.save(topic);
    }

    @Override
    @Transactional
    public void deleteTopic(UUID id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(id));

        topicRepository.delete(topic);
    }

    @Override
    public Page<Topic> getTopics(String queryText, Pageable pageable) {
        if (queryText == null || queryText.isBlank()) {
            return topicRepository.findAll(pageable);
        }
        else {
            return topicRepository.findByNameContainingIgnoreCase(queryText, pageable);
        }
    }

    @Override
    public Topic getTopic(UUID id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException(id));
    }
}
