package com.study.jpa.chap05_practice.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 해시태그
 * 연관관계의 주인
 */
@Setter
@Getter
@ToString(exclude = {"post"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_hash_tag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @Column(name = "tag_no")
    private Long id;
    
    private String tagName; // 해시태그 이름

    // 단방향 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_no")
    private Post post;

}
