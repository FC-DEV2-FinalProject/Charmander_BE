package org.cm.api.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.common.dto.PageResponse;
import org.cm.api.file.dto.GetUploadFileIdResponse;
import org.cm.api.file.dto.UploadUrlResponse;
import org.cm.api.file.dto.UploadedFileResponse;
import org.cm.common.domain.FileType;
import org.cm.infra.storage.PreSignedURLAbortCommand;
import org.cm.infra.storage.PreSignedURLCompleteCommand;
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
    @GetMapping("/my/{fullPath}")
    public UploadedFileResponse getMyUploadedFile(
        @PathVariable String fullPath,
        @AuthUser AuthInfo authInfo
    ) {
        var file = uploadedFileService.getUserUploadedFile(authInfo, fullPath);
        return UploadedFileResponse.from(file);
    }

    @MemberOnly
    @GetMapping("/upload-id")
    public GetUploadFileIdResponse getUploadFileId(
        @NotEmpty @RequestParam String fileName, @NotEmpty @RequestParam FileType fileType,
        @AuthUser AuthInfo authInfo
    ) {
        var file = uploadedFileService.getUploadId(authInfo, fileName, fileType);
        return GetUploadFileIdResponse.from(file);
    }

    @MemberOnly
    @GetMapping("/upload-url")
    public ListResponse<UploadUrlResponse> getUploadUrl(
        @NotEmpty @RequestParam String fileName,
        @NotEmpty @RequestParam String uploadId,
        @Min(1) @Max(100) @RequestParam Integer parts,
        @AuthUser AuthInfo authInfo
    ) {
        var urls = uploadedFileService.getUserFileUploadUrl(authInfo, fileName, uploadId, parts);
        return ListResponse.ofList(urls, UploadUrlResponse::from);
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
