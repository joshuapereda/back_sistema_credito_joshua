package com.codeper.ControlClientes.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreateRolDto;
import com.codeper.ControlClientes.dto.RolDto;
import com.codeper.ControlClientes.dto.RolFilterCriteria;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.exception.BadRequestException;
import com.codeper.ControlClientes.exception.ConflictException;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.PermisoRepository;
import com.codeper.ControlClientes.repository.RolRepository;
import com.codeper.ControlClientes.repository.specofocation.RolSpecification;

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
        try {
            return rolRepository.findById(id)
                    .map(RolDto::fromEntityRol)
                    .orElseThrow(() -> new ResourNotFoundException("Rol ", id));
        } catch (ResourNotFoundException e) {
            throw e;
        }
    }

    @Override
    public RolDto crearRol(CreateRolDto createRolDto) {
        // Validar que existan los permisos
        List<Permiso> permisos = validarYObtenerPermisos(createRolDto.getPermisosId());

        // 4. Recién aquí se crea el rol
        Rol rol = CreateRolDto.toEntityRol(createRolDto, permisos);
        rol = rolRepository.save(rol);
        return RolDto.fromEntityRol(rol);
    }

    @Override
    public RolDto actualizarRol(Integer id, RolDto rolActualizar) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Rol ", id));
        // Validar que existan los permisos
        List<Permiso> permisos = validarYObtenerPermisos(rolActualizar.getPermisosId());

        rol.setNombre(rolActualizar.getNombre());
        rol.setDescripcion(rolActualizar.getDescripcion());
        rol.setPermisos(permisos);
        rol = rolRepository.save(rol);

        return RolDto.fromEntityRol(rol);
    }

    @Override
    public void eliminarRol(Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new ResourNotFoundException("Rol ", id);
        }
        if (permisoRepository.existsByRolesId(id)) {
            throw new ConflictException("No se puede eliminar: el rol está asignado a uno o más permisos");
        }
        permisoRepository.deleteById(id);
    }

    private List<Permiso> validarYObtenerPermisos(List<Integer> permisosId) {
        if (permisosId == null || permisosId.isEmpty()) {
            throw new BadRequestException("Debe enviar al menos un permiso");
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

            throw new ResourNotFoundException("Permisos", noExisten);
        }
        return permisos;
    }

    @Override
    public PageableResponse<RolDto> getRolPagination(PageableRequest<RolFilterCriteria> pageableRequest) {
        try {
            // SORT PERMITE MANEJAR EL ORDENAMIENTO
            Sort sort = "asc".equalsIgnoreCase(pageableRequest.getSortOrder())
                    ? Sort.by(pageableRequest.getSortField()).ascending()
                    : Sort.by(pageableRequest.getSortField()).descending();

            // CONFIGURAMOS LA PAGINACION
            Pageable pageable = PageRequest.of(
                    pageableRequest.getPageNumber(),
                    pageableRequest.getPageSize(),
                    sort);

            // FILTRADO: BUSQUEDA GENERAL Y FILTROS ESPECIFICOS
            Specification<Rol> specification = RolSpecification.createSpecification(
                    pageableRequest.getCriterials(),
                    pageableRequest.getFilterValue());

            // CONSULTAMOS LOS ROLES CON FILTROS Y PAGINACION
            Page<Rol> rolPage = rolRepository.findAll(specification, pageable);

            // DEVOLVEMOS LOS DATOS PAGINADOS
            return PageableResponse.<RolDto>builder()
                    .content(rolPage.getContent().stream()
                            .map(RolDto::fromEntityRol)
                            .toList())
                    .pageNumber(rolPage.getNumber())
                    .pageSize(rolPage.getSize())
                    .totalElements(rolPage.getTotalElements())
                    .totalPages(rolPage.getTotalPages())
                    .build();

        } catch (PropertyReferenceException | IllegalArgumentException e) {
            // sortField que no existe o página inválida -> 400
            throw new BadRequestException("Datos de paginación inválidos: " + e.getMessage());
        }
    }

}
