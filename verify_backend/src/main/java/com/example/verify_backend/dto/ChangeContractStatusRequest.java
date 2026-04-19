package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ChangeContractStatusRequest {

    @JsonProperty("contract_status")
    private ContractStatus contractStatus;

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotNull
    @JsonProperty("contract_id")
    private Long contractId;

}
