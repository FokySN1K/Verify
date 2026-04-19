package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XsdStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XsdLight {

    private Long id;
    private String name;
    private String stage;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String link;
    private XsdStatus status;
    private Long version;

}
