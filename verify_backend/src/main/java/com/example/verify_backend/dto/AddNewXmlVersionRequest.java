package com.example.verify_backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AddNewXmlVersionRequest {

    private String xmlName;

    private Long xsdId;

    private Long contractId;

    private String clientId;

    private String xmlData;

    private String reason;
}
