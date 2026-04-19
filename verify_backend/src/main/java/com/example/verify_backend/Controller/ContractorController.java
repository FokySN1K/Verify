package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.CreateContractService;
import com.example.verify_backend.Service.Xml.AddNewXmlListService;
import com.example.verify_backend.Service.Xml.AddNewXmlVersionService;
import com.example.verify_backend.dto.AddNewXmlListRequest;
import com.example.verify_backend.dto.AddNewXmlVersionRequest;
import com.example.verify_backend.dto.CreateContractRequest;
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
public class ContractorController {

    public final static String BASE_PREFIX = "/contractor";

    public final static String CONTRACT_PREFIX = BASE_PREFIX + "/contract";
    public final static String XML_PREFIX = CONTRACT_PREFIX + "/xml";

    public final static String ADD_NEW_XML_LIST = XML_PREFIX + "/add_new_xml_list";
    public final static String ADD_NEW_XML_VERSION = XML_PREFIX + "/add_new_xml_version";


    private final AddNewXmlListService addNewXmlListService;
    private final AddNewXmlVersionService addNewXmlVersionService;

    @PostMapping(ADD_NEW_XML_LIST)
    public ResponseEntity<Result> addNewXmlList(@NotNull @Validated @RequestBody AddNewXmlListRequest request) {
        return ResponseEntity.ok(addNewXmlListService.addNewXmlListService(request));
    }

    @PostMapping(ADD_NEW_XML_VERSION)
    public ResponseEntity<Result> addNewXmlVersion(@NotNull @Validated @RequestBody AddNewXmlVersionRequest request) {
        return ResponseEntity.ok(addNewXmlVersionService.addNewXmlVersion(request));
    }




}
