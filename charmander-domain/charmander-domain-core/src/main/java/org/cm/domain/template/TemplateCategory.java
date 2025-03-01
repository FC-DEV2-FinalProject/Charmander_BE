package org.cm.domain.template;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TemplateCategory implements PersistenceEnum<Integer> {
    // @formatter:off
    Social        (10, "사회"),
    Politic       (11, "정치"),
    Economy       (12, "경제"),
    Culture       (20, "문화"),
    Entertainment (21, "연예"),
    Sport         (22, "스포츠"),
    ;
    // @formatter:on

    private final Integer value;
    private final String description;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<TemplateCategory, Integer> {
        public Converter() {
            super(TemplateCategory.class);
        }
    }
}
