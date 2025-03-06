package org.cm.api.member;

import lombok.RequiredArgsConstructor;
import org.cm.api.member.dto.MyProfileResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/my")
    public MyProfileResponse getMyInfo(@AuthUser AuthInfo authInfo) {
        var member = memberService.getMember(authInfo);
        return MyProfileResponse.from(member);
    }
}
