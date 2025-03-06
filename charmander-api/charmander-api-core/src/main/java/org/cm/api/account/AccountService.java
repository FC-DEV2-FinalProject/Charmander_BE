package org.cm.api.account;

import lombok.RequiredArgsConstructor;
import org.cm.api.account.dto.CheckEmailRequest;
import org.cm.api.account.dto.RegisterRequest;
import org.cm.api.account.dto.VerifyEmailRequest;
import org.cm.domain.account.EmailVerification;
import org.cm.domain.account.EmailVerificationRepository;
import org.cm.domain.account.EmailVerificationType;
import org.cm.domain.member.Member;
import org.cm.domain.member.MemberPrincipalType;
import org.cm.domain.member.MemberRepository;
import org.cm.utils.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountEmailClient emailClient;

    private final MemberRepository memberRepository;
    private final EmailVerificationRepository verificationRepository;

    private final PasswordEncoder passwordEncoder;

    public Member register(RegisterRequest request) {
        var verification = verificationRepository.get(new EmailVerification.IdType(request.email(), EmailVerificationType.REGISTER));

        validateEmailNotInUse(request.email());
        validateVerification(verification, request.code());

        var member = request.toMember(passwordEncoder);

        memberRepository.save(member);
        verificationRepository.delete(verification);

        return member;
    }

    public void checkEmail(CheckEmailRequest request, EmailVerificationType type) {
        validateEmailNotInUse(request.email());

        var code = StringUtils.generateRandomNumberString(EmailVerification.Constants.EMAIL_VERIFICATION_CODE_LENGTH);
        var verification = EmailVerification.create(request.email(), type, code);

        emailClient.sendVerificationEmail(verification);
        verificationRepository.save(verification);
    }

    public void verifyEmail(VerifyEmailRequest request, EmailVerificationType type) {
        var verification = verificationRepository.get(new EmailVerification.IdType(request.email(), type));

        validateEmailNotInUse(request.email());
        validateVerification(verification, request.code());
    }

    private void validateEmailNotInUse(String email) {
        if (memberRepository.existsByPrincipal_IdAndPrincipal_Type(email, MemberPrincipalType.LOCAL)) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    private void validateVerification(EmailVerification verification, String code) {
        if (verification != null && !verification.code().equals(code)) {
            throw new IllegalArgumentException("Verification code already sent");
        }
    }
}
