package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 클라이언트 개발자가 원하는대로 정제하는 역할
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // JPA 레파지토리를 사용하는 빈은 반드시 트랜잭션 아노테이션 필수
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;

    public PostListResponseDTO getPosts(PageDTO dto) {

        // Pageable 객체 생성
         Pageable pageable = PageRequest.of(
                 dto.getPage() - 1,
                 dto.getSize(),
                 Sort.by("createDate").descending()
         );

        // 데이터베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(pageable);

        // 게시물 정보만 꺼내기
        List<Post> postList = posts.getContent();
        List<PostDetailResponseDTO> detailList
                = postList.stream()
                            .map(post -> new PostDetailResponseDTO(post))
                            .collect(Collectors.toList());


        // DB 에서 조회한 정보를 JSON 형태에 맞는 DTO 로 변환
        PostListResponseDTO responseDTO = PostListResponseDTO.builder()
                                            .count(detailList.size()) // 총게시물 수가 아니라 조회된 게시물 수
                                            .pageInfo(new PageResponseDTO<Post>(posts))
                                            .posts(detailList)
                                            .build();

        return responseDTO;
    }

    public PostDetailResponseDTO getDetail(Long id) {

        Post postEntity = postRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                id + "번 게시물이 존재하지 않습니다!"
                        )
                );

        return new PostDetailResponseDTO(postEntity);
    }

    public PostDetailResponseDTO insert(final PostCreateDTO dto)
            throws RuntimeException // 정확히 어떤 에러가 날 지 모를때... 사용!
    {

        // 게시물 저장 (해시태그 없이), 저장된 데이터 리턴
        Post saved = postRepository.save(dto.toEntity());

        // 해시태그 저장 (리턴된 데이터에서 해시태그가 있는지 확인)
        List<String> hashTags = dto.getHashTags();
        // 해시태그가 있는 경우 해시태그 테이블에 해시태그와 그 게시물의 정보 저장
        if (hashTags != null && hashTags.size() > 0) {
            hashTags.forEach(ht -> {
                HashTag savedTag = hashTagRepository.save(
                        HashTag.builder()
                                .tagName(ht)
                                .post(saved)
                                .build()
                );
                // 양방향 매핑의 갱신 문제 -> 게시물 쪽에도 해시태그를 저장해주기 (Post 클래스에서 해시태그도 똑같이!)
                saved.addHashTag(savedTag);
            });
        }

        return new PostDetailResponseDTO(saved);
    }
}
