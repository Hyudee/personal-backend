package com.personaltrainer.student;

import com.personaltrainer.common.PageResponse;
import com.personaltrainer.student.dto.StudentSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public PageResponse<StudentSummaryResponse> listar (
            @RequestParam(required = false) String search,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size ) {

        return PageResponse.from (studentService.listar(search,page,size));

    }
}
