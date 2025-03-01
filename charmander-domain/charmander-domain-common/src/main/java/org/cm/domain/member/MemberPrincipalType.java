package org.cm.domain.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cm.domain.common.GenericEnumConverter;
import org.cm.domain.common.PersistenceEnum;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberPrincipalType implements PersistenceEnum<Integer> {
    // @formatter:off
    LOCAL (10),
    OAUTH (20),
    ;
    // @formatter:on

    private final Integer value;

    @jakarta.persistence.Converter
    public static class Converter extends GenericEnumConverter<MemberPrincipalType, Integer> {
        public Converter() {
            super(MemberPrincipalType.class);
        }
    }
}
