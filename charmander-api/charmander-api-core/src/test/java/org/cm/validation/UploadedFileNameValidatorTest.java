package org.cm.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UploadedFileNameValidatorTest {
    @ParameterizedTest
    @DisplayName("파일 이름 유효성 테스트")
    @MethodSource("fileNameTestCases")
    void validFileNames(String fileName) {
        FileNameValidator validator = new FileNameValidator();
        assertTrue(validator.isValid(fileName, null));
    }

    @ParameterizedTest
    @DisplayName("특정 확장자만 허용하는 경우 테스트")
    @MethodSource("fileExtensionTestCases")
    void fileExtensionValidation(String fileName, String[] allowedExtensions, boolean expectedResult) {
        FileNameValidator validator = new FileNameValidator();
        assertEquals(expectedResult, validator.isValid(fileName, null));
    }

    private static Stream<Arguments> fileNameTestCases() {
        return Stream.of(
            // 유효한 파일 이름
            Arguments.of("normal_file.txt", true),
            Arguments.of("document-2023.pdf", true),
            Arguments.of("image_001.png", true),
            Arguments.of("report.2023.05.xlsx", true),
            Arguments.of("한글파일이름.jpg", true),
            Arguments.of("file_with_space.doc", true),
            Arguments.of("report_v1.0.pdf", true),

            // 유효하지 않은 파일 이름
            Arguments.of("file*.txt", false),
            Arguments.of("invalid?.pdf", false),
            Arguments.of("file\\with\\backslash.jpg", false),
            Arguments.of("file:with:colon.png", false),
            Arguments.of("file<with>brackets.doc", false),
            Arguments.of("file|with|pipe.xlsx", false),
            Arguments.of("", false),
            Arguments.of(".hiddenfileonly", false),
            Arguments.of("파일이름?.pdf", false),
            Arguments.of("file/with/slash.txt", false)
        );
    }

    private static Stream<Arguments> fileExtensionTestCases() {
        return Stream.of(
            // 허용된 확장자
            Arguments.of("document.pdf", new String[]{"pdf", "docx"}, true),
            Arguments.of("image.jpg", new String[]{"jpg", "png", "gif"}, true),
            Arguments.of("spreadsheet.xlsx", new String[]{"xlsx", "csv"}, true),

            // 허용되지 않은 확장자
            Arguments.of("document.txt", new String[]{"pdf", "docx"}, false),
            Arguments.of("image.bmp", new String[]{"jpg", "png", "gif"}, false),
            Arguments.of("script.js", new String[]{"html", "css"}, false)
        );
    }
}
