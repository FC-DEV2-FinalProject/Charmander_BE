package org.cm.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;

@Getter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Embedded
    private MemberPrincipal principal;

    @Embedded
    private MemberDetail detail;

    //TODO : Converter로 암호화 하기
    private String phoneNumber;

    public Member(
        MemberPrincipal principal,
        MemberDetail detail,
        String phoneNumber
    ) {
        this.principal = principal;
        this.detail = detail;
        this.phoneNumber = phoneNumber;
    }
}
