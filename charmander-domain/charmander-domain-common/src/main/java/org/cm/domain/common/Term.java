package org.cm.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import java.time.LocalDate;

@Entity(name = "terms")
public class Term extends BaseEntity {

    @Column(name = "terms_type")
    private String type;
    private String version;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDate effectiveDate;
    private boolean mandatory;
    private int priority;
}

