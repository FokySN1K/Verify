package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetContractsResponse extends Result {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private ContractStatus status;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("customer")
    private Client customer;

    @JsonProperty("contactor")
    private Client contractor;

    @Getter
    @Setter
    @Accessors(chain = true)
    private static class Client {
        @JsonProperty("name")
        private String name;
        @JsonProperty("email")
        private String email;
    }

}
