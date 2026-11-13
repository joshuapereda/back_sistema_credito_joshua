package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateCategoriaDto;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CategoriaDto;
import com.codeper.ControlClientes.dto.CategoriaFilterCriteria;

public interface ICategoriaService {
    List<CategoriaDto> listaCategoria();
    CategoriaDto obetenerCategoriaPorId(Integer id);
    CategoriaDto crearCategoria(CreateCategoriaDto createCategoriaDto);
    CategoriaDto actualizarCategoria(Integer id, CategoriaDto categoriaActualizado);
    void eliminarCategoria(Integer id);
     //paginacion
    PageableResponse<CategoriaDto>getCategoriaPagination(PageableRequest<CategoriaFilterCriteria>pageableRequest);
}
