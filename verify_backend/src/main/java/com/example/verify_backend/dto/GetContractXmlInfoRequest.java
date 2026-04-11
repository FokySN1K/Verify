package com.example.verify_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetContractXmlInfoRequest(
        @NotBlank
        @Schema(name = "client_id")
        String clientId,

        @Positive
        @Schema(name = "order_id")
        @NonNull
        Long orderId,

        @Positive
        @Schema(name = "xml_id")
        @NonNull
        Long xmlId
) {
}
