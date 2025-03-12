package org.cm.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
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

    @Value("${jwt.key.generate:true}")
    private Boolean generate = true;

    @Bean
    public Algorithm algorithm() {
        try {
            RSAPrivateKey rsaPrivateKey = RsaKeyConverters.pkcs8().convert(privateKeyResource.getInputStream());
            RSAPublicKey rsaPublicKey = RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
            return Algorithm.RSA512(rsaPublicKey, rsaPrivateKey);
        } catch (Exception e) {
            log.warn("Failed to load RSA key pair");
            if (!generate) {
                throw new RuntimeException("Failed to load RSA key pair", e);
            }
            try {
                log.warn("Generating RSA key pair. This is not recommended for production use.");
                KeyPair keyPair = generateRsaKey();
                return Algorithm.RSA512((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to generate RSA key pair", ex);
            }
        }
    }

    static KeyPair generateRsaKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
