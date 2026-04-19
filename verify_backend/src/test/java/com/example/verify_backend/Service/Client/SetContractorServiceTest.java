package com.example.verify_backend.Service.Client;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.SetContractorRequest;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SetContractorServiceTest {

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private SetContractorService setContractorService;

    @Test
    void shouldAssignContractorToNewContract() {
        Contract contract = TestDataFactory.contract(10L, ContractStatus.NEW);
        contract.setCustomer(TestDataFactory.customer("customer-42"));
        Client contractor = TestDataFactory.contractor("contractor-77");
        SetContractorRequest request = new SetContractorRequest()
                .setContractId(10L)
                .setCustomerClientId("customer-42")
                .setContractorClientId("contractor-77");

        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(contract));
        when(clientRepository.getClient("contractor-77")).thenReturn(Optional.of(contractor));

        setContractorService.setContractorService(request);

        verify(contractRepository).setContractorForContract(10L, "contractor-77");
    }

    @Test
    void shouldThrowWhenContractMissing() {
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> setContractorService.setContractorService(new SetContractorRequest().setContractId(10L)));

        assertThat(exception.getMessage()).contains("Не найден заказ");
    }

    @Test
    void shouldThrowWhenContractorMissing() {
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(TestDataFactory.contract(10L, ContractStatus.NEW)));
        when(clientRepository.getClient("contractor-77")).thenReturn(Optional.empty());

        SetContractorRequest request = new SetContractorRequest()
                .setContractId(10L)
                .setContractorClientId("contractor-77")
                .setCustomerClientId("customer-1");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> setContractorService.setContractorService(request));

        assertThat(exception.getMessage()).contains("Не найден подрядчик");
    }

    @Test
    void shouldThrowWhenContractStatusIsNotNew() {
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(TestDataFactory.contract(10L, ContractStatus.PROCESSING)));
        when(clientRepository.getClient("contractor-1")).thenReturn(Optional.of(TestDataFactory.contractor("contractor-1")));

        SetContractorRequest request = new SetContractorRequest()
                .setContractId(10L)
                .setContractorClientId("contractor-1")
                .setCustomerClientId("customer-1");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> setContractorService.setContractorService(request));

        assertThat(exception.getMessage()).contains("статусом NEW");
    }

    @Test
    void shouldThrowWhenCustomerDoesNotOwnContract() {
        Contract contract = TestDataFactory.contract(10L, ContractStatus.NEW);
        contract.setCustomer(TestDataFactory.customer("customer-1"));
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(contract));
        when(clientRepository.getClient("contractor-1")).thenReturn(Optional.of(TestDataFactory.contractor("contractor-1")));

        SetContractorRequest request = new SetContractorRequest()
                .setContractId(10L)
                .setContractorClientId("contractor-1")
                .setCustomerClientId("other-customer");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> setContractorService.setContractorService(request));

        assertThat(exception.getMessage()).contains("Заказ не принадлежит клиенту");
    }

    @Test
    void shouldThrowWhenSelectedClientIsNotContractor() {
        Contract contract = TestDataFactory.contract(10L, ContractStatus.NEW);
        Client wrongRoleClient = TestDataFactory.customer("customer-2");
        wrongRoleClient.setRole(ClientRole.CUSTOMER);
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(contract));
        when(clientRepository.getClient("customer-2")).thenReturn(Optional.of(wrongRoleClient));

        SetContractorRequest request = new SetContractorRequest()
                .setContractId(10L)
                .setContractorClientId("customer-2")
                .setCustomerClientId("customer-1");

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> setContractorService.setContractorService(request));

        assertThat(exception.getMessage()).contains("не является подрядчиком");
    }
}
