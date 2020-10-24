package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.NewLeadLog;
import com.vladislav.crm.report.operations.GetAllNewLeadLogOperation;
import com.vladislav.crm.report.repositories.NewLeadLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetAllNewLeadLogOperationImpl implements GetAllNewLeadLogOperation {

    private final NewLeadLogRepository newLeadLogRepository;

    @Override
    public Collection<NewLeadLog> execute(LocalDateTime happenedAt, LocalDateTime happenedAt2) {
        return newLeadLogRepository.findAllByHappenedAtBetween(happenedAt, happenedAt2);
    }
}
