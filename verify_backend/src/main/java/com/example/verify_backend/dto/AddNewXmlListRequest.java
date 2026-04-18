package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AddNewXmlListRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("contract_id")
    private Long contractId;

    @JsonProperty("new_xml_data_list")
    private List<NewXmlData> newXmlDataList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class NewXmlData {
        @JsonProperty("xml_name")
        private String xmlName;

        @JsonProperty("xsd_id")
        private Long xsdId;
    }
}
