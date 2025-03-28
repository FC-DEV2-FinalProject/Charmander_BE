package org.cm.api.file;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class UploadedFileController {
    private final UploadedFileService uploadedFileService;

    @MemberOnly
    @GetMapping("/upload-url")
    public String getFileUploadUrl(
        @NotEmpty @RequestParam String fileName,
        @AuthUser AuthInfo authInfo
    ) {
        return uploadedFileService.getUserFileUploadUrl(authInfo, fileName);
    }
}
