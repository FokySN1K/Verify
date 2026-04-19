package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.ChangeContractStatusRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeContractStatusService {

    private final ContractRepository contractRepository;

    public Result changeContractStatus(ChangeContractStatusRequest request) {
        log.info("[changeContractStatus] Operation started");
        Contract contract = contractRepository.getContractByContractId(request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Не найден заказ по клиенту"));

        if (!request.getClientId().equals(contract.getCustomer().getClientId())) {
            throw new BusinessLogicException("Менять статус заказа может только заказчик");
        }

        if (!(ContractStatus.PROCESSING.name().equals(contract.getStatus().name())
                || ContractStatus.NEW.name().equals(contract.getStatus().name()))) {
            throw new BusinessLogicException("Менять статус заказа можно только если текущий статус 'NEW' или 'PROCESSING'");
        }

        if (!(ContractStatus.DONE.name().equals(request.getContractStatus().name())
                || ContractStatus.FAILED.name().equals(request.getContractStatus().name()))) {
            throw new BusinessLogicException("Менять статус можно только на статус 'DONE' или 'FAILED'");
        }

        contractRepository.changeContractStatus(request.getContractId(), request.getContractStatus());

        log.info("[changeContractStatus] Operation finished");
        return new Result();

    }
}
