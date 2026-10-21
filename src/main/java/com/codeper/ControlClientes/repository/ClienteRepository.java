package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeper.ControlClientes.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente ,Integer > {
    
}
