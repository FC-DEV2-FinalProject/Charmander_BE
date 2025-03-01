package org.cm.domain.template;

import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TemplateStatus {
    // @formatter:off
    DRAFT    ("draft"),
    PUBLIC   ("public"),
    DISABLED ("disable"),
    ;
    // @formatter:on

    public final String value;

    public static TemplateStatus of(String value) {
        for (TemplateStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TemplateStatus: " + value);
    }

    @jakarta.persistence.Converter
    public static class Converter implements AttributeConverter<TemplateStatus, String> {
        @Override
        public String convertToDatabaseColumn(TemplateStatus templateStatus) {
            return templateStatus.value;
        }

        @Override
        public TemplateStatus convertToEntityAttribute(String s) {
            return TemplateStatus.of(s);
        }
    }
}
