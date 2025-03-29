package org.cm.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cm.domain.member.Member;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
    private final Algorithm algorithm;

    public String createAccessToken(Member member) {
        return createToken(member.getId().toString(), JwtTokenType.ACCESS_TOKEN.getValue(), JwtConstants.ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String createRefreshToken(Member member) {
        return createToken(member.getId().toString(), JwtTokenType.REFRESH_TOKEN.getValue(), JwtConstants.REFRESH_TOKEN_DURATION);
    }

    public String createAccessToken(String sub) {
        return createToken(sub, JwtTokenType.ACCESS_TOKEN.getValue(), JwtConstants.ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String createRefreshToken(String sub) {
        return createToken(sub, JwtTokenType.REFRESH_TOKEN.getValue(), JwtConstants.REFRESH_TOKEN_DURATION);
    }

    public DecodedJWT verifyAccessToken(String token) {
        return verifyToken(token, JwtTokenType.ACCESS_TOKEN);
    }

    public DecodedJWT verifyRefreshToken(String token) {
        return verifyToken(token, JwtTokenType.REFRESH_TOKEN);
    }

    public boolean shouldReissueRefreshToken(DecodedJWT jwt) {
        return Instant.now().plus(JwtConstants.REFRESH_TOKEN_REISSUE_THRESHOLD).isAfter(jwt.getExpiresAt().toInstant());
    }

    private DecodedJWT verifyToken(String token, JwtTokenType type) {
        return JWT.require(algorithm)
            .withIssuer(JwtConstants.ISSUER)
            .withClaim("typ", type.getValue())
            .build()
            .verify(token);
    }

    private String createToken(String sub, String typ, Duration exp) {
        var issuedAt = Instant.now();
        return JWT.create()
            .withSubject(sub)
            .withIssuer(JwtConstants.ISSUER)
            .withIssuedAt(issuedAt)
            .withExpiresAt(issuedAt.plus(exp))
            .withClaim("typ", typ)
            .sign(algorithm);
    }
}
