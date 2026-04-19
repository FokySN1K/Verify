package com.example.verify_backend.Repository;


import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Repository.Query.XsdQuery;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class XsdRepository {

    private final static BeanPropertyRowMapper<Xsd> XSD_BEAN_PROPERTY_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Xsd.class);
    private final static BeanPropertyRowMapper<XsdLight> XSD_LIGHT_BEAN_PROPERTY_ROW_MAPPER = BeanPropertyRowMapper.newInstance(XsdLight.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Xsd> getProcessedXsdList() {
        return jdbcTemplate.query(XsdQuery.GET_PROCESSING_XSD_LIST.getQuery(),
                new MapSqlParameterSource(),
                XSD_BEAN_PROPERTY_ROW_MAPPER);
    }

    public Optional<Xsd> getXsdByXsdId(@NotNull Long xsdId) {
        return jdbcTemplate.query(XsdQuery.GET_XSD_BY_XSD_ID.getQuery(),
                new MapSqlParameterSource()
                        .addValue("xsd_id", xsdId, Types.BIGINT),
                XSD_BEAN_PROPERTY_ROW_MAPPER).stream().findAny();
    }

    public List<XsdLight> getXsdLightList() {
        return jdbcTemplate.query(XsdQuery.GET_XSD_LIGHT_LIST.getQuery(),
                new MapSqlParameterSource(),
                XSD_LIGHT_BEAN_PROPERTY_ROW_MAPPER);
    }
}
