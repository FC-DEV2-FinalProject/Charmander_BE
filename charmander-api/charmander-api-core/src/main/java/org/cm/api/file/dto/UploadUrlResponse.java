package org.cm.api.file.dto;

import java.util.List;

public record UploadUrlResponse(
    Integer part,
    String url
) {
    public static List<UploadUrlResponse> from(List<String> urls) {
        var idx = new int[]{1};
        return urls.stream()
            .map(url -> new UploadUrlResponse(idx[0]++, url))
            .toList();
    }
}
