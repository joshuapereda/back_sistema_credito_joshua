package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeper.ControlClientes.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

}
