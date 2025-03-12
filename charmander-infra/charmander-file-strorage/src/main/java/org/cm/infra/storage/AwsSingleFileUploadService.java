package org.cm.infra.storage;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.common.domain.File;
import org.cm.common.domain.FileType;
import org.cm.common.utils.RandomKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsSingleFileUploadService implements SingleFileUploadService {

    private final S3Client s3Client;
    private final AwsProperty awsProperty;

    public File upload(MultipartFile multipartFile, ContentsLocator locator) {
        var file = toFile(multipartFile);

        sendRequest(multipartFile, file, locator);

        return file;
    }

    private void sendRequest(MultipartFile multipartFile, File file, ContentsLocator locator) {
        var putRequest = PutObjectRequest.builder()
                .key(locator.combineLocation(file.identifier()))
                .bucket(awsProperty.bucket())
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .build();

        s3Client.putObject(putRequest, this.parseRequestBody(multipartFile));
    }

    private File toFile(MultipartFile multipartFile) {
        var fileName = multipartFile.getOriginalFilename();

        if(fileName == null){
            throw new IllegalArgumentException();
        }

        var extension = fileName.substring(fileName.lastIndexOf('.') + 1);

        return new File(
                RandomKeyGenerator.generateRandomKey(),
                FileType.from(extension)
        );
    }

    private RequestBody parseRequestBody(MultipartFile multipartFile) {
        try {
            return RequestBody.fromBytes(multipartFile.getBytes());
        } catch (IOException e) {
            // TODO add AWS module exception
            throw new RuntimeException();
        }
    }
}
