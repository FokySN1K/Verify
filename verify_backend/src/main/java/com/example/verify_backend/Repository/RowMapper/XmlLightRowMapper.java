package com.example.verify_backend.Repository.RowMapper;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Enums.XsdStatus;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class XmlLightRowMapper implements RowMapper<XmlLight> {

    protected XmlLight createInstance() {
        return new XmlLight();
    }

    @Override
    public @NotNull XmlLight mapRow(ResultSet rs, int rowNum) throws SQLException {

        System.out.println("КАНАРЕЙКА");

        XmlLight xmlLight = createInstance();

        Client contractor = new Client();
        Client customer = new Client();

        Contract contract = new Contract();
        XsdLight xsdLight = new XsdLight();

        xmlLight.setId(rs.getObject("id", Long.class))
                .setName(rs.getString("name"))
                .setStatus(XmlStatus.fromStringSafe(rs.getString("status")))
                .setReason(rs.getString("reason"))
                .setVersion(rs.getObject("version", Long.class));

        contractor.setId(rs.getObject("contractor_id", Long.class))
                .setClientId(rs.getString("contractor_client_id"))
                .setRole(ClientRole.fromStringSafe(rs.getString("contractor_role")));

        customer.setId(rs.getObject("customer_id", Long.class))
                .setClientId(rs.getString("customer_client_id"))
                .setRole(ClientRole.fromStringSafe(rs.getString("customer_role")));


        xsdLight.setId(rs.getObject("xsd_id", Long.class))
                .setName(rs.getString("xsd_name"))
                .setLink(rs.getString("xsd_link"))
                .setStage(rs.getString("xsd_stage"))
                .setStatus(XsdStatus.fromStringSafe(rs.getString("xsd_status")));

        contract.setId(rs.getObject("contract_id", Long.class))
                .setName(rs.getString("contract_name"))  
                .setDescription(rs.getString("contract_description"))
                .setReason(rs.getString("reason"))
                .setStatus(ContractStatus.fromStringSafe(rs.getString("contract_status")));


        xmlLight.setContractor(contractor)
                .setCustomer(customer)
                .setXsdLight(xsdLight)
                .setContract(contract);

        return xmlLight;
    }
}