package com.example.verify_backend.Entity;

import com.example.verify_backend.Enums.XmlStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class XmlLight {

    private String name;

    private Client contractor;

    private XsdLight xsdLight;

    private XmlStatus status;

    private Long version;

    //private String xmlData;

    private String reason;

}
