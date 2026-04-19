package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Xml;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.NoAffectException;
import com.example.verify_backend.Repository.Query.XmlQuery;
import com.example.verify_backend.Repository.RowMapper.XmlLightRowMapper;
import com.example.verify_backend.Repository.RowMapper.XmlRowMapper;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
public class XmlRepository {


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public XmlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Выдаём облегченную информацию по xml договорам
     *
     *
     */
    public List<XmlLight> getXmlLightInfoList(@NotNull String clientId) {
        return jdbcTemplate.query(XmlQuery.GET_XML_LIGHT_INFO_LIST.getQuery(),
                new MapSqlParameterSource()
                        .addValue("client_id", clientId, Types.VARCHAR),
                new XmlLightRowMapper());
    }

    /**
     * Выдаём облегченную информацию по xml договору
     *
     *
     */
    public Optional<XmlLight> getLastXmlLightInfo(@NotNull String name, @NotNull Long xsdId, @NotNull Long contractId) {
        return jdbcTemplate.query(XmlQuery.GET_LAST_XML_LIGHT_INFO.getQuery(),
                new MapSqlParameterSource()
                        .addValue("name", name, Types.VARCHAR)
                        .addValue("xsd_id", xsdId, Types.BIGINT)
                        .addValue("contract_id", contractId, Types.BIGINT),
                new XmlLightRowMapper()).stream().findAny();
    }


    public Optional<Xml> getXmlByXmlId(@NotNull Long xmlId) {
        return jdbcTemplate.query(XmlQuery.GET_XML_BY_XML_ID.getQuery(),
                new MapSqlParameterSource()
                        .addValue("xml_id", xmlId, Types.BIGINT),
                new XmlRowMapper()).stream().map(xmlLight -> (Xml) xmlLight).findAny();
    }


    public void changeXmlStatus(XmlLight xmlLight, XmlStatus status, @Nullable String reason) {
        int affectRow = jdbcTemplate.update(XmlQuery.CHANGE_XML_STATUS.getQuery(),
                new MapSqlParameterSource()
                        .addValue("name", xmlLight.getName(), Types.VARCHAR)
                        .addValue("contract_id", xmlLight.getContract().getId(), Types.BIGINT)
                        .addValue("xsd_id", xmlLight.getXsdLight().getId(), Types.BIGINT)
                        .addValue("version", xmlLight.getVersion(), Types.BIGINT)
                        .addValue("status", status.name(), Types.VARCHAR)
                        .addValue("reason", reason, Types.VARCHAR));

        if (affectRow == 0) {
            throw new NoAffectException("Не удалось изменить статус XML документа");
        }
    }

    /**
     * Создание новой версии xml документа.
     * Привязка версии +1 к существующему xml документу
     *
     * !!!! Перед вызовом метода обязательно вызываем получение XmlInfoLight c блокировкой
     * */
    public void addNewVersionXml(@NotNull XmlLight xmlLightOld, String xmlData, @Nullable String reason) {
        int affectRow = jdbcTemplate.update(XmlQuery.ADD_NEW_VERSION_XML.getQuery(),
                new MapSqlParameterSource()
                        .addValue("name", xmlLightOld.getName(), Types.VARCHAR)
                        .addValue("contract_id", xmlLightOld.getContract().getId(), Types.BIGINT)
                        .addValue("xsd_id", xmlLightOld.getXsdLight().getId(), Types.BIGINT)
                        .addValue("version", xmlLightOld.getVersion(), Types.BIGINT)
                        .addValue("xml_new_data", xmlData, Types.VARCHAR)
                        .addValue("reason", reason, Types.VARCHAR));

        if (affectRow == 0) {
            throw new NoAffectException("Не удалось создать новую версию XML документа");
        }
    }

    /**
     * Метод используется для инициалиазции xml документов
     *
     * Привязка новых xml документов к контракту со статусов NEW и версией 0
     *
     * */
    public void addNewXmlList(@NotNull Long contractId, @NotNull List<XmlLight> xmlLightList) {

        if (xmlLightList.isEmpty()) {
            log.debug("[XmlRepository] Early return");
            return;
        }

        SqlParameterSource[] batchParams = xmlLightList.stream()
                .map(xml -> {
                    XsdLight xsd = xml.getXsdLight();
                    return new MapSqlParameterSource()
                            .addValue("name", xml.getName(), Types.VARCHAR)
                            .addValue("contract_id", contractId, Types.BIGINT)
                            .addValue("xsd_id", xsd.getId(), Types.BIGINT);
                }).toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(XmlQuery.ADD_NEW_XML_LIST.getQuery(), batchParams);

    }

}
