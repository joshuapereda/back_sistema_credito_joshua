package com.codeper.ControlClientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.CreateCategoriaDto;
import com.codeper.ControlClientes.common.dto.PageableRequest;
import com.codeper.ControlClientes.common.dto.PageableResponse;
import com.codeper.ControlClientes.dto.CategoriaDto;
import com.codeper.ControlClientes.dto.CategoriaFilterCriteria;
import com.codeper.ControlClientes.service.ICategoriaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping ("api/categorias")
public class CategoriaController {
    @Autowired 
    private ICategoriaService pCategoriaService;

    /*OTRO MEDOTO DE IMPLEMENTAR EL CONTROLADOR 
    @GetMapping 
    public List<PCategoriaDto>getAllCategorias(){
        return pCategoriaService.listaCategoria();
    }*/
 
    @GetMapping 
    public ResponseEntity<List<CategoriaDto>>listarCategorias(){
        return ResponseEntity.ok(pCategoriaService.listaCategoria());
    }
    //@PathVariable: PERMITE RECIBIR UN PARAMETRO DE LA URL, EN ESTE CASO EL ID DE LA CATEGORIA
    @GetMapping({"/{id}"})
    public ResponseEntity<CategoriaDto>obtenerCategoriaPorId(@PathVariable Integer id){
        return ResponseEntity.ok(pCategoriaService.obetenerCategoriaPorId(id));
    }
    // @RequestBody: PERMITE RECIBIR LA INFORMACION EN EL CUERPO DE LA PETICION
    @PostMapping 
    public ResponseEntity<CategoriaDto>crearCategoria(@Valid @ModelAttribute  CreateCategoriaDto  createPCategoriaDto){
        return ResponseEntity.ok(pCategoriaService.crearCategoria(createPCategoriaDto));
    }
    @PostMapping("/pagination")
    public ResponseEntity<PageableResponse<CategoriaDto>> getCategoriaPagination(
            @RequestBody PageableRequest<CategoriaFilterCriteria> pageableRequest) {
        return ResponseEntity.ok(pCategoriaService.getCategoriaPagination(pageableRequest));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> actualizarCategoria(@PathVariable Integer id, @Valid @ModelAttribute  CreateCategoriaDto  categoriaActualizado) {
        return ResponseEntity.ok(pCategoriaService.actualizarCategoria(id, categoriaActualizado));
    }
    @DeleteMapping("/{id}") 
    public void eliminarCategoria(@PathVariable Integer id){
        pCategoriaService.eliminarCategoria(id);
    }
}
