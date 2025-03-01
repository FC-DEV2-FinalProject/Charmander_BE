package org.cm.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;
import org.cm.domain.term.Term;

@Getter
@Entity(name = "memberTermsAgreement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTermAgreement extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Term term;

    public MemberTermAgreement(
            Member member,
            Term term
    ) {
        this.member = member;
        this.term = term;
    }
}
