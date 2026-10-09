package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreatePermisoDto;
import com.codeper.ControlClientes.dto.PermisoDto;
import com.codeper.ControlClientes.service.IPermisoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping ("api/permisos")
public class PermisoController {
    @Autowired 
    private IPermisoService permisoService;

    @GetMapping()
    public ResponseEntity<List<PermisoDto>>getAllPermiso(){
        return ResponseEntity.ok(permisoService.listarPermiso());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PermisoDto>getAllPermisoPorId(@PathVariable Integer id ) {
        return ResponseEntity.ok(permisoService.obtenerPermisoPorId(id));
    }

    @PostMapping()
    public ResponseEntity<PermisoDto> postPermiso(@Valid @RequestBody CreatePermisoDto createPermisoDto) {
        return ResponseEntity.ok(permisoService.crearPermiso(createPermisoDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermisoDto> putPermiso(@PathVariable Integer id, @RequestBody CreatePermisoDto createPermisoDto) {
        return ResponseEntity.ok(permisoService.actualizarPermiso(id, createPermisoDto));
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable  Integer id){
        permisoService.eliminarPermiso(id);
    }
    
    
    


    
   

}
