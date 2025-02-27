package org.cm.domain.common;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity(name = "member")
public class Member extends BaseEntity {

    @Embedded
    private Principal principal;

    @Embedded
    private MemberDetail memberDetail;

    //TODO : Converter로 암호화 하기
    private String phoneNumber;
}
