package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.CreateContractService;
import com.example.verify_backend.Service.Customer.ChooseContractorService;
import com.example.verify_backend.dto.CreateContractRequest;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.customer.ChooseContractorRequest;
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
    public final static String CHOOSE_CONTRACTOR = BASE_PREFIX + "/contract/choose_contractor";

    private final CreateContractService createContractService;
    private final ChooseContractorService chooseContractorService;

    @PostMapping(CREATE_CONTRACT)
    public ResponseEntity<Result> createContract(@NotNull @Validated @RequestBody CreateContractRequest request) {
        return ResponseEntity.ok(createContractService.createContract(request));
    }

    @PostMapping(CHOOSE_CONTRACTOR)
    public ResponseEntity<Result> chooseContractor(@NotNull @Validated @RequestBody ChooseContractorRequest request) {
        return ResponseEntity.ok(chooseContractorService.choose(request));
    }
}
