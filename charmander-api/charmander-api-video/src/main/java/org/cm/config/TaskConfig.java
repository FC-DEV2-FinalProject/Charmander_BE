package org.cm.config;

import org.cm.infra.storage.ContentsLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

    @Bean("ttsContentsLocator")
    public ContentsLocator ttsContentsLocator() {
        return () -> "tts";
    }
}
