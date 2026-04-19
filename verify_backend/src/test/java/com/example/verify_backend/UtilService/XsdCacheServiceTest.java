package com.example.verify_backend.UtilService;

import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.XsdRepository;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XsdCacheServiceTest {

    @Mock
    private XsdRepository xsdRepository;

    private XsdCacheService xsdCacheService;

    @BeforeEach
    void setUp() {
        xsdCacheService = new XsdCacheService(xsdRepository);
    }

    @Test
    void shouldLoadCacheOnInit() {
        Xsd xsd = TestDataFactory.xsd(1L);
        when(xsdRepository.getProcessedXsdList()).thenReturn(List.of(xsd));

        xsdCacheService.initCache();

        assertThat(xsdCacheService.getXsdDataByXsdId(1L)).isEqualTo(xsd);
        verify(xsdRepository, never()).getXsdByXsdId(1L);
    }

    @Test
    void shouldRenewCacheWithLatestProcessingSchemas() {
        Xsd first = TestDataFactory.xsd(1L);
        Xsd second = TestDataFactory.xsd(2L);
        when(xsdRepository.getProcessedXsdList()).thenReturn(List.of(first), List.of(second));

        xsdCacheService.renewProcessingXsdCacheMap();
        xsdCacheService.renewProcessingXsdCacheMap();

        assertThat(xsdCacheService.getXsdDataByXsdId(2L)).isEqualTo(second);
        verify(xsdRepository, times(2)).getProcessedXsdList();
        verify(xsdRepository, never()).getXsdByXsdId(2L);
    }

    @Test
    void shouldFallbackToRepositoryWhenSchemaAbsentInCache() {
        Xsd xsd = TestDataFactory.xsd(11L);
        when(xsdRepository.getXsdByXsdId(11L)).thenReturn(Optional.of(xsd));

        Xsd result = xsdCacheService.getXsdDataByXsdId(11L);

        assertThat(result).isEqualTo(xsd);
    }

    @Test
    void shouldThrowWhenSchemaAbsentEverywhere() {
        when(xsdRepository.getXsdByXsdId(99L)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> xsdCacheService.getXsdDataByXsdId(99L));

        assertThat(exception.getMessage()).contains("xsd_id не существует");
    }
}
