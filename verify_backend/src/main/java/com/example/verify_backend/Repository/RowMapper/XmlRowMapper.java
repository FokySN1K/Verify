package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;


public class XmlRowMapper extends XmlLightRowMapper {

    @Override
    protected XmlLight createInstance() {
        return new Xml();
    }

    @Override
    public Xml mapRow(ResultSet rs, int rowNum) throws SQLException {
        Xml xml = (Xml) super.mapRow(rs, rowNum);
        xml.setXmlData(rs.getString("data"));
        return xml;
    }
}