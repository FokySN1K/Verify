package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetContractsService {

    private final ContractRepository contractRepository;

    public GetContractsResponse getContracts(GetContractsRequest request) {

        log.info("[getContracts] Operation started");
        GetContractsResponse ent = new GetContractsResponse()
                .setContractList(contractRepository.getContracts(request.getClient_id()));
        log.info("[getContracts] Operation finished");
        return ent;

    }

}
