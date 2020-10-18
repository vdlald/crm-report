package com.vladislav.crm.report.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WeeklyReport {

    private Long userId;

    private List<LeadMoveReport> leadMoveReports = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class LeadMoveReport {
        private Long leadId;
        private Long prevStatus;
        private Long nextStatus;
    }
}
