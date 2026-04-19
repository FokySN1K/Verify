package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Client.SetContractorService;
import com.example.verify_backend.Service.Contract.CreateContractService;
import com.example.verify_backend.dto.CreateContractRequest;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.SetContractorRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public final static String BASE_PREFIX = "/customer";

    public final static String CREATE_CONTRACT = BASE_PREFIX + "/create_contract";
    public final static String SET_CONTRACTOR = BASE_PREFIX + "/set_contractor";

    private final CreateContractService createContractService;
    private final SetContractorService setContractorService;

    @PostMapping(CREATE_CONTRACT)
    public ResponseEntity<Result> createContract(@NotNull @Validated @RequestBody CreateContractRequest request) {
        return ResponseEntity.ok(createContractService.createContract(request));
    }

    @PostMapping(SET_CONTRACTOR)
    public ResponseEntity<Result> setContractor(@NotNull @Validated @RequestBody SetContractorRequest request) {
        return ResponseEntity.ok(setContractorService.setContractorService(request));
    }



}
