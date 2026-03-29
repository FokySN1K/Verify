package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contractor.AddNewXmlListService;
import com.example.verify_backend.Service.Contractor.AddNewXmlVersionService;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.contractor.AddNewXmlListRequest;
import com.example.verify_backend.dto.contractor.AddNewXmlVersionRequest;
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
    public final static String ADD_NEW_XML_LIST = BASE_PREFIX + "/order/add_new_xml_list";
    public final static String ADD_NEW_XML_VERSION = BASE_PREFIX + "/order/xml/add_new_version";

    private final AddNewXmlListService addNewXmlListService;
    private final AddNewXmlVersionService addNewXmlVersionService;

    @PostMapping(ADD_NEW_XML_LIST)
    public ResponseEntity<Result> addNewXmlList(@NotNull @Validated @RequestBody AddNewXmlListRequest request) {
        return ResponseEntity.ok(addNewXmlListService.add(request));
    }

    @PostMapping(ADD_NEW_XML_VERSION)
    public ResponseEntity<Result> addNewXmlVersion(@NotNull @Validated @RequestBody AddNewXmlVersionRequest request) {
        return ResponseEntity.ok(addNewXmlVersionService.add(request));
    }
}
