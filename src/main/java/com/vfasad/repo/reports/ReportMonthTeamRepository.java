package com.vfasad.repo.reports;

import com.vfasad.entity.reports.ReportMonthTeam;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMonthTeamRepository extends JpaRepository<ReportMonthTeam, ReportMonthTeamId> {
    List<ReportMonthTeam> findByYearAndMonthOrderByTeamId(int year, int month);
}
