package org.cm.api.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.cm.api.template.dto.TemplateCategoryDto;
import org.cm.api.template.dto.TemplateSuggestRequest;
import org.cm.api.template.dto.TemplateSuggestAIResult;
import org.cm.domain.template.TemplateCategory;
import org.cm.domain.template.TemplateRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateSuggestService {
    private final TemplateRepository templateRepository;
    private final ChatModel chatModel;

    private final ObjectMapper objectMapper;

    public TemplateSuggestAIResult suggestCategory(TemplateSuggestRequest request) throws Exception {
        return TemplateSuggestAIResult.beanOutputConverter.convert(
            chatModel
                .call(
                    // TODO: items_json대신 Tool Calling으로 리픽토링 하기. 대체 왜 호출 안되는 것??
                    Prompts.systemPrompt.createMessage(Map.of("items_json", objectMapper.writeValueAsString(TemplateCategoryTools.getTemplateCategories()))),
                    Prompts.userPrompt.createMessage(Map.of("article", request.article()))
                )
        );
    }


    @UtilityClass
    public class TemplateCategoryTools {
        // TODO: items_json 대신 Tool Calling으로 리팩토링.
        // @Tool(description = "Get the list of categories of templates")
        public List<TemplateCategoryDto> getTemplateCategories() {
            System.out.println("getTemplateCategories called");
            return Arrays
                .stream(TemplateCategory.values())
                .map(TemplateCategoryDto::from)
                .toList();
        }
    }

    @UtilityClass
    static class Prompts {
        static final PromptTemplate systemPrompt;
        static final PromptTemplate userPrompt;

        static {
            systemPrompt = new SystemPromptTemplate("""
                You are a helpful AI assistant that helps users to find the right category for their article content.
                You should get list of categories from external source.
                here is list of categories.
                ```json
                {items_json}
                ```
                you should answer with following format:
                ```json
                \\{
                  "result": \\{
                    "id": <category_id>
                    "name": "<category_name>"
                  \\}
                \\}
                ```
                """);

            userPrompt = new PromptTemplate("""
                Please provide the appropriate category for the following article content:
                ```text
                {article}
                ```
                """);
        }
    }
}
