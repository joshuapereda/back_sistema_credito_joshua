package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateClienteDto;
import com.codeper.ControlClientes.dto.ClienteDto;
import com.codeper.ControlClientes.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController 
@RequestMapping ("api/clientes")
public class ClienteController {

    @Autowired 
    private IClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarCliente());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }
    @PostMapping
     public ResponseEntity<ClienteDto> crearCliente(@Valid @RequestBody CreateClienteDto createClienteDto) {
        return ResponseEntity.ok(clienteService.crearCliente(createClienteDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarCliente(@PathVariable Integer id, @RequestBody CreateClienteDto createClienteDto) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, createClienteDto));
    }
    @DeleteMapping("/{id}") 
    public void eliminarCliente(@PathVariable Integer id){
        clienteService.eliminarCliente(id);
    }
}
