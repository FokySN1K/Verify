package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Внутренний идентификатор контракта")
    private Long id;

    @JsonProperty("contractor")
    @Schema(description = "Клиент-исполнитель по контракту")
    private Client contractor;

    @JsonProperty("customer")
    @Schema(description = "Клиент-заказчик по контракту")
    private Client customer;

    @JsonProperty("name")
    @Schema(description = "Название контракта")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Описание контракта")
    private String description;

    @JsonProperty("reason")
    @Schema(description = "Причина (например, отказа или изменения статуса)")
    private String reason;

    @JsonProperty("status")
    @Schema(description = "Статус контракта")
    private ContractStatus status;

}