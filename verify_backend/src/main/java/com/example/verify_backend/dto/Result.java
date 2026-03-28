package com.example.verify_backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    @JsonProperty("code")
    private Integer code = 0;

    @JsonProperty("message")
    private String message = "";

}
