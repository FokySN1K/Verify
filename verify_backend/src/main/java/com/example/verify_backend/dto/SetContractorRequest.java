package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SetContractorRequest {

    @JsonProperty("customer_client_id")
    private String customerClientId;

    @JsonProperty("contract_id")
    private Long contractId;

    @JsonProperty("contractor_client_id")
    private String contractorClientId;

}
