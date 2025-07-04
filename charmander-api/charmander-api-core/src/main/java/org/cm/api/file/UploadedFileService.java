package org.cm.api.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.cm.api.file.dto.GetUploadFileIdCommand;
import org.cm.common.domain.FileType;
import org.cm.domain.file.UploadedFile;
import org.cm.domain.file.UploadedFileRepository;
import org.cm.domain.file.UploadedFileStatus;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.infra.storage.*;
import org.cm.security.AuthInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Validated
public class UploadedFileService {
    private final ContentsLocator locator;
    private final PreSignedFileUploadService preSignedFileUploadService;
    private final UploadedFileRepository uploadedFileRepository;

    public UploadedFileService(
        @Qualifier("userUploadedContentsLocator") ContentsLocator locator,
        PreSignedFileUploadService preSignedFileUploadService,
        UploadedFileRepository uploadedFileRepository
    ) {
        this.locator = locator;
        this.preSignedFileUploadService = preSignedFileUploadService;
        this.uploadedFileRepository = uploadedFileRepository;
    }

    public Page<UploadedFile> getUserUploadedFiles(AuthInfo authInfo, Pageable pageable) {
        return uploadedFileRepository.findByOwnerIdAndStatus(authInfo.getMemberId(), UploadedFileStatus.COMPLETED, pageable);
    }


    public UploadedFile getUserUploadedFile(AuthInfo authInfo, String fileId) {
        return uploadedFileRepository.findByFullPathAndOwnerIdAndStatus(fileId, authInfo.getMemberId(), UploadedFileStatus.COMPLETED)
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.FILE_NOT_FOUND));
    }

    public UploadedFile getUploadId(AuthInfo authInfo, @NotEmpty String fileName, @NotEmpty FileType fileType) {
        var command = new GetUploadFileIdCommand(fileName, fileType);
        var identifier = preSignedFileUploadService.sign(locator, command.fileType());
        var file = UploadedFile.createUserUploadFile(identifier.uploadId(), identifier.fullPath(), authInfo.getMemberId());
        return uploadedFileRepository.save(file);
    }

    public List<String> getUserFileUploadUrl(
        AuthInfo authInfo,
        @NotEmpty String fileName,
        @NotEmpty String uploadId,
        @Min(1) @Max(100) Integer parts
    ) {
        var duration = Duration.ofMinutes(10);
        return IntStream.rangeClosed(1, parts)
            .mapToObj(partNumber -> new PreSignedURLGenerateCommand(fileName, uploadId, partNumber))
            .map(command -> preSignedFileUploadService.generateURL(locator, command, duration))
            .toList();
    }

    public void completeUserFileUpload(AuthInfo authInfo, @Valid PreSignedURLCompleteCommand command) {
        var file = uploadedFileRepository.findByUploadIdForUpdate(command.uploadId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.FILE_NOT_FOUND));

        if (!file.hasOwnership(authInfo.getMemberId())) {
            throw new CoreApiException(CoreApiExceptionCode.FILE_FORBIDDEN);
        }
        if (file.getStatus() != UploadedFileStatus.UPLOADING) {
            throw new CoreApiException(CoreApiExceptionCode.FILE_INVALID_STATUS);
        }

        file.startCompletion();
        uploadedFileRepository.save(file);
        preSignedFileUploadService.complete(locator, command);
        file.finishCompletion();
        uploadedFileRepository.save(file);
    }

    public void abortUserFileUpload(AuthInfo authInfo, @NotEmpty PreSignedURLAbortCommand command) {
        var file = uploadedFileRepository.findByUploadIdForUpdate(command.uploadId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.FILE_NOT_FOUND));

        if (file.getStatus() != UploadedFileStatus.UPLOADING) {
            throw new CoreApiException(CoreApiExceptionCode.FILE_INVALID_STATUS);
        }

        file.startAbort();
        uploadedFileRepository.save(file);
        preSignedFileUploadService.abort(locator, command);
        file.finishAbort();
        uploadedFileRepository.save(file);
    }
}
