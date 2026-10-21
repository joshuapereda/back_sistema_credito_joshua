package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Persona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import validator.UniqueName;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class CreateClienteDto {

    @NotBlank (message = "El Nombre del Cliente es obligatorio")
    @Size( min=1,max = 50, message = "El Nombre del cliente no puede tener más de 50 caracteres")
    @UniqueName (fieldName = "nombre")
    //Agregar Customer Validations 
    private String nombre;

    @Email(message = "Por favor ingresa un Gmail valido")
    //Agregar Customer Validations 
    private String gmail;

    @Pattern (regexp = "^9\\d{8}$", message = "El N° Teléfono debe ser un celular peruano válido (9 dígitos, empieza con 9)")
    private String telefono;

    @NotBlank (message = "La Direccion de la Cliente es obligatorio")
    private String direccion;

    @NotBlank (message = "El D° Identidad es obligatorio")
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
