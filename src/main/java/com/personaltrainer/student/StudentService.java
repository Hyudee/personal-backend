package com.personaltrainer.student;

import com.personaltrainer.billing.BillingRecord;
import com.personaltrainer.billing.BillingRecordRepository;
import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.student.dto.StudentSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final int MAX_PAGE_SIZE = 50;

    private final StudentRepository studentRepository;
    private final BillingRecordRepository billingRecordRepository;

    @Transactional (readOnly = true)
    public Page<StudentSummaryResponse> listar (String search, int page, int size) {
        String term = search == null ? "" : search.trim();
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);

        Page<Student> students = studentRepository.search(term, PageRequest.of(safePage, safeSize));

        List<Long> ids = students.getContent().stream().map(Student::getId).toList();
        Map<Long, Plan> planByStudent = ids.isEmpty()
                ? Map.of()
                : billingRecordRepository.findLatestByStudentIds(ids).stream()
                        .collect(Collectors.toMap(b -> b.getStudent().getId(), BillingRecord::getPlan));

        return students.map(s -> StudentSummaryResponse.from(s, planByStudent.get(s.getId())));
    }
}
