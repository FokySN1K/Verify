package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.XmlEntity;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Enums.XmlStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class XmlRowMapper implements RowMapper<XmlEntity> {

    @Override
    public XmlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        XmlEntity xml = new XmlEntity();

        xml.setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .setStatus(XmlStatus.valueOf(rs.getString("status")))
                .setVersion(rs.getLong("version"))
                .setReason(rs.getString("reason"))
                .setXsdId(rs.getLong("xsd_id"))
                .setCreatedTs(rs.getObject("created_ts", OffsetDateTime.class));

        Contract contract = new Contract();
        contract.setName(rs.getString("contract_name"))
                .setDescription(rs.getString("contract_description"))
                .setReason(rs.getString("contract_reason"))
                .setStatus(ContractStatus.valueOf(rs.getString("contract_status")));

        xml.setContract(contract);
        xml.setXsdId(rs.getLong("xsd_id"));

        xml.setData(rs.getString("data"));

        return xml;
    }
}