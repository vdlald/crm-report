package com.vladislav.crm.report.operations.impl;

import com.vladislav.crm.report.documents.MoveLeadLog;
import com.vladislav.crm.report.operations.GetAllUserIdFromMoveLeadLogsOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetAllUserIdFromMoveLeadLogsOperationImpl implements GetAllUserIdFromMoveLeadLogsOperation {

    private final MongoTemplate mongoTemplate;

    @Override
    public Set<Long> execute() {
        final HashSet<Long> result = new HashSet<>();
        final Query query = new Query();
        query.fields().include("userId");

        mongoTemplate.getCollection(mongoTemplate.getCollectionName(MoveLeadLog.class))
                .distinct("userId", Long.class)
                .forEach(result::add);

        return result;
    }
}
