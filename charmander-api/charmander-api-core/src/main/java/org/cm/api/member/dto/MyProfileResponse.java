package org.cm.api.member.dto;

import org.cm.domain.member.Member;
import org.cm.domain.member.MemberPrincipalType;
import org.mapstruct.Mapping;

import static org.cm.api.member.dto.MyProfileResponse.Mapper.INSTANCE;
import static org.mapstruct.factory.Mappers.getMapper;

public record MyProfileResponse(
    Long memberId,
    String email,
    String name,
    String nickname,
    String phone,
    MemberPrincipalDto principal
) {
    @org.mapstruct.Mapper
    public interface Mapper {
        Mapper INSTANCE = getMapper(Mapper.class);

        @Mapping(source = "detail.email", target = "email")
        @Mapping(source = "detail.nickname", target = "nickname")
        @Mapping(source = "detail.name", target = "name")
        @Mapping(source = "id", target = "memberId")
        MyProfileResponse map(Member e);
    }

    public static MyProfileResponse from(Member e) {
        return INSTANCE.map(e);
    }

    public record MemberPrincipalDto(
        MemberPrincipalType type
    ) {

    }
}
