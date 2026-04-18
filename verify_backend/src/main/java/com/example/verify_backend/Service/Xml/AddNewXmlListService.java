package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.AddNewXmlListRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddNewXmlListService {

    private final XmlRepository xmlRepository;
    private final ContractRepository contractRepository;

    public Result addNewXmlListService(AddNewXmlListRequest request) {

        // TODO Добавить проверку, что xsd не истёк
        Contract contract = contractRepository.getContractByContractId(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Заказа не существует"));

        System.out.println(contract);

        if (!request.getClientId().equals(contract.getCustomer().getClientId())) {
            throw new IllegalArgumentException("Ошибка в получении заказа");
        }

        List<XmlLight> xmlLightList = new ArrayList<>();
        for (AddNewXmlListRequest.NewXmlData newXmlData : request.getNewXmlDataList()) {
            XsdLight xsdLight = new XsdLight()
                    .setId(newXmlData.getXsdId());
            xmlLightList.add(new XmlLight().setXsdLight(xsdLight).setName(newXmlData.getXmlName()));
        }

        xmlRepository.addNewXmlList(contract.getId(), xmlLightList);

        return new Result();
    }


}
