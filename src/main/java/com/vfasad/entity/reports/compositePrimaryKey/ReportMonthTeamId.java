package com.vfasad.entity.reports.compositePrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class ReportMonthTeamId implements Serializable{
    private int year;
    private int month;
    private Long teamId;

    public ReportMonthTeamId() {
    }

    public ReportMonthTeamId(int year, int month, Long teamId) {
        this.year = year;
        this.month = month;
        this.teamId = teamId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Long getTeam() {
        return teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportMonthTeamId)) return false;
        ReportMonthTeamId that = (ReportMonthTeamId) o;
        return year == that.year &&
                month == that.month &&
                Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(year, month, teamId);
    }
}
