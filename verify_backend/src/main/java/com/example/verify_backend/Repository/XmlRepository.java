package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Repository.Query.XmlQuery;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class XmlRepository {


    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Выдаём облегченную информацию по xml договора
     *
     *
     */
    public List<XmlLight> getXmlLightInfoList(@NotNull Long contractId) {

    }

    public void getXmlInfo(@NotNull Long xmlId) {

    }

    /**
     * Создаём новую версию xml документа
     *
     * */
    public void changeXmlData() {

    }

    public void changeXmlStatus() {

    }

    /**
     * Создание новой версии xml документа.
     * Привязка версии +1 к существующему xml документу
     *
     *
     * */
    public void addNewVersionXml() {

    }

    /**
     * Метод используется для инициалиазции xml документов
     *
     * Привязка новых xml документов к контракту со статусов NEW и версией 0
     *
     * */
    public void addNewXmlList(Long contractorId, List<XmlLight> xmlLightList) {

        if (xmlLightList.isEmpty()) {
            return;
        }

        SqlParameterSource[] batchParams = xmlLightList.stream()
                .map(xml -> {
                    XsdLight xsd = xml.getXsdLight();
                    return new MapSqlParameterSource()
                            .addValue("name", xml.getName(), Types.VARCHAR)
                            .addValue("contract_id", contractorId, Types.BIGINT)
                            .addValue("xsd_id", xsd.getId(), Types.BIGINT);
                }).toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(XmlQuery.ADD_NEW_XML_LIST.getQuery(), batchParams);

    }



}
