package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.domain.template.Template;
import org.cm.domain.template.TemplateCategory;
import org.cm.domain.template.TemplateRepository;
import org.cm.exception.CoreApiException;
import org.cm.exception.CoreApiExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public List<Template> findAll() {
        return templateRepository.findAll();
    }

    public List<Template> findByCategory(int categoryId) {
        return templateRepository.findByCategory(TemplateCategory.of(categoryId));
    }

    public Template getTemplateById(Long id) {
        return templateRepository.findById(id).orElseThrow(() -> new CoreApiException(CoreApiExceptionCode.TEMPLATE_NOT_FOUND));
    }

    public List<TemplateCategory> getCategories() {
        return List.of(TemplateCategory.values());
    }
}
