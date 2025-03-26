package org.cm.infra.property;

import org.cm.infra.storage.AwsContentsLocator;
import org.cm.infra.storage.ContentsLocator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties("aws.s3.full")
@ConfigurationPropertiesBinding
public record S3URLProperty(
        String resourceOriginal,
        String resourceSource,

        ContentsLocator ttsContentsLocator,
        ContentsLocator avatarContentsLocator

) {

    public S3URLProperty {
        ttsContentsLocator = new AwsContentsLocator(resourceOriginal, "/tts");
        avatarContentsLocator = new AwsContentsLocator(resourceOriginal, "/avatar");

    }
}
