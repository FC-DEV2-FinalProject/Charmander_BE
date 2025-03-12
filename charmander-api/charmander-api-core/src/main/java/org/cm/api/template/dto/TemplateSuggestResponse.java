package org.cm.api.template.dto;

import org.cm.domain.template.Template;

import java.util.List;

public record TemplateSuggestResponse(
    List<TemplateResponse> items,
    SuggestionInfo _info
) {

    public static TemplateSuggestResponse empty() {
        return new TemplateSuggestResponse(List.of(), null);
    }

    public static TemplateSuggestResponse from(TemplateSuggestAIResult response, List<Template> items) {
        return new TemplateSuggestResponse(
            items.stream().map(TemplateResponse::from).toList(),
            SuggestionInfo.from(response.result())
        );
    }

    public record SuggestionInfo(
        TemplateCategoryDto category
    ) {
        public static SuggestionInfo from(TemplateCategoryDto category) {
            return new SuggestionInfo(category);
        }
    }
}
