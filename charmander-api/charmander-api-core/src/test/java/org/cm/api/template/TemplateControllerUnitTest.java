package org.cm.api.template;

import org.cm.jwt.JwtConfig;
import org.cm.jwt.JwtService;
import org.cm.test.fixture.TemplateFixture;
import org.cm.web.resolver.AuthUserResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
    AuthUserResolver.class,
    JwtConfig.class,
    JwtService.class
})
@WebMvcTest(TemplateController.class)
@DisplayName("[단위 테스트] TemplateController")
class TemplateControllerUnitTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    TemplateService templateService;

    @MockitoBean
    TemplateSuggestService templateSuggestService;

    @Nested
    @DisplayName("[JsonPath]")
    class JsonPathTest {
        @Test
        @DisplayName("Template 카테고리 Id를 정수로 반환해야 함")
        void getCategories() throws Exception {
            // given
            var templates = List.of(
                TemplateFixture.create()
            );
            Mockito.when(templateService.findAll()).thenReturn(templates);

            // when
            mvc.perform(get("/api/v1/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].data.categoryId").isNumber());

            // then
        }
    }
}
