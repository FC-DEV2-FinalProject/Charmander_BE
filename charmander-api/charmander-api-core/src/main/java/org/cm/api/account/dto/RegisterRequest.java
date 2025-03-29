package org.cm.api.account.dto;

import org.cm.domain.member.Member;
import org.cm.domain.member.MemberDetail;
import org.cm.domain.member.MemberPrincipal;
import org.cm.domain.member.MemberPrincipalType;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterRequest(
    String email,
    String code,
    String password,
    String name,
    String nickname,
    String phone
) {
    public Member toMember(PasswordEncoder passwordEncoder) {
        var encodedPassword = passwordEncoder.encode(password);
        var principal = new MemberPrincipal(email, encodedPassword, MemberPrincipalType.LOCAL);
        var details = new MemberDetail(name, nickname, email);
        return new Member(principal, details, phone);
    }
}
