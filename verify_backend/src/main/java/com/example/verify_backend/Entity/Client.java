package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.ClientRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client {

    @JsonProperty("id")
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("role")
    private ClientRole role;

    @JsonProperty("email")
    private String email;

}
