package com.study.jpa.chap05_practice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시물
 */
@Setter @Getter
@ToString(exclude = {"hashTags"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @Column(name = "post_no")
    private Long id;

    @Column(nullable = false)
    private String writer; // 작성자

    private String title;

    private String content; // 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate; // 작성시간

    @UpdateTimestamp
    private LocalDateTime updateDate; // 수정시간

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @Builder.Default
    private List<HashTag> hashTags = new ArrayList<>();

    // 양방향 매핑에서 리스트 쪽에 데이터를 추가하는 편의 메서드 생성
    // 자동으로 갱신이 되지 않음!!
    public void addHashTag(HashTag hashTag) {
        hashTags.add(hashTag);
        // 해시태그 안에 들어있는 원본 게시물이 지금 이 게시물과 다르다면
        // 그 해시태그에 지금 이 게시물을 저장해라
        if (this != hashTag.getPost()) {
            hashTag.setPost(this);
        }
    }
}
