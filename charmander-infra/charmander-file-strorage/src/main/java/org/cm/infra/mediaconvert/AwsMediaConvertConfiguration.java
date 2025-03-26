package org.cm.infra.mediaconvert;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;

@Configuration
public class AwsMediaConvertConfiguration {

    @Bean
    public MediaConvertClient mediaConvertConfig() {
        return MediaConvertClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

}
