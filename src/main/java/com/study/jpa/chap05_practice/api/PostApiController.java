package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PostCreateDTO;
import com.study.jpa.chap05_practice.dto.PostDetailResponseDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    // 리소스 : 게시물 (Post)
/*
     게시물 목록 조회:  /posts       - GET
     게시물 개별 조회:  /posts/{id}  - GET
     게시물 등록:      /posts       - POST
     게시물 수정:      /posts/{id}  - PATCH
     게시물 삭제:      /posts/{id}  - DELETE
 */
    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> list(PageDTO pageDTO) {
        log.info("/api/v1/posts?page={}&size={}", pageDTO.getPage(), pageDTO.getSize());

        PostListResponseDTO dto = postService.getPosts(pageDTO);

        return ResponseEntity
                .ok()
                .body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.info("/api/v1/posts/{} GET", id);

        // 조회한 게시물이 없는 경우 예외가 발생하기 때문에 try catch 처리 해주기!
        try {
            PostDetailResponseDTO dto = postService.getDetail(id);
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // ?
        }
    }

    // 게시물 등록
    // 사용자가 작성한 내용을 ajax 로 받기 때문에 비동기로 만들어주기
    @PostMapping
    public ResponseEntity<?> create(@Validated @RequestBody PostCreateDTO dto,
                                    BindingResult result // 검증 에러 정보를 가진 객체
    ) {
        log.info("/api/v1/post POST!! - payload: {}");

        if (dto == null) {
            return ResponseEntity
                    .badRequest()
                    .body("등록 게시물 정보를 전달해주세요!");
        }

        // 입력값 검증에 걸린 경우
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();// 모든 에러 정보를 받을 수 있다!
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity.badRequest().body(fieldErrors);
        }

        try {
            PostDetailResponseDTO responseDTO = postService.insert(dto);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (RuntimeException e) {
//            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body("미안 서버 터짐 원인: " + e.getMessage());
        }
    }

}
