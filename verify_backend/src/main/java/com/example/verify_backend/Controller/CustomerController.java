package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.CreateContractService;
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
public class CustomerController {

    public final static String BASE_PREFIX = "/customer";

    public final static String CREATE_CONTRACT = BASE_PREFIX + "/create_contract";

    private final CreateContractService createContractService;

    @PostMapping(CREATE_CONTRACT)
    public ResponseEntity<Result> createContract(@NotNull @Validated @RequestBody CreateContractRequest request) {
        return ResponseEntity.ok(createContractService.createContract(request));
    }

}
