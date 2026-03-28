package com.example.verify_backend.UtilService;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.XmlErrorHandler;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class XmlValidateService {

    private final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    // Валидация по тексту выгруженному в память
    public List<SAXParseException> validate(String xml, String xsd) throws SAXException, IOException {
        Schema schema = schemaFactory.newSchema(new StreamSource(new StringReader(xsd)));
        Validator validator = schema.newValidator();

        XmlErrorHandler errorHandler = new XmlErrorHandler();
        validator.setErrorHandler(errorHandler);

        validator.validate(new StreamSource(new StringReader(xml)));

        return getSaxParseExceptions(errorHandler);
    }

    // Валидация по файлам
    public List<SAXParseException> validate(InputStream xml, InputStream xsd) throws SAXException, IOException {
        Schema schema = schemaFactory.newSchema(new StreamSource(xsd));
        Validator validator = schema.newValidator();

        XmlErrorHandler errorHandler = new XmlErrorHandler();
        validator.setErrorHandler(errorHandler);

        validator.validate(new StreamSource(xml));

        return getSaxParseExceptions(errorHandler);
    }

    @NonNull
    private List<SAXParseException> getSaxParseExceptions(XmlErrorHandler errorHandler) {
        List<SAXParseException> exceptionList = new ArrayList<>();

        if (!errorHandler.getErrors().isEmpty()) {
            exceptionList.addAll(errorHandler.getErrors());
        }
        if (!errorHandler.getWarnings().isEmpty()) {
            exceptionList.addAll(errorHandler.getWarnings());
        }

        return exceptionList;
    }


}
