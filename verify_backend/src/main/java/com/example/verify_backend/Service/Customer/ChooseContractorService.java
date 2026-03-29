package com.example.verify_backend.Service.Customer;

import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.TenderRepository;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.customer.ChooseContractorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChooseContractorService {

    private final ContractRepository contractRepository;
    private final TenderRepository tenderRepository;

    public Result choose(ChooseContractorRequest request) {
        if (!contractRepository.existsContractForCustomer(request.getCustomerId(), request.getOrderId())) {
            throw new IllegalArgumentException("Заказ не принадлежит customer");
        }

        if (contractRepository.getContractStatus(request.getOrderId()) != ContractStatus.NEW) {
            throw new IllegalArgumentException("Заказ должен быть в статусе NEW");
        }

        if (!tenderRepository.existsTenderForContractAndContractor(request.getOrderId(), request.getContractorId())) {
            throw new IllegalArgumentException("Подрядчик не найден в тендере по заказу");
        }

        contractRepository.chooseContractor(request.getOrderId(), request.getContractorId());

        return new Result();
    }
}
