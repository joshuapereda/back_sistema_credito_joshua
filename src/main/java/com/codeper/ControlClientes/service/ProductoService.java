package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateProductoDto;
import com.codeper.ControlClientes.dto.ProductoDto;
import com.codeper.ControlClientes.entity.Categoria;
import com.codeper.ControlClientes.entity.Producto;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.CategoriaRepository;
import com.codeper.ControlClientes.repository.ProductoRepository;
@Service 
public class ProductoService implements IProductoService {

    @Autowired 
    private CategoriaRepository pCategoriaRepository;

    @Autowired 
    private ProductoRepository pProductoRepository;

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
