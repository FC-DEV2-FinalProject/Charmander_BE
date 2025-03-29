package org.cm.api.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

@Getter
public class PageResponse<T> {
    private final List<T> data;
    private final PageInfo page;

    public PageResponse(Page<T> data) {
        this.data = data.getContent();
        this.page = PageInfo.from(data);
    }

    public static <D, R> PageResponse<R> of(Page<D> data, Function<D, R> mapper) {
        return new PageResponse<>(data.map(mapper));
    }

    @Value
    public static class PageInfo {
        int pageSize;
        int cursor;

        private PageInfo(int pageSize, int cursor) {
            this.pageSize = pageSize;
            this.cursor = cursor;
        }

        public static PageInfo from(Page<?> page) {
            return new PageInfo(page.getSize(), page.getNumber());
        }

        public static PageInfo from(Pageable pageable) {
            return new PageInfo(pageable.getPageSize(), pageable.getPageNumber());
        }
    }
}
