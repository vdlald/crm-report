package com.vladislav.crm.report.operations;

import com.vladislav.crm.report.documents.NewLeadLog;

public interface SaveNewLeadLogOperation {
    NewLeadLog execute(NewLeadLog newLeadLog);
}
