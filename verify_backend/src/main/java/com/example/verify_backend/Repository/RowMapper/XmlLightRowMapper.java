package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.ContractStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class XmlLightRowMapper implements RowMapper<XmlLight> {
    @Override
    public @Nullable XmlLight mapRow(ResultSet rs, int rowNum) throws SQLException {

        Contract contract = new Contract();
        Client customer = new Client();
        Client contractor = new Client();

        customer.setName(rs.getString("customer_name"))
                .setSurname(rs.getString("customer_surname"))
                .setEmail(rs.getString("customer_email"));

        contractor.setName(rs.getString("contractor_name"))
                .setSurname(rs.getString("contractor_surname"))
                .setEmail(rs.getString("contractor_email"));

        contract.setName(rs.getString("name"))
                .setDescription(rs.getString("description"))
                .setStatus(ContractStatus.fromStringSafe(rs.getString("status")))
                .setReason(rs.getString("reason"))
                .setContractor(contractor)
                .setCustomer(customer);

        return contract;
    }
}

