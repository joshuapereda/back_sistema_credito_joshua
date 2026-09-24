package com.codeper.ControlClientes.dto;

import com.codeper.ControlClientes.entity.Cliente;
import com.codeper.ControlClientes.entity.Persona;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class ClienteDto {
    private Integer id;   
    private String nombre;
    private String gmail;
    private String telefono;
    private String direccion;
    private String docIdentidad;

    public static ClienteDto fromEntityCliente(Cliente cliente,Persona persona){
        return ClienteDto.builder()
            .id(persona.getId())
            .nombre(persona.getNombre())
            .gmail(persona.getGmail())
            .telefono(persona.getTelefono())
            .direccion(persona.getDireccion())
            .docIdentidad(cliente.getDocIdentidad())
            .build();
    }

}
