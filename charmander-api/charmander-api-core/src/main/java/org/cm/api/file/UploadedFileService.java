package org.cm.api.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.file.UploadedFile;
import org.cm.domain.file.UploadedFileRepository;
import org.cm.domain.file.UploadedFileStatus;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.infra.storage.*;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Service
@Validated
@RequiredArgsConstructor
public class UploadedFileService {
    private final PreSignedFileUploadService preSignedFileUploadService;

    private final UploadedFileRepository uploadedFileRepository;

    private ContentsLocator locator = new AwsContentsLocator("charmander-resource", "user-uploaded");

    public String getUserFileUploadUrl(AuthInfo authInfo, @NotEmpty String fileName) {
        var uploadId = RandomKeyGenerator.generateRandomKey();
        var command = new PreSignedURLGenerateCommand(fileName, uploadId, 1);
        var duration = Duration.ofMinutes(10);
        var file = UploadedFile.createUserUploadFile(uploadId, authInfo.getMemberId());
        uploadedFileRepository.save(file);
        return preSignedFileUploadService.generateURL(locator, command, duration);
    }

    public void completeUserFileUpload(AuthInfo authInfo, @Valid PreSignedURLCompleteCommand command) {
        var file = uploadedFileRepository.findByIdForUpdate(command.uploadId())
            .orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.FILE_NOT_FOUND));

        if (file.hasOwnership(authInfo.getMemberId())) {
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
}
