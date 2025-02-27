package org.cm.domain.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import java.time.LocalDate;

@Entity(name = "terms")
public class Terms extends BaseEntity {

    private String termsType;
    private String version;
    @Lob
    private String content;
    private LocalDate effectiveDate;
    private boolean isMandatory;
    private int displayOrder;
}

