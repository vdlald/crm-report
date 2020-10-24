package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.NewLeadLog;
import com.vladislav.crm.report.operations.SaveNewLeadLogOperation;
import com.vladislav.crm.report.repositories.NewLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaveNewLeadLogOperationImpl implements SaveNewLeadLogOperation {
    
    private final NewLeadLogRepository newLeadLogRepository;
    
    @Override
    public NewLeadLog execute(NewLeadLog newLeadLog) {
        return newLeadLogRepository.save(newLeadLog);
    }
}
