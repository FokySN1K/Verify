package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ChangeContractStatusRequest {

    @JsonProperty("contract_status")
    @Schema(description = "Новый статус контракта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ContractStatus contractStatus;

    @NotBlank
    @JsonProperty("client_id")
    @Schema(description = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientId;

    @NotNull
    @JsonProperty("contract_id")
    @Schema(description = "Внутренний идентификатор заказа", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contractId;
}