package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Repository.Query.ContractQuery;
import com.example.verify_backend.Repository.RowMapper.ContractRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void createContract(Contract contract) {
        jdbcTemplate.update(ContractQuery.CREATE_CONTRACT.getQuery(),
                new MapSqlParameterSource()
                        .addValue("name", contract.getName(), Types.VARCHAR)
                        .addValue("description", contract.getDescription(), Types.VARCHAR)
                        .addValue("client_id", contract.getCustomer().getClientId(), Types.VARCHAR));
    }

    public List<Contract> getContracts(String clientId) {
        return jdbcTemplate.query(ContractQuery.GET_CONTRACTS.getQuery(),
                new MapSqlParameterSource()
                        .addValue("client_id", clientId, Types.VARCHAR),
                new ContractRowMapper());
    }
}
