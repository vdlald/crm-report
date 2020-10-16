package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.SaveMoveLeadLogOperation;
import com.vladislav.crm.report.repositories.MoveLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaveMoveLeadLogOperationImpl implements SaveMoveLeadLogOperation {

    private final MoveLeadLogRepository repository;

    @Override
    public MoveLeadLog execute(MoveLeadLog log) {
        return repository.save(log);
    }
}
