package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contract {

    @JsonProperty("contractor")
    private Client contractor;

    @JsonProperty("customer")
    private Client customer;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private ContractStatus status;

}
