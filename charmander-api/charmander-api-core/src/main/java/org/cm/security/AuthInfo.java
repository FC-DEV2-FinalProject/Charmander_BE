package org.cm.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.Serializable;
import java.security.Principal;
import lombok.Getter;

@Getter
public class AuthInfo implements Principal, Serializable {
    private final Long memberId;

    private final String name;

    public AuthInfo(Long memberId) {
        this.memberId = memberId;
        this.name = "member-" + memberId;
    }

    public static AuthInfo from(DecodedJWT jwt) {
        return new AuthInfo(
            Long.parseLong(jwt.getSubject())
        );
    }
}
