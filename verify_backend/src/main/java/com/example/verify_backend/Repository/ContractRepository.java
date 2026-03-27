package com.example.verify_backend.Repository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.SQLType;
import java.sql.Types;

@Repository
@RequiredArgsConstructor
public class ContractRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private void test(){
        namedParameterJdbcTemplate.query("select c.id from contract c",
                new MapSqlParameterSource().addValue("asdf", 10, Types.BIGINT),
                rs -> {});
    }

}
