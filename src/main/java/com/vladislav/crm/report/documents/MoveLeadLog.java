package com.vladislav.crm.report.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "moveLeadLogs")
public class MoveLeadLog {

    @MongoId
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    private Long userId;
    private Long leadId;
    private Long prevStatusId;
    private Long nextStatusId;

    private LocalDateTime happenedAt = LocalDateTime.now();

}
