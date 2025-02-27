package org.cm.domain.common;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity(name = "member")
public class Member extends BaseEntity {

    //jpa스럽게 domain스럽게
    @Embedded
    private Principal principal;

    @Embedded
    private PersonalInfo personalInfo;

    private String phoneNumber;

    //약관동의를 한 유저라는것을 컬럼으로 나타내야하는가
}
