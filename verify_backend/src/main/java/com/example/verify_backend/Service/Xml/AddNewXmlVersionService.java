package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.AddNewXmlVersionRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddNewXmlVersionService {

    private final XmlRepository xmlRepository;

    public Result addNewXmlVersion(AddNewXmlVersionRequest request) {

        XmlLight xmlLight = xmlRepository.getLastXmlLightInfo(request.getXmlName(), request.getXsdId(), request.getContractId())
                .orElseThrow(() -> new RuntimeException("Не получилось найти информацию о xml"));

        if (!request.getClientId().equals(xmlLight.getContractor().getClientId())) {
            throw new RuntimeException("Вы не можете добавлять новые версии к этому документу");
        }

        System.out.println(xmlLight);
        xmlRepository.addNewVersionXml(xmlLight, request.getXmlData(), request.getReason());

        return new Result();

    }

}
