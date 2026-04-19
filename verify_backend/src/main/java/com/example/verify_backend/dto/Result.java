package com.example.verify_backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"code", "message"})
@Accessors(chain = true)
public class Result {

    @NotNull
    @JsonProperty("code")
    private Integer code = 0;

    @NotNull
    @JsonProperty("message")
    private String message = "";

}
