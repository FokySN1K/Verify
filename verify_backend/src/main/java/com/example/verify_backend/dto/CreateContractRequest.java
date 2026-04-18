package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateContractRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("contract_name")
    private String contractName;

    @JsonProperty("contract_description")
    private String contractDescription;

}
