package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XmlStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XmlLight {

    @JsonProperty("id")
    @Schema(description = "Внутренний идентификатор XML документа")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Название XML документа")
    private String name;

    @JsonProperty("contractor")
    @Schema(description = "Клиент-исполнитель")
    private Client contractor;

    @JsonProperty("customer")
    @Schema(description = "Клиент-заказчик")
    private Client customer;

    @JsonProperty("contract")
    @Schema(description = "Контракт, к которому привязан XML документ")
    private Contract contract;

    @JsonProperty("xsd_light")
    @Schema(description = "Краткая информация о XSD схеме, используемой для валидации")
    private XsdLight xsdLight;

    @JsonProperty("status")
    @Schema(description = "Статус XML документа")
    private XmlStatus status;

    @JsonProperty("version")
    @Schema(description = "Версия XML документа")
    private Long version;

    @JsonProperty("reason")
    @Schema(description = "Причина последнего изменения статуса")
    private String reason;

}