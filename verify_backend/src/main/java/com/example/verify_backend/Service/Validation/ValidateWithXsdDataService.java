package com.example.verify_backend.Service.Validation;

import com.example.verify_backend.dto.ValidateWithXsdDataRequest;
import com.example.verify_backend.dto.ValidateWithXsdDataResponse;
import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.UtilService.XmlValidateService;
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
public class ValidateWithXsdDataService {

    private final XmlValidateService xmlValidateService;

    public ValidateWithXsdDataResponse validateWithXsdData(ValidateWithXsdDataRequest request) {

        List<SAXParseException> exceptionList;

        try {
            exceptionList = xmlValidateService.validate(request.getXmlData(), request.getXsdData());
        } catch (IOException e) {
            log.error("[{}] An exception occurred.", "validateWithXsdData", e);
            throw new ValidationXmlException("Ошибка при чтении файла");
        } catch (SAXException e) {
            log.error("[{}] An exception occurred.", "validateWithXsdData", e);
            throw new ValidationXmlException("Ошибка при чтении файла. Формат некорретен");
        }

        return new ValidateWithXsdDataResponse()
                .setExceptionList(exceptionList.stream().map(SAXParseException::toString).toList());

    }

}
