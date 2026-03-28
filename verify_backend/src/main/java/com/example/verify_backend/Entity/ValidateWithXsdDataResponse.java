package com.example.verify_backend.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ValidateWithXsdDataResponse extends Result{

    @JsonProperty("exception_list")
    private List<String> exceptionList;

}
