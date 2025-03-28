package org.cm.api.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.cm.infra.storage.PreSignedURLCompleteCommand;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @MemberOnly
    @PostMapping("/complete")
    public void completeFileUpload(
        @Valid @RequestBody PreSignedURLCompleteCommand command,
        @AuthUser AuthInfo authInfo
    ) {
        uploadedFileService.completeUserFileUpload(authInfo, command);
    }
}
