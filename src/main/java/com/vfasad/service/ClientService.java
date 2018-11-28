package com.vfasad.service;

import com.vfasad.entity.Client;
import com.vfasad.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Client getClient(Long clientId) {
        return clientRepository.getById(clientId).orElse(null);
    }

    public void updateClient(Client client) {
        clientRepository.save(client);
    }
}
