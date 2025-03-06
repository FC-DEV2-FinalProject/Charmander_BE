package org.cm.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;

@Slf4j
@Configuration
public class JwtConfig {
    @Value("${jwt.key.private:classpath:keys/id_rsa.pem}")
    private Resource privateKeyResource;

    @Value("${jwt.key.public:classpath:keys/id_rsa.pub}")
    private Resource publicKeyResource;

    @Bean
    public Algorithm algorithm() {
        try {
            RSAPrivateKey rsaPrivateKey = RsaKeyConverters.pkcs8().convert(privateKeyResource.getInputStream());
            RSAPublicKey rsaPublicKey = RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
            return Algorithm.RSA512(rsaPublicKey, rsaPrivateKey);
        } catch (Exception e) {
            log.error("Failed to load RSA key pair", e);
            throw new RuntimeException(e);
        }
    }
}
