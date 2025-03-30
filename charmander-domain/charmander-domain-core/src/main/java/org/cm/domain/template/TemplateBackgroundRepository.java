package org.cm.domain.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateBackgroundRepository extends JpaRepository<TemplateBackground, Long> {
    @Query("""
            SELECT tb
            FROM templateBackground tb
            WHERE tb.owner.id IS NULL
               OR tb.owner.id = :memberId
        """)
    List<TemplateBackground> findAllForMember(Long memberId);
}
