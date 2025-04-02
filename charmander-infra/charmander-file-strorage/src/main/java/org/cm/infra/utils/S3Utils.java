package org.cm.infra.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@RequiredArgsConstructor
public class S3Utils {
    private final S3Client s3Client;

    @Cacheable(value = "s3Utils:getS3BucketRegion", key = "#bucketName")
    public String getBucketRegion(String bucketName) {
        return s3Client.getBucketLocation(b -> b.bucket(bucketName)).locationConstraintAsString();
    }
}
