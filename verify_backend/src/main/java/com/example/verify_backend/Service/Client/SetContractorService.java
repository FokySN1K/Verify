package com.example.verify_backend.Service.Client;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.SetContractorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetContractorService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;

    public Result setContractorService(SetContractorRequest request) {

        Contract contract = contractRepository.getContractByContractId(request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Не найден заказ"));
        Client client = clientRepository.getClient(request.getContractorClientId())
                        .orElseThrow(() -> new RuntimeException("Не найден подрядчик"));

        if (!ContractStatus.NEW.name().equals(contract.getStatus().name())) {
            throw new BusinessLogicException("Устанавливать подрядчика можно только на заказ со статусом NEW");
        }

        if (!(contract.getCustomer().getClientId().equals(request.getCustomerClientId()))) {
            throw new BusinessLogicException("Заказ не принадлежит клиенту");
        }

        if (!(client.getRole().name().equals(ClientRole.CONTRACTOR.name()))) {
            throw new BusinessLogicException("Клиент с переданным customerClientId не является подрядчиком")    ;
        }


        contractRepository.setContractorForContract(request.getContractId(), request.getContractorClientId());

        return new Result();
    }


}
