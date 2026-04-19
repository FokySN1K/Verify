package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Enums.ClientRole;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ClientRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ClientRepository(jdbcTemplate);
    }

    @Test
    void shouldCreateClient() {
        Client client = new Client()
                .setClientId("client-1")
                .setName("Ivan")
                .setSurname("Petrov")
                .setEmail("ivan@example.com")
                .setRole(ClientRole.CUSTOMER);

        repository.createClient(client);

        ArgumentCaptor<SqlParameterSource> captor = ArgumentCaptor.forClass(SqlParameterSource.class);
        verify(jdbcTemplate).update(anyString(), captor.capture());
        SqlParameterSource params = captor.getValue();
        assertThat(params.getValue("client_id")).isEqualTo("client-1");
        assertThat(params.getValue("role")).isEqualTo(ClientRole.CUSTOMER);
    }

    @Test
    void shouldGetClient() {
        Client client = new Client().setClientId("client-1");
        when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class))).thenReturn(List.of(client));

        assertThat(repository.getClient("client-1")).contains(client);
    }
}
