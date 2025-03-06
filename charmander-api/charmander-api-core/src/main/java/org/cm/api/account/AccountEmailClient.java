package org.cm.api.account;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.cm.domain.account.EmailVerification;
import org.cm.email.EmailClient;
import org.cm.email.EmailDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountEmailClient {
    private final EmailClient emailClient;

    public void sendVerificationEmail(EmailVerification verification) {
        var dto = EmailDto.builder()
            .from("no-reply@charmander.xyz")
            .to(verification.id().email())
            .subject("Verification code")
            .template("email/verification")
            .variables(Map.of(
                "title", "Verification code",
                "code", verification.code(),
                "exp", verification.expiresAt()
            ))
            .build();
        emailClient.send(dto);
    }
}
