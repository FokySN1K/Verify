package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ContractStatus;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContractRowMapperTest {

    @Test
    void shouldMapContractWithCustomerAndContractor() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("customer_id", Long.class)).thenReturn(1L);
        when(rs.getString("customer_name")).thenReturn("Ivan");
        when(rs.getString("customer_client_id")).thenReturn("customer-1");
        when(rs.getString("customer_surname")).thenReturn("Petrov");
        when(rs.getString("customer_email")).thenReturn("customer@example.com");
        when(rs.getObject("contractor_id", Long.class)).thenReturn(2L);
        when(rs.getString("contractor_name")).thenReturn("Oleg");
        when(rs.getString("contractor_client_id")).thenReturn("contractor-1");
        when(rs.getString("contractor_surname")).thenReturn("Sidorov");
        when(rs.getString("contractor_email")).thenReturn("contractor@example.com");
        when(rs.getObject("id", Long.class)).thenReturn(10L);
        when(rs.getString("name")).thenReturn("Contract");
        when(rs.getString("description")).thenReturn("Desc");
        when(rs.getString("status")).thenReturn("PROCESSING");
        when(rs.getString("reason")).thenReturn("reason");

        Contract contract = new ContractRowMapper().mapRow(rs, 0);

        assertThat(contract.getId()).isEqualTo(10L);
        assertThat(contract.getStatus()).isEqualTo(ContractStatus.PROCESSING);
        assertThat(contract.getCustomer().getClientId()).isEqualTo("customer-1");
        assertThat(contract.getContractor().getClientId()).isEqualTo("contractor-1");
    }
}
