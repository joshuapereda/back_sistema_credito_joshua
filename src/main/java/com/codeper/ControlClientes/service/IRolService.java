package com.codeper.ControlClientes.service;

import java.util.List;

import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreateRolDto;
import com.codeper.ControlClientes.dto.RolDto;
import com.codeper.ControlClientes.dto.RolFilterCriteria;

public interface IRolService {
    List<RolDto> listarRol();
    RolDto listarRolPorId(Integer id);
    RolDto crearRol(CreateRolDto createRolDto);
    RolDto actualizarRol(Integer id, RolDto rolActualizar );
    void eliminarRol(Integer id);
     //paginacion
    PageableResponse<RolDto>getRolPagination(PageableRequest<RolFilterCriteria>pageableRequest);
}
