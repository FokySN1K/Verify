package com.example.verify_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetContractXmlRequest(
        @NotBlank
        @Schema(name = "client_id")
        String clientId,

        @Positive
        @NonNull
        @Schema(name = "order_id")
        Long orderId
) {
}
