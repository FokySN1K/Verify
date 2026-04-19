package com.example.verify_backend.Service.Contract;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.dto.ChangeContractStatusRequest;
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
class ChangeContractStatusServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ChangeContractStatusService service;

    @Test
    void shouldChangeStatusWhenCustomerOwnsContract() {
        Contract contract = TestDataFactory.contract(1L, ContractStatus.NEW);
        contract.setCustomer(TestDataFactory.customer("customer-1"));
        when(contractRepository.getContractByContractId(1L)).thenReturn(Optional.of(contract));

        ChangeContractStatusRequest request = new ChangeContractStatusRequest()
                .setContractId(1L)
                .setClientId("customer-1")
                .setContractStatus(ContractStatus.DONE);

        service.changeContractStatus(request);

        verify(contractRepository).changeContractStatus(1L, ContractStatus.DONE);
    }

    @Test
    void shouldThrowWhenContractMissing() {
        when(contractRepository.getContractByContractId(1L)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeContractStatus(new ChangeContractStatusRequest().setContractId(1L)));

        assertThat(exception.getMessage()).contains("Не найден заказ");
    }

    @Test
    void shouldThrowWhenClientIsNotCustomer() {
        when(contractRepository.getContractByContractId(1L)).thenReturn(Optional.of(TestDataFactory.contract(1L, ContractStatus.NEW)));

        ChangeContractStatusRequest request = new ChangeContractStatusRequest()
                .setContractId(1L)
                .setClientId("other")
                .setContractStatus(ContractStatus.DONE);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeContractStatus(request));

        assertThat(exception.getMessage()).contains("только заказчик");
    }

    @Test
    void shouldThrowWhenCurrentStatusIsNotNewOrProcessing() {
        when(contractRepository.getContractByContractId(1L)).thenReturn(Optional.of(TestDataFactory.contract(1L, ContractStatus.DONE)));

        ChangeContractStatusRequest request = new ChangeContractStatusRequest()
                .setContractId(1L)
                .setClientId("customer-1")
                .setContractStatus(ContractStatus.FAILED);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeContractStatus(request));

        assertThat(exception.getMessage()).contains("текущий статус 'NEW' или 'PROCESSING'");
    }

    @Test
    void shouldThrowWhenTargetStatusIsInvalid() {
        when(contractRepository.getContractByContractId(1L)).thenReturn(Optional.of(TestDataFactory.contract(1L, ContractStatus.NEW)));

        ChangeContractStatusRequest request = new ChangeContractStatusRequest()
                .setContractId(1L)
                .setClientId("customer-1")
                .setContractStatus(ContractStatus.PROCESSING);

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.changeContractStatus(request));

        assertThat(exception.getMessage()).contains("только на статус 'DONE' или 'FAILED'");
    }
}
