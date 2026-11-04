package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.codeper.ControlClientes.dto.ProductoDto;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.entity.Producto;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.PermisoRepository;
import com.codeper.ControlClientes.repository.specofocation.PermisoSpecification;
import com.codeper.ControlClientes.repository.specofocation.ProductoSpecification;

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

    @Override
    public PageableResponse<PermisoDto> getPermisoPagination(PageableRequest<PermisoFilterCriteria> pageableRequest) {
         try {
            //SORT PERMITE MANEJAR ORDENAMIENTO
            Sort sort=pageableRequest.getSortOrder().equalsIgnoreCase("asc") //ASC = ASENDENTE ,DESC=DESENDENTE
            ? Sort.by(pageableRequest.getSortField()).ascending()
            : Sort.by(pageableRequest.getSortField()).descending();
            Pageable pageable= PageRequest.of(pageableRequest.getPageNumber(),pageableRequest.getPageSize(), sort);

            //filtrado
            Specification<Permiso>specification =null;
            if(pageableRequest.getCriterials() !=null){
               specification = PermisoSpecification.createSpecification(
                                pageableRequest.getCriterials(),
                                pageableRequest.getFilterValue());
            }
            Page<Permiso>permisoPage=permisoRepository.findAll(specification,pageable);
            //devolver los datos
            return PageableResponse.<PermisoDto>builder()
            .content(permisoPage.getContent().stream().map(PermisoDto::fromEntityPermiso).toList())
            .pageNumber(permisoPage.getNumber())
            .pageSize(permisoPage.getSize())
            .totalElements(permisoPage.getTotalElements())
            .totalPages(permisoPage.getTotalPages())
            .build();
            
        } catch (Exception e) {
           throw new RuntimeException("Error de paginacion de permisos");
        }
    }
}
