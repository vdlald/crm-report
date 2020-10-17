package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.GetAllMoveLeadLogByUserIdOperation;
import com.vladislav.crm.report.repositories.MoveLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetAllMoveLeadLogByUserIdOperationImpl
        implements GetAllMoveLeadLogByUserIdOperation {

    private final MoveLeadLogRepository repository;

    @Override
    public Collection<MoveLeadLog> execute(
            Long userId, LocalDateTime happenedAt, LocalDateTime happenedAt2
    ) {
        return repository.findAllByUserIdAndHappenedAtIsBetween(userId, happenedAt, happenedAt2);
    }
}
