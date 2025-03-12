package org.cm.api.template.dto;

import org.cm.domain.template.TemplateCategory;

public record TemplateCategoryDto(
    Integer id,
    String name
) {
    public static TemplateCategoryDto from(TemplateCategory category) {
        return new TemplateCategoryDto(category.getValue(), category.getDescription());
    }
}
