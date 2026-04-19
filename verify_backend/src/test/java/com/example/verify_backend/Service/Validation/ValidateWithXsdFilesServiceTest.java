package com.example.verify_backend.Service.Validation;

import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.UtilService.XmlValidateService;
import com.example.verify_backend.dto.ValidateWithXsdFilesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateWithXsdFilesServiceTest {

    @Mock
    private XmlValidateService xmlValidateService;

    @InjectMocks
    private ValidateWithXsdFilesService service;

    @Test
    void shouldReturnValidationMessages() throws Exception {
        SAXParseException saxParseException = new SAXParseException("validation error", null);
        when(xmlValidateService.validate(any(InputStream.class), any(InputStream.class))).thenReturn(List.of(saxParseException));

        ValidateWithXsdFilesResponse response = service.validateWithXsdFiles(
                new MockMultipartFile("xml", "test.xml", "application/xml", "<xml/>".getBytes()),
                new MockMultipartFile("xsd", "test.xsd", "application/xml", "<xsd/>".getBytes())
        );

        assertThat(response.getExceptionList())
                .anySatisfy(message -> assertThat(message).contains("validation error"));
    }

    @Test
    void shouldMapIOExceptionToValidationException() throws Exception {
        when(xmlValidateService.validate(any(InputStream.class), any(InputStream.class))).thenThrow(new IOException("io"));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class,
                () -> service.validateWithXsdFiles(
                        new MockMultipartFile("xml", "test.xml", "application/xml", "<xml/>".getBytes()),
                        new MockMultipartFile("xsd", "test.xsd", "application/xml", "<xsd/>".getBytes())
                ));

        assertThat(exception.getMessage()).contains("Ошибка при чтении файла");
    }

    @Test
    void shouldMapSaxExceptionToValidationException() throws Exception {
        when(xmlValidateService.validate(any(InputStream.class), any(InputStream.class))).thenThrow(new SAXException("broken"));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class,
                () -> service.validateWithXsdFiles(
                        new MockMultipartFile("xml", "test.xml", "application/xml", "<xml/>".getBytes()),
                        new MockMultipartFile("xsd", "test.xsd", "application/xml", "<xsd/>".getBytes())
                ));

        assertThat(exception.getMessage()).contains("Формат некорретен");
    }
}
