package com.vladislav.crm.report.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "moveLeadLogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveLeadLog {

    @MongoId
    private UUID id = UUID.randomUUID();

    private String userId;
    private String leadId;
    private String prevStatusId;
    private String nextStatusId;

}
