package com.vladislav.crm.report.operations;

import com.vladislav.crm.report.documents.MoveLeadLog;

import java.time.LocalDateTime;
import java.util.Collection;

public interface GetAllMoveLeadLogByUserIdOperation {
    Collection<MoveLeadLog> execute(
            Long userId, LocalDateTime happenedAt, LocalDateTime happenedAt2
    );
}
