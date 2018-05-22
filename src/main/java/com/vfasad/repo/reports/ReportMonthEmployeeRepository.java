package com.vfasad.repo.reports;

import com.vfasad.entity.reports.ReportMonthEmployee;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthEmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMonthEmployeeRepository extends JpaRepository<ReportMonthEmployee, ReportMonthEmployeeId>{
    List<ReportMonthEmployee> findAllByYearAndMonthOrderByEmployeeId(int year, int month);
}
