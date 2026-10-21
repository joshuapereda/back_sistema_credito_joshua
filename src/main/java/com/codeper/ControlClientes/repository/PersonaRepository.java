package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeper.ControlClientes.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona,Integer> {
   //QUERY METHODS
    boolean existsByNombre(String nombre);

    boolean existsByGmail(String gmail);

}
