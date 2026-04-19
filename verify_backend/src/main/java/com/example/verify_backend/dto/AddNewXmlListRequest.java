package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotNull
    @JsonProperty("contract_id")
    private Long contractId;

    @NotNull
    @JsonProperty("new_xml_data_list")
    private List<NewXmlData> newXmlDataList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class NewXmlData {
        @NotBlank
        @JsonProperty("xml_name")
        private String xmlName;

        @NotNull
        @JsonProperty("xsd_id")
        private Long xsdId;
    }
}
