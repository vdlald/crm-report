package com.vladislav.crm.report.repositories;

import com.vladislav.crm.report.documents.NewLeadLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewLeadLogRepository extends MongoRepository<NewLeadLog, UUID> {
}
