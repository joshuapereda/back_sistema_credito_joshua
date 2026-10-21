package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreatePagoDto;
import com.codeper.ControlClientes.dto.PagoDto;
import com.codeper.ControlClientes.service.IPagoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController 
@RequestMapping ("api/pago")
public class PagoController {

    @Autowired 
    private IPagoService pagoService;

    @GetMapping
    public ResponseEntity <List<PagoDto>>listarPagos() {
        return ResponseEntity.ok(pagoService.listarPago());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDto> listarPagoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @PostMapping
    public ResponseEntity<PagoDto> crearPago(@RequestBody CreatePagoDto createPagoDto) {
        return ResponseEntity.ok(pagoService.crearPago(createPagoDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDto> actualizarPago(@PathVariable Integer id, @RequestBody PagoDto actualizarPagoDto) { 
        return ResponseEntity.ok(pagoService.actualizarPago(id, actualizarPagoDto));
    }

    @DeleteMapping("/{id}") 
    public void eliminarPago(@PathVariable Integer id){
        pagoService.eliminarPago(id);
    }
}
