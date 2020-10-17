package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.ReadAllMoveLeadLogByUserIdOperation;
import com.vladislav.crm.report.repositories.MoveLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReadAllMoveLeadLogByUserIdOperationImpl implements ReadAllMoveLeadLogByUserIdOperation {

    private final MoveLeadLogRepository repository;

    @Override
    public Collection<MoveLeadLog> execute(Long userId) {
        return repository.findAllByUserId(userId);
    }
}
