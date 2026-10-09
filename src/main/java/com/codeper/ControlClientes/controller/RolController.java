package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateRolDto;
import com.codeper.ControlClientes.dto.RolDto;
import com.codeper.ControlClientes.service.IRolService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController 
@RequestMapping ("api/roles")
public class RolController {

    @Autowired 
    private IRolService rolService;

    @GetMapping
    public ResponseEntity<List<RolDto>> listarRol() {
        return ResponseEntity.ok(rolService.listarRol());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDto> listarRolPorId(@PathVariable  Integer id) {
        return ResponseEntity.ok(rolService.listarRolPorId(id));
    }

    @PostMapping
    public ResponseEntity<RolDto> crearRol(@RequestBody CreateRolDto createRolDto) {
        return ResponseEntity.ok(rolService.crearRol(createRolDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity <RolDto> actualizarRol (@PathVariable Integer id, @RequestBody RolDto rolActualizado) {
        return ResponseEntity.ok(rolService.actualizarRol(id, rolActualizado));
    }

    @DeleteMapping("/{id}")
    public void eliminarRol(@PathVariable  Integer id){
        rolService.eliminarRol(id);
    }
    
}
