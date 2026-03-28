package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Repository.Query.ClientQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
@RequiredArgsConstructor
public class ClientRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void createClient(Client client) {
        jdbcTemplate.update(ClientQuery.CREATE_CLIENT.getQuery(),
                new MapSqlParameterSource()
                        .addValue("name", client.getName(), Types.VARCHAR)
                        .addValue("surname", client.getSurname(), Types.VARCHAR)
                        .addValue("email", client.getEmail(), Types.VARCHAR)
                        .addValue("client_id", client.getClientId(), Types.VARCHAR)
                        .addValue("role", client.getRole(), Types.OTHER));
    }

}
