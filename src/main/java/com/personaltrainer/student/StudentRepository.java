package com.personaltrainer.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId (Long userId);

    @Query(value = """
        select s from students s join fetch s.user u
        where lower(u.name) like lower(concat('%', :search, '%'))
           or lower(u.email) like lower(concat('%', :search, '%'))
        order by u.name asc
        """,
            countQuery = """
        select count(s) from students s join s.user u
        where lower(u.name) like lower(concat('%', :search, '%'))
           or lower(u.email) like lower(concat('%', :search, '%'))
        """)
    Page<Student> search(@Param("search") String search, Pageable pageable);
}
