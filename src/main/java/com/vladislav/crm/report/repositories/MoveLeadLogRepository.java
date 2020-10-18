package com.vladislav.crm.report.repositories;

import com.vladislav.crm.report.documents.MoveLeadLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface MoveLeadLogRepository extends MongoRepository<MoveLeadLog, UUID> {

    @Query(value = "{ 'happenedAt' : { $gte: ?0, $lte: ?1 } }", sort = "{ 'happenedAt': 1 }")
    Collection<MoveLeadLog> findAllByHappenedAtBetween(LocalDateTime happenedAt, LocalDateTime happenedAt2);
}
