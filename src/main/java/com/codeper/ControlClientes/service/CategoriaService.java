package com.codeper.ControlClientes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateCategoriaDto;
import com.codeper.ControlClientes.dto.CategoriaDto;
import com.codeper.ControlClientes.entity.Categoria;
import com.codeper.ControlClientes.exception.ResourNotFoundException;
import com.codeper.ControlClientes.repository.CategoriaRepository;

@Service 
public class CategoriaService implements ICategoriaService {

    @Autowired 
    private CategoriaRepository pCategoriaRepository;

    @Override
    public List<CategoriaDto> listaCategoria() {
        return  pCategoriaRepository.findAll().stream()
                .map(CategoriaDto::fromEntityPCategoria)
                .toList();
    }
    @Override
    public CategoriaDto obetenerCategoriaPorId(Integer id) {
        try{
             return  pCategoriaRepository.findById(id)
                .map(CategoriaDto::fromEntityPCategoria)
                .orElseThrow(() -> new ResourNotFoundException("Categoria " , id));
        }catch(ResourNotFoundException e){
            throw e;
        }
    }
    @Override
    public CategoriaDto crearCategoria(CreateCategoriaDto createCategoriaDto) {
        Categoria categoria=CreateCategoriaDto.toEntityCategoria(createCategoriaDto);
        categoria=pCategoriaRepository.save(categoria);
        return CategoriaDto.fromEntityPCategoria(categoria);
    }
    
    @Override
    public CategoriaDto actualizarCategoria(Integer id, CategoriaDto categoriaActualizado) {
        //VALIDAR SI EXISTE LA CATEGORIA 
        Categoria categoriaRetrieved = pCategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourNotFoundException("Categoria" , id));

        categoriaRetrieved.setNombre(categoriaActualizado.getNombre());
        categoriaRetrieved.setDescripcion(categoriaActualizado.getDescripcion());
        categoriaRetrieved = pCategoriaRepository.save(categoriaRetrieved);

        return CategoriaDto.fromEntityPCategoria(categoriaRetrieved);
    }
    @Override
    public void eliminarCategoria(Integer id) {
        if (!pCategoriaRepository.existsById(id)) {
        throw new ResourNotFoundException("Categoria " , id);
        }
        pCategoriaRepository.deleteById(id);
    }
}
