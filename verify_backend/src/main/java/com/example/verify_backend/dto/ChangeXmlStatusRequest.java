package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.XmlStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ChangeXmlStatusRequest {

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotNull
    @JsonProperty("xml_status")
    private XmlStatus xmlStatus;

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
    @JsonProperty("reason")
    private String reason;
}