package com.codeper.ControlClientes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.codeper.ControlClientes.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>,JpaSpecificationExecutor<Categoria> {
    //es un contenedor que envuelve el resultado y te obliga a
    //manejar el caso en que no exista nada
    Optional<Categoria> findByNombre(String nombre);
}
