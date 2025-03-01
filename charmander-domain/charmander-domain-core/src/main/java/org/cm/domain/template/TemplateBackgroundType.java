package org.cm.domain.template;

import jakarta.persistence.AttributeConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TemplateBackgroundType {
    // @formatter:off
    Image ("image"),
    ;
    // @formatter:on

    public final String value;

    public static TemplateBackgroundType of(String value) {
        for (TemplateBackgroundType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TemplateBackgroundType: " + value);
    }

    @jakarta.persistence.Converter
    public static class Converter implements AttributeConverter<TemplateBackgroundType, String> {
        @Override
        public String convertToDatabaseColumn(TemplateBackgroundType attr) {
            return attr.value;
        }

        @Override
        public TemplateBackgroundType convertToEntityAttribute(String s) {
            return TemplateBackgroundType.of(s);
        }
    }
}
