package com.vladislav.crm.report.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(collection = "moveLeadLogs")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class MoveLeadLog extends BaseLogDocument {

    private Long userId;
    private Long leadId;
    private Long prevStatusId;
    private Long nextStatusId;

    private LocalDateTime happenedAt = LocalDateTime.now();

}
