package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.UtilService.Notification.EmailSender;
import com.example.verify_backend.dto.ChangeXmlStatusRequest;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeXmlStatusServiceTest {

    @Mock
    private XmlRepository xmlRepository;
    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private ChangeXmlStatusService service;

    @Test
    void shouldAllowContractorToMoveNewToProcessing() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        xmlLight.setContractor(TestDataFactory.contractor("contractor-1"));
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        service.changeXmlStatusService(baseRequest("contractor-1", XmlStatus.PROCESSING, null));

        verify(xmlRepository).changeXmlStatus(xmlLight, XmlStatus.PROCESSING, null);
        verify(emailSender).sendNotification(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq("customer@example.com"));
    }

    @Test
    void shouldAllowCustomerToRefuseCheckingXmlWithReason() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.CHECKING, 1L);
        xmlLight.setCustomer(TestDataFactory.customer("customer-1"));
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        service.changeXmlStatusService(baseRequest("customer-1", XmlStatus.REFUSED, "bad data"));

        verify(xmlRepository).changeXmlStatus(xmlLight, XmlStatus.REFUSED, "bad data");
        verify(emailSender).sendNotification(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq("contractor@example.com"));
    }

    @Test
    void shouldThrowWhenContractorUsesInvalidTransition() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeXmlStatusService(baseRequest("contractor-1", XmlStatus.DONE, null)));

        assertThat(exception.getMessage()).contains("NEW' -> 'PROCESSING'");
    }

    @Test
    void shouldThrowWhenCustomerRefusesWithoutReason() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.CHECKING, 1L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeXmlStatusService(baseRequest("customer-1", XmlStatus.REFUSED, " ")));

        assertThat(exception.getMessage()).contains("необходимо описывать причину");
    }

    @Test
    void shouldThrowWhenClientDoesNotBelongToXml() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeXmlStatusService(baseRequest("other", XmlStatus.PROCESSING, null)));

        assertThat(exception.getMessage()).contains("не принадлежит клиенту");
    }

    @Test
    void shouldIgnoreEmailSenderFailures() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        when(xmlRepository.getLastXmlLightInfo("doc.xml", 100L, 10L)).thenReturn(Optional.of(xmlLight));
        doThrow(new RuntimeException("mail down")).when(emailSender)
                .sendNotification(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());

        service.changeXmlStatusService(baseRequest("contractor-1", XmlStatus.PROCESSING, null));

        verify(xmlRepository).changeXmlStatus(xmlLight, XmlStatus.PROCESSING, null);
    }

    private ChangeXmlStatusRequest baseRequest(String clientId, XmlStatus status, String reason) {
        return new ChangeXmlStatusRequest()
                .setClientId(clientId)
                .setXmlName("doc.xml")
                .setXsdId(100L)
                .setContractId(10L)
                .setXmlStatus(status)
                .setReason(reason);
    }
}
