package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.api.common.dto.ListResponse;
import org.cm.api.template.dto.TemplateCategoryDto;
import org.cm.api.template.dto.TemplateResponse;
import org.cm.domain.template.Template;
import org.cm.security.AuthInfo;
import org.cm.security.annotations.support.AuthUser;
import org.cm.security.annotations.support.MemberOnly;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @MemberOnly
    @GetMapping
    public ListResponse<TemplateResponse> getAllTemplates(
        @Nullable @RequestParam(required = false) Integer categoryId,
        @AuthUser AuthInfo authInfo
    ) {
        List<Template> items;
        if (categoryId == null) {
            items = templateService.findAll();
        }
        else {
            items = templateService.findByCategory(categoryId);
        }
        return ListResponse.of(items, TemplateResponse::from);
    }

    @MemberOnly
    @GetMapping("/{id}")
    public TemplateResponse getTemplateById(@PathVariable Long id, @AuthUser AuthInfo authInfo) {
        var item = templateService.getTemplateById(id);
        return TemplateResponse.from(item);
    }

    @GetMapping("/categories")
    public ListResponse<TemplateCategoryDto> getCategories(@AuthUser AuthInfo authInfo) {
        var items = templateService.getCategories();
        return ListResponse.of(items, TemplateCategoryDto::from);
    }
}
