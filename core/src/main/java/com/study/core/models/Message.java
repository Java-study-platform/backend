package com.study.core.models;


import com.study.core.enums.MessageEventType;
import com.study.core.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String senderLogin;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Message parentMessage;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @OneToMany(mappedBy = "parentMessage", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Message> replies;

    @ElementCollection
    @CollectionTable(name = "message_reactions", joinColumns = @JoinColumn(name = "message_id"))
    @MapKeyColumn(name = "reaction_type")
    @Column(name = "reaction_count")
    @Enumerated(EnumType.STRING)
    private Map<ReactionType, Integer> reactions = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private MessageEventType eventType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt;
}
