package com.codeper.ControlClientes.dto;

import java.util.List;
import com.codeper.ControlClientes.entity.Permiso;
import com.codeper.ControlClientes.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class RolDto {

    private Integer id;
    private String nombre;
    private String descripcion;
    private List<Integer>permisosId;

    public static  RolDto fromEntityRol(Rol rol){
        return  RolDto.builder()
            .id(rol.getId())
            .nombre(rol.getNombre())
            .descripcion(rol.getDescripcion())
            //Ahora si quiero que me liste solo el id de permiso dentro del listado  
            .permisosId(rol.getPermisos().stream()
                    .map(Permiso::getId)
                    .toList()) 
            .build();
    }
}
