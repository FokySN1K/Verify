package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum XsdQuery implements Query{
    GET_PROCESSING_XSD_LIST("/sql/get_processing_xsd_list.sql"),
    GET_XSD_BY_XSD_ID("/sql/get_xsd_by_xsd_id.sql"),
    GET_XSD_LIGHT_LIST("/sql/get_xsd_light_list.sql"),

    ;

    private final String query;

    XsdQuery(String path) {
        this.query = FileReaderService.readFileFromResources(path);
    }

    @Override
    public String getQuery() {
        return query;
    }

}
