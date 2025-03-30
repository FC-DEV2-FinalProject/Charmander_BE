package org.cm.domain.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateAvatarRepository extends JpaRepository<TemplateAvatar, Long> {
    @Query("""
            SELECT ta
            FROM templateAvatar ta
            WHERE ta.owner.id IS NULL
               OR ta.owner.id = :memberId
        """)
    List<TemplateAvatar> findAllForMember(Long memberId);
}
