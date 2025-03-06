package org.cm.api.member;

import lombok.RequiredArgsConstructor;
import org.cm.domain.member.Member;
import org.cm.domain.member.MemberRepository;
import org.cm.security.AuthInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member getMember(AuthInfo authInfo) {
        return memberRepository.findById(authInfo.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}
