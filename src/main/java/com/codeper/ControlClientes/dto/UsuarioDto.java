package com.codeper.ControlClientes.dto;

import java.util.List;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class UsuarioDto {
    //Usuario
    private Integer id;
    private String username;
    //Persona
    private String gmail;
    private String nombrePersona;
    //Rol 
    private List<Integer>rolesId;

    public static UsuarioDto fromEntityUsuario(Usuario usuario){
        return UsuarioDto.builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .gmail(usuario.getPersona().getGmail())
            .nombrePersona(usuario.getPersona().getNombre())
           .rolesId(usuario.getRoles().stream()
                    .map(Rol::getId)
                    .toList()) 
            .build();
    } 
}
