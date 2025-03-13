package org.cm.domain.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    @Query("SELECT t FROM template t WHERE t.data.category = ?1")
    List<Template> findByCategory(TemplateCategory category);
}
