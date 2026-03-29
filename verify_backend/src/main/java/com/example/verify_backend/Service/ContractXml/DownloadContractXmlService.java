package com.example.verify_backend.Service.ContractXml;

import com.example.verify_backend.Entity.XmlDocument;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.contractxml.DownloadContractXmlRequest;
import com.example.verify_backend.dto.contractxml.DownloadContractXmlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadContractXmlService {

    private final XmlRepository xmlRepository;

    public DownloadContractXmlResponse download(DownloadContractXmlRequest request) {
        XmlDocument xmlDocument = xmlRepository.getAccessibleXml(request.getClientId(), request.getOrderId(), request.getXmlId())
                .orElseThrow(() -> new IllegalArgumentException("XML не найден или недоступен клиенту"));

        return new DownloadContractXmlResponse()
                .setXmlId(xmlDocument.getId())
                .setOrderId(xmlDocument.getContractId())
                .setName(xmlDocument.getName())
                .setStatus(xmlDocument.getStatus())
                .setVersion(xmlDocument.getVersion())
                .setXsdId(xmlDocument.getXsdId())
                .setData(xmlDocument.getData());
    }
}
