package org.cm.api.file;

import org.cm.test.config.BaseWebMvcUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UploadedFileController.class)
@DisplayName("[단위 테스트] UploadedFileController")
class UploadedFileControllerTest extends BaseWebMvcUnitTest {
    @MockitoBean
    UploadedFileService uploadedFileService;

    @Test
    @DisplayName("GET /api/v1/files/upload-url - 1부터 단조 증가하는 parts를 반환해야 함")
    void file_upload_url_api_response_should_return_incremental_parts_value() throws Exception {
        Mockito.when(uploadedFileService.getUserFileUploadUrl(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(List.of("url1", "url2", "url3"));

        mvc.perform(get("/api/v1/files/upload-url")
                .queryParam("fileName", "test.txt")
                .queryParam("uploadId", "uploadId")
                .queryParam("parts", "3")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].part").value(1))
            .andExpect(jsonPath("$.data[0].url").exists())
            .andExpect(jsonPath("$.data[1].part").value(2))
            .andExpect(jsonPath("$.data[1].url").exists())
            .andExpect(jsonPath("$.data[2].part").value(3))
            .andExpect(jsonPath("$.data[2].url").exists());
    }
}
