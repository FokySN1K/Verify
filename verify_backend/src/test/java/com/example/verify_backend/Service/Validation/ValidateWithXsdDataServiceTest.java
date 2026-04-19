package com.example.verify_backend.Service.Validation;

import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.UtilService.XmlValidateService;
import com.example.verify_backend.dto.ValidateWithXsdDataRequest;
import com.example.verify_backend.dto.ValidateWithXsdDataResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateWithXsdDataServiceTest {

    @Mock
    private XmlValidateService xmlValidateService;

    @InjectMocks
    private ValidateWithXsdDataService service;

    @Test
    void shouldReturnValidationMessages() throws Exception {
        SAXParseException saxParseException = new SAXParseException("validation error", null);
        when(xmlValidateService.validate("xml", "xsd")).thenReturn(List.of(saxParseException));

        ValidateWithXsdDataResponse response = service.validateWithXsdData(
                new ValidateWithXsdDataRequest().setXmlData("xml").setXsdData("xsd")
        );

        assertThat(response.getExceptionList())
                .anySatisfy(message -> assertThat(message).contains("validation error"));
    }

    @Test
    void shouldMapIOExceptionToValidationException() throws Exception {
        when(xmlValidateService.validate("xml", "xsd")).thenThrow(new IOException("io"));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class,
                () -> service.validateWithXsdData(new ValidateWithXsdDataRequest().setXmlData("xml").setXsdData("xsd")));

        assertThat(exception.getMessage()).contains("Ошибка при чтении файла");
    }

    @Test
    void shouldMapSaxExceptionToValidationException() throws Exception {
        when(xmlValidateService.validate("xml", "xsd")).thenThrow(new SAXException("broken"));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class,
                () -> service.validateWithXsdData(new ValidateWithXsdDataRequest().setXmlData("xml").setXsdData("xsd")));

        assertThat(exception.getMessage()).contains("Формат некорретен");
    }
}
