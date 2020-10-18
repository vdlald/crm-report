package com.vladislav.crm.report.operations;

import com.vladislav.crm.report.documents.MoveLeadLog;

import java.time.LocalDateTime;
import java.util.Collection;

public interface GetAllMoveLeadLogOperation {
    Collection<MoveLeadLog> execute(LocalDateTime happenedAt, LocalDateTime happenedAt2);
}
