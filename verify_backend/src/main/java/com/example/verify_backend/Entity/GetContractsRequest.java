package com.example.verify_backend.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetContractsRequest {

    @JsonProperty("front_id")
    @NotNull
    private Long front_id;

}
