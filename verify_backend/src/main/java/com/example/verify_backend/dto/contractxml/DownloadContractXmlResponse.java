package com.example.verify_backend.dto.contractxml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DownloadContractXmlResponse {

    @JsonProperty("xml_id")
    private Long xmlId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("version")
    private Long version;

    @JsonProperty("xsd_id")
    private Long xsdId;

    @JsonProperty("data")
    private String data;
}
