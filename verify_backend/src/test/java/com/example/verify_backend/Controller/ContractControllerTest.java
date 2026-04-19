package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Contract.GetContractsService;
import com.example.verify_backend.Service.Xml.AddNewXmlListService;
import com.example.verify_backend.dto.GetContractsResponse;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock private GetContractsService getContractsService;
    @Mock private AddNewXmlListService addNewXmlListService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new ContractController(getContractsService, addNewXmlListService))
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldReturnContracts() throws Exception {
        when(getContractsService.getContracts(any()))
                .thenReturn(new GetContractsResponse().setContractList(List.of(TestDataFactory.contract(1L, com.example.verify_backend.Enums.ContractStatus.NEW))));

        mockMvc.perform(post(ContractController.GET_CONTRACTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"client_id\":\"client-1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contract_list[0].id").value(1));
    }
}
