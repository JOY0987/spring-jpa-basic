package com.study.jpa.chap05_practice.dto;

import lombok.*;

// dto 에는 세터, Noarg 필수
@Getter @Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class PageDTO {

    private int page;
    private int size;

    public PageDTO() {
        this.page = 1;
        this.size = 10;
    }
}
