package com.example.verify_backend.Service.ContractXml;

import com.example.verify_backend.Entity.XmlDocument;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.contractxml.ChangeXmlStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeXmlStatusService {

    private final ContractRepository contractRepository;
    private final XmlRepository xmlRepository;

    public Result change(ChangeXmlStatusRequest request) {
        xmlRepository.getAccessibleXml(request.getClientId(), request.getOrderId(), request.getXmlId())
                .orElseThrow(() -> new IllegalArgumentException("XML не найден или недоступен клиенту"));

        if (contractRepository.getContractStatus(request.getOrderId()) != ContractStatus.PROCESSING) {
            throw new IllegalArgumentException("Заказ должен быть в статусе PROCESSING");
        }

        XmlStatus newStatus;
        try {
            newStatus = XmlStatus.valueOf(request.getNewStatus());
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный статус XML");
        }

        if (newStatus == XmlStatus.REFUSED && (request.getReason() == null || request.getReason().isBlank())) {
            throw new IllegalArgumentException("Для статуса REFUSED требуется reason");
        }

        xmlRepository.updateXmlStatus(request.getXmlId(), newStatus,
                newStatus == XmlStatus.REFUSED ? request.getReason() : null);

        return new Result();
    }
}
