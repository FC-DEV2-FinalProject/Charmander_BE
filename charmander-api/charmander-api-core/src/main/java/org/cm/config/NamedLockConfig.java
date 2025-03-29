package org.cm.config;

import org.cm.domain.common.NamedLockRepository;
import org.cm.repository.MySQLNamedNamedLockProvider;
import org.cm.repository.NamedLockProvider;
import org.cm.repository.NamedLockTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NamedLockConfig {
    @Bean
    @Qualifier("mySqlNamedLockProvider")
    public NamedLockProvider mySqlNamedLockProvider(NamedLockRepository lockRepository) {
        return new MySQLNamedNamedLockProvider(lockRepository);
    }

    @Bean
    public NamedLockTemplate namedLockTemplate(NamedLockRepository lockRepository) {
        return NamedLockTemplate.builder()
            .setLockProvider(mySqlNamedLockProvider(lockRepository))
            .build();
    }
}
