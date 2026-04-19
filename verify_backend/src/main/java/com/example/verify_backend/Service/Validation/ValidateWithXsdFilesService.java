package com.example.verify_backend.Service.Validation;

import com.example.verify_backend.dto.ValidateWithXsdFilesResponse;
import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.UtilService.XmlValidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateWithXsdFilesService {

    private final XmlValidateService xmlValidateService;

    public ValidateWithXsdFilesResponse validateWithXsdFiles(MultipartFile xml, MultipartFile xsd) {

        List<SAXParseException> exceptionList;

        try {
            exceptionList = xmlValidateService.validate(xml.getInputStream(), xsd.getInputStream());
        } catch (IOException e) {
            log.error("[{}] An exception occurred.", "validateWithXsdFiles", e);
            throw new ValidationXmlException("Ошибка при чтении файла");
        } catch (SAXException e) {
            log.error("[{}] An exception occurred.", "validateWithXsdFiles", e);
            throw new ValidationXmlException("Ошибка при чтении файла. Формат некорретен");
        }

        return new ValidateWithXsdFilesResponse()
                .setExceptionList(exceptionList.stream().map(SAXParseException::toString).toList());

    }

}
