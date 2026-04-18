package com.example.verify_backend.Repository.Query;

import com.example.verify_backend.UtilService.FileReaderService;

public enum ContractQuery implements Query{

    CREATE_CONTRACT("/sql/create_contract.sql"),
    GET_CONTRACTS("/sql/get_contracts.sql"),
    SET_CONTRACTOR_FOR_CONTRACT("/sql/set_contractor_for_contract.sql"),
    CHANGE_CONTRACT_STATUS("/sql/change_contract_status.sql")
    ;

    private final String query;

    ContractQuery(String path) {
        this.query = FileReaderService.readFileFromResources(path);
    }

    @Override
    public String getQuery() {
        return query;
    }
}
