package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateUsuarioDto;
import com.codeper.ControlClientes.dto.UsuarioDto;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.ClienteRepository;
import com.codeper.ControlClientes.repository.RolRepository;
import com.codeper.ControlClientes.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class UsuarioService implements IUsuarioService {

    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private ClienteRepository personaRepository;

    @Autowired 
    private RolRepository rolRepository;

    @Override
    public List<UsuarioDto> listarUsuario() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarUsuario'");
    }

    @Override
    public UsuarioDto obtenerUsuarioPorId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerUsuarioPorId'");
    }

    @Override
    public UsuarioDto crearUsuario(CreateUsuarioDto createUsuarioDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearUsuario'");
    }

    @Override
    public UsuarioDto actualizarUsuario(Integer id, UsuarioDto usuarioActualizar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarUsuario'");
    }

    @Override
    public void eliminarUsuario(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarUsuario'");
    }

    

}
