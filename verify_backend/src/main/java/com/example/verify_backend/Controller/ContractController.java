package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.GetContractsService;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
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

    private final GetContractsService getContractsService;

    @PostMapping(GET_CONTRACTS)
    public ResponseEntity<GetContractsResponse> getContracts(@NotNull @Validated @RequestBody GetContractsRequest request) {
        return ResponseEntity.ok(getContractsService.getContracts(request));
    }


}
