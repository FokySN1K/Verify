package com.example.verify_backend.dto;

import com.example.verify_backend.Entity.Contract;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetContractsResponse extends Result {

    @JsonProperty("contract_list")
    private List<Contract> contractList;

}
