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
import com.codeper.ControlClientes.dto.CreateTipoCompraDto;
import com.codeper.ControlClientes.dto.ProductoDto;
import com.codeper.ControlClientes.dto.TipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraFilterCriteria;
import com.codeper.ControlClientes.entity.Producto;
import com.codeper.ControlClientes.entity.TipoCompra;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.TipoCompraRepository;
import com.codeper.ControlClientes.repository.specofocation.ProductoSpecification;
import com.codeper.ControlClientes.repository.specofocation.TipoCompraSpecification;

@Service 
public class TipoCompraService implements ITipoCompraService {

    @Autowired 
    private TipoCompraRepository tipoCompraRepository;
    @Override
    public List<TipoCompraDto> listarTipoCompra() {
        return tipoCompraRepository.findAll().stream()
            .map(TipoCompraDto::fromEntityTipoCompra)
            .toList();
    }

    @Override
    public TipoCompraDto listarTipoCompraPorId(Integer id) {
        try{
             return  tipoCompraRepository.findById(id)
                .map(TipoCompraDto:: fromEntityTipoCompra)
                .orElseThrow(() -> new ResourNotFoundException("Tipo de Compra ",id));
        }catch(ResourNotFoundException e){
            throw e;
        }
    }

    @Override
    public TipoCompraDto crearTipoCompra(CreateTipoCompraDto createTipoCompraDto) {
        TipoCompra tCompra=CreateTipoCompraDto.toEntityTipoCompra(createTipoCompraDto);
        tCompra=tipoCompraRepository.save(tCompra);
        return  TipoCompraDto.fromEntityTipoCompra(tCompra);
    }

    @Override
    public TipoCompraDto editarTipoCompra(Integer id, CreateTipoCompraDto actualizarTipoCompra) {
       //VALIDAR SI EXISTE LA PERSONA 
        TipoCompra tcompraRetrieved = tipoCompraRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Tipo de compra" , id));
        
        tcompraRetrieved.setNombre(actualizarTipoCompra.getNombre());
        tcompraRetrieved.setDescripcion(actualizarTipoCompra.getDescripcion());
        tcompraRetrieved.setEsCredito(actualizarTipoCompra.getEsCredito());
        tcompraRetrieved=tipoCompraRepository.save(tcompraRetrieved);

        return TipoCompraDto.fromEntityTipoCompra(tcompraRetrieved);
    }

    @Override
    public void eliminarTipoCompra(Integer id) {
         if (!tipoCompraRepository.existsById(id)) {
        throw new ResourNotFoundException("Permiso" , id);
        }
        tipoCompraRepository.deleteById(id);
    }

    @Override
    public PageableResponse<TipoCompraDto> getTipoCompraPagination(
            PageableRequest<TipoCompraFilterCriteria> pageableRequest) {
        try {
            //SORT PERMITE MANEJAR ORDENAMIENTO
            Sort sort=pageableRequest.getSortOrder().equalsIgnoreCase("asc") //ASC = ASENDENTE ,DESC=DESENDENTE
            ? Sort.by(pageableRequest.getSortField()).ascending()
            : Sort.by(pageableRequest.getSortField()).descending();
            Pageable pageable= PageRequest.of(pageableRequest.getPageNumber(),pageableRequest.getPageSize(), sort);

            //filtrado
            Specification<TipoCompra>specification =null;
            if(pageableRequest.getCriterials() !=null){
               specification = TipoCompraSpecification.createSpecification(
                                pageableRequest.getCriterials(),
                                pageableRequest.getFilterValue());
            }
            Page<TipoCompra>tipoCompraPage=tipoCompraRepository.findAll(specification,pageable);
            //devolver los datos
            return PageableResponse.<TipoCompraDto>builder()
            .content(tipoCompraPage.getContent().stream().map(TipoCompraDto::fromEntityTipoCompra).toList())
            .pageNumber(tipoCompraPage.getNumber())
            .pageSize(tipoCompraPage.getSize())
            .totalElements(tipoCompraPage.getTotalElements())
            .totalPages(tipoCompraPage.getTotalPages())
            .build();
            
        } catch (Exception e) {
           throw new RuntimeException("Error de paginacion de tipos de compra");
        }
    }
}
