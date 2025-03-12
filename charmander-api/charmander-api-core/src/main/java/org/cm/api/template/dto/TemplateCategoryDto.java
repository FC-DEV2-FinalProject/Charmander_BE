package org.cm.api.template.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cm.domain.template.TemplateCategory;

public record TemplateCategoryDto(
    @JsonProperty(required = true)
    Integer id,

    @JsonProperty(required = true)
    String name
) {
    public static TemplateCategoryDto from(TemplateCategory category) {
        return new TemplateCategoryDto(category.getValue(), category.getDescription());
    }
}
