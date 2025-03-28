package org.cm.api.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.PageResponse;
import org.cm.api.file.dto.GetUploadFileIdCommand;
import org.cm.api.file.dto.UploadedFileResponse;
import org.cm.infra.storage.PreSignedURLAbortCommand;
import org.cm.infra.storage.PreSignedURLCompleteCommand;
import org.cm.infra.storage.PreSignedURLIdentifier;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class UploadedFileController {
    private final UploadedFileService uploadedFileService;

    @MemberOnly
    @GetMapping("/my")
    public PageResponse<UploadedFileResponse> getMyUploadedFiles(
        Pageable pageable,
        @AuthUser AuthInfo authInfo
    ) {
        var page = uploadedFileService.getUserUploadedFiles(authInfo, pageable);
        return PageResponse.of(page, UploadedFileResponse::from);
    }

    @MemberOnly
    @GetMapping("/my/{fileId}")
    public UploadedFileResponse getMyUploadedFile(
        @PathVariable String fileId,
        @AuthUser AuthInfo authInfo
    ) {
        var file = uploadedFileService.getUserUploadedFile(authInfo, fileId);
        return UploadedFileResponse.from(file);
    }

    @MemberOnly
    @PostMapping("/upload-id")
    public PreSignedURLIdentifier getUploadFileId(
        @RequestBody GetUploadFileIdCommand command,
        @AuthUser AuthInfo authInfo
    ) {
        return uploadedFileService.getUploadId(authInfo, command);
    }

    @MemberOnly
    @PostMapping("/complete")
    public void completeFileUpload(
        @Valid @RequestBody PreSignedURLCompleteCommand command,
        @AuthUser AuthInfo authInfo
    ) {
        uploadedFileService.completeUserFileUpload(authInfo, command);
    }

    @MemberOnly
    @PostMapping("/abort")
    public void abortFileUpload(
        @NotEmpty @RequestBody PreSignedURLAbortCommand command,
        @AuthUser AuthInfo authInfo
    ) {
        uploadedFileService.abortUserFileUpload(authInfo, command);
    }
}
