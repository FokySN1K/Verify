package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.Xml;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.GetXmlDataRequest;
import com.example.verify_backend.dto.GetXmlDataResponse;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetXmlDataServiceTest {

    @Mock
    private XmlRepository xmlRepository;

    @InjectMocks
    private GetXmlDataService service;

    @Test
    void shouldReturnXmlDataForRelatedClient() {
        Xml xml = TestDataFactory.xml(1L);
        when(xmlRepository.getXmlByXmlId(1L)).thenReturn(Optional.of(xml));

        GetXmlDataResponse response = service.getXmlData(new GetXmlDataRequest().setXmlId(1L).setClientId("customer-1"));

        assertThat(response.getXmlData()).isEqualTo(xml.getXmlData());
    }

    @Test
    void shouldThrowWhenXmlMissing() {
        when(xmlRepository.getXmlByXmlId(1L)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.getXmlData(new GetXmlDataRequest().setXmlId(1L).setClientId("customer-1")));

        assertThat(exception.getMessage()).contains("Не найден xml документ");
    }

    @Test
    void shouldThrowWhenClientIsNotRelatedToXml() {
        when(xmlRepository.getXmlByXmlId(1L)).thenReturn(Optional.of(TestDataFactory.xml(1L)));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.getXmlData(new GetXmlDataRequest().setXmlId(1L).setClientId("other")));

        assertThat(exception.getMessage()).contains("не принадлежит клиенту");
    }
}
