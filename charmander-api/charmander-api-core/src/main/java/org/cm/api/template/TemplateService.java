package org.cm.api.template;

import lombok.RequiredArgsConstructor;
import org.cm.domain.template.Template;
import org.cm.domain.template.TemplateCategory;
import org.cm.domain.template.TemplateRepository;
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

    public Template getTemplateById(Long id) {
        return templateRepository.findById(id).orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public List<TemplateCategory> getCategories() {
        return List.of(TemplateCategory.values());
    }
}
