package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired 
    private FileService fileService;


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
        producto.setFoto(guardarFoto(createPProductoDto.getFoto()));
        producto = pProductoRepository.save(producto);
        return ProductoDto.fromEntityProducto(producto);
    }

    @Override
    public ProductoDto actualizarProducto(Integer id, CreateProductoDto dto) {
        Producto producto = pProductoRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Producto ", id));
        Categoria categoria = pCategoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourNotFoundException("Categoría ", dto.getCategoriaId()));

        producto.setNombre(dto.getNombre().trim());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setCategoria(categoria);

        if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
            producto.setFoto(guardarFoto(dto.getFoto()));
        }

        return ProductoDto.fromEntityProducto(pProductoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(Integer id) {
       if (!pProductoRepository.existsById(id)) {
        throw new ResourNotFoundException("Producto  " , id);
        }
        pProductoRepository.deleteById(id);
    }
    // método privado compartido: un solo lugar para manejar el archivo y el error
    private String guardarFoto(MultipartFile foto) {
        if (foto == null || foto.isEmpty()) {
        return null;
    }
    return fileService.createFile(foto);
    }
}
