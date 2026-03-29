package com.example.verify_backend.Service.Contractor;

import com.example.verify_backend.Entity.XmlDocument;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.dto.contractor.AddNewXmlVersionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddNewXmlVersionService {

    private final ContractRepository contractRepository;
    private final XmlRepository xmlRepository;

    public Result add(AddNewXmlVersionRequest request) {
        if (!contractRepository.existsContractForContractor(request.getClientId(), request.getOrderId())) {
            throw new IllegalArgumentException("Заказ не принадлежит contractor");
        }

        if (contractRepository.getContractStatus(request.getOrderId()) != ContractStatus.PROCESSING) {
            throw new IllegalArgumentException("Заказ должен быть в статусе PROCESSING");
        }

        XmlDocument currentXml = xmlRepository.getContractorOwnedXml(request.getClientId(), request.getOrderId(), request.getXmlId())
                .orElseThrow(() -> new IllegalArgumentException("XML не найден в заказе"));

        if (!xmlRepository.existsProcessingXsd(currentXml.getXsdId())) {
            throw new IllegalArgumentException("XSD для XML должен быть в статусе PROCESSING");
        }

        Long nextVersion = xmlRepository.nextVersion(request.getOrderId(), currentXml.getXsdId());
        xmlRepository.markOldVersions(request.getOrderId(), currentXml.getXsdId());
        xmlRepository.insertNewVersion(currentXml.getName(), request.getOrderId(), currentXml.getXsdId(), nextVersion, request.getData());

        return new Result();
    }
}
