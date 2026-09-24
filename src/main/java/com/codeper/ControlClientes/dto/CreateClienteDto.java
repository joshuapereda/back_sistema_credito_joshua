package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Persona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateClienteDto {
    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;

    @Email(message = "Por favor ingresa un correo valido") 
    private String gmail;

    @Pattern (regexp = "^9\\d{8}$", message = "El teléfono debe ser un celular peruano válido (9 dígitos, empieza con 9)")
    private String telefono;

    @NotBlank (message = "La direccion no puede estar vacío")
    private String direccion;

    @NotBlank (message = "El D° Identidad  no puede estar vacío")
    private String docIdentidad;

    public static Persona toEntityPersona(CreateClienteDto createPersonaDto){
        return  Persona.builder()
        .nombre(createPersonaDto.getNombre())
        .gmail(createPersonaDto.getGmail())
        .telefono(createPersonaDto.getTelefono())
        .direccion(createPersonaDto.getDireccion())
        .build();
    }

    public static Cliente toEntityCliente(CreateClienteDto createPersonaDto, Persona persona){
        return Cliente.builder()
        .docIdentidad(createPersonaDto.getDocIdentidad())
        .persona(persona)
        .build();
    }

    
}
