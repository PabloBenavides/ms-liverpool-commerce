package com.liverpool.api.controller;

import com.liverpool.api.dto.Address;
import com.liverpool.api.dto.Client;
import com.liverpool.api.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PutMapping
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PatchMapping("/{clientId}/address")
    public ResponseEntity<Client> addAddress(@Valid @PathVariable String clientId, @RequestBody Address address) {
        return ResponseEntity.ok(clientService.addAddress(clientId, address));
    }

    @PatchMapping("/{clientId}/address/{addressName}")
    public ResponseEntity<Client> updateAddress(@Valid @PathVariable String clientId,
                                                @PathVariable String addressName,
                                                @RequestBody Address updatedAddress) {
        return ResponseEntity.ok(clientService.updateAddress(clientId, addressName, updatedAddress));
    }

    @DeleteMapping("/{clientId}/address/{addressName}")
    public ResponseEntity<Client> deleteAddress(@PathVariable String clientId, @PathVariable String addressName) {
        return ResponseEntity.ok(clientService.deleteAddress(clientId, addressName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable String id) {
        return ResponseEntity.ok(clientService.deleteClient(id));
    }

}
