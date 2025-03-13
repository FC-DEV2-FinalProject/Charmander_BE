package org.cm.domain.account;

import org.jspecify.annotations.Nullable;

public interface EmailVerificationRepository {
    @Nullable
    EmailVerification get(EmailVerification.IdType id);

    void save(EmailVerification verification);

    void delete(EmailVerification verification);
}
