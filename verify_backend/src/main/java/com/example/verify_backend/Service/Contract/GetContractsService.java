package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetContractsService {

    private final ContractRepository contractRepository;

    public GetContractsResponse getContracts(GetContractsRequest request) {

        return new GetContractsResponse()
                .setContractList(contractRepository.getContracts(request.getClient_id()));

    }

}
