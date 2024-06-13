package com.study.core.enums;

import lombok.Getter;

@Getter
public enum ReactionType {
    LIKE("Лайк"),
    DISLIKE("Дизлайк");

    private final String prettyName;

    ReactionType(String prettyName) {
        this.prettyName = prettyName;
    }
}
