package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.UtilService.XmlValidateService;
import com.example.verify_backend.UtilService.XsdCacheService;
import com.example.verify_backend.dto.AddNewXmlVersionRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddNewXmlVersionService {

    private final XmlRepository xmlRepository;
    private final XmlValidateService xmlValidateService;
    private final XsdCacheService xsdCacheService;

    public Result addNewXmlVersion(AddNewXmlVersionRequest request) {

        log.info("[addNewXmlVersion] Operation started");
        XmlLight xmlLight = xmlRepository.getLastXmlLightInfo(request.getXmlName(), request.getXsdId(), request.getContractId())
                .orElseThrow(() -> new BusinessLogicException("Не получилось найти информацию о xml"));

        if (!(ContractStatus.PROCESSING.name().equals(xmlLight.getContract().getStatus().name()))) {
            throw new BusinessLogicException("Заказ должен находиться в статусе 'PROCESSING'");
        }

        if (!(XmlStatus.NEW == xmlLight.getStatus()
                || XmlStatus.PROCESSING == xmlLight.getStatus()
                || XmlStatus.REFUSED == xmlLight.getStatus())) {
            throw new BusinessLogicException("Xml документ должен находиться в статусе 'NEW', 'PROCESSING' или 'REFUSED'");
        }

        if (!request.getClientId().equals(xmlLight.getContractor().getClientId())) {
            throw new BusinessLogicException("Вы не можете добавлять новые версии к этому документу");
        }

        // Валидируем документ перед сохранением в бд
        Xsd xsd = xsdCacheService.getXsdDataByXsdId(xmlLight.getXsdLight().getId());
        List<SAXParseException> exceptionList;

        try {
            exceptionList = xmlValidateService.validate(request.getXmlData(), xsd.getXsdData());
        } catch (IOException e) {
            throw new ValidationXmlException("Ошибка при чтении файла");
        } catch (SAXException e) {
            throw new ValidationXmlException("Ошибка при чтении файла. Формат некорректен");
        }

        if (!exceptionList.isEmpty()) {
            throw new ValidationXmlException(exceptionList.stream().map(SAXParseException::toString).toList().toString());
        }


        xmlRepository.addNewVersionXml(xmlLight, request.getXmlData(), request.getReason());

        log.info("[addNewXmlVersion] Operation finished");
        return new Result();

    }

}
