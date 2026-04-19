package com.example.verify_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description  = "Внешний идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientId;

    @NotNull
    @JsonProperty("contract_id")
    @Schema(description  = "Внутренний идентификатор заказа", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long contractId;

    @NotNull
    @JsonProperty("new_xml_data_list")
    @Schema(description  = "Список xml документов, переданных для инициализации", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NewXmlData> newXmlDataList;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class NewXmlData {
        @NotBlank
        @JsonProperty("xml_name")
        @Schema(description  = "Название xml документа", requiredMode = Schema.RequiredMode.REQUIRED,
                $comment = "Так как xml привязывается к xsd, то имя xml может быть равно имени xsd.")
        private String xmlName;

        @NotNull
        @JsonProperty("xsd_id")
        @Schema(description  = "Идентификатор xsd схемы для валидация", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long xsdId;
    }
}
