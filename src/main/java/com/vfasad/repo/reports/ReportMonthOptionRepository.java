package com.vfasad.repo.reports;

import com.vfasad.entity.reports.ReportMonthOption;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthOptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportMonthOptionRepository extends JpaRepository<ReportMonthOption, ReportMonthOptionId>{
    List<ReportMonthOption> findByYearAndMonth(int year, int month);
}
