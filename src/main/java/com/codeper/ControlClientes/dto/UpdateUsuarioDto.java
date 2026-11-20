package com.codeper.ControlClientes.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@ToString (exclude = "passwprd")
public class UpdateUsuarioDto {

    @NotBlank (message = "El Username es obligatorio")
    @Pattern (
        regexp = "^[A-Z][a-zA-Z]*(\\d[a-zA-Z]*){2,4}$",
        message = "El Username debe empezar con mayúscula y contener entre 2 y 4 números"
    )
    private String username;

    // Opcional: vacío o null = mantener la contraseña anterior
    @Pattern(
        regexp = "^$|^(?=[A-Z])(?=.{4,}$)(?=^(?:\\D*\\d){2,4}\\D*$)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`]).+$",
        message = "El Password debe empezar con mayúscula, tener mínimo 4 caracteres, entre 2 y 4 números, y al menos un símbolo especial"
    )
    private String passwprd;

    @NotNull (message = "Debe asignar al menos una Persona al Usuario")
    private Integer personaId;

    @NotEmpty (message = "Debe asignar al menos un Rol al usuario")
    private List<Integer> rolesId;
}
