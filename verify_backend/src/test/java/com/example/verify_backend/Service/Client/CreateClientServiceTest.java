package com.example.verify_backend.Service.Client;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Enums.ClientRole;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.dto.CreateClientRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CreateClientService createClientService;

    @Test
    void shouldMapRequestAndPersistClient() {
        CreateClientRequest request = new CreateClientRequest()
                .setClientId("client-1")
                .setName("Ivan")
                .setSurname("Petrov")
                .setEmail("ivan@example.com")
                .setRole(ClientRole.CUSTOMER);

        createClientService.createClient(request);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).createClient(captor.capture());
        Client client = captor.getValue();
        assertThat(client.getClientId()).isEqualTo("client-1");
        assertThat(client.getName()).isEqualTo("Ivan");
        assertThat(client.getSurname()).isEqualTo("Petrov");
        assertThat(client.getEmail()).isEqualTo("ivan@example.com");
        assertThat(client.getRole()).isEqualTo(ClientRole.CUSTOMER);
    }
}
