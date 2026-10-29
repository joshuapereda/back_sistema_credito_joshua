package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreatePermisoDto;
import com.codeper.ControlClientes.dto.PermisoDto;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
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
        try{
            return permisoRepository.findById(id)
               .map(PermisoDto::fromEntityPermiso)
               .orElseThrow(()-> new ResourNotFoundException("Permiso ",id));
        }catch(ResourNotFoundException e){
            throw e;
        }
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
                .orElseThrow(() -> new ResourNotFoundException("Permiso" ,id));

        permisoRetrieved.setNombre(actualizarPermiso.getNombre());
        permisoRetrieved.setRecurso(actualizarPermiso.getRecurso());
        permisoRetrieved.setAccion(actualizarPermiso.getAccion());

        permisoRetrieved = permisoRepository.save(permisoRetrieved);
        return  PermisoDto.fromEntityPermiso(permisoRetrieved);
    }

    @Override
    public void eliminarPermiso(Integer id) {
         if (!permisoRepository.existsById(id)) {
        throw new ResourNotFoundException("Permiso " , id);
        }
        permisoRepository.deleteById(id);
    }
}
