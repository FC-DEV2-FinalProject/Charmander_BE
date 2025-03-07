package org.cm.api.common.dto;

import java.util.List;
import java.util.function.Function;
import lombok.Getter;

@Getter
public class ListResponse<D> {
    private final List<D> data;

    public ListResponse(List<D> data) {
        this.data = data;
    }

    public static <R, D> ListResponse<D> of(List<R> data, Function<R, D> mapper) {
        return new ListResponse<>(data.stream().map(mapper).toList());
    }
}
