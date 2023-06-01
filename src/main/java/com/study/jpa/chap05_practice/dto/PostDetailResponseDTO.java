package com.study.jpa.chap05_practice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 클라이언트 측에서 요청한 필드명으로 작성합니다!!
 */
@Getter @Setter
@ToString
public class PostDetailResponseDTO {

    private String author;
    private String title;
    private String content;
    private List<String> hashTags;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;

    // 엔터티를 DTO 로 변환하는 생성자
    public  PostDetailResponseDTO(Post post) {
        this.author = post.getWriter();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.regDate = post.getCreateDate();
        this.hashTags = post.getHashTags()
                .stream()
                .map(ht -> ht.getTagName())
                .collect(Collectors.toList());
    }
}
