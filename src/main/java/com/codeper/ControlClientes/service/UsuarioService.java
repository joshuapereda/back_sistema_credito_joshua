package com.codeper.ControlClientes.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeper.ControlClientes.dto.CreateUsuarioDto;
import com.codeper.ControlClientes.dto.UsuarioDto;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.entity.Usuario;
import com.codeper.ControlClientes.repository.PersonaRepository;
import com.codeper.ControlClientes.repository.RolRepository;
import com.codeper.ControlClientes.repository.UsuarioRepository;

@Service 
public class UsuarioService implements IUsuarioService {

    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private PersonaRepository personaRepository;

    @Autowired 
    private RolRepository rolRepository;

    @Override
    public List<UsuarioDto> listarUsuario() {
       return usuarioRepository.findAll().stream().map(usuario->{
        return UsuarioDto.fromEntityUsuario(usuario);
       }).toList();
    }

    @Override
    public UsuarioDto obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id).map(usuario->{
            return UsuarioDto.fromEntityUsuario(usuario);
        }).orElseThrow(()->new RuntimeException("Usuario con el id: "+id+" no emcontrado"));
    }

    @Override
    public UsuarioDto crearUsuario(CreateUsuarioDto createUsuarioDto) {
        //VALIDAR SI EXISTE LA PERSONA 
        Persona persona = personaRepository.findById(createUsuarioDto.getPersonaId())
            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        // VALIDAR ROL EXISTENTE 
        List<Rol>roles=validarYObtenerRoles(createUsuarioDto.getRolesId());

        Usuario usuario= CreateUsuarioDto.toEntityUsuario(createUsuarioDto, persona, roles);
        usuario=usuarioRepository.save(usuario);
        return UsuarioDto.fromEntityUsuario(usuario);
    }

   @Override
    public UsuarioDto actualizarUsuario(Integer id, CreateUsuarioDto usuarioActualizar) {
        //VALIDAR USUARIO
        Usuario usuarioRetrieved = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id: " + id));

        //VALIDAR PERSONA
        Persona persona = personaRepository.findById(usuarioActualizar.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con el id: " + usuarioActualizar.getPersonaId()));

        //VALIDAR ROL
        List<Rol>roles=validarYObtenerRoles(usuarioActualizar.getRolesId());

        usuarioRetrieved.setUsername(usuarioActualizar.getUsername());
        usuarioRetrieved.setPasswprd(usuarioActualizar.getPasswprd());
        usuarioRetrieved.setPersona(persona);
        usuarioRetrieved.setRoles(roles);

        return UsuarioDto.fromEntityUsuario(usuarioRepository.save(usuarioRetrieved));
    }   

    @Override
    public void eliminarUsuario(Integer id) {
      if (!usuarioRepository.existsById(id)) {
        throw new RuntimeException("Usuario no encontrada con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private List<Rol> validarYObtenerRoles(List<Integer> rolesId) {
    if (rolesId == null || rolesId.isEmpty()) {
        throw new IllegalArgumentException("Debe enviar al menos un rol");
    }

    List<Integer> ids = rolesId.stream().distinct().toList();
    List<Rol> roles = rolRepository.findAllById(ids);

    if (roles.size() != ids.size()) {
        Set<Integer> encontrados = roles.stream()
                .map(Rol::getId)
                .collect(Collectors.toSet());

        List<Integer> noExisten = ids.stream()
                .filter(id -> !encontrados.contains(id))
                .toList();

        throw new RuntimeException("Roles no encontrados: " + noExisten);
    }
    return roles;
    }  
}
