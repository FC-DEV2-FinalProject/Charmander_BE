package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.template.dto.TemplateAvatarResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/templates/avatars")
@RequiredArgsConstructor
public class TemplateAvatarController {
    private final TemplateAvatarService templateAvatarService;

    @MemberOnly
    @GetMapping
    public ListResponse<TemplateAvatarResponse> getAvatars(@AuthUser AuthInfo authInfo) {
        var items = templateAvatarService.getAvatars(authInfo);
        return ListResponse.of(items, TemplateAvatarResponse::from);
    }
}
