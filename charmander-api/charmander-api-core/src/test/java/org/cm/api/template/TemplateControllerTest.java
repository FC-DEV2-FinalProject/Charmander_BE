package org.cm.api.template;

import org.cm.test.config.BaseWebMvcUnitTest;
import org.cm.test.fixture.TemplateFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TemplateController.class)
@DisplayName("[단위 테스트] TemplateController")
class TemplateControllerTest extends BaseWebMvcUnitTest {
    @MockitoBean
    TemplateService templateService;

    @MockitoBean
    TemplateSuggestService templateSuggestService;

    @Nested
    @DisplayName("[JsonPath]")
    class JsonPathTest {
        @Test
        @DisplayName("TemplateResponse")
        void template_avatar_sisze_should_exists() throws Exception {
            // given
            var template = TemplateFixture.create();
            Mockito.when(templateService.getTemplateById(Mockito.any()))
                .thenReturn(template);

            // when
            var res = mvc.perform(get("/api/v1/templates/1"))
                .andExpectAll(
                    jsonPath("$.data.avatar.size.width").exists(),
                    jsonPath("$.data.avatar.size.height").exists()
                );

            // then
        }
    }
}
