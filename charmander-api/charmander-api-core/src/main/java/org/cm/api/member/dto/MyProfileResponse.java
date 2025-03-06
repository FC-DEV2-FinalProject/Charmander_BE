package org.cm.api.member.dto;

import org.cm.domain.member.Member;

public record MyProfileResponse(
    Long memberId,
    String email
) {
    public static MyProfileResponse from(Member member) {
        return new MyProfileResponse(
            member.getId(),
            member.getDetail().email()
        );
    }
}
