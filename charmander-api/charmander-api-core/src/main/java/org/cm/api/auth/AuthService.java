package org.cm.api.auth;

import lombok.RequiredArgsConstructor;
import org.cm.api.auth.dto.LoginRequest;
import org.cm.api.auth.dto.LoginResponse;
import org.cm.domain.member.MemberPrincipalType;
import org.cm.domain.member.MemberRepository;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.cm.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        var member = memberRepository.findByPrincipal_IdAndPrincipal_Type(request.username(), MemberPrincipalType.LOCAL);

        if (member == null || !passwordEncoder.matches(request.password(), member.getPrincipal().credential())) {
            throw new CoreApiException(CoreApiExceptionCode.AUTH_INVALID_CREDENTIAL);
        }

        var accessToken = jwtService.createAccessToken(member);
        var refreshToken = jwtService.createRefreshToken(member);
        return new LoginResponse(accessToken, refreshToken);
    }

    public void logout() {
        // TODO: 로그아웃 이벤트
    }

    public LoginResponse refresh(String refreshToken) {
        var jwt = jwtService.verifyRefreshToken(refreshToken);
        var accessToken = jwtService.createAccessToken(jwt.getSubject());
        if (jwtService.shouldReissueRefreshToken(jwt)) {
            refreshToken = jwtService.createRefreshToken(jwt.getSubject());
        }
        return new LoginResponse(accessToken, refreshToken);
    }
}
