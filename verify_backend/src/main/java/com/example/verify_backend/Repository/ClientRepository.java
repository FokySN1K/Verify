package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Repository.Query.ClientQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientRepository {

    private static final BeanPropertyRowMapper<Client> CLIENT_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Client.class);

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

    public Optional<Client> getClient(Long clientId) {
        return jdbcTemplate.query(ClientQuery.GET_CLIENT.getQuery(),
                new MapSqlParameterSource()
                        .addValue("client_id", clientId, Types.VARCHAR),
                CLIENT_ROW_MAPPER)
                .stream()
                .findAny();
    }


}
