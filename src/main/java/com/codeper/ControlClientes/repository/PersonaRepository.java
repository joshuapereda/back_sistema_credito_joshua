package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeper.ControlClientes.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona,Integer> {
    //VALIDACION POR SERVICE
    boolean existsByGmail(String email);

}
