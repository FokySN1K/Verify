package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XsdStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class Xsd extends XsdLight {

    @JsonProperty("xsd_data")
    @Schema(description = "Содержимое XSD схемы в виде строки")
    private String xsdData;

    @JsonProperty("id")
    @Schema(description = "Внутренний идентификатор XSD схемы")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Название XSD схемы")
    private String name;

    @JsonProperty("stage")
    @Schema(description = "Этап (стадия), к которому относится схема")
    private String stage;

    @JsonProperty("begin_date")
    @Schema(description = "Дата начала действия схемы")
    private LocalDate beginDate;

    @JsonProperty("end_date")
    @Schema(description = "Дата окончания действия схемы")
    private LocalDate endDate;

    @JsonProperty("link")
    @Schema(description = "Ссылка на схему (например, URL или путь к файлу)")
    private String link;

    @JsonProperty("status")
    @Schema(description = "Статус XSD схемы")
    private XsdStatus status;

    @JsonProperty("version")
    @Schema(description = "Версия XSD схемы")
    private Long version;
}