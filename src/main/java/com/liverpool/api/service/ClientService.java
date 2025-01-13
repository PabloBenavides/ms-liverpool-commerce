package com.liverpool.api.service;

import com.liverpool.api.dto.Address;
import com.liverpool.api.dto.Client;
import com.liverpool.api.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static com.liverpool.api.commons.enums.ActionsEnum.CREATED;
import static com.liverpool.api.commons.enums.ActionsEnum.UPDATED;
import static com.liverpool.api.commons.lastmodification.LastModificationCreator.createLastModification;

@Service
public class ClientService {

    private static final String MSG_CLIENT_NOT_FOUND = "Cliente no encontrado con ID: %s.";
    private static final String MSG_CLIENT_DUPLICATED = "Ya existe un Cliente con el Correo Electronico %s.";
    private static final String MSG_EMAIL_CLIENT_NOT_FOUND = "Cliente no encontrado con email: %s.";
    private static final String MSG_ID_NULL = "El Cliente no puede ser actualizado, el ID no debe ir null.";
    private static final String MSG_ADDRESS_NOT_FOUND = "Dirección no encontrada con el nombre especificado.";
    private static final String MSG_DUPLICATED_ADDRESS = "Ya existe una dirección con el nombre especificado.";

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        boolean existsEmail = clientRepository.findByEmail(client.getEmail().toUpperCase()).isPresent();

        if(existsEmail){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(MSG_CLIENT_DUPLICATED, client.getEmail())
            );
        }
        client.setCreationDate(LocalDateTime.now());
        client.setLastModification(createLastModification(CREATED.getActionText(), "system"));
        return clientRepository.save(client);
    }

    public Client findById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format(MSG_CLIENT_NOT_FOUND, id)));
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format(MSG_EMAIL_CLIENT_NOT_FOUND, email)));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client updateClient(Client client) {
        if (client.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    MSG_ID_NULL
            );
        }

        Client existingClient = clientRepository.findById(client.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format(MSG_CLIENT_NOT_FOUND, client.getId())
                ));

        existingClient.setFirstName(client.getFirstName());
        existingClient.setMiddleName(client.getMiddleName());
        existingClient.setPaternalLastName(client.getPaternalLastName());
        existingClient.setMaternalLastName(client.getMaternalLastName());

        existingClient.setLastModification(createLastModification("ACTUALIZADO", "system"));

        return clientRepository.save(existingClient);
    }


    public Client addAddress(String clientId, Address address) {
        Client client = findById(clientId);

        boolean addressExists = client.getAddresses().stream()
                .anyMatch(existingAddress -> existingAddress.getAddressName().equalsIgnoreCase(address.getAddressName()));

        if (addressExists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    MSG_DUPLICATED_ADDRESS
            );
        }

        client.getAddresses().add(address);
        client.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return clientRepository.save(client);
    }

    public Client updateAddress(String clientId, String addressName, Address address) {
        Client client = findById(clientId);

        boolean addressUpdated = client.getAddresses().removeIf(updatedAddress -> updatedAddress.getAddressName().equals(addressName.toUpperCase()));

        if (!addressUpdated) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    MSG_ADDRESS_NOT_FOUND
            );
        }

        client.getAddresses().add(address);
        client.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return clientRepository.save(client);
    }

    public Client deleteAddress(String clientId, String addressName) {
        Client client = findById(clientId);

        boolean removed = client.getAddresses().removeIf(address -> address.getAddressName().equals(addressName.toUpperCase()));

        if (!removed) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    MSG_ADDRESS_NOT_FOUND
            );
        }

        client.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return clientRepository.save(client);
    }

    public boolean deleteClient(String id) {
        if (!clientRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format(MSG_CLIENT_NOT_FOUND, id));
        }

        clientRepository.deleteById(id);
        return true;
    }
}
