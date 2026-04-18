package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.GetContractsService;
import com.example.verify_backend.Service.Xml.AddNewXmlListService;
import com.example.verify_backend.dto.AddNewXmlListRequest;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
import com.example.verify_backend.dto.Result;
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
    public final static String ADD_NEW_XML_LIST = BASE_PREFIX + "/add_new_xml_list";

    private final GetContractsService getContractsService;
    private final AddNewXmlListService addNewXmlListService;

    @PostMapping(GET_CONTRACTS)
    public ResponseEntity<GetContractsResponse> getContracts(@NotNull @Validated @RequestBody GetContractsRequest request) {
        return ResponseEntity.ok(getContractsService.getContracts(request));
    }

    @PostMapping(ADD_NEW_XML_LIST)
    public ResponseEntity<Result> addNewXmlList(@NotNull @Validated @RequestBody AddNewXmlListRequest request) {
        return ResponseEntity.ok(addNewXmlListService.addNewXmlListService(request));
    }



}
