package org.cm.api.account.dto;

import org.cm.domain.member.Member;

public record RegisterResponse(
    Long memberId
) {
    public static RegisterResponse from(Member member) {
        return new RegisterResponse(member.getId());
    }
}
