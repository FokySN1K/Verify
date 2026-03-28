package com.example.verify_backend.Repository;

import com.example.verify_backend.Repository.Query.ContractQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
@RequiredArgsConstructor
public class ContractRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;



}
