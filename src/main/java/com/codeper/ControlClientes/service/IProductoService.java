package com.codeper.ControlClientes.service;

import java.util.List;

import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CreateProductoDto;
import com.codeper.ControlClientes.dto.ProductoDto;
import com.codeper.ControlClientes.dto.ProductoFilterCriteria;

public interface IProductoService {
    List<ProductoDto>listarProducto();
    ProductoDto obtenerProductoPorId(Integer id);
    ProductoDto crearProducto(CreateProductoDto createPProductoDto);
    ProductoDto actualizarProducto(Integer id, ProductoDto pproductoActualizado);
    void eliminarProducto(Integer id);
    //paginacion
    PageableResponse<ProductoDto>getProductosPagination(PageableRequest<ProductoFilterCriteria>pageableRequest);

}
