
package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateClienteDto;
import com.codeper.ControlClientes.dto.ClienteDto;

public interface IClienteService {

    List<ClienteDto> listarCliente();
    ClienteDto obtenerClientePorId(Integer id);
    ClienteDto crearCliente(CreateClienteDto createClienteDto);
    ClienteDto actualizarCliente(Integer id, CreateClienteDto clienteActualizado);
    void eliminarCliente(Integer id);
}
