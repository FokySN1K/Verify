package com.example.verify_backend.Controller;

import com.example.verify_backend.Entity.XmlEntity;
import com.example.verify_backend.Service.Contract.GetContractsService;
import com.example.verify_backend.dto.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContractController {

    public final static String BASE_PREFIX = "/contract";

    public final static String GET_CONTRACTS = BASE_PREFIX + "/get_contracts";

    public final static String GET_XML = BASE_PREFIX + "/get_xml";

    public final static String GET_XML_INFO = BASE_PREFIX + "/get_xml_info";

    private final GetContractsService getContractsService;

    @PostMapping(GET_CONTRACTS)
    public ResponseEntity<GetContractsResponse> getContracts(@NotNull @Validated @RequestBody GetContractsRequest request) {
        return ResponseEntity.ok(getContractsService.getContracts(request));
    }

    @PostMapping(GET_XML)
    public ResponseEntity<GetItemsResponse<XmlEntity>> getContractXml(@RequestBody @Validated GetContractXmlRequest request) {
        return ResponseEntity.ok(getContractsService.getContractXml(request));
    }
    
    @PostMapping(GET_XML_INFO)
    public ResponseEntity<XmlEntity> getXmlInfo(@RequestBody @Validated GetContractXmlInfoRequest request) {
        return ResponseEntity.ok(getContractsService.getXmlInfo(request));
    }

}
