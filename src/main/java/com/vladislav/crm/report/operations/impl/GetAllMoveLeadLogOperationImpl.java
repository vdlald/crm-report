package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.GetAllMoveLeadLogOperation;
import com.vladislav.crm.report.repositories.MoveLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetAllMoveLeadLogOperationImpl implements GetAllMoveLeadLogOperation {

    private final MoveLeadLogRepository moveLeadLogRepository;

    @Override
    public Collection<MoveLeadLog> execute(LocalDateTime happenedAt, LocalDateTime happenedAt2) {
        return moveLeadLogRepository.findAllByHappenedAtBetween(happenedAt, happenedAt2);
    }
}
