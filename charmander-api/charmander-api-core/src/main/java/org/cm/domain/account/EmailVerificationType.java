package org.cm.domain.account;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EmailVerificationType implements PersistenceEnum<String> {
    // @formatter:off
    REGISTER       ("register"),
    FIND_USERNAME  ("find-username"),
    RESET_PASSWORD ("reset-password"),
    ;
    // @formatter:on

    private final String value;
}
