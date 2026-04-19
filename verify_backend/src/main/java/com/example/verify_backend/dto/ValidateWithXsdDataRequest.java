package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ValidateWithXsdDataRequest {

    @NotBlank(message = "xml_data должен быть не пустым")
    @JsonProperty("xml_data")
    @Schema(description = "Содержимое XML документа для валидации", requiredMode = Schema.RequiredMode.REQUIRED)
    private String xmlData;

    @NotBlank(message = "xsd_data должен быть не пустым")
    @JsonProperty("xsd_data")
    @Schema(description = "Содержимое XSD схемы для валидации", requiredMode = Schema.RequiredMode.REQUIRED)
    private String xsdData;
}