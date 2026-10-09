package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateRolDto;
import com.codeper.ControlClientes.dto.RolDto;

public interface IRolService {
    List<RolDto> listarRol();
    RolDto listarRolPorId(Integer id);
    RolDto crearRol(CreateRolDto createRolDto);
    RolDto actualizarRol(Integer id, RolDto rolActualizar );
    void eliminarRol(Integer id);

}
