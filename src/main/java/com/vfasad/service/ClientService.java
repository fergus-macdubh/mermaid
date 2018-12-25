package com.vfasad.service;

import com.vfasad.entity.Client;
import com.vfasad.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClients() {
        return clientRepository.findAll()
                .stream()
                .filter(client -> !client.isDeleted())
                .collect(Collectors.toList());
    }

    public Client getClient(Long clientId) {
        return clientRepository.getById(clientId).orElse(null);
    }

    public void updateClient(Client client) {
        clientRepository.save(client);
    }
}
