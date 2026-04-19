package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XsdRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private XsdRepository repository;

    @BeforeEach
    void setUp() {
        repository = new XsdRepository(jdbcTemplate);
    }

    @Test
    void shouldReturnProcessingXsdList() {
        List<Xsd> expected = List.of(TestDataFactory.xsd(1L));
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(expected);

        assertThat(repository.getProcessedXsdList()).isEqualTo(expected);
    }

    @Test
    void shouldReturnXsdById() {
        Xsd xsd = TestDataFactory.xsd(1L);
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(List.of(xsd));

        assertThat(repository.getXsdByXsdId(1L)).contains(xsd);
    }

    @Test
    void shouldReturnXsdLightList() {
        List<XsdLight> expected = List.of(TestDataFactory.xsdLight(1L));
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(expected);

        assertThat(repository.getXsdLightList()).isEqualTo(expected);
    }
}
