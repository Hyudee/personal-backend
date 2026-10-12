package com.personaltrainer.billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BillingRecordRepository extends JpaRepository <BillingRecord, Long> {

    @Query("""
        select b from BillingRecord b join fetch b.plan
        where b.student.id in :studentIds
          and b.id = (select max(b2.id) from BillingRecord b2
                      where b2.student.id = b.student.id)
        """)
    List<BillingRecord> findLatestByStudentIds(@Param("studentIds") Collection<Long> studentIds);
}
