package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateCategoriaDto;
import com.codeper.ControlClientes.dto.CategoriaDto;
import com.codeper.ControlClientes.service.ICategoriaService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController 
@RequestMapping ("api/pcategorias")
public class CategoriaController {
    @Autowired 
    private ICategoriaService pCategoriaService;
    //OTRO MEDOTO DE IMPLEMENTAR EL CONTROLADOR 
    /*
    @GetMapping 
    public List<PCategoriaDto>getAllCategorias(){
        return pCategoriaService.listaCategoria();
    }
    */
 
    @GetMapping 
    public ResponseEntity<List<CategoriaDto>>getAllCategorias(){
        return ResponseEntity.ok(pCategoriaService.listaCategoria());
    }
    //para recibir el id en la ruta osea en la url se usa el @PathVariable
    @GetMapping({"/{id}"})
    public ResponseEntity<CategoriaDto>getAllCategoriaById(@PathVariable Integer id){
        return ResponseEntity.ok(pCategoriaService.obetenerCategoriaPorId(id));
    }
    //la informacion para crear datos es recibir la informacion en el cuerpo de la peticion con el RequestBody
    @PostMapping 
    public ResponseEntity<CategoriaDto>createCategoria(@RequestBody CreateCategoriaDto  createPCategoriaDto){
        return ResponseEntity.ok(pCategoriaService.crearCategoria(createPCategoriaDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> putMethodName(@PathVariable Integer id, @RequestBody CategoriaDto categoriaActualizado) {
        return ResponseEntity.ok(pCategoriaService.actualizarCategoria(id, categoriaActualizado));
    }

    @DeleteMapping("/{id}") 
    public void eliminarCategoria(@PathVariable Integer id){
        pCategoriaService.eliminarCategoria(id);
    }
}
