package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetContractsRequest {

    @JsonProperty("client_id")
    @NotNull
    private String client_id;

}
