package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ValidateWithXsdFilesResponse extends Result{

    @NotNull
    @JsonProperty("exception_list")
    private List<String> exceptionList;

}
