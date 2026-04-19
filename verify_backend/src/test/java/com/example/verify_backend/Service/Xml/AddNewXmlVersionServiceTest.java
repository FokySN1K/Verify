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
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddNewXmlVersionServiceTest {

    @Mock
    private XmlRepository xmlRepository;
    @Mock
    private XmlValidateService xmlValidateService;
    @Mock
    private XsdCacheService xsdCacheService;

    @InjectMocks
    private AddNewXmlVersionService service;

    @Test
    void shouldAddNewVersionWhenXmlIsValid() throws Exception {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        xmlLight.getContract().setStatus(ContractStatus.PROCESSING);
        xmlLight.setContractor(TestDataFactory.contractor("contractor-1"));
        Xsd xsd = TestDataFactory.xsd(100L);
        AddNewXmlVersionRequest request = baseRequest();

        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));
        when(xsdCacheService.getXsdDataByXsdId(100L)).thenReturn(xsd);
        when(xmlValidateService.validate(request.getXmlData(), xsd.getXsdData())).thenReturn(List.of());

        service.addNewXmlVersion(request);

        verify(xmlRepository).addNewVersionXml(xmlLight, request.getXmlData(), request.getReason());
    }

    @Test
    void shouldThrowWhenXmlMetadataMissing() {
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("Не получилось найти информацию о xml");
    }

    @Test
    void shouldThrowWhenContractStatusIsInvalid() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        xmlLight.getContract().setStatus(ContractStatus.DONE);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("статусе 'PROCESSING'");
    }

    @Test
    void shouldThrowWhenXmlStatusIsInvalid() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.DONE, 0L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("должен находиться в статусе 'NEW', 'PROCESSING' или 'REFUSED'");
    }

    @Test
    void shouldThrowWhenClientIsNotContractor() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        xmlLight.setContractor(TestDataFactory.contractor("other-contractor"));
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("не можете добавлять новые версии");
    }

    @Test
    void shouldMapIOExceptionToValidationException() throws Exception {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        Xsd xsd = TestDataFactory.xsd(100L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));
        when(xsdCacheService.getXsdDataByXsdId(100L)).thenReturn(xsd);
        when(xmlValidateService.validate(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString()))
                .thenThrow(new IOException("io"));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("Ошибка при чтении файла");
    }

    @Test
    void shouldMapSaxExceptionToValidationException() throws Exception {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        Xsd xsd = TestDataFactory.xsd(100L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));
        when(xsdCacheService.getXsdDataByXsdId(100L)).thenReturn(xsd);
        when(xmlValidateService.validate(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString()))
                .thenThrow(new SAXException("broken"));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("Формат некорректен");
    }

    @Test
    void shouldThrowWhenSchemaValidationReturnsErrors() throws Exception {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        Xsd xsd = TestDataFactory.xsd(100L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));
        when(xsdCacheService.getXsdDataByXsdId(100L)).thenReturn(xsd);
        when(xmlValidateService.validate(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(List.of(new SAXParseException("validation", null)));

        ValidationXmlException exception = assertThrows(ValidationXmlException.class, () -> service.addNewXmlVersion(baseRequest()));

        assertThat(exception.getMessage()).contains("validation");
    }

    private AddNewXmlVersionRequest baseRequest() {
        return new AddNewXmlVersionRequest()
                .setXmlName("doc.xml")
                .setXsdId(100L)
                .setContractId(10L)
                .setClientId("contractor-1")
                .setXmlData(TestDataFactory.validXml())
                .setReason("fix");
    }
}
