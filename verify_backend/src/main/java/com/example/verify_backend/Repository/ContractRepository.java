package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ContractStatus;
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

    public boolean existsContractForCustomer(String customerId, Long orderId) {
        String sql = """
                select count(*)
                  from contract ct
                  join client c on c.id = ct.customer_id
                 where c.client_id = :client_id
                   and ct.id = :order_id
                """;

        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("client_id", customerId, Types.VARCHAR)
                        .addValue("order_id", orderId, Types.BIGINT),
                Integer.class);

        return count != null && count > 0;
    }

    public boolean existsContractForContractor(String contractorId, Long orderId) {
        String sql = """
                select count(*)
                  from contract ct
                  join client c on c.id = ct.contractor_id
                 where c.client_id = :client_id
                   and ct.id = :order_id
                """;

        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("client_id", contractorId, Types.VARCHAR)
                        .addValue("order_id", orderId, Types.BIGINT),
                Integer.class);

        return count != null && count > 0;
    }

    public ContractStatus getContractStatus(Long orderId) {
        String sql = """
                select status::text
                  from contract
                 where id = :order_id
                """;

        String status = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("order_id", orderId, Types.BIGINT),
                String.class);

        return ContractStatus.valueOf(status);
    }

    public void chooseContractor(Long orderId, String contractorId) {
        String sql = """
                update contract
                   set contractor_id = (select id from client where client_id = :contractor_id),
                       status = 'PROCESSING'::contract_status
                 where id = :order_id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("contractor_id", contractorId, Types.VARCHAR)
                        .addValue("order_id", orderId, Types.BIGINT));
    }
}
