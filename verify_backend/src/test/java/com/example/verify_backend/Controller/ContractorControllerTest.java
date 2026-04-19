package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Xml.AddNewXmlListService;
import com.example.verify_backend.Service.Xml.AddNewXmlVersionService;
import com.example.verify_backend.dto.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContractorControllerTest {

    @Mock private AddNewXmlListService addNewXmlListService;
    @Mock private AddNewXmlVersionService addNewXmlVersionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new ContractorController(addNewXmlListService, addNewXmlVersionService))
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldAddNewXmlVersion() throws Exception {
        when(addNewXmlVersionService.addNewXmlVersion(any())).thenReturn(new Result());

        mockMvc.perform(post(ContractorController.ADD_NEW_XML_VERSION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "xml_name": "doc.xml",
                                  "xsd_id": 1,
                                  "contract_id": 2,
                                  "client_id": "contractor-1",
                                  "xml_data": "<person><name>Ivan</name></person>"
                                }
                                """))
                .andExpect(status().isOk());

        verify(addNewXmlVersionService).addNewXmlVersion(any());
    }
}
