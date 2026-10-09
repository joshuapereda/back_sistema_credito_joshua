package com.codeper.ControlClientes.entity;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@Entity 
@Table (name ="roles")
public class Rol {
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 100)
    private String nombre;
    
    @Column (length = 200)
    private String descripcion;


    /**
     * Relación N:M entre Rol y Permiso.
     * - Un rol tiene MUCHOS permisos (por eso es una List, como un array que crece).
     * - Un permiso puede pertenecer a MUCHOS roles.
     * - En la BD se crea la tabla intermedia "rol_permiso" (rol_id, permiso_id).
     * - joinColumns        -> columna que apunta a ESTA entidad (Rol).
     * - inverseJoinColumns -> columna que apunta a la OTRA entidad (Permiso).
     * - EAGER: al cargar un Rol, sus permisos se cargan de inmediato.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (
        name = "rol_permiso",
        joinColumns = @JoinColumn(name = "rol_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private List<Permiso> permisos;

    //Definir mapeo de relacion con la tabla Usuario
    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;
}
