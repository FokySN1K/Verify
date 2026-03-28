package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

// TODO добавить ограничение по размеру
@Getter
@Setter
@Accessors(chain = true)
public class ValidateWithXsdDataRequest {

    @NotBlank(message = "xml_data должен быть не пустым")
    @JsonProperty("xml_data")
    private String xmlData;

    @NotBlank(message = "xsd_data должен быть не пустым")
    @JsonProperty("xsd_data")
    private String xsdData;

}
