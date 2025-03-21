package org.cm.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cm.converter.DurationToSecondConverter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskOutput {
    // TODO fileId로 바꾸는게 좋을 듯
    @Column
    private String fileUrl;
    @Column
    private String fileName;
    @Convert(converter = DurationToSecondConverter.class)
    @Column
    private Duration playtime;
    @Column
    private Integer downloadCount;

    public TaskOutput(
            String fileName,
            Duration playtime
    ) {
        this.fileName = fileName;
        this.fileUrl = "";
        this.playtime = playtime;
        this.downloadCount = 0;
    }

    public TaskOutput(
            String fileUrl,
            String fileName,
            Duration playtime,
            Integer downloadCount
    ) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.playtime = playtime;
        this.downloadCount = downloadCount;
    }

    public String fileUrl() {
        return fileUrl;
    }

    public String fileName() {
        return fileName;
    }

    public Duration playtime() {
        return playtime;
    }

    public Integer downloadCount() {
        return downloadCount;
    }

}
