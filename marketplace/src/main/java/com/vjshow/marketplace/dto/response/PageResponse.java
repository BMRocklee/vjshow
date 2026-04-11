package com.vjshow.marketplace.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse<T> {
    private List<T> data;
    private boolean hasNext;
    private long totalElements;
    private int currentPage;
}