package com.example.verify_backend.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XmlDocument {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("contract_id")
    private Long contractId;

    @JsonProperty("xsd_id")
    private Long xsdId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("version")
    private Long version;

    @JsonProperty("data")
    private String data;

    @JsonProperty("reason")
    private String reason;
}
