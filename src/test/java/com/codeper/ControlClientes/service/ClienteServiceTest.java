package com.codeper.ControlClientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.codeper.ControlClientes.dto.CreateClienteDto;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.exception.ConflictException;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.PersonaRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private PersonaRepository personaRepository;
    @Mock private CompraRepository compraRepository;
    @InjectMocks private ClienteService clienteService;

    private CreateClienteDto dto(String gmail) {
        return CreateClienteDto.builder().nombre("Ana").gmail(gmail)
            .telefono("912345678").direccion("Lima").docIdentidad("12345678").build();
    }

    // Deja listo el guardado: devuelve lo mismo que recibe
    private void guardarFunciona() {
        when(personaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    private void existeClienteConPersona10() {
        Persona persona = Persona.builder().id(10).gmail("ana@gmail.com").build();
        when(clienteRepository.findById(1))
            .thenReturn(Optional.of(Cliente.builder().id(1).persona(persona).build()));
    }

    @Test
    void crearConCorreoRepetidoSeRechaza() {
        when(personaRepository.existsByGmailIgnoreCase("ana@gmail.com")).thenReturn(true);

        ConflictException e = assertThrows(ConflictException.class,
            () -> clienteService.crearCliente(dto("ana@gmail.com")));

        assertEquals("El correo electrónico ya está en uso", e.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void crearConCorreoNuevoSeGuarda() {
        guardarFunciona();
        clienteService.crearCliente(dto("nuevo@gmail.com"));
        verify(clienteRepository).save(any());
    }

    @Test
    void elCorreoVacioNoSeValida() {
        guardarFunciona();
        clienteService.crearCliente(dto(""));
        clienteService.crearCliente(dto(null));
        verify(personaRepository, never()).existsByGmailIgnoreCase(any());
    }

    @Test
    void editarManteniendoSuPropioCorreoSeGuarda() {
        existeClienteConPersona10();
        guardarFunciona();
        clienteService.actualizarCliente(1, dto("ana@gmail.com"));
        verify(personaRepository).existsByGmailIgnoreCaseAndIdNot("ana@gmail.com", 10);
        verify(clienteRepository).save(any());
    }

    @Test
    void editarConElCorreoDeOtroSeRechaza() {
        existeClienteConPersona10();
        when(personaRepository.existsByGmailIgnoreCaseAndIdNot("otro@gmail.com", 10)).thenReturn(true);

        assertThrows(ConflictException.class,
            () -> clienteService.actualizarCliente(1, dto("otro@gmail.com")));
        verify(clienteRepository, never()).save(any());
    }
}
