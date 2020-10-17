package com.vladislav.crm.report.repositories;

import com.vladislav.crm.report.documents.MoveLeadLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface MoveLeadLogRepository extends MongoRepository<MoveLeadLog, UUID> {

    Collection<MoveLeadLog> findAllByUserId(Long userId);
}
