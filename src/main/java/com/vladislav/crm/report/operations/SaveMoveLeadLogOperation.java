package com.vladislav.crm.report.operations;

import com.vladislav.crm.report.documents.MoveLeadLog;

public interface SaveMoveLeadLogOperation {
    MoveLeadLog execute(MoveLeadLog log);
}
