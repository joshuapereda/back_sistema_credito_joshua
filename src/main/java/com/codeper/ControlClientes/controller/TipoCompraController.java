package com.codeper.ControlClientes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateTipoCompraDto;
import com.codeper.ControlClientes.dto.TipoCompraDto;
import com.codeper.ControlClientes.service.ITipoCompraService;
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
    public ResponseEntity<List<TipoCompraDto>>getAllTCompra(){
        return ResponseEntity.ok(tipoCompraService.listarTipoCompra());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCompraDto> getMethodTipoCompra(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoCompraService.listarTipoCompraPorId(null));
    }

    @PostMapping
    public ResponseEntity<TipoCompraDto> postTipoCompra(@RequestBody CreateTipoCompraDto createTipoCompraDto) {
        return ResponseEntity.ok(tipoCompraService.crearTipoCompra(createTipoCompraDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoCompraDto> putTipoCompra(@PathVariable Integer id, @RequestBody CreateTipoCompraDto createTipoCompraDto) {
        return ResponseEntity.ok(tipoCompraService.editarTipoCompra(id, createTipoCompraDto));
    }

    @DeleteMapping("/{id}")
    public void  deleteTipoCompra(@PathVariable Integer id){
       tipoCompraService.eliminarTipoCompra(id);
    }

    

    
    
    


}
