package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("특정 부서를 조회하면 해당 부서원들도 함께 조회되어야 한다")
    void testFindDept() {
        //given
        Long id = 2L;
        //when
        Department department = departmentRepository.findById(id).orElseThrow();
        //then
        System.out.println("\n\n\n");
        System.out.println("department = " + department);
        System.out.println("department.getEmployees() = " + department.getEmployees());
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("Lazy로딩과 Eager로딩의 차이")
    void testLazyAndEager() {
        // 3번 사원을 조회하고 싶은데 굳이 부서정보는 필요없다.
        //given
        Long id = 3L;
        //when
        Employee employee = employeeRepository.findById(id).orElseThrow();
        //then
        // EAGER : 항상 무조건 조인을 수행
        // LAZY : 필요한 경우에만 조인을 수행 (실무)
        System.out.println("\n\n\n");
        System.out.println("employee = " + employee);
        // 이런 코드를 작성했을 때 조인을 수행한다!!
        System.out.println("employee.getDepartment() = " + employee.getDepartment());
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("양방향 연관관계에서 연관데이터의 수정")
    void testChangeDept() {
        // 3번 사원의 부서를 2번 부서에서 1번 부서로 변경해야 한다.
        //given
        Long id = 3L;
        Long deptId = 1L;
        Employee employee = employeeRepository.findById(id).orElseThrow();
        Department department = departmentRepository.findById(deptId).orElseThrow();

        employee.setDepartment(department);
//        department.getEmployees().add(employee); // <변경에서 문제가 생긴 경우> 반대쪽에서도 처리해야 한다는 점 떠올리기!

        employeeRepository.save(employee);

        //when
        // 1번 부서 정보를 조회해서 모든 사원을 보겠다.
        Department foundDept = departmentRepository.findById(deptId).orElseThrow();

        System.out.println("\n\n\n");
        foundDept.getEmployees().forEach(System.out::println);
        System.out.println("\n\n\n");
        //then
    }

    // 면접에서 많이 물어보는 문제!
    @Test
    @DisplayName("N+1 문제 발생 예시")
    void testNPlus1() {
        //given
        List<Department> departments = departmentRepository.findAll();
        //when
        departments.forEach(dept -> {
            System.out.println("\n\n======== 사원 리스트 =========");

            // 하나의 부서에서 사원 정보를 다 꺼내보기
            List<Employee> employees = dept.getEmployees();
            System.out.println("employees = " + employees);

            System.out.println("\n\n");
        });
        //then
    }

    // N+1 문제 해결
    // JPA + fetch LAZY 조인이 필요한 경우, 조인을 하지 않는다는 문제가 있다.
    //
    @Test
    @DisplayName("N+1 문제 해결 예시")
    void testNPlus1Solution() {
        //given
        List<Department> departments = departmentRepository.findAllIncludeEmployees();
        //when
        departments.forEach(dept -> {
            System.out.println("\n\n======= 사원 리스트 =========");

            List<Employee> employees = dept.getEmployees();
            System.out.println("employees = " + employees);

            System.out.println("\n\n");
        });
        //then
    }

}