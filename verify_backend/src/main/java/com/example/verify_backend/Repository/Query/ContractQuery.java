package com.example.verify_backend.Repository.Query;

public enum ContractQuery implements Query{

    GET_CONTRACTS_BY_CLIENT_ID("/sql/get_contracts_by_client_id.sql"),
    ;

    private final String path;
    private final String query;

    ContractQuery(String path) {
        this.path = path;
        this.query = "123";
    }


    @Override
    public String getQuery() {
        return "";
    }
}
