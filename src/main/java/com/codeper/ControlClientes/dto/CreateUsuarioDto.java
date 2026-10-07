package com.codeper.ControlClientes.dto;

import java.util.List;
import com.codeper.ControlClientes.entity.Persona;
import com.codeper.ControlClientes.entity.Rol;
import com.codeper.ControlClientes.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateUsuarioDto {

    //Usuario
    @NotBlank(message = "El username no puede estar vacío")
    @Pattern (
        regexp = "^[A-Z][a-zA-Z]*(\\d[a-zA-Z]*){2,4}$",
        message = "El username debe empezar con mayúscula y contener entre 2 y 4 números"
    )
    private String username;

    @NotBlank(message = "El passwprd no puede estar vacío")
    @Pattern(
        regexp = "^(?=[A-Z])(?=.{4,}$)(?=^(?:\\D*\\d){2,4}\\D*$)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`]).+$",
        message = "El password debe empezar con mayúscula, tener mínimo 4 caracteres, entre 2 y 4 números, y al menos un símbolo especial"
    )
    private String passwprd;
    
    //Persona
    //Nos indica que las entidades se van a crear al mismo tiempo
    //ose la entidad Persona tampoco existe ne la BD
    //La entidad Persona ya existe en la BD (se crea por separado)
    @NotNull (message = "Debe seleccionar una persona")
    private Integer personaId;

    //Rol
    //La entidad Rol ya existe en la Bd
    //private List<Integer>roles; Se usa solo cuando al relacion es de N a n
    @NotEmpty  (message = "Debe asignar al menos un rol al usuario")
    private List<Integer>rolesId;

    public static Usuario toEntityUsuario(CreateUsuarioDto createUsuarioDto, Persona persona,List<Rol> roles){ 
        return Usuario.builder()
            .username(createUsuarioDto.getUsername())
            .passwprd(createUsuarioDto.getPasswprd())
            .persona(persona)
            .roles(roles)
            .build();
    }
}
