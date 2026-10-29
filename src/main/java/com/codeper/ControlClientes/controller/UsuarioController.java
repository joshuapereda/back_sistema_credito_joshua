package com.codeper.ControlClientes.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping ("api/usuarios")
public class UsuarioController {

    // Logs por defecto
    private static final Logger logger=LoggerFactory.getLogger(UsuarioController.class);
   
    @Autowired 
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDto>>listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuario());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> listarUsuarioPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }
    @PostMapping
    public ResponseEntity<UsuarioDto> createUsuario(@Validated  @RequestBody CreateUsuarioDto createUsuarioDto) {
        logger.info("Datos recibidos para creacion de Usuario: {}",createUsuarioDto.toString());
        return  ResponseEntity.ok(usuarioService.crearUsuario(createUsuarioDto));
    }
    @PutMapping ("/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(@PathVariable Integer id, @Validated @RequestBody CreateUsuarioDto createUsuarioDto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, createUsuarioDto));
    }
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable  Integer id){
        usuarioService.eliminarUsuario(id);
    }
}
