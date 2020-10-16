package com.vladislav.crm.report.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(collection = "moveLeadLogs")
public class MoveLeadLog {

    @MongoId
    private UUID id = UUID.randomUUID();

    private Long userId;
    private Long leadId;
    private Long prevStatusId;
    private Long nextStatusId;

}
