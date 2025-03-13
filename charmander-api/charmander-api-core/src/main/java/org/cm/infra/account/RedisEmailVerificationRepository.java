package org.cm.infra.account;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.cm.domain.account.EmailVerification;
import org.cm.domain.account.EmailVerificationRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisEmailVerificationRepository implements EmailVerificationRepository {
    private static final String KEY_PREFIX = "email-verification:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public @Nullable EmailVerification get(EmailVerification.IdType id) {
        final var key = getRedisKey(id);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
            .map(String.class::cast)
            .map(code -> {
                var ttl = Optional.of(redisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
                var exp = LocalDateTime.now().plusSeconds(ttl);
                return new EmailVerification(id, code, exp);
            })
            .orElse(null);
    }

    @Override
    public void save(EmailVerification verification) {
        final var key = getRedisKey(verification.id());
        var exp = EmailVerification.Constants.EMAIL_VERIFICATION_EXPIRATION_TIME;
        redisTemplate
            .opsForValue()
            .set(key, verification.code(), exp);
    }

    @Override
    public void delete(EmailVerification verification) {
        final var key = getRedisKey(verification.id());
        redisTemplate
            .opsForValue()
            .getOperations()
            .delete(key);
    }

    private String getRedisKey(EmailVerification.IdType id) {
        return KEY_PREFIX + id.type() + ":" + id.email();
    }
}
