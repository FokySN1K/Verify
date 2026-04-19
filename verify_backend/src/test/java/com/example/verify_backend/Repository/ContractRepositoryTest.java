package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ContractStatus;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ContractRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ContractRepository(jdbcTemplate);
    }

    @Test
    void shouldCreateContract() {
        Contract contract = TestDataFactory.contract(1L, ContractStatus.NEW);

        repository.createContract(contract);

        ArgumentCaptor<SqlParameterSource> captor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(anyString(), captor.capture());
        SqlParameterSource params = captor.getValue();
        assertThat(params.getValue("name")).isEqualTo(contract.getName());
        assertThat(params.getValue("description")).isEqualTo(contract.getDescription());
        assertThat(params.getValue("client_id")).isEqualTo(contract.getCustomer().getClientId());
    }

    @Test
    void shouldReturnContracts() {
        List<Contract> expected = List.of(TestDataFactory.contract(1L, ContractStatus.NEW));
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(expected);

        List<Contract> result = repository.getContracts("client-1");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnContractById() {
        Contract expected = TestDataFactory.contract(1L, ContractStatus.NEW);
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(List.of(expected));

        assertThat(repository.getContractByContractId(1L)).contains(expected);
    }

    @Test
    void shouldThrowWhenSetContractorAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);

        NoAffectException exception = assertThrows(NoAffectException.class,
                () -> repository.setContractorForContract(1L, "contractor-1"));

        assertThat(exception.getMessage()).contains("Ошибка установки подрядчика заказу");
    }

    @Test
    void shouldThrowWhenChangeStatusAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);

        NoAffectException exception = assertThrows(NoAffectException.class,
                () -> repository.changeContractStatus(1L, ContractStatus.DONE));

        assertThat(exception.getMessage()).contains("Ошибка при переводе контракта");
    }
}
