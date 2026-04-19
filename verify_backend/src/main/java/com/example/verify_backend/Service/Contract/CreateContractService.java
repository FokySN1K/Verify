package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.CreateContractRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateContractService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;

    public Result createContract(CreateContractRequest request) {

        log.info("[createContract] Operation started");
        Client client = clientRepository.getClient(request.getClientId())
                .orElseThrow(() -> new BusinessLogicException("Клиента не существует"));


        if (!ClientRole.CUSTOMER.name().equals(client.getRole().name())) {
            throw new BusinessLogicException("Создавать заказ может только заказчик. Создайте аккаунт с другой ролью");
        }

        Contract contract = new Contract()
                .setName(request.getContractName())
                .setDescription(request.getContractDescription())
                .setCustomer(new Client().setClientId(request.getClientId()));

        contractRepository.createContract(contract);

        log.info("[createContract] Operation finished");
        return new Result();
    }


}
