package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum ClientQuery implements Query{
    CREATE_CLIENT("/sql/create_client.sql")
    ;

    private final String query;

    ClientQuery(String path) {
        this.query = FileReaderService.readFileFromResources(path);
    }

    @Override
    public String getQuery() {
        return query;
    }

}
