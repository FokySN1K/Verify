package com.example.verify_backend.Controller;

import com.example.verify_backend.Service.Client.SetContractorService;
import com.example.verify_backend.Service.Contract.CreateContractService;
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
class CustomerControllerTest {

    @Mock private CreateContractService createContractService;
    @Mock private SetContractorService setContractorService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(createContractService, setContractorService))
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldCreateContract() throws Exception {
        when(createContractService.createContract(any())).thenReturn(new Result());

        mockMvc.perform(post(CustomerController.CREATE_CONTRACT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "client_id": "customer-1",
                                  "contract_name": "Contract",
                                  "contract_description": "Description"
                                }
                                """))
                .andExpect(status().isOk());

        verify(createContractService).createContract(any());
    }

    @Test
    void shouldSetContractor() throws Exception {
        when(setContractorService.setContractorService(any())).thenReturn(new Result());

        mockMvc.perform(post(CustomerController.SET_CONTRACTOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "contract_id": 1,
                                  "customer_client_id": "customer-1",
                                  "contractor_client_id": "contractor-1"
                                }
                                """))
                .andExpect(status().isOk());

        verify(setContractorService).setContractorService(any());
    }
}
