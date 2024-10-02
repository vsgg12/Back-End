package com.example.mdmggreal.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PageInfo {

    private long size; // 한 페이지의 크기 (한 페이지 내의 데이터의 개수)
    private long page; // 페이지 넘버 (받고자/주고자 하는 페이지 넘버)
    private long totalPageNum; // 요청한 페이지 크기에 따른 총 페이지의 수

    public static PageInfo from(long size, long page, long totalPageNum) {
        return PageInfo.builder()
                .size(size)
                .page(page)
                .totalPageNum(totalPageNum)
                .build();
    }

    public static PageInfo from(Page page) {
        return PageInfo.builder()
                .size(page.getSize())
                .page(page.getPageable().getPageNumber() + 1L)
                .totalPageNum(page.getTotalPages())
                .build();
    }
}
