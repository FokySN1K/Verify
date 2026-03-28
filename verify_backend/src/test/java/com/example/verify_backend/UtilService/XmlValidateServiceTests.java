package com.example.verify_backend.UtilService;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class XmlValidateServiceTests {

    @Autowired
    private XmlValidateService xmlValidateService;

    @Test
    void validateSuccessXmlWithStringData() throws IOException, SAXException {
        String xml = IOUtils.resourceToString("/xml/xml_001.xml", StandardCharsets.UTF_8);
        String xsd = IOUtils.resourceToString("/xsd/success_xsd_001.xml", StandardCharsets.UTF_8);

        List<SAXParseException> exceptionList =  xmlValidateService.validate(xml, xsd);
        assertEquals(0, exceptionList.size());
    }

    @Test
    void validateFailedXmlWithStringData() throws IOException, SAXException {
        String xml = IOUtils.resourceToString("/xml/xml_001.xml", StandardCharsets.UTF_8);
        String xsd = IOUtils.resourceToString("/xsd/failed_xsd_001.xml", StandardCharsets.UTF_8);

        List<SAXParseException> exceptionList =  xmlValidateService.validate(xml, xsd);
        assertNotEquals(0, exceptionList.size());
    }

    @Test
    void validateSuccessXsdWithInputStream() throws IOException, URISyntaxException, SAXException {

        File xmlFile = new File(IOUtils.resourceToURL("/xml/xml_001.xml").toURI());
        File xsdFile = new File(IOUtils.resourceToURL("/xsd/success_xsd_001.xml").toURI());


        MockMultipartFile xmlMultipartFile = new MockMultipartFile(
                xmlFile.getName(),
                new FileInputStream(xmlFile)
        );
        MockMultipartFile xsdMultipartFile = new MockMultipartFile(
                xsdFile.getName(),
                new FileInputStream(xsdFile)
        );

        List<SAXParseException> exceptionList = xmlValidateService
                .validate(xmlMultipartFile.getInputStream(), xsdMultipartFile.getInputStream());

        assertEquals(0, exceptionList.size());
    }

    @Test
    void validateFailedXsdWithInputStream() throws IOException, URISyntaxException, SAXException {

        File xmlFile = new File(IOUtils.resourceToURL("/xml/xml_001.xml").toURI());
        File xsdFile = new File(IOUtils.resourceToURL("/xsd/failed_xsd_001.xml").toURI());


        MockMultipartFile xmlMultipartFile = new MockMultipartFile(
                xmlFile.getName(),
                new FileInputStream(xmlFile)
        );
        MockMultipartFile xsdMultipartFile = new MockMultipartFile(
                xsdFile.getName(),
                new FileInputStream(xsdFile)
        );

        List<SAXParseException> exceptionList = xmlValidateService
                .validate(xmlMultipartFile.getInputStream(), xsdMultipartFile.getInputStream());

        assertNotEquals(0, exceptionList.size());
    }

}
