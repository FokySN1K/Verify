package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SetContractorRequest {

    @NotBlank
    @JsonProperty("customer_client_id")
    @Schema(description = "Внешний идентификатор клиента-заказчика", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerClientId;

    @NotNull
    @JsonProperty("contract_id")
    @Schema(description = "Внутренний идентификатор заказа", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contractId;

    @NotBlank
    @JsonProperty("contractor_client_id")
    @Schema(description = "Внешний идентификатор клиента-исполнителя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractorClientId;
}