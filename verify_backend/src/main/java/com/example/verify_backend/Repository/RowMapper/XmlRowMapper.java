package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;


@Slf4j
public class XmlRowMapper extends XmlLightRowMapper {

    @Override
    protected XmlLight createInstance() {
        return new Xml();
    }

    @Override
    public Xml mapRow(ResultSet rs, int rowNum) throws SQLException {
        Xml xml = (Xml) super.mapRow(rs, rowNum);
        try {
            if(xml != null){
                xml.setXmlData(rs.getString("data"));
            }else{
                throw new RuntimeException("xml is null! com.example.verify_backend.Repository.RowMapper.mapRow");
            }
        } catch (SQLException e) {
            log.error("[{}] An exception occurred.", "mapRow", e);
            throw new RuntimeException(e);
        }

        return xml;
    }
}