package com.vladislav.crm.report.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WeeklyReport {

    // key - leadId, value - statusId
    private Map<Long, Long> leadsMove = new HashMap<>();

}
