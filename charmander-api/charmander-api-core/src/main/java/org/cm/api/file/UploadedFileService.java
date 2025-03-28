package org.cm.api.file;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.cm.common.utils.RandomKeyGenerator;
import org.cm.domain.file.UploadedFile;
import org.cm.domain.file.UploadedFileRepository;
import org.cm.infra.storage.AwsContentsLocator;
import org.cm.infra.storage.ContentsLocator;
import org.cm.infra.storage.PreSignedFileUploadService;
import org.cm.infra.storage.PreSignedURLGenerateCommand;
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
}
