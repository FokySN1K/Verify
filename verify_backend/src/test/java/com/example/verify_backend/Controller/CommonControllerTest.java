package com.example.verify_backend.Controller;

import com.example.verify_backend.Config.ExceptionConfig;
import com.example.verify_backend.Service.Client.CreateClientService;
import com.example.verify_backend.Service.Contract.ChangeContractStatusService;
import com.example.verify_backend.Service.Validation.ValidateWithXsdDataService;
import com.example.verify_backend.Service.Validation.ValidateWithXsdFilesService;
import com.example.verify_backend.Service.Xml.ChangeXmlStatusService;
import com.example.verify_backend.Service.Xml.GetXmlDataService;
import com.example.verify_backend.Service.Xml.GetXmlLightInfoListService;
import com.example.verify_backend.Service.Xsd.GetXsdLightListService;
import com.example.verify_backend.dto.GetXsdLightListResponse;
import com.example.verify_backend.dto.Result;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommonControllerTest {

    @Mock private ValidateWithXsdDataService validateWithXsdDataService;
    @Mock private ValidateWithXsdFilesService validateWithXsdFilesService;
    @Mock private CreateClientService createClientService;
    @Mock private ChangeXmlStatusService changeXmlStatusService;
    @Mock private ChangeContractStatusService changeContractStatusService;
    @Mock private GetXmlDataService getXmlDataService;
    @Mock private GetXmlLightInfoListService getXmlLightInfoListService;
    @Mock private GetXsdLightListService getXsdLightListService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CommonController controller = new CommonController(
                validateWithXsdDataService,
                validateWithXsdFilesService,
                createClientService,
                changeXmlStatusService,
                changeContractStatusService,
                getXmlDataService,
                getXmlLightInfoListService,
                getXsdLightListService
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionConfig())
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldCreateClient() throws Exception {
        when(createClientService.createClient(any())).thenReturn(new Result());

        mockMvc.perform(post(CommonController.CREATE_CLIENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "client_id": "client-1",
                                  "name": "Ivan",
                                  "surname": "Petrov",
                                  "role": "CUSTOMER",
                                  "email": "ivan@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(createClientService).createClient(any());
    }

    @Test
    void shouldReturnBadRequestForInvalidCreateClientPayload() throws Exception {
        mockMvc.perform(post(CommonController.CREATE_CLIENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void shouldReturnXsdLightList() throws Exception {
        when(getXsdLightListService.getXsdLightList())
                .thenReturn(new GetXsdLightListResponse().setXsdLightList(List.of(TestDataFactory.xsdLight(1L))));

        mockMvc.perform(post(CommonController.GET_XSD_LIGHT_LIST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.xsd_light_list[0].id").value(1));
    }
}
