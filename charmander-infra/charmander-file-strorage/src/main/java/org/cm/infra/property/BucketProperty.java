package org.cm.infra.property;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties("aws.s3.single")
@ConfigurationPropertiesBinding
public record BucketProperty(
        String resourceOriginal,
        String resourceSource
) {

}
