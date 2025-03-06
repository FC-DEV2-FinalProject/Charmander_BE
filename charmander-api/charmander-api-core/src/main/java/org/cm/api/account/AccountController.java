package org.cm.api.account;

import lombok.RequiredArgsConstructor;
import org.cm.api.account.dto.CheckEmailRequest;
import org.cm.api.account.dto.RegisterRequest;
import org.cm.api.account.dto.RegisterResponse;
import org.cm.api.account.dto.VerifyEmailRequest;
import org.cm.domain.account.EmailVerificationType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register/check-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkEmailForRegister(@RequestBody CheckEmailRequest request) {
        accountService.checkEmail(request, EmailVerificationType.REGISTER);
    }

    @PostMapping("/register/verify-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyEmailForRegister(@RequestBody VerifyEmailRequest request) {
        accountService.verifyEmail(request, EmailVerificationType.REGISTER);
    }
}
