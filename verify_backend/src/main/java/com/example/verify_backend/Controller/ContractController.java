package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.GetContractsService;
import com.example.verify_backend.Service.ContractXml.ChangeXmlStatusService;
import com.example.verify_backend.Service.ContractXml.DownloadContractXmlService;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.contractxml.ChangeXmlStatusRequest;
import com.example.verify_backend.dto.contractxml.DownloadContractXmlRequest;
import com.example.verify_backend.dto.contractxml.DownloadContractXmlResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContractController {

    public final static String BASE_PREFIX = "/contract";

    public final static String GET_CONTRACTS = BASE_PREFIX + "/get_contracts";

    private final static String CONTRACT_XML_SECTION = BASE_PREFIX + "/xml";

    public final static String CONTRACT_DOWNLOAD_XML = CONTRACT_XML_SECTION + "/download";
    public final static String CONTRACT_CHANGE_XML_STATUS = CONTRACT_XML_SECTION + "/change_xml_status";

    private final GetContractsService getContractsService;
    private final DownloadContractXmlService downloadContractXmlService;
    private final ChangeXmlStatusService changeXmlStatusService;

    @PostMapping(GET_CONTRACTS)
    public ResponseEntity<GetContractsResponse> getContracts(@NotNull @Validated @RequestBody GetContractsRequest request) {
        return ResponseEntity.ok(getContractsService.getContracts(request));
    }

    @PostMapping(CONTRACT_DOWNLOAD_XML)
    public ResponseEntity<DownloadContractXmlResponse> downloadContractXML(@NotNull @Validated @RequestBody DownloadContractXmlRequest request) {
        return ResponseEntity.ok(downloadContractXmlService.download(request));
    }

    @PostMapping(CONTRACT_CHANGE_XML_STATUS)
    public ResponseEntity<Result> changeXmlStatus(@NotNull @Validated @RequestBody ChangeXmlStatusRequest request) {
        return ResponseEntity.ok(changeXmlStatusService.change(request));
    }
}
