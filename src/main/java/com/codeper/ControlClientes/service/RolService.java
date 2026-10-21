package com.codeper.ControlClientes.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateRolDto;
import com.codeper.ControlClientes.dto.RolDto;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.repository.PermisoRepository;
import com.codeper.ControlClientes.repository.RolRepository;

@Service 
public class RolService implements IRolService {

    @Autowired 
    private RolRepository rolRepository;

    @Autowired 
    private PermisoRepository permisoRepository;

  
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
     // Validar que existan los permisos
        List<Permiso> permisos = validarYObtenerPermisos(createRolDto.getPermisosId());

        // 4. Recién aquí se crea el rol
       Rol rol= CreateRolDto.toEntityRol(createRolDto, permisos);
       rol= rolRepository.save(rol);
       return RolDto.fromEntityRol(rol);
    }

    @Override
    public RolDto actualizarRol(Integer id, RolDto rolActualizar) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con el id: " + id));
        // Validar que existan los permisos
        List<Permiso> permisos = validarYObtenerPermisos(rolActualizar.getPermisosId());
       
        rol.setNombre(rolActualizar.getNombre());
        rol.setDescripcion(rolActualizar.getDescripcion());
        rol.setPermisos(permisos);
        rol=rolRepository.save(rol);

        return RolDto.fromEntityRol(rol);
    }

    @Override
    public void eliminarRol(Integer id) {
        if (!rolRepository.existsById(id)) {
        throw new RuntimeException("Rol no encontrada con id: " + id);
        }
        rolRepository.deleteById(id);
    }

    private List<Permiso> validarYObtenerPermisos(List<Integer> permisosId) {
    if (permisosId == null || permisosId.isEmpty()) {
        throw new IllegalArgumentException("Debe enviar al menos un permiso");
    }

    List<Integer> ids = permisosId.stream().distinct().toList();
    List<Permiso> permisos = permisoRepository.findAllById(ids);

    if (permisos.size() != ids.size()) {
        Set<Integer> encontrados = permisos.stream()
                .map(Permiso::getId)
                .collect(Collectors.toSet());

        List<Integer> noExisten = ids.stream()
                .filter(id -> !encontrados.contains(id))
                .toList();

        throw new RuntimeException("Permisos no encontrados: " + noExisten);
    }
        return permisos;
    }
}
