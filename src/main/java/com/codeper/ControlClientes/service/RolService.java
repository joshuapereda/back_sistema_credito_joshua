package com.codeper.ControlClientes.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateRolDto;
import com.codeper.ControlClientes.dto.RolDto;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.repository.RolRepository;

@Service 
public class RolService implements IRolService {

    @Autowired 
    private RolRepository rolRepository;

  
    @Override
    public List<RolDto> listarRol() {
       return rolRepository.findAll().stream()
            .map(RolDto::fromEntityRol)
            .toList();
    }

    @Override
    public RolDto listarRolPorId(Integer id) {
        return rolRepository.findById(id)
                .map(RolDto::fromEntityRol)
                .orElseThrow(()->new RuntimeException("Rol no encontrado con el id: "+ id));
    }

    @Override
    public RolDto crearRol(CreateRolDto createRolDto) {
        Rol rol=CreateRolDto.toEntityRol(createRolDto);
        rol=rolRepository.save(rol);
        return RolDto.fromEntityRol(rol);
    }

    @Override
    public RolDto actualizarRol(Integer id, RolDto rolActualizar) {
        Rol rol=new Rol();
        rol.setNombre(rolActualizar.getNombre());
        rol.setDescripcion(rolActualizar.getDescripcion());
        rol=rolRepository.save(rol);

        return RolDto.fromEntityRol(rol);
    }

    @Override
    public void eliminarRol(Integer id) {
        if (!rolRepository.existsById(id)) {
        throw new RuntimeException("Categoria no encontrada con id: " + id);
        }
        rolRepository.deleteById(id);
    }

}
