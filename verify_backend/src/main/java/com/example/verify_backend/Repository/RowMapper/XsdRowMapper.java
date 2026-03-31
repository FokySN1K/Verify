package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.XsdEntity;
import com.example.verify_backend.Enums.XsdStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class XsdRowMapper implements RowMapper<XsdEntity> {

    @Override
    public XsdEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        XsdEntity xsd = new XsdEntity();
        
        xsd.setId(rs.getLong("id"));
        xsd.setStage(rs.getString("stage"));
        xsd.setName(rs.getString("name"));
        
        LocalDate beginDate = rs.getObject("begin_date", LocalDate.class);
        xsd.setBeginDate(beginDate);
        
        LocalDate endDate = rs.getObject("end_date", LocalDate.class);
        xsd.setEndDate(endDate);
        
        String data = rs.getString("data");
        if (data != null) {
            xsd.setData(data);
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            xsd.setStatus(XsdStatus.valueOf(statusStr));
        }
        
        xsd.setLink(rs.getString("link"));
        xsd.setVersion(rs.getLong("version"));
        
        return xsd;
    }
}