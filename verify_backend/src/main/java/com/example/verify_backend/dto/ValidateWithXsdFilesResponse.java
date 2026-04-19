package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ValidateWithXsdFilesResponse extends Result {

    @NotNull
    @JsonProperty("exception_list")
    @Schema(description = "Список ошибок валидации XML против XSD (может быть пустым при успешной валидации)", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> exceptionList;
}