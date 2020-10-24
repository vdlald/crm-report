package com.vladislav.crm.report.configurations;

import com.vladislav.crm.report.documents.MoveLeadLog;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;

import javax.annotation.PostConstruct;

//@Configuration
@DependsOn("mongoTemplate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoIndexesConfiguration {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps(MoveLeadLog.class)
                .ensureIndex(
                        new CompoundIndexDefinition(
                                new Document()
                                        .append("userId", 1)
                                        .append("leadId", 1)
                        ).unique()
                );
    }
}
