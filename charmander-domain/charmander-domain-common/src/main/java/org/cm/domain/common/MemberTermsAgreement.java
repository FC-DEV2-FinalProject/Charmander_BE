package org.cm.domain.common;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "mamber_terms_agreement")
public class MemberTermsAgreement extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    private String terms_type;
    private String version;
    private boolean isAgreed;
}