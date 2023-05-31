package com.study.jpa.chap03_pagination;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPageRepository extends JpaRepository<Student, String> {
    
    // 학생 조건없이 전체조회 페이징 (기본기능 - 안만들어도됨)
    Page<Student> findAll(Pageable pageable);

    // 밑은 직접 만들어야 함!

    // 학생의 이름에 특정단어가 포함된 걸 조회 + 페이징 (검색 + 페이징) - 조건이 몇개든 마지막에 Pageable 넣어주기)
    Page<Student> findByNameContaining(String name, Pageable pageable);
}
