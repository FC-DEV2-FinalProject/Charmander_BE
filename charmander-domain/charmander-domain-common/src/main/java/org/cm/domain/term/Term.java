package org.cm.domain.term;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cm.domain.common.BaseEntity;

@Getter
@Entity(name = "terms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term extends BaseEntity {
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String version;

    @Lob
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    @Column(nullable = false)
    private boolean mandatory;

    @Column(nullable = false)
    private int priority;
}
