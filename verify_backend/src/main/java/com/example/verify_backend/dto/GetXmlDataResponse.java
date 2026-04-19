package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class GetXmlDataResponse {

    @NotBlank
    @JsonProperty("xml_data")
    @Schema(description = "Содержимое XML документа в виде строки", requiredMode = Schema.RequiredMode.REQUIRED)
    private String xmlData;
}