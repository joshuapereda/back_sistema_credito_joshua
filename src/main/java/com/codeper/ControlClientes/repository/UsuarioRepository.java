package com.codeper.ControlClientes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.codeper.ControlClientes.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> ,JpaSpecificationExecutor<Usuario>{
    //QUERY METHODS
    boolean existsByUsername(String username);

    //METO QUE BUSQUE POR CORREO
    //viene del AppConfig 
  Optional<Usuario> findByPersonaGmail(String gmail);
}

