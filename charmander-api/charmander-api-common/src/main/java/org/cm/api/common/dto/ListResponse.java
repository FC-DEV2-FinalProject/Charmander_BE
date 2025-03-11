package org.cm.api.common.dto;

import java.util.List;
import java.util.function.Function;
import lombok.Getter;

@Getter
public class ListResponse<T> {
    private final List<T> data;

    public ListResponse(List<T> data) {
        this.data = data;
    }

    public static <D, R> ListResponse<R> of(List<D> data, Function<D, R> mapper) {
        return new ListResponse<>(data.stream().map(mapper).toList());
    }
}
