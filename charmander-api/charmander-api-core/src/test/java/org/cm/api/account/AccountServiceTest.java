package org.cm.api.account;

import org.cm.api.account.dto.VerifyEmailRequest;
import org.cm.common.exception.BaseException;
import org.cm.domain.account.EmailVerificationRepository;
import org.cm.domain.account.EmailVerificationType;
import org.cm.domain.member.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @MockitoBean
    private AccountEmailClient emailClient;

    @MockitoBean
    private MemberRepository memberRepository;

    @MockitoBean
    private EmailVerificationRepository verificationRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(emailClient, memberRepository, verificationRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(emailClient, memberRepository, verificationRepository, passwordEncoder);
        accountService = null;
    }

    @Test
    @DisplayName("[이메일 검증] 이메일 인증 이력이 없을 경우 예외가 발생해야 한다.")
    void test00001() {
        // Given
        var req = new VerifyEmailRequest("email@test.com", "1234");
        Mockito.when(verificationRepository.get(Mockito.any()))
            .thenReturn(null);

        // When
        Assertions.assertThrows(
            BaseException.class, () -> {
                accountService.verifyEmail(req, EmailVerificationType.REGISTER);
            }
        );
    }
}
