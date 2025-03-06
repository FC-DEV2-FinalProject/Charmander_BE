package org.cm.api.auth;

import lombok.RequiredArgsConstructor;
import org.cm.security.auth.oauth.OAuthClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthClient oAuthClient;

    @GetMapping("/authorize/{provider}")
    public String authorize(@PathVariable String provider) {
        var authorizeUrl = oAuthClient.getAuthorizeUri(provider, "state");
        return "redirect:" + authorizeUrl;
    }

    @GetMapping("/callback/{provider}")
    public Object callback(@PathVariable String provider, @RequestParam String code, @RequestParam String state
    ) {
        var token = oAuthClient.getToken(provider, code, state);
        var userInfo = oAuthClient.getUserInfo(provider, token, state);
        // TODO: OAuth 인증 성공하고 어떻게 처리할지?
        return ResponseEntity.ok(userInfo);
    }
}
