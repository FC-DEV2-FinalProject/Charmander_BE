package org.cm.api.template.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.converter.BeanOutputConverter;

public record TemplateSuggestAIResult(
    @JsonProperty(required = true)
    TemplateCategoryDto result
) {
    public static BeanOutputConverter<TemplateSuggestAIResult> beanOutputConverter = new BeanOutputConverter<>(TemplateSuggestAIResult.class);
}
