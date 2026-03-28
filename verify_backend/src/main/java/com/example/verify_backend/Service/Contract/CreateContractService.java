package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.CreateContractRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateContractService {

    private final ContractRepository contractRepository;

    public Result createContract(CreateContractRequest request) {

        Contract contract = new Contract()
                .setName(request.getOrderName())
                .setDescription(request.getOrderDescription())
                .setCustomer(new Client().setClientId(request.getClientId()));

        contractRepository.createContract(contract);

        return new Result();
    }


}
