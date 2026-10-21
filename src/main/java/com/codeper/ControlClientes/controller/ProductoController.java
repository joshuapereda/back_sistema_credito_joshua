package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateProductoDto;
import com.codeper.ControlClientes.dto.ProductoDto;
import com.codeper.ControlClientes.service.IProductoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping("api/productos")
public class ProductoController {

    @Autowired 
    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listarProducto() {
        return ResponseEntity.ok(productoService.listarProducto());
    }
    @GetMapping("/{id}")
     public ResponseEntity<ProductoDto> obtenerProductoPorId(@PathVariable  Integer id) {
         return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
     }
     @PostMapping
     public ResponseEntity<ProductoDto> crearProducto(@Valid @RequestBody CreateProductoDto createPProductoDto) {
         return ResponseEntity.ok(productoService.crearProducto(createPProductoDto));
     }
     @PutMapping ("/{id}")
     public ResponseEntity<ProductoDto> actualizarProducto(@PathVariable Integer id, @Valid @RequestBody ProductoDto pproductoActualizado) {
         return ResponseEntity.ok(productoService.actualizarProducto(id, pproductoActualizado));
     }
     @DeleteMapping("/{id}")
     public void eliminarProducto(@PathVariable Integer id){
        productoService.eliminarProducto(id);
    }
}
