package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GetXmlDataRequest {

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotNull
    @JsonProperty("xml_id")
    private Long xmlId;

}
