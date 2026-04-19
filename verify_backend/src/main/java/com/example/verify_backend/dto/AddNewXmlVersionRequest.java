package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AddNewXmlVersionRequest {

    @NotBlank
    @JsonProperty("xml_name")
    @Schema(description = "Название xml документа", requiredMode = Schema.RequiredMode.REQUIRED)
    private String xmlName;

    @NotNull
    @JsonProperty("xsd_id")
    @Schema(description = "Идентификатор xsd схемы для валидации", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long xsdId;

    @NotNull
    @JsonProperty("contract_id")
    @Schema(description = "Внутренний идентификатор заказа", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contractId;

    @NotBlank
    @JsonProperty("client_id")
    @Schema(description = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientId;

    @NotBlank
    @JsonProperty("xml_data")
    @Schema(description = "Содержимое xml документа в виде строки", requiredMode = Schema.RequiredMode.REQUIRED)
    private String xmlData;

    @Nullable
    @JsonProperty("reason")
    @Schema(description = "Причина добавления новой версии (опционально)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reason;
}