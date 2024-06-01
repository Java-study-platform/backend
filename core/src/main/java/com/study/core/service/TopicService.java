package com.study.core.service;


import com.study.core.dto.Topic.CreateTopicModel;
import com.study.core.dto.Topic.EditTopicModel;
import com.study.core.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.UUID;

public interface TopicService {
    Topic createTopic(Principal user, UUID categoryId, CreateTopicModel createTopicModel);

    Topic editTopic(EditTopicModel editTopicModel, UUID id);

    void deleteTopic(UUID id);

    Page<Topic> getTopics(String queryText, Pageable pageable);
}
