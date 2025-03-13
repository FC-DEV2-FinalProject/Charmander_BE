package org.cm.domain.account;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

public record EmailVerification(
    IdType id,
    String code,
    LocalDateTime expiresAt
) {
    public record IdType(
        @NonNull
        String email,
        @NonNull
        EmailVerificationType type
    ) {
    }

    public static EmailVerification create(String email, EmailVerificationType type, String code) {
        var now = LocalDateTime.now();
        var exp = now.plus(Constants.EMAIL_VERIFICATION_EXPIRATION_TIME);
        return new EmailVerification(new IdType(email, type), code, exp);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        // @formatter:off
        public static final int      EMAIL_VERIFICATION_CODE_LENGTH     = 6;
        public static final Duration EMAIL_VERIFICATION_EXPIRATION_TIME = Duration.ofMinutes(10);
        // @formatter:on
    }
}
