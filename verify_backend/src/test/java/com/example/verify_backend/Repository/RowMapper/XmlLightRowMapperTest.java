package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Enums.XsdStatus;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class XmlLightRowMapperTest {

    @Test
    void shouldMapXmlLightGraph() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("id", Long.class)).thenReturn(1L);
        when(rs.getString("name")).thenReturn("doc.xml");
        when(rs.getString("status")).thenReturn("CHECKING");
        when(rs.getString("reason")).thenReturn("reason");
        when(rs.getObject("version", Long.class)).thenReturn(3L);
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

        XmlLight xmlLight = new XmlLightRowMapper().mapRow(rs, 0);

        assertThat(xmlLight.getStatus()).isEqualTo(XmlStatus.CHECKING);
        assertThat(xmlLight.getContractor().getRole()).isEqualTo(ClientRole.CONTRACTOR);
        assertThat(xmlLight.getCustomer().getRole()).isEqualTo(ClientRole.CUSTOMER);
        assertThat(xmlLight.getXsdLight().getStatus()).isEqualTo(XsdStatus.PROCESSING);
        assertThat(xmlLight.getContract().getStatus()).isEqualTo(ContractStatus.PROCESSING);
        assertThat(xmlLight.getContract().getReason()).isEqualTo("contract reason");
    }
}
