package com.codeper.ControlClientes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreatePermisoDto;
import com.codeper.ControlClientes.dto.PermisoDto;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.repository.PermisoRepository;

@Service 
public class PermisoService implements IPermisoService {
    
    @Autowired
    private PermisoRepository permisoRepository; 

    @Override
    public List<PermisoDto> listarPermiso() {
        return permisoRepository.findAll().stream()
               .map(PermisoDto::fromEntityPermiso)
               .toList();  
    }

    @Override
    public PermisoDto obtenerPermisoPorId(Integer id) {
        return permisoRepository.findById(id)
               .map(PermisoDto::fromEntityPermiso)
               .orElseThrow(()-> new RuntimeException("Permiso no encontrado con el id: "+id));
    }

    @Override
    public PermisoDto crearPermiso(CreatePermisoDto createPermisoDto) {
        Permiso permiso= CreatePermisoDto.toEntityPermisos(createPermisoDto);
        permiso=permisoRepository.save(permiso);
        return  PermisoDto.fromEntityPermiso(permiso);
    }

    @Override
    public PermisoDto actualizarPermiso(Integer id, CreatePermisoDto actualizarPermiso) {
         //VALIDAR SI EXISTE LA PERSONA 
        Permiso permisoRetrieved = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con el id: " + id));

        permisoRetrieved.setNombre(actualizarPermiso.getNombre());
        permisoRetrieved.setRecurso(actualizarPermiso.getRecurso());
        permisoRetrieved.setAccion(actualizarPermiso.getAccion());

        permisoRetrieved = permisoRepository.save(permisoRetrieved);
        return  PermisoDto.fromEntityPermiso(permisoRetrieved);
    }

    @Override
    public void eliminarPermiso(Integer id) {
         if (!permisoRepository.existsById(id)) {
        throw new RuntimeException("Permiso no encontrada con id: " + id);
        }
        permisoRepository.deleteById(id);
    }

}
