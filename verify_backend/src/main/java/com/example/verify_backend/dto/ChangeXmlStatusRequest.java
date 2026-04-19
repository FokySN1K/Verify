package com.example.verify_backend.dto;

import com.example.verify_backend.Enums.XmlStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ChangeXmlStatusRequest {

    private String clientId;

    private XmlStatus xmlStatus;

    private String xmlName;

    private Long xsdId;

    private Long contractId;

    private String reason;

}
