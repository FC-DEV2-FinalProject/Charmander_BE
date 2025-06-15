package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.template.dto.TemplateBackgroundResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/templates/backgrounds")
@RequiredArgsConstructor
public class TemplateBackgroundController {
    private final TemplateBackgroundService templateBackgroundService;

    @MemberOnly
    @GetMapping
    public ListResponse<TemplateBackgroundResponse> getBackgrounds(@AuthUser AuthInfo authInfo) {
        var items = templateBackgroundService.getBackgrounds(authInfo);
        return ListResponse.of(items, TemplateBackgroundResponse::from);
    }
}
