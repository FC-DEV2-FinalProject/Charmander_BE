package org.cm.domain.common;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "member_terms_agreement")
public class MemberTermsAgreement extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Member member;
    private String termsType;
    private String version;
    private boolean isAgreed;
}