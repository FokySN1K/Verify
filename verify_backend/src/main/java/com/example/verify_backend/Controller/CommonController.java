package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Client.CreateClientService;
import com.example.verify_backend.dto.*;
import com.example.verify_backend.Service.Validation.ValidateWithXsdDataService;
import com.example.verify_backend.Service.Validation.ValidateWithXsdFilesService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CommonController {


    public final static String BASE_PREFIX = "";

    public final static String VALIDATE_WITH_XSD_DATA = BASE_PREFIX + "/validate_with_xsd_data";
    public final static String VALIDATE_WITH_XSD_FILES = BASE_PREFIX + "/validate_with_xsd_files";
    public final static String CREATE_CLIENT = BASE_PREFIX + "/create_client";

    private final ValidateWithXsdDataService validateWithXsdDataService;
    private final ValidateWithXsdFilesService validateWithXsdFilesService;
    private final CreateClientService createClientService;

    @PostMapping(VALIDATE_WITH_XSD_DATA)
    public ResponseEntity<ValidateWithXsdDataResponse> validateWithXsdData(@NotNull @Validated @RequestBody ValidateWithXsdDataRequest request) {
        return ResponseEntity
                .ok(validateWithXsdDataService.validateWithXsdData(request));
    }

    @PostMapping(value = VALIDATE_WITH_XSD_FILES, consumes = "multipart/form-data")
    public ResponseEntity<ValidateWithXsdFilesResponse> validateWithXsdFiles(@RequestParam("xml") MultipartFile xml,
                                                                             @RequestParam("xsd") MultipartFile xsd) {
        return ResponseEntity
                .ok(validateWithXsdFilesService.validateWithXsdFiles(xml, xsd));
    }

    @PostMapping(CREATE_CLIENT)
    public ResponseEntity<Result> createClient(@NotNull @Validated @RequestBody CreateClientRequest request) {
        return ResponseEntity
                .ok(createClientService.createClient(request));
    }


}
