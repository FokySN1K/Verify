package com.example.verify_backend.support;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.Xml;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Entity.Xsd;
import com.example.verify_backend.Entity.XsdLight;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Enums.XsdStatus;

import java.time.LocalDate;

public final class TestDataFactory {

    public static final String DEFAULT_CUSTOMER_CLIENT_ID = "customer-1";
    public static final String DEFAULT_CONTRACTOR_CLIENT_ID = "contractor-1";
    public static final Long DEFAULT_CUSTOMER_ID = 11L;
    public static final Long DEFAULT_CONTRACTOR_ID = 22L;
    public static final LocalDate DEFAULT_BEGIN_DATE = LocalDate.of(2025, 1, 1);
    public static final LocalDate DEFAULT_END_DATE = LocalDate.of(2030, 12, 31);

    private TestDataFactory() {
    }

    public static Client customer() {
        return customer(DEFAULT_CUSTOMER_ID, DEFAULT_CUSTOMER_CLIENT_ID);
    }

    public static Client customer(String clientId) {
        return customer(DEFAULT_CUSTOMER_ID, clientId);
    }

    public static Client customer(Long id, String clientId) {
        return client(id, clientId, "Customer", "User", "customer@example.com", ClientRole.CUSTOMER);
    }

    public static Client contractor() {
        return contractor(DEFAULT_CONTRACTOR_ID, DEFAULT_CONTRACTOR_CLIENT_ID);
    }

    public static Client contractor(String clientId) {
        return contractor(DEFAULT_CONTRACTOR_ID, clientId);
    }

    public static Client contractor(Long id, String clientId) {
        return client(id, clientId, "Contractor", "User", "contractor@example.com", ClientRole.CONTRACTOR);
    }

    public static Client client(Long id,
                                String clientId,
                                String name,
                                String surname,
                                String email,
                                ClientRole role) {
        return new Client()
                .setId(id)
                .setClientId(clientId)
                .setName(name)
                .setSurname(surname)
                .setEmail(email)
                .setRole(role);
    }

    public static Contract contract() {
        return contract(1L, ContractStatus.NEW);
    }

    public static Contract contract(Long id, ContractStatus status) {
        return contract(id, status, customer(), contractor());
    }

    public static Contract contract(Long id, ContractStatus status, Client customer, Client contractor) {
        return new Contract()
                .setId(id)
                .setName("Contract " + id)
                .setDescription("description-" + id)
                .setReason("reason-" + id)
                .setStatus(status)
                .setCustomer(customer)
                .setContractor(contractor);
    }

    public static Xsd xsd() {
        return xsd(1L);
    }

    public static Xsd xsd(Long id) {
        Xsd xsd = new Xsd();
        xsd.setId(id);
        xsd.setName("schema-" + id);
        xsd.setStage("stage-" + id);
        xsd.setBeginDate(DEFAULT_BEGIN_DATE);
        xsd.setEndDate(DEFAULT_END_DATE);
        xsd.setLink("https://example.com/xsd/" + id);
        xsd.setStatus(XsdStatus.PROCESSING);
        xsd.setVersion(1L);
        xsd.setXsdData(validXsd());
        return xsd;
    }

    public static XsdLight xsdLight() {
        return xsdLight(1L);
    }

    public static XsdLight xsdLight(Long id) {
        return new XsdLight()
                .setId(id)
                .setName("schema-" + id)
                .setStage("stage-" + id)
                .setBeginDate(DEFAULT_BEGIN_DATE)
                .setEndDate(DEFAULT_END_DATE)
                .setLink("https://example.com/xsd/" + id)
                .setStatus(XsdStatus.PROCESSING)
                .setVersion(1L);
    }

    public static XmlLight xmlLight(Long xmlId, Long contractId, Long xsdId, XmlStatus status, Long version) {
        Client customer = customer();
        Client contractor = contractor();
        Contract contract = contract(contractId, ContractStatus.PROCESSING, customer, contractor);

        return new XmlLight()
                .setId(xmlId)
                .setName("doc.xml")
                .setStatus(status)
                .setReason("reason-" + xmlId)
                .setVersion(version)
                .setCustomer(customer)
                .setContractor(contractor)
                .setContract(contract)
                .setXsdLight(xsdLight(xsdId));
    }

    public static Xml xml(Long xmlId) {
        return xml(xmlId, 99L, 77L, XmlStatus.CHECKING, 1L);
    }

    public static Xml xml(Long xmlId, Long contractId, Long xsdId, XmlStatus status, Long version) {
        XmlLight xmlLight = xmlLight(xmlId, contractId, xsdId, status, version);

        Xml xml = new Xml();
        xml.setId(xmlLight.getId());
        xml.setName(xmlLight.getName());
        xml.setStatus(xmlLight.getStatus());
        xml.setReason(xmlLight.getReason());
        xml.setVersion(xmlLight.getVersion());
        xml.setCustomer(xmlLight.getCustomer());
        xml.setContractor(xmlLight.getContractor());
        xml.setContract(xmlLight.getContract());
        xml.setXsdLight(xmlLight.getXsdLight());
        xml.setXmlData(validXml());
        return xml;
    }

    public static String validXml() {
        return "<person><name>Ivan</name></person>";
    }

    public static String validXsd() {
        return """
                <xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">
                    <xs:element name=\"person\">
                        <xs:complexType>
                            <xs:sequence>
                                <xs:element name=\"name\" type=\"xs:string\"/>
                            </xs:sequence>
                        </xs:complexType>
                    </xs:element>
                </xs:schema>
                """;
    }

    public static String invalidXsdForXml() {
        return """
                <xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">
                    <xs:element name=\"person\">
                        <xs:complexType>
                            <xs:sequence>
                                <xs:element name=\"age\" type=\"xs:int\"/>
                            </xs:sequence>
                        </xs:complexType>
                    </xs:element>
                </xs:schema>
                """;
    }
}
