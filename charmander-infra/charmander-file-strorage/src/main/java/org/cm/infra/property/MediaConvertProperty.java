package org.cm.infra.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
@ConfigurationProperties("aws.media-convert")
public record MediaConvertProperty(
        String userArn,
        Queue queue
) {

    public record Queue(
            String voiceCombine,
            String sceneCombine
    ) {
    }
}
