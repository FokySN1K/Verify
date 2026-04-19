package com.example.verify_backend.dto;

import com.example.verify_backend.Entity.XmlLight;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class GetXmlLightInfoListResponse {

    @NotEmpty
    @JsonProperty("xml_light_list")
    @Schema(description = "Список объектов XmlLight с краткой информацией", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<XmlLight> xmlLightList;
}