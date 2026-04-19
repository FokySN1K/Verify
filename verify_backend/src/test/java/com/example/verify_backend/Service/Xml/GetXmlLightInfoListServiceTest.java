package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.GetXmlLightInfoListRequest;
import com.example.verify_backend.dto.GetXmlLightInfoListResponse;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetXmlLightInfoListServiceTest {

    @Mock
    private XmlRepository xmlRepository;

    @InjectMocks
    private GetXmlLightInfoListService service;

    @Test
    void shouldSortByNameThenVersion() {
        XmlLight second = TestDataFactory.xmlLight(2L, 10L, 100L, XmlStatus.NEW, 2L).setName("b.xml");
        XmlLight first = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 1L).setName("a.xml");
        XmlLight firstVersionTwo = TestDataFactory.xmlLight(3L, 10L, 100L, XmlStatus.NEW, 2L).setName("a.xml");
        when(xmlRepository.getXmlLightInfoList("client-1")).thenReturn(List.of(second, firstVersionTwo, first));

        GetXmlLightInfoListResponse response = service.getXmlLightInfoList(new GetXmlLightInfoListRequest().setClientId("client-1"));

        assertThat(response.getXmlLightList())
                .extracting(XmlLight::getName, XmlLight::getVersion)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("a.xml", 1L),
                        org.assertj.core.groups.Tuple.tuple("a.xml", 2L),
                        org.assertj.core.groups.Tuple.tuple("b.xml", 2L)
                );
    }
}
