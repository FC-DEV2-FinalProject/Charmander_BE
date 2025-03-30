package org.cm.api.project.dto;

import org.springframework.ai.converter.BeanOutputConverter;

import java.util.List;

public record ProjectGenerateTranscriptAIResult(
    List<Content> result
) {
    public static BeanOutputConverter<ProjectGenerateTranscriptAIResult> beanOutputConverter = new BeanOutputConverter<>(ProjectGenerateTranscriptAIResult.class);

    public record Content(
        String transcript
    ) {
    }
}
