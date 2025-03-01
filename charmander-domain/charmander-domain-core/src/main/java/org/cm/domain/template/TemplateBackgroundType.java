package org.cm.domain.template;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TemplateBackgroundType implements PersistenceEnum<String> {
    // @formatter:off
    Image ("image"),
    ;
    // @formatter:on

    private final String value;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<TemplateBackgroundType, String> {
        public Converter() {
            super(TemplateBackgroundType.class);
        }
    }
}
