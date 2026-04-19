package com.example.verify_backend.Service.Xsd;

import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Repository.XsdRepository;
import com.example.verify_backend.dto.GetXsdLightListResponse;
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
class GetXsdLightListServiceTest {

    @Mock
    private XsdRepository xsdRepository;

    @InjectMocks
    private GetXsdLightListService service;

    @Test
    void shouldReturnXsdLightList() {
        List<XsdLight> xsdList = List.of(TestDataFactory.xsdLight(1L));
        when(xsdRepository.getXsdLightList()).thenReturn(xsdList);

        GetXsdLightListResponse response = service.getXsdLightList();

        assertThat(response.getXsdLightList()).isEqualTo(xsdList);
    }
}
