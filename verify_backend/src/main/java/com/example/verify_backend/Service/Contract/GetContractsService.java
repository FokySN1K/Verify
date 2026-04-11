package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.XmlEntity;
import com.example.verify_backend.Exception.NotFoundException;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetContractsService {

    private final ContractRepository contractRepository;

    private final XmlRepository xmlRepository;

    public GetContractsResponse getContracts(GetContractsRequest request) {
        return new GetContractsResponse()
                .setContractList(contractRepository.getContracts(request.getClient_id()));

    }

    public GetItemsResponse<XmlEntity> getContractXml(GetContractXmlRequest request) {
        log.info("Get contract xml-s: {}", request);
        return new GetItemsResponse<>(
                xmlRepository.findAllByClientIdAndContractId(request.clientId(), request.orderId())
        );
    }

    public XmlEntity getXmlInfo(GetContractXmlInfoRequest request) {
        log.info("Get xml info: {}", request);
        return xmlRepository.findByClientIdAndContractIdAndId(request.clientId(), request.orderId(), request.xmlId())
                .orElseThrow(() -> new NotFoundException(
                        "Xml not found by id '" + request.xmlId() + "'"
                                + " for client with id '" + request.clientId() + "'"
                                + " and contract id '" + request.orderId() + "'"
                ));
    }

}
