package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum XsdQuery implements Query {

    GET_LIGHT_XSD("/sql/get_light_xsd.sql");

    private final String query;

    XsdQuery(String query) {
        this.query = FileReaderService.readFileFromResources(query);
    }

    @Override
    public String getQuery() {
        return query;
    }

}
