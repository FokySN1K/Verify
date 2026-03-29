package com.example.verify_backend.dto.contractxml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DownloadContractXmlRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("xml_id")
    private Long xmlId;
}
