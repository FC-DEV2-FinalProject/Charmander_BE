package org.cm.fixture;

import org.cm.domain.member.Member;
import org.cm.domain.member.MemberDetail;
import org.cm.domain.member.MemberPrincipal;
import org.cm.domain.member.MemberPrincipalType;

public class MemberFixture {

    public static Member create(){
        var principal = new MemberPrincipal("id", "password", MemberPrincipalType.LOCAL);
        var detail = new MemberDetail("파2리", "파2리", "파2리@gmail.com");
        var phoneNumber = "010-9999-9999";

        return new Member(principal, detail, phoneNumber);
    }
}
