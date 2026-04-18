package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.CreateContractRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateContractService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;

    public Result createContract(CreateContractRequest request) {

        Client client = clientRepository.getClient(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Клиента не существует"));


        if (!ClientRole.CUSTOMER.name().equals(client.getRole().name())) {
            throw new RuntimeException("Создавать заказ может только заказчик. Создайте аккаунт с другой ролью");
        }

        Contract contract = new Contract()
                .setName(request.getContractName())
                .setDescription(request.getContractDescription())
                .setCustomer(new Client().setClientId(request.getClientId()));

        contractRepository.createContract(contract);

        return new Result();
    }


}
