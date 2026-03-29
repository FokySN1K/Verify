package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.XmlDocument;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class XmlDocumentRowMapper implements RowMapper<XmlDocument> {
    @Override
    public XmlDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new XmlDocument()
                .setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .setContractId(rs.getLong("contract_id"))
                .setXsdId(rs.getLong("xsd_id"))
                .setStatus(rs.getString("status"))
                .setVersion(rs.getLong("version"))
                .setData(rs.getString("data"))
                .setReason(rs.getString("reason"));
    }
}
