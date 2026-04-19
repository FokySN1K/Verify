package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateContractRequest {

    @NotBlank
    @JsonProperty("client_id")
    @Schema(description = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientId;

    @NotBlank
    @JsonProperty("contract_name")
    @Schema(description = "Название контракта", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;

    @NotBlank
    @JsonProperty("contract_description")
    @Schema(description = "Описание контракта", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractDescription;
}