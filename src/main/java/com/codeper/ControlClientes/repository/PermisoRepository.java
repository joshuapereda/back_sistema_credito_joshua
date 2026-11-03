package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.codeper.ControlClientes.entity.Permiso;


public interface PermisoRepository extends JpaRepository<Permiso, Integer>,JpaSpecificationExecutor<Permiso> {
    

}
