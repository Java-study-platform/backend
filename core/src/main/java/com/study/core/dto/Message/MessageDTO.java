package com.study.core.dto.Message;

import com.study.core.enums.MessageEventType;
import com.study.core.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private UUID id;
    private String content;
    private String senderLogin;
    private UUID parentMessageId;
    private UUID topicId;
    private List<MessageDTO> replies;
    private Map<ReactionType, Integer> reactions;
    private Set<ReactionType> currentUserReactions;
    private MessageEventType eventType;
    private LocalDateTime sentAt;
}