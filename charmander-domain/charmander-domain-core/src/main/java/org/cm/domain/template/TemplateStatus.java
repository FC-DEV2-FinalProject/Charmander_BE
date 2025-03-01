package org.cm.domain.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor
public enum TemplateStatus implements PersistenceEnum<String> {
    // @formatter:off
    DRAFT    ("draft"),
    PUBLIC   ("public"),
    DISABLED ("disable"),
    ;
    // @formatter:on

    private final String value;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<TemplateStatus, String> {
        public Converter() {
            super(TemplateStatus.class);
        }
    }
}
