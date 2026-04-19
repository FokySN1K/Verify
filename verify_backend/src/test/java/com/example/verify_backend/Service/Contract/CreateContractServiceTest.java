package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.CreateContractRequest;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateContractServiceTest {

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CreateContractService createContractService;

    @Test
    void shouldCreateContractForCustomer() {
        Client customer = TestDataFactory.customer("customer-1");
        when(clientRepository.getClient("customer-1")).thenReturn(Optional.of(customer));

        CreateContractRequest request = new CreateContractRequest()
                .setClientId("customer-1")
                .setContractName("Important contract")
                .setContractDescription("description");

        createContractService.createContract(request);

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        verify(contractRepository).createContract(captor.capture());
        Contract saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Important contract");
        assertThat(saved.getDescription()).isEqualTo("description");
        assertThat(saved.getCustomer().getClientId()).isEqualTo("customer-1");
    }

    @Test
    void shouldThrowWhenClientDoesNotExist() {
        when(clientRepository.getClient("customer-1")).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> createContractService.createContract(new CreateContractRequest().setClientId("customer-1")));

        assertThat(exception.getMessage()).contains("Клиента не существует");
    }

    @Test
    void shouldThrowWhenClientIsNotCustomer() {
        Client contractor = TestDataFactory.contractor("contractor-1");
        contractor.setRole(ClientRole.CONTRACTOR);
        when(clientRepository.getClient("contractor-1")).thenReturn(Optional.of(contractor));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> createContractService.createContract(new CreateContractRequest().setClientId("contractor-1")));

        assertThat(exception.getMessage()).contains("Создавать заказ может только заказчик");
    }
}
