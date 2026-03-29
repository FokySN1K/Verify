package com.example.verify_backend.Service.Contractor;

import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.contractor.AddNewXmlListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddNewXmlListService {

    private final ContractRepository contractRepository;
    private final XmlRepository xmlRepository;

    public Result add(AddNewXmlListRequest request) {
        if (!contractRepository.existsContractForContractor(request.getClientId(), request.getOrderId())) {
            throw new IllegalArgumentException("Заказ не принадлежит contractor");
        }

        if (contractRepository.getContractStatus(request.getOrderId()) != ContractStatus.PROCESSING) {
            throw new IllegalArgumentException("Заказ должен быть в статусе PROCESSING");
        }

        xmlRepository.addNewXmlListForContract(request.getOrderId());
        return new Result();
    }
}
