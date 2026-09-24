package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateUsuarioDto;
import com.codeper.ControlClientes.dto.UsuarioDto;
import com.codeper.ControlClientes.service.IUsuarioService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping ("api/usuarios")
public class UsuarioController {
   
    @Autowired 
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDto>>getAllUsuario() {
        return ResponseEntity.ok(usuarioService.listarUsuario());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getAllUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> createUsuario(@Validated  @RequestBody CreateUsuarioDto createUsuarioDto) {
        return  ResponseEntity.ok(usuarioService.crearUsuario(createUsuarioDto));
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable  Integer id){
        usuarioService.eliminarUsuario(id);
    }
    
    
    

}
