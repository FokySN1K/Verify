package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


// TODO Из-за неверной архитектуры пришлось вытягивать внутренние
// id-ки, что считается плохим подходом - фронт не должен видеть "внутренности" бэка,
// но пока что в угоду функциональности отсупим от этого, чтобы показать, как мы видим этот проект.
//
@Setter
@Getter
@Accessors(chain = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contract {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("contractor")
    private Client contractor;

    @JsonProperty("customer")
    private Client customer;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private ContractStatus status;

}
