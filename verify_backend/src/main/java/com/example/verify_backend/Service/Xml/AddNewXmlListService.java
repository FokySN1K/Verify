package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.AddNewXmlListRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddNewXmlListService {

    private final XmlRepository xmlRepository;
    private final ContractRepository contractRepository;

    public Result addNewXmlList(AddNewXmlListRequest request) {

        log.info("[addNewXmlList] Operation started");
        Contract contract = contractRepository.getContractByContractId(request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Заказа не существует"));

        if(!ContractStatus.PROCESSING.name().equals(contract.getStatus().name())) {
            throw new BusinessLogicException("Заказ должен находиться в статусе 'PROCESSING'");
        }

        if (!request.getClientId().equals(contract.getCustomer().getClientId())) {
            throw new BusinessLogicException("Ошибка в получении заказа");
        }

        List<XmlLight> xmlLightList = new ArrayList<>();
        for (AddNewXmlListRequest.NewXmlData newXmlData : request.getNewXmlDataList()) {
            XsdLight xsdLight = new XsdLight()
                    .setId(newXmlData.getXsdId());
            xmlLightList.add(new XmlLight().setXsdLight(xsdLight).setName(newXmlData.getXmlName()));
        }

        xmlRepository.addNewXmlList(contract.getId(), xmlLightList);

        log.info("[addNewXmlList] Operation finished");
        return new Result();
    }


}
