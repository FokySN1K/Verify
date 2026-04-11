package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.XsdEntity;
import com.example.verify_backend.Repository.Query.XsdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class XsdRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<XsdEntity> rowMapper = new BeanPropertyRowMapper<>(XsdEntity.class);

    public List<XsdEntity> getXsdInfo() {
        return jdbcTemplate.query(XsdQuery.GET_LIGHT_XSD.getQuery(), rowMapper);
    }

}
