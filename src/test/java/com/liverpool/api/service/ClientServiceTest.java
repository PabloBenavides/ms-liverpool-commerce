package com.liverpool.api.service;

import com.liverpool.api.dto.Address;
import com.liverpool.api.dto.Client;
import com.liverpool.api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClient_Success() {
        Client client = new Client();
        client.setEmail("test@example.com");

        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.createClient(client);

        assertNotNull(result);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void createClient_DuplicateEmail() {
        Client client = new Client();
        client.setEmail("test@example.com");

        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(client));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.createClient(client));
        assertTrue(exception.getMessage().contains("Ya existe un Cliente con el Correo Electronico"));
    }

    @Test
    void findById_Success() {
        Client client = new Client();
        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));

        Client result = clientService.findById("1");

        assertNotNull(result);
    }

    @Test
    void findById_NotFound() {
        when(clientRepository.findById(anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.findById("1"));
        assertTrue(exception.getMessage().contains("Cliente no encontrado con ID"));
    }

    @Test
    void findByEmail_Success() {
        Client client = new Client();
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(client));

        Client result = clientService.findByEmail("test@example.com");

        assertNotNull(result);
    }

    @Test
    void findByEmail_NotFound() {
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.findByEmail("test@example.com"));
        assertTrue(exception.getMessage().contains("Cliente no encontrado con email"));
    }

    @Test
    void getAllClients() {
        when(clientRepository.findAll()).thenReturn(List.of(new Client()));

        List<Client> result = clientService.getAllClients();

        assertFalse(result.isEmpty());
    }

    @Test
    void updateClient_Success() {
        Client client = new Client();
        client.setId("1");

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(new Client()));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.updateClient(client);

        assertNotNull(result);
    }

    @Test
    void updateClient_IdNull() {
        Client client = new Client();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.updateClient(client));
        assertTrue(exception.getMessage().contains("El Cliente no puede ser actualizado, el ID no debe ir null."));
    }

    @Test
    void addAddress_Success() {
        Client client = new Client();
        Address address = new Address();
        address.setAddressName("Home");
        client.setAddresses(new ArrayList<>());

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.addAddress("1", address);

        assertNotNull(result);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void addAddress_DuplicateAddress() {
        Client client = new Client();
        Address address = new Address();
        address.setAddressName("Home");
        client.setAddresses(List.of(address));

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.addAddress("1", address));
        assertTrue(exception.getMessage().contains("Ya existe una dirección con el nombre especificado."));
    }

    @Test
    void updateAddress_Success() {
        Client client = new Client();
        Address oldAddress = new Address();
        oldAddress.setAddressName("HOME"); // Asegura que el formato coincide con lo esperado
        Address newAddress = new Address();
        newAddress.setAddressName("HOME"); // Nuevo formato coincide
        client.setAddresses(new ArrayList<>(List.of(oldAddress))); // Usa una lista mutable

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.updateAddress("1", "HOME", newAddress); // Nombre en mayúsculas

        assertNotNull(result);
        verify(clientRepository, times(1)).save(client);
    }


    @Test
    void updateAddress_NotFound() {
        Client client = new Client();
        client.setAddresses(new ArrayList<>());

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.updateAddress("1", "Home", new Address()));
        assertTrue(exception.getMessage().contains("Dirección no encontrada con el nombre especificado."));
    }

    @Test
    void deleteAddress_Success() {
        Client client = new Client();
        Address address = new Address();
        address.setAddressName("HOME");
        client.setAddresses(new ArrayList<>(List.of(address)));

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.deleteAddress("1", "HOME");

        assertNotNull(result);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void deleteAddress_NotFound() {
        Client client = new Client();
        client.setAddresses(new ArrayList<>());

        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.deleteAddress("1", "Home"));
        assertTrue(exception.getMessage().contains("Dirección no encontrada con el nombre especificado."));
    }

    @Test
    void deleteClient_Success() {
        when(clientRepository.existsById(anyString())).thenReturn(true);

        boolean result = clientService.deleteClient("1");

        assertTrue(result);
        verify(clientRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteClient_NotFound() {
        when(clientRepository.existsById(anyString())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> clientService.deleteClient("1"));
        assertTrue(exception.getMessage().contains("Cliente no encontrado con ID"));
    }
}
