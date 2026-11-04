package com.codeper.ControlClientes.service;

import java.util.List;

import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreateUsuarioDto;
import com.codeper.ControlClientes.dto.UsuarioDto;
import com.codeper.ControlClientes.dto.UsuarioFilterCriteria;

public interface IUsuarioService {
    List<UsuarioDto> listarUsuario();
    UsuarioDto obtenerUsuarioPorId(Integer id );
    UsuarioDto crearUsuario(CreateUsuarioDto createUsuarioDto);
    UsuarioDto actualizarUsuario(Integer id, CreateUsuarioDto usuarioActualizar);
    void eliminarUsuario(Integer id);
     //paginacion
    PageableResponse<UsuarioDto>getUsuarioPagination(PageableRequest<UsuarioFilterCriteria>pageableRequest);
}
