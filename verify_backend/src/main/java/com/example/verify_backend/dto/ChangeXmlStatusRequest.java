package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.XmlStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ChangeXmlStatusRequest {

    @NotBlank
    @JsonProperty("client_id")
    @Schema(description = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientId;

    @NotNull
    @JsonProperty("xml_status")
    @Schema(description = "Новый статус XML документа", requiredMode = Schema.RequiredMode.REQUIRED)
    private XmlStatus xmlStatus;

    @NotBlank
    @JsonProperty("xml_name")
    @Schema(description = "Название XML документа", requiredMode = Schema.RequiredMode.REQUIRED)
    private String xmlName;

    @NotNull
    @JsonProperty("xsd_id")
    @Schema(description = "Идентификатор xsd схемы", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long xsdId;

    @NotNull
    @JsonProperty("contract_id")
    @Schema(description = "Внутренний идентификатор заказа", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contractId;

    @Nullable
    @JsonProperty("reason")
    @Schema(description = "Причина изменения статуса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reason;
}