package org.cm.utils;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URLUtils {
  public static String buildQueryString(Map<String, ?> params) {
    var queryString = new StringBuilder();
    for (var entry : params.entrySet()) {
      queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
    }
    return queryString.substring(0, queryString.length() - 1);
  }
}
