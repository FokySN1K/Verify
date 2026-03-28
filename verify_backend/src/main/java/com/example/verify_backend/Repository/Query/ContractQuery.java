package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum ContractQuery implements Query{

    GET_CONTRACTS_BY_CLIENT_ID("/sql/get_contracts_by_client_id.sql"),
    ;

    private final String query;

    ContractQuery(String path) {
        this.query =  FileReaderService.readFileFromResources(path);
    }

    @Override
    public String getQuery() {
        return query;
    }
}
