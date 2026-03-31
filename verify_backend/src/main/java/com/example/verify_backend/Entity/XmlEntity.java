package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XmlStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.OffsetDateTime;

/**
 * Xml Entity Назван так, чтобы не пересекаться по названию с Xml.
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XmlEntity {

    private Long id;

    private String name;

    private Contract contract;

    private Long xsdId;

    private XmlStatus status;

    private Long version;

    private String reason;

    private OffsetDateTime createdTs;

    private String data;

}
