package org.cm.infra.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties("aws")
@ConfigurationPropertiesBinding
public record AwsProperty(
        String accessKey,
        String secretKey,
        String bucket
) {

}

