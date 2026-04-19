package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.ClientRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CreateClientRequest {

    @NotBlank
    @JsonProperty("client_id")
    @Schema(description = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientId;

    @NotBlank
    @JsonProperty("name")
    @Schema(description = "Имя клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @JsonProperty("surname")
    @Schema(description = "Фамилия клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String surname;

    @JsonProperty("role")
    @NotNull
    @Schema(description = "Роль клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private ClientRole role;

    @JsonProperty("email")
    @Email
    @NotNull
    @Schema(description = "Электронная почта клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}