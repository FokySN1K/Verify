package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"code", "message"})
@Accessors(chain = true)
public class Result {

    @NotNull
    @JsonProperty("code")
    @Schema(description = "Код результата (0 — успех, иначе — код ошибки)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer code = 0;

    @NotNull
    @JsonProperty("message")
    @Schema(description = "Сообщение, описывающее результат операции", requiredMode = Schema.RequiredMode.REQUIRED, example = "OK")
    private String message = "";
}