package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XsdStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@Setter
@Getter
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XsdEntity {

    private Long id;

    private String name;

    private String stage;

    private XsdStatus status;

    private Long version;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String link;

    private String data;

}