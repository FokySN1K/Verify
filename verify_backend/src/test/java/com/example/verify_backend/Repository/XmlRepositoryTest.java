package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Xml;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.NoAffectException;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XmlRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private XmlRepository repository;

    @BeforeEach
    void setUp() {
        repository = new XmlRepository(jdbcTemplate);
    }

    @Test
    void shouldReturnXmlLightList() {
        List<XmlLight> expected = List.of(TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L));
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(expected);

        assertThat(repository.getXmlLightInfoList("client-1")).isEqualTo(expected);
    }

    @Test
    void shouldReturnLastXmlLightInfo() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L);
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(List.of(xmlLight));

        assertThat(repository.getLastXmlLightInfo("doc.xml", 100L, 10L)).contains(xmlLight);
    }

    @Test
    void shouldReturnXmlById() {
        Xml xml = TestDataFactory.xml(1L);
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(List.of(xml));

        assertThat(repository.getXmlByXmlId(1L)).contains(xml);
    }

    @Test
    void shouldUseContractIdWhenChangingXmlStatus() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 555L, 100L, XmlStatus.NEW, 3L);
        xmlLight.getContractor().setId(999L);
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        repository.changeXmlStatus(xmlLight, XmlStatus.PROCESSING, null);

        ArgumentCaptor<SqlParameterSource> captor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(anyString(), captor.capture());
        assertThat(captor.getValue().getValue("contract_id")).isEqualTo(555L);
    }

    @Test
    void shouldThrowWhenChangeStatusAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);

        NoAffectException exception = assertThrows(NoAffectException.class,
                () -> repository.changeXmlStatus(TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.NEW, 0L), XmlStatus.PROCESSING, null));

        assertThat(exception.getMessage()).contains("изменить статус XML документа");
    }

    @Test
    void shouldUseContractIdWhenAddingNewVersion() {
        XmlLight xmlLight = TestDataFactory.xmlLight(1L, 777L, 100L, XmlStatus.REFUSED, 3L);
        xmlLight.getContractor().setId(333L);
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        repository.addNewVersionXml(xmlLight, "<xml/>", "reason");

        ArgumentCaptor<SqlParameterSource> captor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(anyString(), captor.capture());
        assertThat(captor.getValue().getValue("contract_id")).isEqualTo(777L);
        assertThat(captor.getValue().getValue("xml_new_data")).isEqualTo("<xml/>");
    }

    @Test
    void shouldThrowWhenAddNewVersionAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);

        NoAffectException exception = assertThrows(NoAffectException.class,
                () -> repository.addNewVersionXml(TestDataFactory.xmlLight(1L, 10L, 100L, XmlStatus.REFUSED, 1L), "<xml/>", "reason"));

        assertThat(exception.getMessage()).contains("создать новую версию XML документа");
    }

    @Test
    void shouldSkipBatchInsertForEmptyList() {
        repository.addNewXmlList(10L, List.of());

        verify(jdbcTemplate, never()).batchUpdate(anyString(), any(SqlParameterSource[].class));
    }

    @Test
    void shouldBuildBatchInsertParameters() {
        XmlLight first = new XmlLight().setName("a.xml").setXsdLight(TestDataFactory.xsdLight(1L));
        XmlLight second = new XmlLight().setName("b.xml").setXsdLight(TestDataFactory.xsdLight(2L));

        repository.addNewXmlList(10L, List.of(first, second));

        ArgumentCaptor<SqlParameterSource[]> captor = ArgumentCaptor.forClass(SqlParameterSource[].class);
        verify(jdbcTemplate).batchUpdate(anyString(), captor.capture());
        SqlParameterSource[] batch = captor.getValue();
        assertThat(batch).hasSize(2);
        assertThat(batch[0].getValue("name")).isEqualTo("a.xml");
        assertThat(batch[0].getValue("contract_id")).isEqualTo(10L);
        assertThat(batch[0].getValue("xsd_id")).isEqualTo(1L);
        assertThat(batch[1].getValue("name")).isEqualTo("b.xml");
        assertThat(batch[1].getValue("xsd_id")).isEqualTo(2L);
    }
}
