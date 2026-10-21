package validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeper.ControlClientes.repository.PersonaRepository;
import com.codeper.ControlClientes.repository.UsuarioRepository;

import validator.spec.IUniqueNameCheker;

@Service 
public class UniqueNameCheker implements IUniqueNameCheker {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public boolean isUniqueName(String fieldName, String value) {
        return switch (fieldName) {
            case "username" -> !usuarioRepository.existsByUsername(value);
            case "nombre"   -> !personaRepository.existsByNombre(value);
            default -> throw new IllegalArgumentException("Campo no soportado: " + fieldName);
        };
    }
}