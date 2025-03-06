package org.cm.config;

import org.cm.email.EmailClient;
import org.cm.email.ThymeleafEmailClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
public class EmailConfig {
    @Bean
    @ConditionalOnMissingBean(EmailClient.class)
    public EmailClient emailClient(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        return new ThymeleafEmailClient(javaMailSender, templateEngine);
    }
}
