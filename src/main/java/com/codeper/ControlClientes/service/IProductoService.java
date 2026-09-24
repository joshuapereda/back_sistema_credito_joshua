package com.codeper.ControlClientes.service;

import java.util.List;
import com.codeper.ControlClientes.dto.CreateProductoDto;
import com.codeper.ControlClientes.dto.ProductoDto;

public interface IProductoService {
    List<ProductoDto>listarProducto();
    ProductoDto obtenerProductoPorId(Integer id);
    ProductoDto crearProducto(CreateProductoDto createPProductoDto);
    ProductoDto actualizarProducto(Integer id, ProductoDto pproductoActualizado);
    void eliminarProducto(Integer id);
}
