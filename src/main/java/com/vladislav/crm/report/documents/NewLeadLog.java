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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "newLeadLogs")
public class NewLeadLog {

    private Long userId;
    private Long leadId;

    private LocalDateTime happenedAt = LocalDateTime.now();

}
