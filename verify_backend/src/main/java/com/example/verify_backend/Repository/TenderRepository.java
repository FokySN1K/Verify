package com.example.verify_backend.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
@RequiredArgsConstructor
public class TenderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public boolean existsTenderForContractAndContractor(Long orderId, String contractorId) {
        String sql = """
                select count(*)
                  from tender t
                  join client c on c.id = t.contractor_id
                 where t.contract_id = :order_id
                   and c.client_id = :contractor_id
                """;

        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("order_id", orderId, Types.BIGINT)
                        .addValue("contractor_id", contractorId, Types.VARCHAR),
                Integer.class);

        return count != null && count > 0;
    }
}
