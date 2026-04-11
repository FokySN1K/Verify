package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.XmlEntity;
import com.example.verify_backend.Repository.Query.XmlQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class XmlRepository {

    private final RowMapper<XmlEntity> xmlRowMapper = new BeanPropertyRowMapper<>(XmlEntity.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<XmlEntity> findAllByClientIdAndContractId(String clientId,
                                                          Long contractId) {
        return jdbcTemplate.query(
                XmlQuery.GET_LIGHT_XML.getQuery(),
                new MapSqlParameterSource()
                        .addValue("clientId", clientId, Types.VARCHAR)
                        .addValue("contractId", contractId, Types.BIGINT),
                xmlRowMapper
        );
    }

    public Optional<XmlEntity> findByClientIdAndContractIdAndId(String clientId,
                                                                Long contractId,
                                                                Long id) {
        List<XmlEntity> xml = jdbcTemplate.query(
                XmlQuery.GET_XML.getQuery(),
                new MapSqlParameterSource()
                        .addValue("clientId", clientId, Types.VARCHAR)
                        .addValue("contractId", contractId, Types.BIGINT)
                        .addValue("id", id, Types.BIGINT),
                xmlRowMapper
        );

        if (xml.isEmpty()) {
            return Optional.empty();
        }

        if (xml.size() > 1) {
            throw new IllegalStateException("More than one row found for id " + id);
        }

        return Optional.of(xml.getFirst());
    }

}
