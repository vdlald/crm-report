package com.vladislav.crm.report.operations;

import com.vladislav.crm.report.documents.NewLeadLog;

import java.time.LocalDateTime;
import java.util.Collection;

public interface GetAllNewLeadLogOperation {
    Collection<NewLeadLog> execute(LocalDateTime happenedAt, LocalDateTime happenedAt2);
}
