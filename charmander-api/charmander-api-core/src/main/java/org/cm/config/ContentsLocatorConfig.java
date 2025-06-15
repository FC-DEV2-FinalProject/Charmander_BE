package org.cm.config;

import org.cm.infra.storage.ContentsLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContentsLocatorConfig {
    @Bean("userUploadedContentsLocator")
    public ContentsLocator userUploadedContentsLocator() {
        return () -> "user-uploaded";
    }
}
