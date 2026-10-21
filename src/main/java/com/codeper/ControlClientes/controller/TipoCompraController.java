package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateTipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraDto;
import com.codeper.ControlClientes.service.ITipoCompraService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController 
@RequestMapping("api/tipoCompras")
public class TipoCompraController {

    @Autowired 
    private ITipoCompraService tipoCompraService;

    @GetMapping 
    public ResponseEntity<List<TipoCompraDto>> listarTipoCompra(){
        return ResponseEntity.ok(tipoCompraService.listarTipoCompra());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TipoCompraDto> listarTipoCompraPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoCompraService.listarTipoCompraPorId(id));
    }
    @PostMapping
    public ResponseEntity<TipoCompraDto> crearTipoCompra(@Valid @RequestBody CreateTipoCompraDto createTipoCompraDto) {
        return ResponseEntity.ok(tipoCompraService.crearTipoCompra(createTipoCompraDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<TipoCompraDto> actualizarTipoCompra(@PathVariable Integer id, @RequestBody CreateTipoCompraDto createTipoCompraDto) {
        return ResponseEntity.ok(tipoCompraService.editarTipoCompra(id, createTipoCompraDto));
    }
    @DeleteMapping("/{id}")
    public void  eliminarTipoCompra(@PathVariable Integer id){
       tipoCompraService.eliminarTipoCompra(id);
    }
}
