package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateClienteDto;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.ClienteDto;
import com.codeper.ControlClientes.dto.ClienteFilterCriteria;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.exception.BadRequestException;
import com.codeper.ControlClientes.exception.ConflictException;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.CompraRepository;
import com.codeper.ControlClientes.repository.PersonaRepository;
import com.codeper.ControlClientes.repository.specofocation.ClienteSpecification;


@Service
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public List<ClienteDto> listarCliente() {
        return clienteRepository.findAll().stream().map(cliente -> {
            // obtener la Persona correspondiente a ese cliente
            Persona persona = cliente.getPersona();
            // retornar el listado de cliente
            return ClienteDto.fromEntityCliente(cliente, persona);
        }).toList();
    }

    @Override
    public ClienteDto obtenerClientePorId(Integer id) {
        try {
            return clienteRepository.findById(id).map(cliente -> {
                // obtener el Persona correspondiente a ese cliente
                Persona persona = cliente.getPersona();
                return ClienteDto.fromEntityCliente(cliente, persona);
            }).orElseThrow(() -> new ResourNotFoundException("Cliente", id));
        } catch (ResourNotFoundException e) {
            throw e;
        }
    }

    @Override
    public ClienteDto crearCliente(CreateClienteDto createClienteDto) {
        Persona persona = CreateClienteDto.toEntityPersona(createClienteDto);
        persona = personaRepository.save(persona);
        Cliente cliente = CreateClienteDto.toEntityCliente(createClienteDto, persona);
        cliente = clienteRepository.save(cliente);
        // LISTAR
        return ClienteDto.fromEntityCliente(cliente, persona);
    }

    @Override
    public ClienteDto actualizarCliente(Integer id, CreateClienteDto clienteActualizado) {
        Cliente clienteRetrieved = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Cliente ", id));
        // Actualizar Cliente
        clienteRetrieved.setDocIdentidad(clienteActualizado.getDocIdentidad());
        Persona personaRetrieved = clienteRetrieved.getPersona();
        // Actualizar Persona
        personaRetrieved.setNombre(clienteActualizado.getNombre());
        personaRetrieved.setGmail(clienteActualizado.getGmail());
        personaRetrieved.setTelefono(clienteActualizado.getTelefono());
        personaRetrieved.setDireccion(clienteActualizado.getDireccion());
        // guardar
        return ClienteDto.fromEntityCliente(clienteRepository.save(clienteRetrieved),
                personaRepository.save(personaRetrieved));
    }

    @Override
    public void eliminarCliente(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourNotFoundException("Cliente", id);
        }
        if (compraRepository.existsByClienteId(id)) {
            throw new ConflictException("No se puede eliminar: el cliente tiene compras registradas");
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public PageableResponse<ClienteDto> getClientePagination(PageableRequest<ClienteFilterCriteria> pageableRequest) {

        try {

            // SORT PERMITE MANEJAR EL ORDENAMIENTO
            // ASC = Ascendente
            // DESC = Descendente
            Sort sort = "asc".equalsIgnoreCase(pageableRequest.getSortOrder())
                    ? Sort.by(pageableRequest.getSortField()).ascending()
                    : Sort.by(pageableRequest.getSortField()).descending();

            // CONFIGURAMOS LA PAGINACION
            Pageable pageable = PageRequest.of(pageableRequest.getPageNumber(), pageableRequest.getPageSize(), sort);

            // FILTRADO
            // Creamos la Specification para aplicar la busqueda general
            // y los filtros especificos
            Specification<Cliente> specification = ClienteSpecification.createSpecification(
                    pageableRequest.getCriterials(),
                    pageableRequest.getFilterValue());
            // EJECUTAMOS LA CONSULTA CON FILTROS Y PAGINACION
            Page<Cliente> clientePage = clienteRepository.findAll(
                    specification,
                    pageable);
            // DEVOLVEMOS LOS RESULTADOS
            return PageableResponse.<ClienteDto>builder()

                    // Convertimos cada Cliente en ClienteDto
                    .content(clientePage.getContent().stream()
                            .map(cliente -> ClienteDto.fromEntityCliente(
                                    cliente, cliente.getPersona()))
                            .toList())
                    .pageNumber(clientePage.getNumber())
                    .pageSize(clientePage.getSize())
                    .totalElements(clientePage.getTotalElements())
                    .totalPages(clientePage.getTotalPages())
                    .build();

        } catch (PropertyReferenceException | IllegalArgumentException e) {
            // sortField que no existe o página inválida -> 400
            throw new BadRequestException("Datos de paginación inválidos: " + e.getMessage());
        }
    }
}
