package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum XmlQuery implements Query {

    GET_LIGHT_XML("/sql/get_light_xml.sql"),
    GET_XML("/sql/get_xml.sql"),;

    private final String query;

    XmlQuery(String query) {
        this.query = FileReaderService.readFileFromResources(query);
    }


    @Override
    public String getQuery() {
        return query;
    }

}
