package com.example.verify_backend.dto;

import com.example.verify_backend.Entity.XsdLight;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GetXsdLightListResponse {
    @NotNull
    @JsonProperty("xsd_light_list")
    private List<XsdLight> xsdLightList;
}
