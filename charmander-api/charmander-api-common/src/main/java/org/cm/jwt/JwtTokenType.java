package org.cm.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum JwtTokenType implements PersistenceEnum<String> {
    // @formatter:off
    ACCESS_TOKEN  ("token:access"),
    REFRESH_TOKEN ("token:refresh"),
    ;
    // @formatter:on

    private final String value;
}
