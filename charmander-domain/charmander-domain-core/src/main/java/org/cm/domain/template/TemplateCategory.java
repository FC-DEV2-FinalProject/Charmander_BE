package org.cm.domain.template;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TemplateCategory {
    // @formatter:off
    Social        (10, "사회"),
    Politic       (11, "정치"),
    Economy       (12, "경제"),
    Culture       (20, "문화"),
    Entertainment (21, "연예"),
    Sport         (22, "스포츠"),
    ;
    // @formatter:on

    public final int value;
    public final String description;

    public static TemplateCategory of(int value) {
        for (TemplateCategory category : values()) {
            if (category.value == value) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid TemplateCategory: " + value);
    }
}
