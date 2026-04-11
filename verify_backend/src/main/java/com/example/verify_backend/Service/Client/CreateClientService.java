package com.example.verify_backend.Service.Client;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.dto.CreateClientRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateClientService {

    private final ClientRepository clientRepository;

    public Result createClient(CreateClientRequest request) {

        // TODO Лучше map struct, разве нет?
        // TODO переписать на маппер
        Client client = new Client()
                .setClientId(request.getClientId())
                .setName(request.getName())
                .setSurname(request.getSurname())
                .setEmail(request.getEmail())
                .setRole(request.getRole());

        clientRepository.createClient(client);
        return new Result();
    }


}
