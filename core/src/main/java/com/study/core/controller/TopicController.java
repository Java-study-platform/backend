package com.study.core.controller;



import com.study.core.dto.DefaultResponse;
import com.study.core.dto.Topic.CreateTopicModel;
import com.study.core.dto.Topic.EditTopicModel;
import com.study.core.dto.Topic.TopicDTO;
import com.study.core.mapper.TopicMapper;
import com.study.core.models.Topic;
import com.study.core.service.TopicService;
import com.study.core.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

import static com.study.common.Constants.Consts.TOPICS;

@RestController
@RequiredArgsConstructor
@Tag(name = "Topic")
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @PostMapping(TOPICS + "/{categoryId}")
    @Operation(summary = "Создать тему в конкретной категории")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<TopicDTO>> createTopic(@AuthenticationPrincipal Jwt user,
                                                                 @Validated @RequestBody CreateTopicModel createTopicModel,
                                                                 @PathVariable UUID categoryId) {
        Topic createdTopic = topicService.createTopic(user, categoryId, createTopicModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Тема '%s' успешно создана", createdTopic.getName()),
                topicMapper.toDTO(createdTopic)
        ));
    }

    @PutMapping(TOPICS + "/{id}")
    @Operation(summary = "Редактировать тему")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<TopicDTO>> editTopic(@AuthenticationPrincipal Principal user,
                                                               @Validated @RequestBody EditTopicModel editTopicModel,
                                                               @PathVariable UUID id) {
        Topic editedTopic = topicService.editTopic(editTopicModel, id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Тема '%s' успешно изменена", editedTopic.getName()),
                topicMapper.toDTO(editedTopic)
        ));
    }


    @DeleteMapping(TOPICS + "/{id}")
    @Operation(summary = "Удалить тему")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<?>> deleteTopic(@AuthenticationPrincipal Principal user,
                                                          @PathVariable UUID id) {
        topicService.deleteTopic(id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Тема успешно удалена",
                null
        ));
    }

    @GetMapping(TOPICS)
    @Operation(summary = "Получить список тем с пагинацией и запросом поиска")
    public ResponseEntity<DefaultResponse<Page<TopicDTO>>> getTopics(@RequestParam(required = false) String queryText,
                                                                     @ParameterObject @PageableDefault(sort="name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Список тем",
                topicService.getTopics(queryText, pageable).map(topicMapper::toDTO)
        ));
    }

}
