package com.codeper.ControlClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeper.ControlClientes.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
}
