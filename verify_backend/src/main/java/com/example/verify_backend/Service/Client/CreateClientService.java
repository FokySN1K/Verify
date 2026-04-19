package com.example.verify_backend.Service.Client;

import com.example.verify_backend.Entity.Client;
import com.example.verify_backend.Repository.ClientRepository;
import com.example.verify_backend.dto.CreateClientRequest;
import com.example.verify_backend.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClientService {

    private final ClientRepository clientRepository;

    public Result createClient(CreateClientRequest request) {

        log.info("[createClient] Operation started");
        Client client = new Client()
                .setClientId(request.getClientId())
                .setName(request.getName())
                .setSurname(request.getSurname())
                .setEmail(request.getEmail())
                .setRole(request.getRole());

        clientRepository.createClient(client);
        log.info("[createClient] Operation finished");
        return new Result();
    }


}
