package com.codeper.ControlClientes.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.codeper.ControlClientes.entity.Persona;


public interface PersonaRepository extends JpaRepository<Persona,Integer>,JpaSpecificationExecutor<Persona> {
   //QUERY METHODS
    boolean existsByNombre(String nombre);

    // Correo repetido, sin importar mayúsculas
    boolean existsByGmailIgnoreCase(String gmail);
    // Igual, pero sin contar a la propia persona (para editar)
    boolean existsByGmailIgnoreCaseAndIdNot(String gmail, Integer id);
}
