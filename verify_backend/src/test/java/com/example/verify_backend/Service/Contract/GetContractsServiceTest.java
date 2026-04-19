package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.GetContractsRequest;
import com.example.verify_backend.dto.GetContractsResponse;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetContractsServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private GetContractsService getContractsService;

    @Test
    void shouldReturnContractsFromRepository() {
        List<Contract> contracts = List.of(TestDataFactory.contract(1L, com.example.verify_backend.Enums.ContractStatus.NEW));
        when(contractRepository.getContracts("client-1")).thenReturn(contracts);

        GetContractsResponse response = getContractsService.getContracts(new GetContractsRequest().setClient_id("client-1"));

        assertThat(response.getContractList()).isEqualTo(contracts);
    }
}
