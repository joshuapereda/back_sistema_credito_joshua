package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeper.ControlClientes.entity.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    

}
