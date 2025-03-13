package org.cm.domain.member;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Nullable
    Member findByPrincipal_IdAndPrincipal_Type(String username, MemberPrincipalType memberPrincipalType);

    boolean existsByPrincipal_IdAndPrincipal_Type(String email, MemberPrincipalType memberPrincipalType);
}
