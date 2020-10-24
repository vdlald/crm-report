package com.vladislav.crm.report.documents;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseLogDocument {

    @MongoId
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

}
