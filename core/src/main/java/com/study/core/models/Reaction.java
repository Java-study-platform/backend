package com.study.core.models;


import com.study.core.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "author_login", nullable = false)
    private String authorLogin;

    @ElementCollection(targetClass = ReactionType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "reaction_types", joinColumns = @JoinColumn(name = "reaction_id"))
    @Column(name = "reaction_type")
    private Set<ReactionType> reactions = new HashSet<>();
}
