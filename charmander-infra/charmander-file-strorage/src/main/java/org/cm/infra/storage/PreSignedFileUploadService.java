package org.cm.infra.storage;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.cm.common.domain.FileType;
import org.cm.common.utils.RandomKeyGenerator;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

@Service
@RequiredArgsConstructor
public class PreSignedFileUploadService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AwsProperty awsProperty;

    public PreSignedURLIdentifier sign(ContentsLocator contentsLocator, FileType fileType) {
        var randomFileName = RandomKeyGenerator.generateRandomKey();
        var fullPath = contentsLocator.combineLocation(randomFileName);
        
        var createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(awsProperty.bucket())
                .key(fullPath)
                .contentType(fileType.mimeType)
                .build();

        var multipartUpload = s3Client.createMultipartUpload(createMultipartUploadRequest);

        return new PreSignedURLIdentifier(
                fullPath,
                randomFileName,
                multipartUpload.uploadId()
        );
    }

    public String generateURL(
            ContentsLocator contentsLocator,
            PreSignedURLGenerateCommand command,
            Duration duration
    ) {
        String path = contentsLocator.combineLocation(command.fileName());

        var uploadRequest = UploadPartPresignRequest.builder()
                .signatureDuration(duration)
                .uploadPartRequest(uploadPartReq -> uploadPartReq
                        .bucket(awsProperty.bucket())
                        .key(path)
                        .uploadId(command.uploadId())
                        .partNumber(command.partNumber())
                )
                .build();

        return s3Presigner.presignUploadPart(uploadRequest)
                .url()
                .toString();
    }


    public void complete(ContentsLocator contentsLocator, PreSignedURLCompleteCommand command) {
        String path = contentsLocator.combineLocation(command.fileName());

        var completeMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
                .bucket(awsProperty.bucket())
                .key(path)
                .uploadId(command.uploadId())
                .multipartUpload(builder -> builder.parts(command.toParts()))
                .build();

        s3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }

    public void abort(ContentsLocator contentsLocator, PreSignedURLAbortCommand command) {
        String path = contentsLocator.combineLocation(command.fileName());

        var abortMultipartUploadRequest = AbortMultipartUploadRequest.builder()
                .bucket(awsProperty.bucket())
                .key(path)
                .uploadId(command.uploadId())
                .build();

        s3Client.abortMultipartUpload(abortMultipartUploadRequest);
    }

}
