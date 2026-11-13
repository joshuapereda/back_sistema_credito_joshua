package com.codeper.ControlClientes.service;

import java.util.List;

import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreatePermisoDto;
import com.codeper.ControlClientes.dto.PermisoDto;
import com.codeper.ControlClientes.dto.PermisoFilterCriteria;

public interface IPermisoService {
    List<PermisoDto>listarPermiso();
    PermisoDto obtenerPermisoPorId(Integer id);
    PermisoDto crearPermiso(CreatePermisoDto createPermisoDto);
    PermisoDto actualizarPermiso(Integer id, CreatePermisoDto actualizarPermiso);
    void eliminarPermiso(Integer id);
     //paginacion
    PageableResponse<PermisoDto>getPermisoPagination(PageableRequest<PermisoFilterCriteria>pageableRequest);
}

