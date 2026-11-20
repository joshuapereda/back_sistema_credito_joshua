package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codeper.ControlClientes.dto.CreateCategoriaDto;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CategoriaDto;
import com.codeper.ControlClientes.dto.CategoriaFilterCriteria;
import com.codeper.ControlClientes.entity.Categoria;
import org.springframework.data.domain.Page;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.CategoriaRepository;
import com.codeper.ControlClientes.repository.ProductoRepository;
import com.codeper.ControlClientes.repository.specofocation.CategoriaSpecification;

/**
 * CategoriaService
 */
@Service 
public class CategoriaService implements ICategoriaService {

    @Autowired 
    private CategoriaRepository pCategoriaRepository;

    @Autowired 
    private ProductoRepository productoRepository;

    @Autowired 
    private FileService fileService;

    @Override
    public List<CategoriaDto> listaCategoria() {
        return  pCategoriaRepository.findAll().stream()
                .map(CategoriaDto::fromEntityPCategoria)
                .toList();
    }
    @Override
    public CategoriaDto obetenerCategoriaPorId(Integer id) {
        try{
             return  pCategoriaRepository.findById(id)
                .map(CategoriaDto::fromEntityPCategoria)
                .orElseThrow(() -> new ResourNotFoundException("Categoria " , id));
        }catch(ResourNotFoundException e){
            throw e;
        }
    }
    @Override
    public CategoriaDto crearCategoria(CreateCategoriaDto createCategoriaDto) {
        Categoria categoria=CreateCategoriaDto.toEntityCategoria(createCategoriaDto);
        categoria.setFoto(guardarFoto(createCategoriaDto.getFoto()));
        categoria=pCategoriaRepository.save(categoria);
        return CategoriaDto.fromEntityPCategoria(categoria);
    }
    
    @Override
    public CategoriaDto actualizarCategoria(Integer id, CreateCategoriaDto categoriaActualizado) {
        //VALIDAR SI EXISTE LA CATEGORIA
        Categoria categoriaRetrieved = pCategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Categoria", id));

        categoriaRetrieved.setNombre(categoriaActualizado.getNombre().trim());
        categoriaRetrieved.setDescripcion(
                categoriaActualizado.getDescripcion() != null ? categoriaActualizado.getDescripcion().trim() : null);

        //solo si eligió una foto nueva, si no se conserva la anterior
        if (categoriaActualizado.getFoto() != null && !categoriaActualizado.getFoto().isEmpty()) {
            categoriaRetrieved.setFoto(guardarFoto(categoriaActualizado.getFoto()));
        }

        categoriaRetrieved = pCategoriaRepository.save(categoriaRetrieved);
        return CategoriaDto.fromEntityPCategoria(categoriaRetrieved);
    }
    @Override
    public void eliminarCategoria(Integer id) {
        if (!pCategoriaRepository.existsById(id)) {
            throw new ResourNotFoundException("Categoria ", id);
        }
        if (productoRepository.existsByCategoriaId(id)) {
            throw new IllegalStateException("No se puede eliminar: la categoría tiene productos asociados");
        }
        pCategoriaRepository.deleteById(id);
    }
    private String guardarFoto(MultipartFile foto) {
        if (foto == null || foto.isEmpty()) {
        return null;
    }
    return fileService.createFile(foto);
    }
    
   
@Override
public PageableResponse<CategoriaDto> getCategoriaPagination(PageableRequest<CategoriaFilterCriteria> pageableRequest) {

    try {

        // SORT PERMITE MANEJAR EL ORDENAMIENTO ASC = Ascendente, DESC = Descendente
        Sort sort = pageableRequest.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(pageableRequest.getSortField()).ascending()
                : Sort.by(pageableRequest.getSortField()).descending();

        // CONFIGURAMOS LA PAGINACION
        Pageable pageable = PageRequest.of(pageableRequest.getPageNumber(),pageableRequest.getPageSize(),sort );

        // FILTRADO
        // Creamos la Specification para aplicar la busqueda general
        // y los filtros especificos por nombre de categoria
        Specification<Categoria> specification =CategoriaSpecification.createSpecification(
                pageableRequest.getCriterials(),
                pageableRequest.getFilterValue()
        );

        // EJECUTAMOS LA CONSULTA CON FILTROS Y PAGINACION
        Page<Categoria> categoriaPage = pCategoriaRepository.findAll(
                specification,
                pageable
        );
        // DEVOLVEMOS LOS RESULTADOS
        return PageableResponse.<CategoriaDto>builder()
                // Convertimos cada Categoria en CategoriaDto
                .content(categoriaPage.getContent().stream()
                        .map(CategoriaDto::fromEntityPCategoria)
                        .toList())
                .pageNumber(categoriaPage.getNumber())
                .pageSize(categoriaPage.getSize())
                .totalElements(categoriaPage.getTotalElements())
                .totalPages(categoriaPage.getTotalPages())

                .build();

    } catch (Exception e) {
        throw new RuntimeException("Error de paginacion de categorias", e);
    }
} 
}
    
