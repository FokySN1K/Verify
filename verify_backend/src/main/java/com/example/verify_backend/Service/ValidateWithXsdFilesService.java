package com.example.verify_backend.Service;

import com.example.verify_backend.Entity.ValidateWithXsdFilesResponse;
import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.UtilService.XmlValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidateWithXsdFilesService {

    private final XmlValidateService xmlValidateService;

    public ValidateWithXsdFilesResponse validateWithXsdFilesService(MultipartFile xml, MultipartFile xsd) {

        List<SAXParseException> exceptionList;

        try {
            exceptionList = xmlValidateService.validate(xml.getInputStream(), xsd.getInputStream());
        } catch (IOException e) {
            throw new ValidationXmlException("Ошибка при чтении файла");
        } catch (SAXException e) {
            throw new ValidationXmlException("Ошибка при чтении файла. Формат некорретен");
        }

        return new ValidateWithXsdFilesResponse()
                .setExceptionList(exceptionList.stream().map(SAXParseException::toString).toList());

    }

}
