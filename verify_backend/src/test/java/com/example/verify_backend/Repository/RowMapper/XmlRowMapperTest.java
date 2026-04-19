package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.Xml;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class XmlRowMapperTest {

    @Test
    void shouldMapXmlDataField() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("id", Long.class)).thenReturn(1L);
        when(rs.getString("name")).thenReturn("doc.xml");
        when(rs.getString("status")).thenReturn("DONE");
        when(rs.getString("reason")).thenReturn("reason");
        when(rs.getObject("version", Long.class)).thenReturn(1L);
        when(rs.getObject("contractor_id", Long.class)).thenReturn(2L);
        when(rs.getString("contractor_client_id")).thenReturn("contractor-1");
        when(rs.getString("contractor_email")).thenReturn("contractor@example.com");
        when(rs.getString("contractor_role")).thenReturn("CONTRACTOR");
        when(rs.getObject("customer_id", Long.class)).thenReturn(3L);
        when(rs.getString("customer_client_id")).thenReturn("customer-1");
        when(rs.getString("customer_email")).thenReturn("customer@example.com");
        when(rs.getString("customer_role")).thenReturn("CUSTOMER");
        when(rs.getObject("xsd_id", Long.class)).thenReturn(100L);
        when(rs.getString("xsd_name")).thenReturn("schema");
        when(rs.getString("xsd_link")).thenReturn("http://example.com");
        when(rs.getString("xsd_stage")).thenReturn("stage");
        when(rs.getString("xsd_status")).thenReturn("PROCESSING");
        when(rs.getObject("contract_id", Long.class)).thenReturn(10L);
        when(rs.getString("contract_name")).thenReturn("Contract");
        when(rs.getString("contract_description")).thenReturn("Desc");
        when(rs.getString("contract_reason")).thenReturn("contract reason");
        when(rs.getString("contract_status")).thenReturn("PROCESSING");
        when(rs.getString("data")).thenReturn("<xml/>");

        Xml xml = new XmlRowMapper().mapRow(rs, 0);

        assertThat(xml.getXmlData()).isEqualTo("<xml/>");
    }
}
