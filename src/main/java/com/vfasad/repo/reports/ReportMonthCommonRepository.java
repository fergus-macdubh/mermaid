package com.vfasad.repo.reports;

import com.vfasad.entity.reports.ReportMonthCommon;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportMonthCommonRepository extends JpaRepository<ReportMonthCommon, ReportMonthId> {
    Optional<ReportMonthCommon> findByYearAndMonth(int year, int month);
}
