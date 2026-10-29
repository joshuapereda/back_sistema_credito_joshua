package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateClienteDto;
import com.codeper.ControlClientes.dto.ClienteDto;
import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.PersonaRepository;

@Service 
public class ClienteService implements IClienteService {

    @Autowired 
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository; 

    @Override
    public List<ClienteDto> listarCliente() {
        return  clienteRepository.findAll().stream().map(cliente ->{
            //obtener la Persona correspondiente a ese cliente
            Persona persona= cliente.getPersona();
            //retornar el listado de cliente
            return ClienteDto.fromEntityCliente(cliente, persona);
        }).toList();
    }

    @Override
    public ClienteDto obtenerClientePorId(Integer id) {
        try{
            return clienteRepository.findById(id).map(cliente -> {
            //obtener el Persona correspondiente a ese cliente
            Persona persona= cliente.getPersona();
            return ClienteDto.fromEntityCliente(cliente, persona);
            }).orElseThrow(() -> new ResourNotFoundException("Usuario", id));
        }catch(ResourNotFoundException e){
            throw e;
        }
    }

    @Override
    public ClienteDto crearCliente(CreateClienteDto createClienteDto) {
        //  VALIDAR 
        if(isEmailExist(createClienteDto.getGmail())){
            throw new RuntimeException("El correo electronico ya está en uso");
        }
        
        Persona persona= CreateClienteDto.toEntityPersona(createClienteDto);
        persona= personaRepository.save(persona);
        Cliente cliente = CreateClienteDto.toEntityCliente(createClienteDto,persona);
        cliente= clienteRepository.save(cliente);
        //LISTAR
        return ClienteDto.fromEntityCliente(cliente, persona);
    }

    @Override
    public ClienteDto actualizarCliente(Integer id, CreateClienteDto clienteActualizado) {
        //  VALIDAR 
        if(isEmailExist(clienteActualizado.getGmail())){
            throw new RuntimeException("El correo electronico ya está en uso");
        }
        Cliente clienteRetrieved= clienteRepository.findById(id).orElseThrow(() -> 
        new ResourNotFoundException("Cliente " , id));
        //Actualizar Cliente
        clienteRetrieved.setDocIdentidad(clienteActualizado.getDocIdentidad());
        Persona personaRetrieved= clienteRetrieved.getPersona();
        //Actualizar Persona
        personaRetrieved.setNombre(clienteActualizado.getNombre());
        personaRetrieved.setGmail(clienteActualizado.getGmail());
        personaRetrieved.setTelefono(clienteActualizado.getTelefono());
        personaRetrieved.setDireccion(clienteActualizado.getDireccion());
        //guardar
        return ClienteDto.fromEntityCliente(clienteRepository.save(clienteRetrieved),personaRepository.save(personaRetrieved));
    }

    @Override
    public void eliminarCliente(Integer id) {
       if (!clienteRepository.existsById(id)) {
            throw new ResourNotFoundException("Cliente", id);
        }

        clienteRepository.deleteById(id);
    }
     // VALIDATIONS
    
    boolean isEmailExist(String email) {
        return personaRepository.existsByGmail(email);
    }

}
