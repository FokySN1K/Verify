package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetContractsRequest {

    @JsonProperty("client_id")
    @NotNull
    @Schema(description = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String client_id;
}