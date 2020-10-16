package com.vladislav.crm.report.operations;

import com.vladislav.crm.report.documents.MoveLeadLog;

import java.util.Collection;

public interface ReadAllMoveLeadLogByUserIdOperation {
    Collection<MoveLeadLog> execute(Long userId);
}
