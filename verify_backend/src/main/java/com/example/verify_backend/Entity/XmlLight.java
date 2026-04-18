package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XmlStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XmlLight {

    private Long id;

    private String name;

    @JsonProperty("contractor")
    private Client contractor;

    @JsonProperty("customer")
    private Client customer;

    private Contract contract;

    private XsdLight xsdLight;

    private XmlStatus status;

    private Long version;

    //private String xmlData;

    private String reason;

}
