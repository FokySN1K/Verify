package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.XmlDocument;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Repository.RowMapper.XmlDocumentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class XmlRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<XmlDocument> getAccessibleXml(String clientId, Long orderId, Long xmlId) {
        String sql = """
                select x.id,
                       x.name,
                       x.contract_id,
                       x.xsd_id,
                       x.status::text as status,
                       x.version,
                       x.data::text as data,
                       x.reason
                  from xml x
                  join contract ct on ct.id = x.contract_id
                  join client c on c.id in (ct.customer_id, ct.contractor_id)
                 where c.client_id = :client_id
                   and ct.id = :order_id
                   and x.id = :xml_id
                """;

        List<XmlDocument> result = jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("client_id", clientId, Types.VARCHAR)
                        .addValue("order_id", orderId, Types.BIGINT)
                        .addValue("xml_id", xmlId, Types.BIGINT),
                new XmlDocumentRowMapper());

        return result.stream().findFirst();
    }

    public Optional<XmlDocument> getContractorOwnedXml(String clientId, Long orderId, Long xmlId) {
        String sql = """
                select x.id,
                       x.name,
                       x.contract_id,
                       x.xsd_id,
                       x.status::text as status,
                       x.version,
                       x.data::text as data,
                       x.reason
                  from xml x
                  join contract ct on ct.id = x.contract_id
                  join client c on c.id = ct.contractor_id
                 where c.client_id = :client_id
                   and ct.id = :order_id
                   and x.id = :xml_id
                """;

        List<XmlDocument> result = jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("client_id", clientId, Types.VARCHAR)
                        .addValue("order_id", orderId, Types.BIGINT)
                        .addValue("xml_id", xmlId, Types.BIGINT),
                new XmlDocumentRowMapper());

        return result.stream().findFirst();
    }

    public void updateXmlStatus(Long xmlId, XmlStatus status, String reason) {
        String sql = """
                update xml
                   set status = cast(:status as xml_status),
                       reason = :reason
                 where id = :xml_id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("xml_id", xmlId, Types.BIGINT)
                        .addValue("status", status.name(), Types.VARCHAR)
                        .addValue("reason", reason, Types.VARCHAR));
    }

    public int addNewXmlListForContract(Long orderId) {
        String sql = """
                insert into xml (name, contract_id, xsd_id, status, version, data, reason)
                select xsd.name,
                       :order_id,
                       xsd.id,
                       'NEW'::xml_status,
                       1,
                       xmlparse(document '<xml/>'),
                       null
                  from xsd
                 where xsd.status = 'PROCESSING'::xsd_status
                   and not exists (
                        select 1
                          from xml x
                         where x.contract_id = :order_id
                           and x.xsd_id = xsd.id
                   )
                """;

        return jdbcTemplate.update(sql,
                new MapSqlParameterSource().addValue("order_id", orderId, Types.BIGINT));
    }

    public boolean existsProcessingXsd(Long xsdId) {
        String sql = """
                select count(*)
                  from xsd
                 where id = :xsd_id
                   and status = 'PROCESSING'::xsd_status
                """;

        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("xsd_id", xsdId, Types.BIGINT),
                Integer.class);

        return count != null && count > 0;
    }

    public Long nextVersion(Long orderId, Long xsdId) {
        String sql = """
                select coalesce(max(version), 0) + 1
                  from xml
                 where contract_id = :order_id
                   and xsd_id = :xsd_id
                """;

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("order_id", orderId, Types.BIGINT)
                        .addValue("xsd_id", xsdId, Types.BIGINT),
                Long.class);
    }

    public void markOldVersions(Long orderId, Long xsdId) {
        String sql = """
                update xml
                   set status = 'OLD'::xml_status
                 where contract_id = :order_id
                   and xsd_id = :xsd_id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("order_id", orderId, Types.BIGINT)
                        .addValue("xsd_id", xsdId, Types.BIGINT));
    }

    public void insertNewVersion(String name, Long orderId, Long xsdId, Long version, String data) {
        String sql = """
                insert into xml (name, contract_id, xsd_id, status, version, data, reason)
                values (:name,
                        :order_id,
                        :xsd_id,
                        'NEW'::xml_status,
                        :version,
                        cast(:data as xml),
                        null)
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("name", name, Types.VARCHAR)
                        .addValue("order_id", orderId, Types.BIGINT)
                        .addValue("xsd_id", xsdId, Types.BIGINT)
                        .addValue("version", version, Types.BIGINT)
                        .addValue("data", data, Types.VARCHAR));
    }
}
