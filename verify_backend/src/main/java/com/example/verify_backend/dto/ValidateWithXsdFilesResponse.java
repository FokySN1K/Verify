package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ValidateWithXsdFilesResponse extends Result{

    @JsonProperty("exception_list")
    private List<String> exceptionList;

}
