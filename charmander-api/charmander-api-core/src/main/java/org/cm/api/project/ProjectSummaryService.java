package org.cm.api.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.cm.api.project.dto.ProjectGenerateTranscriptAIResult;
import org.cm.api.project.dto.ProjectGenerateTranscriptRequest;
import org.cm.security.AuthInfo;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProjectSummaryService {
    private final ChatModel chatModel;

    private final ObjectMapper objectMapper;

    public ProjectGenerateTranscriptAIResult generateTranscript(AuthInfo authInfo, ProjectGenerateTranscriptRequest request) {
        return ProjectGenerateTranscriptAIResult.beanOutputConverter.convert(
            chatModel.call(
                Prompts.systemPrompt.createMessage(Map.of("format", ProjectGenerateTranscriptAIResult.beanOutputConverter.getFormat())),
                Prompts.userPrompt.createMessage(Map.of("article", request.article()))
            )
        );
    }

    @UtilityClass
    static class Prompts {
        static final PromptTemplate systemPrompt;
        static final PromptTemplate userPrompt;

        static {
            systemPrompt = new SystemPromptTemplate("""
                너는 사용자가 뉴스 영상 대본을 제작하도록 돕는 AI 도우미야.
                기사 내용을 요약하고 영상을 위한 대본을 작성해줘.
                각 문장은 한 번에 말하기 쉬운 길이로 구성되어야 하고 최대 300글자를 넘으면 안 돼.
                각 문장은 경어체와 문어체를 사용하며, 아나운서의 대본 느낌으로 작성돼어야 해.
                {format}
                """.strip());

            userPrompt = new PromptTemplate("""
                대본 제작을 위한 기사 본문이야.
                ===
                ```text
                {article}
                ```
                """.strip());
        }
    }
}
