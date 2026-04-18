package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum XmlQuery implements Query{
    ADD_NEW_XML_LIST("/sql/add_new_xml_list.sql"),
    ;

    private final String query;

    XmlQuery(String path) {
        this.query = FileReaderService.readFileFromResources(path);
    }

    @Override
    public String getQuery() {
        return query;
    }

}
