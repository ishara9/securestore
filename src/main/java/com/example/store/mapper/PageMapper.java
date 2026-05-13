package com.example.store.mapper;

import com.example.store.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public interface PageMapper<T, R> {

    default PageResponse<R> mapPage(Page<T> page, Function<T, R> mapper) {
        return new PageResponse<>(
                page.map(mapper).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
