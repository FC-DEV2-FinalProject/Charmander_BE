package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.template.dto.TemplateResponse;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @MemberOnly
    @GetMapping
    public ListResponse<TemplateResponse> getAllTemplates(@AuthUser AuthInfo authInfo) {
        var items = templateService.findAll();
        return ListResponse.of(items, TemplateResponse::from);
    }

    @MemberOnly
    @GetMapping("/{id}")
    public TemplateResponse getTemplateById(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        var item = templateService.getTemplateById(id);
        return TemplateResponse.from(item);
    }
}
