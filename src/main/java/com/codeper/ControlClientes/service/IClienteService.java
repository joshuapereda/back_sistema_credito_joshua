
package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateClienteDto;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.ClienteDto;
import com.codeper.ControlClientes.dto.ClienteFilterCriteria;

public interface IClienteService {

    List<ClienteDto> listarCliente();
    ClienteDto obtenerClientePorId(Integer id);
    ClienteDto crearCliente(CreateClienteDto createClienteDto);
    ClienteDto actualizarCliente(Integer id, CreateClienteDto clienteActualizado);
    void eliminarCliente(Integer id);
    //paginacion
    PageableResponse<ClienteDto>getClientePagination(PageableRequest<ClienteFilterCriteria>pageableRequest);
}
