package org.cm.api.file;

import org.cm.security.AuthInfo;
import org.cm.test.config.BaseServiceIntergrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Import({UploadedFileService.class})
@DisplayName("[통합 테스트] UploadedFileService")
class UploadedFileServiceTest extends BaseServiceIntergrationTest {
    @Autowired
    UploadedFileService uploadedFileService;

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
    }
}
