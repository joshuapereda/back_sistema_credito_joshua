package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateUsuarioDto;
import com.codeper.ControlClientes.dto.UsuarioDto;

public interface IUsuarioService {
    List<UsuarioDto> listarUsuario();
    UsuarioDto obtenerUsuarioPorId(Integer id );
    UsuarioDto crearUsuario(CreateUsuarioDto createUsuarioDto);
    UsuarioDto actualizarUsuario(Integer id, CreateUsuarioDto usuarioActualizar);
    void eliminarUsuario(Integer id);
}
