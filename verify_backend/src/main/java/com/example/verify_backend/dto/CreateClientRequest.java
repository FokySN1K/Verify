package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.ClientRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateClientRequest {

    @JsonProperty("client_id")
    @Schema(description = "Идентификатор клиента на фронте")
    private String clientId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("role")
    private ClientRole role;

    @JsonProperty("email")
    @Email
    private String email;
}
