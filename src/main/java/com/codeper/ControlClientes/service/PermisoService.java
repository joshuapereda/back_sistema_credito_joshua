package com.codeper.ControlClientes.service;

import java.util.List;
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
import com.codeper.ControlClientes.dto.CreatePermisoDto;
import com.codeper.ControlClientes.dto.PermisoDto;
import com.codeper.ControlClientes.dto.PermisoFilterCriteria;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.exception.BadRequestException;
import com.codeper.ControlClientes.exception.ConflictException;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.PermisoRepository;
import com.codeper.ControlClientes.repository.RolRepository;
import com.codeper.ControlClientes.repository.specofocation.PermisoSpecification;

@Service
public class PermisoService implements IPermisoService {

    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<PermisoDto> listarPermiso() {
        return permisoRepository.findAll().stream()
                .map(PermisoDto::fromEntityPermiso)
                .toList();
    }

    @Override
    public PermisoDto obtenerPermisoPorId(Integer id) {
        try {
            return permisoRepository.findById(id)
                    .map(PermisoDto::fromEntityPermiso)
                    .orElseThrow(() -> new ResourNotFoundException("Permiso ", id));
        } catch (ResourNotFoundException e) {
            throw e;
        }
    }

    @Override
    public PermisoDto crearPermiso(CreatePermisoDto createPermisoDto) {
        Permiso permiso = CreatePermisoDto.toEntityPermisos(createPermisoDto);
        permiso = permisoRepository.save(permiso);
        return PermisoDto.fromEntityPermiso(permiso);
    }

    @Override
    public PermisoDto actualizarPermiso(Integer id, CreatePermisoDto actualizarPermiso) {
        // VALIDAR SI EXISTE LA PERSONA
        Permiso permisoRetrieved = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Permiso", id));

        permisoRetrieved.setNombre(actualizarPermiso.getNombre());
        permisoRetrieved.setRecurso(actualizarPermiso.getRecurso());
        permisoRetrieved.setAccion(actualizarPermiso.getAccion());

        permisoRetrieved = permisoRepository.save(permisoRetrieved);
        return PermisoDto.fromEntityPermiso(permisoRetrieved);
    }

    @Override
    public void eliminarPermiso(Integer id) {
        if (!permisoRepository.existsById(id)) {
            throw new ResourNotFoundException("Permiso ", id);
        }
        if (rolRepository.existsByPermisosId(id)) {
            throw new ConflictException("No se puede eliminar: el permiso está asignado a uno o más roles");
        }
        permisoRepository.deleteById(id);
    }

    @Override
    public PageableResponse<PermisoDto> getPermisoPagination(PageableRequest<PermisoFilterCriteria> pageableRequest) {

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
            Specification<Permiso> specification = PermisoSpecification.createSpecification(
                    pageableRequest.getCriterials(),
                    pageableRequest.getFilterValue());

            // CONSULTAMOS LOS PERMISOS CON FILTROS Y PAGINACION
            Page<Permiso> permisoPage = permisoRepository.findAll(specification, pageable);

            // DEVOLVEMOS LOS DATOS PAGINADOS
            return PageableResponse.<PermisoDto>builder()
                    .content(permisoPage.getContent().stream()
                            .map(PermisoDto::fromEntityPermiso)
                            .toList())
                    .pageNumber(permisoPage.getNumber())
                    .pageSize(permisoPage.getSize())
                    .totalElements(permisoPage.getTotalElements())
                    .totalPages(permisoPage.getTotalPages())
                    .build();

        } catch (PropertyReferenceException | IllegalArgumentException e) {
            // sortField que no existe o página inválida -> 400
            throw new BadRequestException("Datos de paginación inválidos: " + e.getMessage());
        }
    }
}
