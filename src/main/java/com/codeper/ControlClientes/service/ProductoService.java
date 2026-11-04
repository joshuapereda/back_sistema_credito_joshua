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
import com.codeper.ControlClientes.dto.CreateProductoDto;
import com.codeper.ControlClientes.dto.ProductoDto;
import com.codeper.ControlClientes.dto.ProductoFilterCriteria;
import com.codeper.ControlClientes.entity.Categoria;
import com.codeper.ControlClientes.entity.Producto;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.CategoriaRepository;
import com.codeper.ControlClientes.repository.ProductoRepository;
import com.codeper.ControlClientes.repository.specofocation.ProductoSpecification;
//@RequiredArgsConstructor
@Service 
public class ProductoService implements IProductoService {
    /*Otra forma de inyeccion de dependencia  
    private final CategoriaRepository pCategoriaRepository;*/
    @Autowired 
    private CategoriaRepository pCategoriaRepository;

    @Autowired 
    private ProductoRepository pProductoRepository;
    
    //PAGINACION 
    @Override
    public PageableResponse<ProductoDto> getProductosPagination(PageableRequest<ProductoFilterCriteria> pageableRequest) {
        try {
            //SORT PERMITE MANEJAR ORDENAMIENTO
            Sort sort=pageableRequest.getSortOrder().equalsIgnoreCase("asc") //ASC = ASENDENTE ,DESC=DESENDENTE
            ? Sort.by(pageableRequest.getSortField()).ascending()
            : Sort.by(pageableRequest.getSortField()).descending();
            Pageable pageable= PageRequest.of(pageableRequest.getPageNumber(),pageableRequest.getPageSize(), sort);

            //filtrado
            Specification<Producto>specification =null;
            if(pageableRequest.getCriterials() !=null){
               specification = ProductoSpecification.createSpecification(
                                pageableRequest.getCriterials(),
                                pageableRequest.getFilterValue());
            }
            Page<Producto>productoPage=pProductoRepository.findAll(specification,pageable);
            //devolver los datos
            return PageableResponse.<ProductoDto>builder()
            .content(productoPage.getContent().stream().map(ProductoDto::fromEntityProducto).toList())
            .pageNumber(productoPage.getNumber())
            .pageSize(productoPage.getSize())
            .totalElements(productoPage.getTotalElements())
            .totalPages(productoPage.getTotalPages())
            .build();
            
        } catch (Exception e) {
           throw new RuntimeException("Error de paginacion de productos");
        }
    }

    @Override
    public List<ProductoDto> listarProducto() {
        return  pProductoRepository.findAll().stream()
                .map(ProductoDto::fromEntityProducto)
                .toList();
    }

    @Override
    public ProductoDto obtenerProductoPorId(Integer id) {
        try{
             return pProductoRepository.findById(id)
                .map(ProductoDto::fromEntityProducto)
                .orElseThrow(()-> new ResourNotFoundException("Producto " , id));
        }catch(ResourNotFoundException e){
            throw e;
        }
    }
    @Override
    public ProductoDto crearProducto(CreateProductoDto createPProductoDto) {
        Categoria categoria = pCategoriaRepository.findById(createPProductoDto.getCategoriaId())
            .orElseThrow(() -> new ResourNotFoundException("Categoría ",createPProductoDto.getCategoriaId()));

        Producto producto = CreateProductoDto.toEntityPProducto(createPProductoDto, categoria);
        producto = pProductoRepository.save(producto);
        return ProductoDto.fromEntityProducto(producto);
    }

    @Override
    public ProductoDto actualizarProducto(Integer id, ProductoDto pproductoActualizado) {
        Producto productoRetrieved= pProductoRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Producto " , id));
        Categoria categoria = pCategoriaRepository.findById(pproductoActualizado.getCategoriaId())
                .orElseThrow(() -> new ResourNotFoundException("Categoría ",pproductoActualizado.getCategoriaId()));
        
        productoRetrieved.setNombre(pproductoActualizado.getNombre());
        productoRetrieved.setFoto(pproductoActualizado.getFoto());
        productoRetrieved.setPrecioVenta(pproductoActualizado.getPrecioVenta());
        productoRetrieved.setPrecioCompra(pproductoActualizado.getPrecioCompra());
        productoRetrieved.setCategoria(categoria);

        pProductoRepository.save(productoRetrieved);
        return ProductoDto.fromEntityProducto(productoRetrieved);
    }

    @Override
    public void eliminarProducto(Integer id) {
       if (!pProductoRepository.existsById(id)) {
        throw new ResourNotFoundException("Producto  " , id);
        }
        pProductoRepository.deleteById(id);
    }

  

}
