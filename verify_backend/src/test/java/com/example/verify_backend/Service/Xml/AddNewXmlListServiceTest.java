package com.example.verify_backend.Service.Xml;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Entity.XmlLight;
import com.example.verify_backend.Enums.ContractStatus;
import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Repository.ContractRepository;
import com.example.verify_backend.Repository.XmlRepository;
import com.example.verify_backend.dto.AddNewXmlListRequest;
import com.example.verify_backend.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddNewXmlListServiceTest {

    @Mock
    private XmlRepository xmlRepository;
    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private AddNewXmlListService service;

    @Test
    void shouldMapIncomingXmlListAndPersistIt() {
        Contract contract = TestDataFactory.contract(10L, ContractStatus.PROCESSING);
        contract.setCustomer(TestDataFactory.customer("customer-1"));
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(contract));

        AddNewXmlListRequest request = new AddNewXmlListRequest()
                .setClientId("customer-1")
                .setContractId(10L)
                .setNewXmlDataList(List.of(
                        new AddNewXmlListRequest.NewXmlData().setXmlName("a.xml").setXsdId(100L),
                        new AddNewXmlListRequest.NewXmlData().setXmlName("b.xml").setXsdId(200L)
                ));

        service.addNewXmlList(request);

        ArgumentCaptor<List<XmlLight>> captor = ArgumentCaptor.forClass(List.class);
        verify(xmlRepository).addNewXmlList(org.mockito.ArgumentMatchers.eq(10L), captor.capture());
        assertThat(captor.getValue())
                .extracting(XmlLight::getName)
                .containsExactly("a.xml", "b.xml");
        assertThat(captor.getValue())
                .extracting(xml -> xml.getXsdLight().getId())
                .containsExactly(100L, 200L);
    }

    @Test
    void shouldThrowWhenContractMissing() {
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.empty());

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.addNewXmlList(new AddNewXmlListRequest().setContractId(10L)));

        assertThat(exception.getMessage()).contains("Заказа не существует");
    }

    @Test
    void shouldThrowWhenContractStatusIsInvalid() {
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(TestDataFactory.contract(10L, ContractStatus.NEW)));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.addNewXmlList(new AddNewXmlListRequest().setContractId(10L)));

        assertThat(exception.getMessage()).contains("статусе 'PROCESSING'");
    }

    @Test
    void shouldThrowWhenContractDoesNotBelongToCustomer() {
        Contract contract = TestDataFactory.contract(10L, ContractStatus.PROCESSING);
        contract.setCustomer(TestDataFactory.customer("customer-1"));
        when(contractRepository.getContractByContractId(10L)).thenReturn(Optional.of(contract));

        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> service.addNewXmlList(
                        new AddNewXmlListRequest().setContractId(10L).setClientId("other").setNewXmlDataList(List.of())
                ));

        assertThat(exception.getMessage()).contains("Ошибка в получении заказа");
    }
}
