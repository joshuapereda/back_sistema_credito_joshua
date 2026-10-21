package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CompraDto;
import com.codeper.ControlClientes.dto.CreateCompraDto;
import com.codeper.ControlClientes.service.ICompraService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController 
@RequestMapping ("api/compra")
public class CompraController {

    @Autowired 
    private ICompraService compraService;

    @GetMapping()
    public ResponseEntity<List<CompraDto>> listarCompras() {
        return ResponseEntity.ok(compraService.listarCompra());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CompraDto> obtenerCompraPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(compraService.listarCompraPorId(id));
    }
    @PostMapping()
    public ResponseEntity<CompraDto> crearCompra(@Valid @RequestBody CreateCompraDto  createCompraDto) {        
        return ResponseEntity.ok(compraService.crearCompra(createCompraDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CompraDto> actualizarCompra(@PathVariable Integer id, @Valid @RequestBody CreateCompraDto createCompraDto) {
        return ResponseEntity.ok(compraService.actualizarCompra(id, createCompraDto));
    }
    @DeleteMapping("/{id}") 
    public void eliminarCompra(@PathVariable Integer id){
        compraService.eliminarCompra(id);
    }
}
