package org.cm.api.file;

import org.assertj.core.api.Assertions;
import org.cm.config.ContentsLocatorConfig;
import org.cm.exception.CoreApiException;
import org.cm.infra.storage.PreSignedFileUploadService;
import org.cm.infra.storage.PreSignedURLGenerateCommand;
import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceIntergrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Import({
    UploadedFileService.class,
    ContentsLocatorConfig.class
})
@DisplayName("[통합 테스트] UploadedFileService")
class UploadedFileServiceIntergrationTest extends BaseServiceIntergrationTest {
    @Autowired
    UploadedFileService uploadedFileService;

    @MockitoBean
    PreSignedFileUploadService preSignedFileUploadService;

    @Nested
    @DisplayName("[조회]")
    class ReadTest {
        @Test
        @DisplayName("001. 사용자가 업로드한 파일 목록을 조회할 수 있다.")
        void 사용자가_업로드한_파일_목록을_조회할_수_있다() {
            // stub
            var member = createMember();
            var nFiles = 5;
            IntStream.range(0, nFiles)
                .forEach(i -> createUploadedFile(member));

            // given
            var authInfo = new AuthInfo(member.getId());
            var pageable = PageRequest.of(0, 10);

            // when
            var files = uploadedFileService.getUserUploadedFiles(authInfo, pageable);

            // then
            assertEquals(nFiles, files.getTotalElements());
        }

        @Test
        @DisplayName("002. 사용자가 업로드한 파일을 조회할 수 있다.")
        void 사용자가_업로드한_파일을_조회할_수_있다() {
            // stub
            var member = createMember();
            var file = createUploadedFile(member);

            // given
            var authInfo = new AuthInfo(member.getId());

            // when
            var foundFile = uploadedFileService.getUserUploadedFile(authInfo, file.getFullPath());

            // then
            assertEquals(file.getFullPath(), foundFile.getFullPath());
        }

        @Test
        @DisplayName("003. 다른 사용자가 업로드한 파일을 조회할 수 없다.")
        void 다른_사용자가_업로드한_파일을_조회할_수_없다() {
            // stub
            var member = createMember();
            var file = createUploadedFile(member);
            var otherMember = createMember();

            // given
            var authInfo = new AuthInfo(otherMember.getId());

            // when
            assertThrows(CoreApiException.class, () -> uploadedFileService.getUserUploadedFile(authInfo, file.getFullPath()));
        }
    }

    @Nested
    @DisplayName("[기능]")
    class FeatureTest {
        @Test
        @DisplayName("001. getUserFileUploadUrl - 1부터 단조 증가하는 parts를 사용해야 함")
        void getUserFileUploadUrl_should_generate_incremental_parts_value() {
            // given
            var authInfo = new AuthInfo(1L);
            var fileName = "test.txt";
            var uploadId = "uploadId";
            var parts = 10;

            // when
            var urls = uploadedFileService.getUserFileUploadUrl(authInfo, fileName, uploadId, parts);

            // then
            var captor = ArgumentCaptor.forClass(PreSignedURLGenerateCommand.class);
            Mockito.verify(preSignedFileUploadService, Mockito.times(10))
                .generateURL(Mockito.any(), captor.capture(), Mockito.any());
            var capturedCommands = captor.getAllValues();

            assertEquals(parts, capturedCommands.size());
            for (int i = 1; i <= parts; i++) {
                assertEquals(i, capturedCommands.get(i - 1).partNumber());
            }
        }
    }
}
