package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AddNewXmlVersionRequest {

    @NotBlank
    @JsonProperty("xml_name")
    private String xmlName;

    @NotNull
    @JsonProperty("xsd_id")
    private Long xsdId;

    @NotNull
    @JsonProperty("contract_id")
    private Long contractId;

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotBlank
    @JsonProperty("xml_data")
    private String xmlData;

    @Nullable
    @JsonProperty("reason")
    private String reason;

}
