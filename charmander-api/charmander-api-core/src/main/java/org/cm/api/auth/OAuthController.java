package org.cm.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cm.api.account.AccountService;
import org.cm.api.auth.dto.LoginResponse;
import org.cm.api.auth.dto.OAuthSignupRequest;
import org.cm.security.auth.oauth.OAuthClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthClient oAuthClient;

    private final AuthService authService;
    private final AccountService accountService;

    @GetMapping("/authorize/{provider}")
    public String authorize(@PathVariable String provider, HttpServletRequest request) {
        var authorizeUrl = oAuthClient.getAuthorizeUri(provider, "state");
        return "redirect:" + authorizeUrl;
    }

    @PostMapping("/signup")
    @ResponseBody
    public LoginResponse signup(@RequestBody OAuthSignupRequest request, HttpServletResponse httpResponse) {
        var token = oAuthClient.getToken(request.provider(), request.code(), request.state());
        var oAuthUserInfo = oAuthClient.getUserInfo(request.provider(), token, request.state());

        accountService.register(oAuthUserInfo);
        var tokens = authService.login(oAuthUserInfo);

        AuthHttpUtils.addRefreshTokenCookie(httpResponse, tokens.refreshToken());

        return tokens;
    }
}
