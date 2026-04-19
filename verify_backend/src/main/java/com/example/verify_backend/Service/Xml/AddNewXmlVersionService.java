package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.UtilService.XmlValidateService;
import com.example.verify_backend.dto.AddNewXmlVersionRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddNewXmlVersionService {

    private final XmlRepository xmlRepository;
    private final XmlValidateService xmlValidateService;

    public Result addNewXmlVersion(AddNewXmlVersionRequest request) {

        // TODO xmlValidateService.validate(request.getXmlData(), );


        XmlLight xmlLight = xmlRepository.getLastXmlLightInfo(request.getXmlName(), request.getXsdId(), request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Не получилось найти информацию о xml"));

        if(!ContractStatus.PROCESSING.name().equals(xmlLight.getContract().getStatus().name())) {
            throw new BusinessLogicException("Заказ должен находиться в статусе 'PROCESSING'");
        }

        if (!request.getClientId().equals(xmlLight.getContractor().getClientId())) {
            throw new BusinessLogicException("Вы не можете добавлять новые версии к этому документу");
        }

        xmlRepository.addNewVersionXml(xmlLight, request.getXmlData(), request.getReason());

        return new Result();

    }

}
