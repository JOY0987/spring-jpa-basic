package com.study.jpa.chap05_practice.dto;

import com.study.jpa.chap05_practice.entity.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

/**
 * 페이지 정보
 * ResponseDTO 에서는 어떤식으로 프론트에 JSON 데이터를 보내줄 건지 설계합니다.
 */
@Setter @Getter
@ToString
public class PageResponseDTO<T> {

    private int startPage;
    private int endPage;
    private int currentPage;

    private boolean prev;
    private boolean next;

    private int totalCount;

    // 한 페이지에 배치할 페이지 수 (1 ~ 10 // 11 ~ 20 ...)
    private static final int PAGE_COUNT = 10;

    public PageResponseDTO(Page<T> pageData) {
        this.totalCount = (int) pageData.getTotalElements();
        this.currentPage = pageData.getPageable().getPageNumber() + 1;
        this.endPage = (int) (Math.ceil((double) currentPage / PAGE_COUNT) * PAGE_COUNT);
        this.startPage = endPage - PAGE_COUNT + 1;

        int realEnd = pageData.getTotalPages();

        if (realEnd < this.endPage) this.endPage = realEnd;

        this.prev = startPage > 1;
        this.next = endPage < realEnd;
    }
}
